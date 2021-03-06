import cats.data.Kleisli
import cats.effect.{ExitCode, IO, IOApp}
import controller.{HelloWorldController, ImagesController}
import org.http4s.server.Router
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.{HttpRoutes, Request, Response}
import repo.ImagesRepo
import service.ImageService
import configuration.serverConfig

object ServerApp extends IOApp {

  /**
   * Config and Aggregate area
   */

  val imageRepo: ImagesRepo[IO] = ImagesRepo()
  val imageService: ImageService[IO] = ImageService[IO](imageRepo)
  val helloWorldRoutes: HttpRoutes[IO] = HelloWorldController.helloWorldRoutes[IO]
  val imagesRoutes: HttpRoutes[IO] = ImagesController.imagesRoutes[IO](imageService)

  val routs: Kleisli[IO, Request[IO], Response[IO]] = Router(
    "/hello" -> helloWorldRoutes,
    "/api" -> imagesRoutes
  ).orNotFound

  /**
   * Starting server area
   */
  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(serverConfig.port, serverConfig.host)
      .withHttpApp(routs)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
