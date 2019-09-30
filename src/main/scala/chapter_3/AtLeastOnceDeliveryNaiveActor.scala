package chapter_3

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem}
import akka.pattern.{AskTimeoutException, ask}
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag
import scala.util.{Failure, Success}

class AtLeastOnceDeliveryNaiveActor[Input, Output: ClassTag](send: Input, to: ActorRef)
                                                            (implicit
                                                             ec: ExecutionContext,
                                                             system: ActorSystem)
  extends Actor with ActorLogging {
  implicit val timeout: Timeout = Timeout(2 seconds)

  def execute: Future[Output] =
    (to ? send)
      .mapTo[Output]
      .recoverWith {
        case _: AskTimeoutException =>
          execute
      }

  case class Return(output: Output, sender: ActorRef)

  case class Try(input: Input, sender: ActorRef)

  case class Retry(sender: ActorRef)


  override def receive: Receive = {
    case "send" => self ! Try(send, sender())


    case Try(input, sender) => (to ? send) andThen {
      case Success(output: Output) ⇒
        self ! Return(output, sender)
      case Failure(_: AskTimeoutException) ⇒
        self ! Try(input, sender)
    }

    case Return(output, sender) =>
      sender ! output
  }

}
