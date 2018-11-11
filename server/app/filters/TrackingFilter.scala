package filters

import java.util.UUID

import akka.stream.Materializer
import com.google.inject.Inject
import play.api.Configuration
import play.api.libs.typedmap.TypedKey
import play.api.mvc.request.Cell
import play.api.mvc.{Filter, Request, RequestHeader, Result}

import scala.concurrent.{ExecutionContext, Future}

class TrackingHolder(val rh: RequestHeader) {
  import TrackingFilter._
  def userTrackingCode: String = rh.attrs.get(TRACKING_KEY).get.value
  def requestHost: String = rh.host
  def userIp: String = {
    println(f"fowarded: ${rh.headers.get("X-Forwarded-For")}")
    println(f"remote: ${rh.remoteAddress}")
    rh.headers.get("X-Forwarded-For").getOrElse(rh.remoteAddress)
  }
}

trait TrackingSupport {
  implicit def request2TrackingHolder(implicit rh: RequestHeader): TrackingHolder = {
    new TrackingHolder(rh)
  }
}

class TrackingFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext, cfg: Configuration)
    extends Filter{
  import TrackingFilter._
  override def apply(f: RequestHeader => Future[Result])(rh: RequestHeader): Future[Result] = {
    rh.session.get(TRACKING_KEY_ID) match {
      case None =>
        f(rh.addAttr(TRACKING_KEY, genNewTrackingCode()))
      case Some(_) =>
        f(rh)
    }
  }
}

object TrackingFilter {
  val TRACKING_KEY_ID = "tracking"
  val TRACKING_KEY: TypedKey[Cell[String]] = TypedKey.apply[Cell[String]]("tracking")
  def genNewTrackingCode(): Cell[String] = {
    Cell[String](UUID.randomUUID().toString.replace("-", ""))
  }
}