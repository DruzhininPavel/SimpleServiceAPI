package controller

import cats.effect.Concurrent
import cats.implicits._
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import org.http4s.HttpRoutes
import org.http4s.blaze.http.Url
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl
import service.ImageService

object ImagesController {

  def imagesRoutes[F[_] : Concurrent](imageService: ImageService[F]): HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "all" => Ok(imageService.getAll()).orElse(NotFound())

      case GET -> Root / "random" => Ok(imageService.getRandomImage()).orElse(NotFound())

      case GET -> Root / LongVar(id) => Ok(imageService.getImage(id).flatMap{case Some(image) => image.pure[F]}).orElse(NotFound())

      case POST -> Root / LongVar(id) / "like" => Ok(imageService.likeImage(id).as(())).orElse(NotFound())

      case req @ PUT -> Root =>
        for {
          url  <- req.as[Url]
          id   <- imageService.createImage(url)
          resp <- Ok(id.asJson)
        } yield resp
    }
  }
}
