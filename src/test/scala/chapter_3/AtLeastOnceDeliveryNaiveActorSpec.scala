package chapter_3

import akka.actor.{ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import chapter_1.ImperfectActor
import utils.Spec

import scala.concurrent.duration._

class AtLeastOnceDeliveryNaiveActorSpec extends Spec {

  "ImperfectActor" should {
    " should reply all the times thanks to retry by AtLeastOnceDelivery" in {

      for {
        a <- (naiveAtLeastOnceActor ? "send").mapTo[String]

      } yield assert(a == "Hello there!")
    }
  }

  implicit val timeout: Timeout = Timeout(10 seconds)
  val imperfectActor: ActorRef = system.actorOf(Props(
    new ImperfectActor()), "ImperfectActor2")
  type Input = String
  type Output = String
  val naiveAtLeastOnceActor: ActorRef = system.actorOf(Props(
    new AtLeastOnceDeliveryNaiveActor[Input, Output](
      "Hello there!", imperfectActor)),
    "naiveAtLeastOnceActor")

}