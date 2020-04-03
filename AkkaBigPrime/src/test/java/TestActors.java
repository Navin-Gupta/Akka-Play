import akka.actor.testkit.typed.CapturedLogEvent;
import akka.actor.testkit.typed.javadsl.BehaviorTestKit;
import akka.actor.testkit.typed.javadsl.TestInbox;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestActors {

    @Test
    void testPrimeGeneratorActor(){
        // create an actor reference for test
        BehaviorTestKit<PrimeGeneratorActor.Command> testActor =
                        BehaviorTestKit.create(PrimeGeneratorActor.create());

        // also need a test guardian actor ( TestInbox )
        // wrapper around test Actor ref
        TestInbox<GuardianActor.Command> testInbox = TestInbox.create();

        // create the message to send
        PrimeGeneratorActor.Command message = new PrimeGeneratorActor.Command("generate",testInbox.getRef());

        // send message to actor
        testActor.run(message);

        // fetch the log entries
        List<CapturedLogEvent> logMessages = testActor.getAllLogEntries();
        assertEquals(logMessages.size(), 1);
        assertEquals(logMessages.get(0).message(), "Success");
        assertEquals(logMessages.get(0).level(), Level.INFO);
    }
}
