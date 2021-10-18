package controller.utils

import cats.effect.IO
import dto.Test
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

object Codecs {
  implicit val testCodec: Codec[Test] = deriveCodec[Test]
  implicit val opsCodec: Codec[Option[String]] = deriveCodec[Option[String]]
  implicit val testDecoder: EntityDecoder[IO, Test] = jsonOf[IO, Test]
  implicit val opsDecoder: EntityDecoder[IO, Option[String]] = jsonOf[IO, Option[String]]
}
