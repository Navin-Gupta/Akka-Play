import akka.actor.typed.ActorSystem;

public class MyApp {
    public static void main(String[] args) {
        // Message type reflects the message type of parent actor
        // Factory method to create actor system
        // will going to initiate an actorsystem with a guardian actor in it
        ActorSystem<String> actorSystem =
                    ActorSystem.create(FirstActor.create(), "FirstActor");
        // pass a message to actor system -> message to guardian actor
        actorSystem.tell("Hello");
        // actorSystem.tell("This is second message");
        actorSystem.tell("who are you");
        actorSystem.tell("create a child actor");
        actorSystem.tell("This is third message");
    }
}
