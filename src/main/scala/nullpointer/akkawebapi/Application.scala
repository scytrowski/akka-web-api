package nullpointer.akkawebapi

object Application extends App {
//  implicit val system: ActorSystem = ActorSystem()
//  implicit val ec: ExecutionContext = system.dispatcher
//  implicit val materializer: ActorMaterializer = ActorMaterializer()
  WebServer.startServer("localhost", 8080)
}
