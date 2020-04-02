import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.Duration;
import java.util.SortedSet;
import java.util.TreeSet;

public class GuardianActor extends AbstractBehavior<GuardianActor.Command> {

    // common interface for messages to be received by Guardian Actor
    public interface Command extends Serializable{}

    // MEssage class for message from main
    public static class InstructionCommand implements  Command{
        public static final long serialVersionUID = 1L;
        private String message;
        // virtual actor ref not having command structure but data structure
        private ActorRef<SortedSet<BigInteger>> sender;

        /*public InstructionCommand(String message){
            this.message = message;
        }*/

        public InstructionCommand(String message, ActorRef<SortedSet<BigInteger>> sender) {
            this.message = message;
            this.sender = sender;
        }

        public String getMessage(){
            return this.message;
        }

        public ActorRef<SortedSet<BigInteger>> getSender() {
            return sender;
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

    // message class for message to be send to self
    private class NoResponseReceivedCommand implements Command{
        public static final long serialVersionUID = 1L;
        // reference to the child actor to resend message
        private ActorRef<PrimeGeneratorActor.Command> generator;

        public NoResponseReceivedCommand(ActorRef<PrimeGeneratorActor.Command> generator) {
            this.generator = generator;
        }

        public ActorRef<PrimeGeneratorActor.Command> getGenerator() {
            return generator;
        }
    }


    public GuardianActor(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create(){
        return Behaviors.setup(GuardianActor::new);
    }

    private SortedSet<BigInteger> primes = new TreeSet<>();

    // helper method to send an ask message to PrimeGenerator
    private void askGeneratorForPrime(ActorRef<PrimeGeneratorActor.Command> generator){
        // sending message using tell
        // generator.tell(new PrimeGeneratorActor.Command("generate", getContext().getSelf()));

        /*getContext().ask(<type of message to expect back>,
                         <actor ref where to send message>,
                         <expected time to receive message>,
                         <instruction to send the message (lambda exp)>,
                         <code construct to identify that response(lambda exp)>
        );*/
        getContext().ask(Command.class,
                         generator,
                         Duration.ofSeconds(5),
                         //(<current Actor Ref>/<getContext().getSelf()>) ->{}
                        (me) -> new PrimeGeneratorActor.Command("generate", me),
                        (response, throwable)-> {
                            if(response != null){
                                // success message returned from child actor - add it to message queue...
                                return response; // ~ ResultCommand
                            }else{
                                System.out.println("Child Actor " + generator.path() + " failed to respond");
                                // we want to send message again
                                // an actor generating a message for itself to resend message to child actor
                                // add it to message queue
                                return new NoResponseReceivedCommand(generator);
                            }
                        }
                );
    }

    // need to maintain external sender
    private ActorRef<SortedSet<BigInteger>> sender;

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                // .onMessage(<type>, <lambda>)
                .onMessage(InstructionCommand.class, command -> {
                    if(command.getMessage().equals("start")){
                        // maintain sender for future use
                        this.sender = command.getSender();
                        // initiate child actors...
                        for(int i = 0 ; i < 10 ; i++){
                            ActorRef<PrimeGeneratorActor.Command> generator = getContext().spawn(PrimeGeneratorActor.create(), "generator" + (i + 1));
                            // instead of telling PrimeGenerator, we will ask
                            this.askGeneratorForPrime(generator);
                        }
                    }
                    return Behaviors.same();
                })
                .onMessage(ResultCommand.class, command -> {
                    this.primes.add(command.getPrime());
                    System.out.println("Received " + primes.size() + " prime numbers");
                    if(this.primes.size() == 10){
                        // respond back to external environment
                        this.sender.tell(this.primes);
                        // this.primes.forEach(System.out :: println);
                    }
                    return Behaviors.same();
                })
                .onMessage(NoResponseReceivedCommand.class, command -> {

                    System.out.println("Retrying to send message to " + command.getGenerator().path());
                    // resend message to child actor
                    this.askGeneratorForPrime(command.getGenerator());
                    return Behaviors.same();
                })
                .build();
    }
}
