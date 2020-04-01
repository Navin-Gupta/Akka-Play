import java.math.BigInteger;
import java.util.SortedSet;
import java.util.TreeSet;

public class Results {
    private SortedSet<BigInteger> primes;

    public Results(){
        this.primes = new TreeSet<>();
    }

    // add method to expose properties in sync manner

    // method to add new prime
    public void addPrime(BigInteger prime){
        synchronized (this) {
            this.primes.add(prime);
        }
    }

    // method to get size
    public int getSize(){
        synchronized (this) {
            return this.primes.size();
        }
    }

    // to display prime numbers
    public void print(){
        synchronized (this) {
            this.primes.forEach(System.out::println);
        }
    }



    // don't return the complete collection
    /*public SortedSet<BigInteger> getPrimes(){
        return this.primes;
    }*/
}
