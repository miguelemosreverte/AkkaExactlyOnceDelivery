package chapter_2

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.{AskTimeoutException, ask}
import akka.util.Timeout

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag


object Retry {
  def Retry[Input, Output: ClassTag](send: Input, to: ActorRef)
                                                  (implicit
                                                   timeout: Timeout,
                                                   ec: ExecutionContext,
                                                   system: ActorSystem): Future[Output] = {

    (to ? send)
      .mapTo[Output]
      .recoverWith {
        case _: AskTimeoutException => {
          Retry(send, to)
        }
      }


  }

}
