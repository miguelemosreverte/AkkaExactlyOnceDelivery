package utils

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.{ AsyncWordSpecLike, BeforeAndAfter, MustMatchers }

abstract class Spec(_system: ActorSystem)
  extends TestKit(_system)
  with AsyncWordSpecLike
  with MustMatchers
  with BeforeAndAfter {

  def this() = this(ExampleSystem.system)

}
