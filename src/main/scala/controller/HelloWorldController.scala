package controller

import cats.effect.Concurrent
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl


object HelloWorldController {
  def helloWorldRoutes[F[_] : Concurrent]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case req @ GET -> Root => Ok(s"Hello, ${req.params("name")}!")
    }
  }

}
