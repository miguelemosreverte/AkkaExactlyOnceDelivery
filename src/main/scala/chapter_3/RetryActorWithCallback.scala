package chapter_3

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.pattern.{AskTimeoutException, ask}
import akka.util.Timeout
import chapter_3.RetryActor.Try

import scala.concurrent.{Future, Promise}
import scala.concurrent.duration._
import scala.util.{Failure, Success}


class RetryActorWithCallback(taskDescription: Try, onSucess: Promise[Any])
  extends Actor with ActorLogging {

  import RetryActor._

  implicit val timeout: Timeout = Timeout(2 seconds)

  import context.dispatcher

  override def preStart(): Unit = {
    super.preStart()
    run()
  }

  case class Retry(input: Any, to: ActorRef)
  case class Return(output: Any)

  def run(): Unit = {
    val send = taskDescription.send
    val to = taskDescription.to
    self ! Retry(send, to)
  }



  override def receive: Receive = {

    case Retry(input, to) => (to ? input) andThen {
      case Success(output) ⇒
        self ! Return(output)
      case Failure(_: AskTimeoutException) ⇒
        self ! Retry(input, to)
    }

    case Return(output) =>
      onSucess.success(output)

  }

}
