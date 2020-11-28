package com.sezzle.dataneptune

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.{GraphTraversal, GraphTraversalSource}
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.structure.T

object Main extends App {
  println("Started main!")

  var conn : Connection = Connection()

  println("made a connection")

  var g : GraphTraversalSource = traversal().withRemote(DriverRemoteConnection.using(conn.getCluster()));

  g.addV("Shopper")
    .property(T.id, "8315ef05-b180-4a6a-8e48-3e228dda52cd")
    .property("firstname", "Daniel")
    .property("lastname", "Hartig")
    .next()
  g.addV("Merchant")
    .property(T.id, "a116af67-d0a2-45b6-a726-dc17a7d5efb4")
    .property("dba", "A Quiver Full")
    .next()
  g.addE("order_at")
    .from(g.V("8315ef05-b180-4a6a-8e48-3e228dda52cd"))
    .to(g.V("a116af67-d0a2-45b6-a726-dc17a7d5efb4"))
    .next()

  // find all stores my user shopped at
  var t = g.V("8315ef05-b180-4a6a-8e48-3e228dda52cd")
    .out("order_at")
    .values("dba")

  t map (println(_))

  println("did some work")

  conn.close()

  println("closed!")

}
