package repo

import cats.Monad
import cats.implicits.catsSyntaxApplicativeId
import dto.Image

import scala.collection.concurrent.TrieMap
import scala.util.Random

trait ImagesRepo[F[_]] {
  def getAllKeys(): F[List[Long]]
  def getAll(): F[List[Image]]
  def get(id: Long): F[Option[Image]]
  def create(image: Image): F[Long]
  def update(id: Long, image: Image): F[Unit]
  def delete(id: Long): F[Option[Image]]
  def getRandom(): F[Option[Image]]
}

object ImagesRepo {
  val random: Random = Random
  val repo = new TrieMap[Long, Image]()

  def apply[F[_]]()(implicit F: Monad[F]): ImagesRepo[F] = new ImagesRepo[F] {
    override def getAllKeys(): F[List[Long]] = repo.keySet.toList.sorted.pure[F]

    override def getAll(): F[List[Image]] = repo.values.toList.pure[F]

    override def get(id: Long): F[Option[Image]] = repo.get(id).pure[F]

    override def getRandom(): F[Option[Image]] = repo.get(random.between(0, repo.size)).pure[F]

    override def create(image: Image): F[Long] = {
      val newId = repo.keySet.maxOption match {
        case Some(id) => id + 1
        case None => 0L
      }
      repo.addOne((newId, image.copy(id = newId)))
      newId.pure[F]
    }

    override def update(id: Long, image: Image): F[Unit] = repo.update(id, image).pure[F]

    override def delete(id: Long): F[Option[Image]] = repo.remove(id).pure[F]
  }

}