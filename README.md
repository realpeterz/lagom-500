# Lagom 500 Error Problem

## Reproducing Steps

1. `$ sbt runAll`
2. `$ curl --request GET --url 'http://localhost:9000/api/hello/Marvel?from=Doctor>Strange'`
3. Notice a HTML page with a 500 code due to a `java.net.URISyntaxException` has returned instead of catching the error in [CustomHttpErrorHandler](https://github.com/realpeterz/lagom-500/blob/master/lagom-500-impl/src/main/scala/com/example/lagom500/impl/Lagom500Loader.scala#L42) and [`hello`](https://github.com/realpeterz/lagom-500/blob/master/lagom-500-impl/src/main/scala/com/example/lagom500/impl/Lagom500ServiceImpl.scala#L20) ServiceCall is never invoked.

At the moment, this is no known way to handle this kind of errors as proper client errors. Wrongly triggered 500 server errors in production is very problematic.

## Log Message
```
19:01:46.984 [error] application [] -

! @78j4afm0o - Internal server error, for (GET) [/api/hello/Marvel?from=Doctor>Strange] ->

play.api.http.HttpErrorHandlerExceptions$$anon$1: Execution exception[[IllegalArgumentException: Illegal character in query at index 29: /api/hello/Marvel?from=Doctor>Strange]]
  at play.api.http.HttpErrorHandlerExceptions$.throwableToUsefulException(HttpErrorHandler.scala:255)
  at play.api.http.DefaultHttpErrorHandler.onServerError(HttpErrorHandler.scala:182)
  at play.api.http.DefaultHttpErrorHandler$.onServerError(HttpErrorHandler.scala:286)
  at play.core.server.Server$.logExceptionAndGetResult$1(Server.scala:111)
  at play.core.server.Server$.getHandlerFor(Server.scala:141)
  at play.core.server.AkkaHttpServer.getHandler(AkkaHttpServer.scala:246)
  at play.core.server.AkkaHttpServer.handleRequest(AkkaHttpServer.scala:217)
  at play.core.server.AkkaHttpServer.$anonfun$createServerBinding$1(AkkaHttpServer.scala:117)
  at akka.stream.impl.fusing.MapAsync$$anon$25.onPush(Ops.scala:1190)
  at akka.stream.impl.fusing.GraphInterpreter.processPush(GraphInterpreter.scala:519)
  at akka.stream.impl.fusing.GraphInterpreter.processEvent(GraphInterpreter.scala:482)
  at akka.stream.impl.fusing.GraphInterpreter.execute(GraphInterpreter.scala:378)
  at akka.stream.impl.fusing.GraphInterpreterShell.runBatch(ActorGraphInterpreter.scala:588)
  at akka.stream.impl.fusing.GraphInterpreterShell$AsyncInput.execute(ActorGraphInterpreter.scala:472)
  at akka.stream.impl.fusing.GraphInterpreterShell.processEvent(ActorGraphInterpreter.scala:563)
  at akka.stream.impl.fusing.ActorGraphInterpreter.akka$stream$impl$fusing$ActorGraphInterpreter$$processEvent(ActorGraphInterpreter.scala:745)
  at akka.stream.impl.fusing.ActorGraphInterpreter$$anonfun$receive$1.applyOrElse(ActorGraphInterpreter.scala:760)
  at akka.actor.Actor.aroundReceive(Actor.scala:517)
  at akka.actor.Actor.aroundReceive$(Actor.scala:515)
  at akka.stream.impl.fusing.ActorGraphInterpreter.aroundReceive(ActorGraphInterpreter.scala:670)
  at akka.actor.ActorCell.receiveMessage(ActorCell.scala:588)
  at akka.actor.ActorCell.invoke(ActorCell.scala:557)
  at akka.dispatch.Mailbox.processMailbox(Mailbox.scala:258)
  at akka.dispatch.Mailbox.run(Mailbox.scala:225)
  at akka.dispatch.Mailbox.exec(Mailbox.scala:235)
  at akka.dispatch.forkjoin.ForkJoinTask.doExec(ForkJoinTask.java:260)
  at akka.dispatch.forkjoin.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1339)
  at akka.dispatch.forkjoin.ForkJoinPool.runWorker(ForkJoinPool.java:1979)
  at akka.dispatch.forkjoin.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:107)
Caused by: java.lang.IllegalArgumentException: Illegal character in query at index 29: /api/hello/Marvel?from=Doctor>Strange
  at java.net.URI.create(URI.java:852)
  at com.lightbend.lagom.internal.server.ServiceRouter.$anonfun$routes$2(ServiceRouter.scala:72)
  at scala.PartialFunction$Unlifted.applyOrElse(PartialFunction.scala:233)
  at scala.collection.TraversableOnce.collectFirst(TraversableOnce.scala:145)
  at scala.collection.TraversableOnce.collectFirst$(TraversableOnce.scala:132)
  at scala.collection.AbstractTraversable.collectFirst(Traversable.scala:104)
  at com.lightbend.lagom.internal.server.ServiceRouter.$anonfun$routes$1(ServiceRouter.scala:67)
  at play.api.routing.Router.handlerFor(Router.scala:40)
  at play.api.routing.Router.handlerFor$(Router.scala:39)
  at com.lightbend.lagom.internal.server.ServiceRouter.handlerFor(ServiceRouter.scala:36)
  at play.api.http.DefaultHttpRequestHandler.routeRequest(HttpRequestHandler.scala:206)
  at play.api.http.DefaultHttpRequestHandler.routeWithFallback$1(HttpRequestHandler.scala:124)
  at play.api.http.DefaultHttpRequestHandler.handlerForRequest(HttpRequestHandler.scala:156)
  at play.core.server.Server$.getHandlerFor(Server.scala:128)
  ... 24 common frames omitted
Caused by: java.net.URISyntaxException: Illegal character in query at index 29: /api/hello/Marvel?from=Doctor>Strange
  at java.net.URI$Parser.fail(URI.java:2848)
  at java.net.URI$Parser.checkChars(URI.java:3021)
  at java.net.URI$Parser.parseHierarchical(URI.java:3111)
  at java.net.URI$Parser.parse(URI.java:3063)
  at java.net.URI.<init>(URI.java:588)
  at java.net.URI.create(URI.java:850)
  ... 37 common frames omitted
```
