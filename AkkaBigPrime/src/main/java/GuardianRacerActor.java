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

public class GuardianRacerActor extends AbstractBehavior<GuardianRacerActor.Command> {

    // common interface for messages to be received by Guardian Actor
    public interface Command extends Serializable{}

    public class GetPositionCommand implements  Command{

    }

    public GuardianRacerActor(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create(){
        return Behaviors.setup(GuardianRacerActor::new);
    }

    private SortedSet<BigInteger> primes = new TreeSet<>();

    private Object TIMER_KEY1;
    private Object TIMER_KEY2;

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.class, command -> {
                    // send a message to racer actor to start the race
                    // start the timer
                    return Behaviors.withTimers(timers->{
                        timers.startTimerAtFixedRate(TIMER_KEY1,
                                // what message to add in your message queue
                                new GetPositionCommand(),
                                Duration.ofSeconds(1)
                                );

                        //  launch another timer

                        return Behaviors.same();
                    });

                    // return Behaviors.same();
                })
                .onMessage(GetPositionCommand.class, command ->{
                        // send message to child actor to respond with its current position
                        return this;
                        })
                // on message to receive current position
                // on message for race finishing
                .onMessage(Command.class, command->{
                    // display results
                    // stop Timer
                    return Behaviors.withTimers(timer->{
                       // cancel all timers
                        timer.cancelAll();

                       // cancel specific timer
                       // timer.cancel(TIMER_KEY1);
                       // stop the current actor
                       return Behaviors.stopped();
                       // current actor will ignore all incoming messages
                       // return Behaviors.ignore();
                    });
                    // return Behaviors.same();
                })
                .build();
    }



}
