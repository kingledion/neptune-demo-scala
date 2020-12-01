package com.sezzle.dataneptune

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

  private val addpath = "/order"
  private val getpath = "/recommend"

  // set up connection with Neptune
  var g : GraphTraversalSource = traversal().withRemote(DriverRemoteConnection.using(conn.getCluster()));


  final val addOrder: Endpoint[IO, Unit] = post(addpath :: jsonBody[Order]) { order: Order =>

    // validate
    order.validate

    // insert

    g.addV("Shopper")
      .property(T.id, order.shopper.id)
      .property("firstname", order.shopper.firstname)
      .property("lastname", order.shopper.lastname)
      .next()

    g.addV("Merchant")
      .property(T.id, order.merchant.id)
      .property("dba", order.merchant.dba)
      .next()

    g.addE("order_at")
      .from(g.V(order.shopper.id))
      .to(g.V(order.merchant.id))
      .property("amount_in_cents", order.amount_in_cents)
      .next()

      //case e: java.util.concurrent.CompletionException => println("Caught java exception")

      Ok()
    } handle {
      case e: OrderValidationException => BadRequest(e)
    }


  final def toService: Service[Request, Response] = Bootstrap
    .serve[Application.Json](addOrder)
    .toService

}