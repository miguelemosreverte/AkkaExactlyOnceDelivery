package chapter_3

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem}
import akka.pattern.{AskTimeoutException, ask}
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag
import scala.util.{Failure, Success}

object RetryActor {

  case class Return(output: Any, sender: ActorRef)

  case class Retry(input: Any, to: ActorRef, sender: ActorRef)

  case class Try(send: Any, to: ActorRef)

}
class RetryActor
  extends Actor with ActorLogging {
  import RetryActor._
  implicit val timeout: Timeout = Timeout(2 seconds)
  import context.dispatcher



  override def receive: Receive = {

    case Try(send, to) => self ! Retry(send, to, sender())
    case Retry(input, to, sender) => (to ? input) andThen {
      case Success(output) ⇒
        self ! Return(output, sender)
      case Failure(_: AskTimeoutException) ⇒
        self ! Retry(input, to, sender)
    }

    case Return(output, sender) =>
      sender ! output
  }

}
