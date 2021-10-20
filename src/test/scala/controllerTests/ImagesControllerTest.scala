package controllerTests

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import controller.ImagesController
import io.circe.Json
import org.http4s.circe.jsonDecoder
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.{HttpApp, Uri}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import repo.ImagesRepo
import service.ImageService

class ImagesControllerTest extends AnyWordSpec
  with Matchers
  with Http4sDsl[IO]
  with Http4sClientDsl[IO] {
  def getResources(): HttpApp[IO] = {
    val imageRepo: ImagesRepo[IO] = ImagesRepo()
    val imageService: ImageService[IO] = ImageService[IO](imageRepo)
    val imagesRoutes = Router(("/api", ImagesController.imagesRoutes[IO](imageService))).orNotFound
    imagesRoutes
  }

  "HelloWorldController" when {
    "request hello / name" should {
      "return hello, name" in {
        val imagesRoutes = getResources()
        val json = Json.arr()

        val (response, respObject) = (for {
          resp <- imagesRoutes.run(GET(Uri.unsafeFromString(s"/api/all")))
          jsonResp <- resp.as[Json]
        } yield (resp, jsonResp) ).unsafeRunSync()

        response.status shouldEqual Ok
        respObject shouldBe json
      }
    }
  }
}