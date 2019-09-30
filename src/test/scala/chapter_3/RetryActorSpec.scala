package chapter_3

import akka.actor.{ActorRef, Props}
import akka.util.Timeout
import chapter_1.ImperfectActor
import chapter_3.RetryActor.Try
import utils.Spec

import scala.concurrent.Promise
import scala.concurrent.duration._

class RetryActorSpec extends Spec {

  "ImperfectActor" should {
    " should reply all the times thanks to retry by Retry" in {

      for {
        a <- callback.future

      } yield assert(a == "Hello there!")
    }
  }

  val imperfectActor: ActorRef = system.actorOf(Props(
    new ImperfectActor()), "ImperfectActor3")

  val callback: Promise[Any] = Promise()

  val naiveAtLeastOnceActor: ActorRef = system.actorOf(Props(
    new RetryActorWithCallback(Try("Hello there!", imperfectActor), callback)),
    "naiveAtLeastOnceActor")

}