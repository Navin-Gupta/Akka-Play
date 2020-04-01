import java.util.ArrayList;
import java.util.List;

public class MultiThreadedImpl {
    public static void main(String[] args) throws InterruptedException{
        Long start = System.currentTimeMillis();

        // object to hold collection
        Results results = new Results();

        // share result object with threads
        Runnable task = new PrimeGenerator(results);

        // Tracker object
        Runnable tracker = new Tracker(results);
        Thread trackerThread = new Thread(tracker);
        trackerThread.start();

        // collection to hold all thread objects
        List<Thread> threads = new ArrayList<>();

        // creating 10 threads
        for(int i=0; i < 100; i++){
            Thread thread = new Thread(task);
            // add each thread in collection
            threads.add(thread);
            thread.start();
        }

        // block the current thread till all generators stops : join()
        for(Thread thread : threads){
            thread.join();
        }

        Long end = System.currentTimeMillis();
        // display all results
        // results.getPrimes().forEach(System.out :: println);
        results.print();
        System.out.println("Time Taken : " + (end - start) + " ms.");
    }
}
