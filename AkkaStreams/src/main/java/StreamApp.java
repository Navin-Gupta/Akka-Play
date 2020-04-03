import akka.Done;
import akka.NotUsed;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.util.concurrent.CompletionStage;

public class StreamApp {
    public static void main(String[] args) {

         // Create an ActorSystem
        // not a typed actor system
        ActorSystem actorSystem = ActorSystem.create();

        // create a materializer
        Materializer materializer = ActorMaterializer.create(actorSystem);


         // source will give out large number of integers
         Source<Integer, NotUsed> source = Source.range(0, 20000000);

         // flow to transform integer into string
         Flow<Integer, String, NotUsed> flow = Flow.fromFunction(val-> val.toString());

         // sink to consume string  (display them)
        Sink<String, CompletionStage<Done>> sink = Sink.foreach(System.out::println);

        // create a graph (typesafe)
        RunnableGraph<NotUsed> runnableGraph =  source.via(flow).to(sink);  // Graph DSL

        // run this graph
        // materializer : abstract wrapper of Actor System
        // graph run on actor system
        runnableGraph.run(materializer);
    }
}
