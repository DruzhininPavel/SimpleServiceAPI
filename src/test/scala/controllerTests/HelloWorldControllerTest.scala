package controllerTests

import cats.effect._
import cats.effect.unsafe.implicits.global
import controller.HelloWorldController
import fs2.text
import org.http4s._
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.dsl._
import org.http4s.server.Router
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class HelloWorldControllerTest extends AnyWordSpec
  with Matchers
  with Http4sDsl[IO]
  with Http4sClientDsl[IO] {
  def getResources(): HttpApp[IO] = {
    val hwRoutes = Router(("/hello",HelloWorldController.helloWorldRoutes[IO])).orNotFound
    hwRoutes
  }

  "HelloWorldController" when {
    "request hello / name" should {
      "return hello, name" in {
        val hwRoutes = getResources()
        val name = "John"
        val response = (for {
          resp <- hwRoutes.run(GET(Uri.unsafeFromString(s"/hello?name=$name")))
        } yield resp).unsafeRunSync()

        response.status shouldEqual Ok
        response.body.through(text.utf8.decode).compile.string.unsafeRunSync() shouldEqual s"Hello, $name!"
      }
    }
  }
}
