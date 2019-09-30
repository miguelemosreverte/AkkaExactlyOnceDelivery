package utils

import akka.actor.ActorSystem
import akka.testkit.TestKit
import akka.util.Timeout
import org.scalatest.{AsyncWordSpecLike, BeforeAndAfter, MustMatchers}
import scala.concurrent.duration._

abstract class Spec(_system: ActorSystem)
  extends TestKit(_system)
  with AsyncWordSpecLike
  with MustMatchers
  with BeforeAndAfter {

  implicit val timeout: Timeout = Timeout(10 seconds)
  def this() = this(ExampleSystem.system)

}
