package utils

import java.util.concurrent.TimeUnit

import akka.kafka.testkit.internal.TestFrameworkInterface
import akka.kafka.testkit.scaladsl.{ EmbeddedKafkaLike, KafkaSpec }
import akka.testkit.TestKit
import org.scalatest.concurrent.{ Eventually, ScalaFutures }
import org.scalatest.time.{ Millis, Seconds, Span }
import org.scalatest.{ AsyncFlatSpecLike, BeforeAndAfter, Matchers }

import scala.concurrent.duration._

abstract class TransactionSpec(
  _kafkaPort: Int
)
  extends KafkaSpec(
    _kafkaPort,
    zooKeeperPort = _kafkaPort + 1,
    actorSystem   = ExampleSystem.system
  )
  with EmbeddedKafkaLike
  with AsyncFlatSpecLike
  with TestFrameworkInterface.Scalatest
  with Matchers
  with BeforeAndAfter
  with ScalaFutures
  with Eventually {

  override implicit def patienceConfig: PatienceConfig =
    PatienceConfig(timeout  = scaled(Span(5, Seconds)), interval = scaled(Span(150, Millis)))

  import akka.util.Timeout

  implicit val timeout: Timeout = 5 seconds

  override def cleanUp(): Unit = {
    //testProducer.close(60, TimeUnit.SECONDS)
    //cleanUpAdminClient()
  }

}

