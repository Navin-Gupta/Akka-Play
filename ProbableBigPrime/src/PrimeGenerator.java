import java.math.BigInteger;
import java.util.Random;

public class PrimeGenerator implements Runnable {

    private Results results;

    public PrimeGenerator(Results results){
        this.results = results;
    }
    @Override
    public void run() {
        BigInteger bigInteger = new BigInteger(2000, new Random());
        // System.out.println(bigInteger.nextProbablePrime());

        // add prime to collection  (results)
        // this.results.getPrimes().add(bigInteger.nextProbablePrime());
        this.results.addPrime(bigInteger.nextProbablePrime());
    }
}
