import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.SortedSet;
import java.util.TreeSet;

public class GuardianActor extends AbstractBehavior<GuardianActor.Command> {

    // common interface for messages to be received by Guardian Actor
    public interface Command extends Serializable{}

    // MEssage class for message from main
    public static class InstructionCommand implements  Command{
        public static final long serialVersionUID = 1L;
        private String message;

        public InstructionCommand(String message){
            this.message = message;
        }

        public String getMessage(){
            return this.message;
        }
    }

    // message class for message to be received from Child Actor
    public static class ResultCommand implements Command{
        public static final long serialVersionUID = 1L;
        private BigInteger prime;

        public ResultCommand(BigInteger prime) {
            this.prime = prime;
        }

        public BigInteger getPrime() {
            return prime;
        }
    }


    public GuardianActor(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create(){
        return Behaviors.setup(GuardianActor::new);
    }

    private SortedSet<BigInteger> primes = new TreeSet<>();

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                // .onMessage(<type>, <lambda>)
                .onMessage(InstructionCommand.class, command -> {
                    if(command.getMessage().equals("start")){
                        // initiate child actors...
                        for(int i = 0 ; i < 10 ; i++){
                            ActorRef<PrimeGeneratorActor.Command> generator = getContext().spawn(PrimeGeneratorActor.create(), "generator" + (i + 1));
                            // send the message
                            generator.tell(new PrimeGeneratorActor.Command("generate", getContext().getSelf()));
                            generator.tell(new PrimeGeneratorActor.Command("generate", getContext().getSelf()));
                        }
                    }
                    return this;
                })
                .onMessage(ResultCommand.class, command -> {
                    this.primes.add(command.getPrime());
                    System.out.println("Received " + primes.size() + " prime numbers");
                    if(this.primes.size() == 10){
                        this.primes.forEach(System.out :: println);
                    }
                    return this;
                })
                .build();
    }



}
