import akka.actor.typed.ActorSystem;

public class MyApp {
    public static void main(String[] args) {

        // create an ActorSystem
        ActorSystem<GuardianActor.Command> bigPrimeActorSystem =
                ActorSystem.create(GuardianActor.create(), "BigPrimeActor");

        // send the message to system (guardianactor)
        bigPrimeActorSystem.tell(new GuardianActor.InstructionCommand("start"));
    }
}
