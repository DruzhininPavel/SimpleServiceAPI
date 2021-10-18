package service

import cats.Monad
import cats.data._
import cats.implicits._
import dto.Image
import repo.ImagesRepo

import scala.util.Random

trait ImageService[F[_]] {
  def getAll(): F[List[Image]]
  def getRandomImage(): F[Option[Image]]
  def getImage(id: Long): F[Option[Image]]
  def createImage(url: String): F[Long]
  def updateImage(id: Long, str: Image): F[Unit]
  def deleteImage(id: Long): F[Option[Image]]
  def likeImage(id: Long): F[Unit]
}

object ImageService {
  val random: Random = Random
  def apply[F[_]](imageRepo: ImagesRepo[F])(implicit F: Monad[F]): ImageService[F] = new ImageService[F] {
    override def getAll(): F[List[Image]] = imageRepo.getAll()

    override def getRandomImage(): F[Option[Image]] = OptionT(imageRepo.getRandom())
        .semiflatMap(image => updateImage(image.id, image.copy(touched = image.touched + 1)).as(image)).value

    override def getImage(id: Long): F[Option[Image]] = imageRepo.get(id)

    override def createImage(url: String): F[Long] = imageRepo.create(Image(url))

    override def updateImage(id: Long, str: Image): F[Unit] = imageRepo.update(id, str)

    override def deleteImage(id: Long): F[Option[Image]] = imageRepo.delete(id)

    override def likeImage(id: Long): F[Unit] = {
      OptionT(imageRepo.get(id)).cata((), image => updateImage(id, image.copy(liked = image.liked + 1)))
    }
  }
}
