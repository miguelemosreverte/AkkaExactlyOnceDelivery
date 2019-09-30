# AkkaExactlyOnceDelivery
## Avoiding AskTimeoutException

**abstract**
I will show how to send messages in Akka without fear of failure thanks to, Retry, and then show how to achieve ExactlyOnceDelivery thanks to Deduplication.


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

# Chapter_2  Naive Solution
I present you, a recursive method that retries in case of failure. I call it, Retry.

# Chapter 2 revisited
Even though we have created a recursive function that will retry to send a message until completion,
this function exists in RAM, and if the system crashes, it will be lost, along with the message it
was supposed to send until it was sent successfully.

So, in order to survive a system crash, we are going to need to save to disk the fact that we are trying
to send the message, so that it can be continued to be sent once the system restarts.
We are going to need an Actor, a PersistentActor. Let's start with just an Actor first,
later we can focus on making it persistent.

# Chapter 3 Naive Actor
I present you, a recursive actor that retries in case of failure. I call it, RetryActor.

[RetryActor implementation](https://github.com/miguelemosreverte/ReactiveExample/blob/master/src/main/scala/introduction/ImperfectActor.scala)

[RetryActor Specs](https://github.com/miguelemosreverte/ReactiveExample/blob/master/src/test/scala/introduction/IntroductionSpec.scala)

# Chapter 3 revisited
Even though we have created an actor that will retry to send a message until completion,
there is a problem with this actor, still:
## _the initial message problem_
  - Though it will try until success, what happens if it does not receive _the initial message?_
    We have just posponed the eventuality of a message failure!


We need to go deeper. We need to make this guarantee of message sending a typed result.

We need to play with the actor lifecycle.

What we are going to do is to give a callback to this actor so that when it eventually works, it tell us it did.

To do so, we are going to use scala.concurrent.Promise, we are going to send "a callback" to the Actor constructor.

This solves the problem.

[RetryActor implementation](https://github.com/miguelemosreverte/ReactiveExample/blob/master/src/main/scala/introduction/ImperfectActor.scala)

[RetryActor Specs](https://github.com/miguelemosreverte/ReactiveExample/blob/master/src/test/scala/introduction/IntroductionSpec.scala)
