import cats.data.Kleisli
import cats.effect._
import controller.{HelloWorldController, ImagesController}
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.{HttpRoutes, Request, Response}
import repo.ImagesRepo
import service.ImageService

sealed case class Test(test: Long)

object Server extends IOApp {

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
//    val app: Resource[IO, Server[IO]] = for {
//      blocker <- Blocker
//      server <- BlazeServerBuilder[IO]
//        .bindHttp(8080)
//        .withHttpApp(fileService[IO](FileService.Config(".", blocker)).orNotFound)
//        .resource
//    } yield server

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(routs)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
