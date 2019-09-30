package akka

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.cluster.sharding.ShardRegion.{HashCodeMessageExtractor, MessageExtractor}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings, ShardRegion}
import ddd.{Command, Query}

  trait ShardedEntity {
  
      val typeName: String
  
      def props(): Props
  
      private val hashCodeMessageExtractor: MessageExtractor = new HashCodeMessageExtractor(3 * 10) {
        override def entityId(message: Any): String = message match {
    
          case qry: Query => qry.aggregateRoot.toString
              case cmd: Command => cmd.aggregateRoot.toString
            }
      }
  
      def start(implicit system: ActorSystem): ActorRef = ClusterSharding(system).start(
        typeName        = typeName,
        entityProps     = props(),
        settings        = ClusterShardingSettings(system),
        messageExtractor = hashCodeMessageExtractor
        )
  }

  

