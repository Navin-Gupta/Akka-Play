import java.math.BigInteger;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class SingleThreadedImpl {
    public static void main(String[] args) {

        Long start = System.currentTimeMillis();

        SortedSet<BigInteger> primes = new TreeSet<>();

        while(primes.size() < 10){
            // 1. range : eg: 2000 : 2 pow 0 -- 1999
            // 2. random number
            // generate a random big integer
            BigInteger bigInteger = new BigInteger(2000, new Random());
            primes.add(bigInteger.nextProbablePrime());
        }

        Long end = System.currentTimeMillis();
        // System.out.println(primes);
        primes.forEach(System.out :: println);
        System.out.println("Time Taken : " + (end - start) + " ms.");
    }
}
