import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.AskPattern;

import java.math.BigInteger;
import java.time.Duration;
import java.util.SortedSet;
import java.util.concurrent.CompletionStage;

public class MyApp {
    public static void main(String[] args) {

        // create an ActorSystem
        ActorSystem<GuardianActor.Command> bigPrimeActorSystem =
                ActorSystem.create(GuardianActor.create(), "BigPrimeActor");

        // send the message to system (guardianactor)
        // bigPrimeActorSystem.tell(new GuardianActor.InstructionCommand("start"));

       /* getContext().ask(<type of message to expect back>,
                         <actor ref where to send message>,
                         <expected time to receive message>,
                         <instruction to send the message (lambda exp)>,
                         <code construct to identify that response(lambda exp)>
        );*/

        // replace tell() pattern with ask() pattern
        // 1. AskPattern : virtual context
        // 2. we are not going to receive message back from Actor system
        /*AskPattern.ask(<Actor System where to send message>,
                        <instruction to send the message (lambda exp)>,
                        <expected time to receive result>
                        <schedular>);*/

        // CompletionStage is future implementation
        CompletionStage<SortedSet<BigInteger>> futureResult = AskPattern.ask(bigPrimeActorSystem,
                        // (Virtual Context Reference)
                        (me) -> new GuardianActor.InstructionCommand("start", me),
                        Duration.ofSeconds(6),
                        bigPrimeActorSystem.scheduler());

        // handling of response will be callback pattern
        // define a callback on futureResult
        futureResult.whenComplete(
                (response, failure)->{
                        if(response != null){
                            response.forEach(System.out :: println);
                        }else{
                            System.out.println("Actor System didn't responded in time");
                        }
                        bigPrimeActorSystem.terminate();
                });



    }
}
