package chapter_4

import akka.actor.{ActorLogging, ActorRef}
import akka.pattern.{AskTimeoutException, ask}
import akka.persistence.PersistentActor
import akka.util.Timeout

import scala.util.{Failure, Success}

object PersistentRetryActor {

  case object Retry

  case class Return(output: Any)

}

class PersistentRetryActor(send: Any, to: ActorRef, client: ActorRef)(implicit t: Timeout)
  extends PersistentActor with ActorLogging {
  import PersistentRetryActor._

  private val transactionId: Int = self.path.name.toInt
  private var finished: Boolean = false

  import context.dispatcher

  override def preStart(): Unit = {
    super.preStart()
    self ! Retry
  }



  override def receive: Receive = {

    case Retry => (to ? send) andThen {
      case Success(output) ⇒
        self ! Return(output)
      case Failure(_: AskTimeoutException) ⇒
        self ! Retry
    }

    case Return(output) =>
      persist(true) { evt =>
        finished = evt // true
        client ! output
        context.stop(self)
      }

  }

  override def receiveRecover: Receive = {
    case evt: Boolean => finished = evt
  }

  override def receiveCommand: Receive = receive

  override def persistenceId: String = "TransactionActor-" + transactionId
}
