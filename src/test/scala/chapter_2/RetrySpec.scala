package chapter_2

import akka.actor.{ActorRef, Props}
import akka.util.Timeout
import chapter_1.ImperfectActor
import utils.Spec

import scala.concurrent.duration._

class RetrySpec extends Spec {

  "ImperfectActor" should {
    " should reply all the times thanks to retry by Retry" in {

      type Input  = String
      type Output = String
      for {
        a <- Retry.Retry[Input, Output]("Hello there!", imperfectActor)

      } yield assert(a == "Hello there!")
    }
  }

  val imperfectActor: ActorRef = system.actorOf(Props(new ImperfectActor()), "ImperfectActor2")

}