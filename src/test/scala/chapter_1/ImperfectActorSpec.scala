package chapter_1

import akka.actor.{ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import utils.Spec

import scala.concurrent.Future
import scala.concurrent.duration._

class ImperfectActorSpec extends Spec {

  "ImperfectActor" should {
    " should reply half the times" in {

      for {
        a <- getReply("Hello there!")
        b <- getReply("Hello there!")

      } yield assert(a == "AskTimeoutException" && b == "Hello there!")
    }
  }

  def getReply(message: String): Future[String] =

    (imperfectActor ? message).mapTo[String].recoverWith {
      case a => Future(a.getClass.getSimpleName)
    }

  val imperfectActor: ActorRef = system.actorOf(Props(new ImperfectActor()), "ImperfectActor")

}