package com.sezzle.dataneptune

import org.apache.tinkerpop.gremlin.driver.{Cluster, Client}
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.{GraphTraversalSource, GraphTraversal}
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal

class Connection(var cluster: Cluster) {

  def close() = {
    cluster.close()
  }

  def getCluster() : Cluster = {
    cluster
  }
}

object Connection {

  def apply() : Connection = {
    var builder =
      Cluster.build()
        .addContactPoint("db-neptune-test-1-instance-1.cvmfjfrcwnag.us-east-2.neptune.amazonaws.com")
        .port(8182)
        .enableSsl(true)
        .keyCertChainFile("SFSRootCAG2.pem")

    new Connection(builder.create())

  }
}
