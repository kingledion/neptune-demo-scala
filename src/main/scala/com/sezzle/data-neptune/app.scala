package com.sezzle.dataneptune

import scala.collection.mutable.ListBuffer

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.{GraphTraversal, GraphTraversalSource}
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.structure.T

import cats.effect.concurrent.Ref
import cats.effect.{ContextShift, IO}
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response, Status}
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._

class App(conn : Connection) extends Endpoint.Module[IO] {

  private val addpath = "order"
  private val getpath = "recommend"

  // set up connection with Neptune
  var g : GraphTraversalSource = traversal().withRemote(DriverRemoteConnection.using(conn.getCluster()));


  final val addOrder: Endpoint[IO, Unit] = post(addpath :: jsonBody[Order]) { order: Order =>

    //println("Mapped the order")

    // validate
    order.validate

    //println("Validated the order")

    // insert

    scala.util.control.Exception.ignoring(classOf[java.util.concurrent.CompletionException]) {
      g.addV("Shopper")
        .property(T.id, order.shopper.id)
        .property("firstname", order.shopper.firstname)
        .property("lastname", order.shopper.lastname)
        .next()
    }

    scala.util.control.Exception.ignoring(classOf[java.util.concurrent.CompletionException]) {
      g.addV("Merchant")
        .property(T.id, order.merchant.id)
        .property("dba", order.merchant.dba)
        .next()
    }

    g.addE("order_at")
      .from(g.V(order.shopper.id))
      .to(g.V(order.merchant.id))
      .property("amount_in_cents", order.amount_in_cents)
      .next()

      //println("called the gremlin and returning")

    Ok()
  } handle {
    case e: OrderValidationException => BadRequest(e)
  }

  final val getRecommendations: Endpoint[IO, List[String]] = get(getpath :: path[String]) { id: String =>

    println(s"Shopper id is $id")

    var t = g.V(id)
      .out("order_at")  // all stores this shopper ordered at
      .inE("order_at")  // all other orders made at these stores
      .outV()           // all other shoppers who made orders at stores the target shopper shopped at
      .outE("order_at") // all other orders these other shoppers shopped at
      .inV()            // all other stores those other shoppers shopped at
      .dedup()          // remove duplicates
      .values("dba")

    //println("Did some gremlin")

    val returnBuffer = new ListBuffer[String]
    t.forEachRemaining{ s: String =>
      returnBuffer += s
    }

    //println("Got a return value")

    Ok(returnBuffer.toList)
  } handle {
    case e: java.lang.IllegalStateException => BadRequest(e)
  }


  final def toService: Service[Request, Response] = Bootstrap
    .serve[Application.Json](addOrder :+: getRecommendations)
    .toService

}
