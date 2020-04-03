package com.cts.training.employeestream;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.RunnableGraph;
import com.cts.training.employeestream.flow.EmployeeFlow;
import com.cts.training.employeestream.sink.EmployeeSink;
import com.cts.training.employeestream.source.EmployeeSource;

public class MyApp {
    public static void main(String[] args) {
        // not a typed actor system
        ActorSystem actorSystem = ActorSystem.create();

        // create a materializer
        Materializer materializer = ActorMaterializer.create(actorSystem);

        // create a graph
        RunnableGraph<NotUsed> runnableGraph = new EmployeeSource().source().via(new EmployeeFlow().flow()).to(new EmployeeSink().sink());

        // run the graph
        runnableGraph.run(materializer);
    }
}
