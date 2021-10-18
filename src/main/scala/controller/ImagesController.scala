package controller

import cats.data.OptionT
import cats.effect.Concurrent
import cats.implicits._
import dto.Image
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.blaze.http.Url
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import service.ImageService

object ImagesController {

  def imagesRoutes[F[_] : Concurrent](imageService: ImageService[F]): HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "all" =>
        for {
          all  <- imageService.getAll()
          resp <- Ok(all.asJson)
        } yield resp

      case GET -> Root / "random" =>
        for {
          rand <- imageService.getRandomImage()
          resp <- Ok(rand.asJson)
        } yield resp

      case GET -> Root / LongVar(id) =>
        for {
          image <- imageService.getImage(id)
          resp  <- Ok(image.asJson)
        } yield resp

      case POST -> Root / LongVar(id) / "like" =>
        for {
          _    <- imageService.likeImage(id)
          resp <- Ok()
        } yield resp

      case req @ PUT -> Root =>
        for {
          url  <- req.as[Url]
          id   <- imageService.createImage(url)
          resp <- Ok(id.asJson)
        } yield resp
    }
  }
}
