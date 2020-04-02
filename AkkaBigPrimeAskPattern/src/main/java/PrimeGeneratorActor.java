import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

public class PrimeGeneratorActor extends AbstractBehavior<PrimeGeneratorActor.Command> {

    public static class Command implements Serializable{
        private String message;
        private ActorRef<GuardianActor.Command> sender;

        public Command(String message, ActorRef<GuardianActor.Command> sender) {
            this.message = message;
            this.sender = sender;
        }

        public String getMessage() {
            return message;
        }

        public ActorRef<GuardianActor.Command> getSender() {
            return sender;
        }
    }

    public PrimeGeneratorActor(ActorContext context) {
        super(context);
    }

    public static Behavior<PrimeGeneratorActor.Command> create(){
        return Behaviors.setup(PrimeGeneratorActor::new);
    }

    private  BigInteger prime;



    public Receive<Command> createReceive() {
       return whenWeDontHavePrimeNumber();
    }

    public Receive<Command> whenWeDontHavePrimeNumber(){
        return newReceiveBuilder()
                .onMessage(PrimeGeneratorActor.Command.class, command -> {
                    if(command.getMessage().equals("generate")){
                        // generate big prime
                        BigInteger bigInteger = new BigInteger(2000, new Random());
                        this.prime = bigInteger.nextProbablePrime();
                        // send the big Prime back to guardian
                        command.getSender().tell(new GuardianActor.ResultCommand(prime));
                    }
                    //  going ahead whenAlreadyHavePrime() method will handle messages
                    return whenAlreadyHavePrime();
                })
                .build();
    }

   // alternate behaviors for the actor
    public Receive<Command> whenAlreadyHavePrime(){
       return newReceiveBuilder()
               .onAnyMessage(command -> {
                   // 1. might not return any message back
                   // 2. might return same prime number
                   command.getSender().tell(new GuardianActor.ResultCommand(prime));
                   return Behaviors.same(); // ~ return this
               })
               .build();
    }

}
