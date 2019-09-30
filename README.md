# AkkaExactlyOnceDelivery

**abstract**
I will show how to send messages in Akka without fear of failure thanks to, AtLeastOnceDelivery, and then show how to achieve ExactlyOnceDelivery thanks to Deduplication.


**longer abstract**
Once a message is received, it **will** be processed.
 And only processed **once**.
 Reason for this clarification is that, in order to guarantee that a message **will** be processed, it has to be sent again and again until it is. Until a confirmation is received.
  This property, to be able to repeat message sending until confirmation of processing, is called **at least once delivery**.

 Now, there is the risk of processing two times a message, in case it is sent, it is processed, and for some reason confirmation fails to be received, and so it is sent once again for processing.
 Solution for is is called message deduplication, which means that a message has an id, and on the processing side messages are filtered first, to remove duplicates. Once a message is successfully processed, it is marked as so, and then further incoming messages with the same id are thrown away.
  This property, to be able to process only once a message, is called **idempotence**. Same message, same result.

 **idempotence** _plus_ **at least once delivery** = **exactly once delivery**.


# Chapter_1  Introduction
I present you, an Actor that fails half the time. I call it, The Imperfect Actor.
##  The Imperfect Actor
[Imperfect Actor implementation](https://github.com/miguelemosreverte/ReactiveExample/blob/master/src/main/scala/introduction/ImperfectActor.scala)

[Imperfect Actor Specs](https://github.com/miguelemosreverte/ReactiveExample/blob/master/src/test/scala/introduction/IntroductionSpec.scala)
