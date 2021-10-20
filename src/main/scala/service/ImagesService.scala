package service

import cats.Monad
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

    override def getRandomImage(): F[Option[Image]] =
      imageRepo
        .getRandom()
        .flatTap( im => im.fold(F.pure())(image => updateImage(image.id, image.copy(touched = image.touched + 1))))

    override def getImage(id: Long): F[Option[Image]] = imageRepo.get(id)

    override def createImage(url: String): F[Long] = imageRepo.create(Image(url))

    override def updateImage(id: Long, str: Image): F[Unit] = imageRepo.update(id, str)

    override def deleteImage(id: Long): F[Option[Image]] = imageRepo.delete(id)

    override def likeImage(id: Long): F[Unit] = {
      imageRepo
        .get(id)
        .flatTap(im => im.fold(F.pure())(image => updateImage(id, image.copy(liked = image.liked + 1)))).as(())
    }
  }
}
