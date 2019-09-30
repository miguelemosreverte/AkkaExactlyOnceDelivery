package chapter_4

import akka.RestartActorSupervisorFactory
import akka.actor.{ActorRef, Kill, Props}
import akka.testkit.TestProbe
import akka.util.Timeout
import chapter_1.ImperfectActor
import chapter_3.RetryActor.Try
import utils.Spec

import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}

class PersistentRetryActorSpec extends Spec {

  "The PersistentRetry Actor" should {
    "retry imperfect until Done" in {
      val father = new RestartActorSupervisorFactory
      val testProbe = TestProbe("sender")
      val imperfectActor = father.create(Props(new ImperfectActor()), "ImperfectActor4")
      val expectedTxId = 1
      val transactionActor = father.create(Props(new PersistentRetryActor("Hello there!", imperfectActor, testProbe.ref)), expectedTxId.toString)
      imperfectActor ! Kill
      transactionActor ! Kill
      imperfectActor ! Kill
      transactionActor ! Kill
      imperfectActor ! Kill
      transactionActor ! Kill

      watch(transactionActor)
      testProbe.expectMsgPF(3 seconds) {
        case expectedTxId: String =>
          println(s"SENDER received finished $expectedTxId")
          expectTerminated(transactionActor, 2 seconds)
          unwatch(transactionActor)
          Future(assert(true))
        case _ =>
          unwatch(transactionActor)
          Future(assert(false))
      }
    }
  }
}