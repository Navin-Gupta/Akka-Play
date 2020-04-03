import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Source;

import java.util.Objects;


public class StreamAppFluent {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create();

        Materializer materializer = ActorMaterializer.create(actorSystem);

        Source.range(0,2000000)
                .map(Objects::toString) // flow
                .runForeach(System.out :: println, materializer); // sink

    }
}
