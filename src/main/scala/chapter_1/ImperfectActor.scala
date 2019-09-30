package chapter_1

import akka.actor.{Actor, ActorLogging}

class ImperfectActor extends Actor with ActorLogging {

  var acc: Int = 0

  def receive: Receive = {
    case _ if acc == 0 =>
      acc += 1
      log.info("AskTimeout on purpose!")

    case msg =>
      acc += 1
      log.info(s"$msg")
      sender() ! msg
  }
}
