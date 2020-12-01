package com.sezzle.dataneptune

import cats.effect.IO
import cats.effect.concurrent.Ref
import com.twitter.finagle.Http
import com.twitter.server.TwitterServer
import com.twitter.util.Await

object Main extends TwitterServer {

  private val port: Int = 8081

  def main(): Unit = {

    val conn : Connection = Connection()

    val app = new App(conn)
    val server = Http.server.serve(":${port}", app.toService)

    onExit(conn.close())
    Await.ready(server)
  }

}


// try {
//   g.addV("Shopper")
//     .property(T.id, "8315ef05-b180-4a6a-8e48-3e228dda52cd")
//     .property("firstname", "Daniel")
//     .property("lastname", "Hartig")
//     .next()
//   g.addV("Merchant")
//     .property(T.id, "a116af67-d0a2-45b6-a726-dc17a7d5efb4")
//     .property("dba", "A Quiver Full")
//     .next()
//   g.addE("order_at")
//     .from(g.V("8315ef05-b180-4a6a-8e48-3e228dda52cd"))
//     .to(g.V("a116af67-d0a2-45b6-a726-dc17a7d5efb4"))
//     .next()
// } catch {
//   case e: java.util.concurrent.CompletionException => println("Caught java exception")
// }
