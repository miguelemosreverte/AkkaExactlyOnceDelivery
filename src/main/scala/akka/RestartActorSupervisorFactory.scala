package akka

import akka.actor.SupervisorStrategy.{Directive, Restart}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, OneForOneStrategy, PoisonPill, Props, SupervisorStrategy}
import akka.pattern._
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._

/** RestartActorSupervisorFactory serves as a step father to other actors
  * with Reset as base directive
  *
  * @param system ActorSystem used to create the step father
  */
class RestartActorSupervisorFactory(implicit system: ActorSystem) {

  private val actorSupervisorFactoryRef: ActorRef = system.actorOf(Props(new ActorSupervisorFactory(Restart)))

  implicit val timeout: Timeout = Timeout(1.second)

  def create(props: Props, name: String): ActorRef =
    Await.result(
      (actorSupervisorFactoryRef ? (props -> name)).mapTo[ActorRef],
      1.second
    )

  def stop(): Unit =
    actorSupervisorFactoryRef ! PoisonPill

  private class ActorSupervisorFactory(baseDir: Directive) extends Actor with ActorLogging {
    override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
      case _ => Restart

    }

    override def receive: Receive = {
      case (props: Props, name: String) =>
        sender() ! context.actorOf(props, name)
    }
  }

}
