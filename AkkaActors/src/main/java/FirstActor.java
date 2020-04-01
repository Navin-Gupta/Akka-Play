import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

// # must import javadsl (typed)
// # AbstractBehavior (generic) : Message Typed : what type of messages this actor is able to process
public class FirstActor extends AbstractBehavior<String> {


    public FirstActor(ActorContext<String> context) {
        super(context);
    }

    // need to create factory method to intantiate an actor
    // would not return the complete object
    // shall return/expose only the behavior
    public static Behavior<String> create(){
        // inbuilt factory method of Behaviors which provides ActorContext
        // Behaviors.setup(<lambda expression>)
        /*return Behaviors.setup(context -> {
           return new FirstActor(context);
        });*/
        return Behaviors.setup(FirstActor::new);
    }

    // Behavior logic goes here
    public Receive<String> createReceive() {
        // ReceiveBuilder method gives a build method to build Receive
        return newReceiveBuilder()
                // add behavior
                // complex message // type matching
                // .onMessage()
                // value matching
                .onMessageEquals("Hello", ()->{
                    System.out.println("Hello");
                    return this;
                })
                .onMessageEquals("who are you", ()->{
                    System.out.println("My path is : " + getContext().getSelf().path());
                    return this;
                })
                .onMessageEquals("create a child actor", ()->{
                    // creating a child actor
                    ActorRef<String> childActor = getContext().spawn(FirstActor.create(), "childActor");
                    childActor.tell("who are you");
                    return this;
                })
                // intercept any message and allows to write the logic for it (lambda expression)
                // put the general one at the end
                .onAnyMessage(message ->{
                    // put your logic
                    System.out.println("I received : " + message);

                    return this;
                })
                .build();

    }
}
