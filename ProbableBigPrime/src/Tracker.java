public class Tracker  implements  Runnable{

    private  Results results;

    public Tracker(Results results){
        this.results = results;
    }
    // track the results after every second
    @Override
    public void run() {
        /*while(this.results.getPrimes().size() < 100){
            System.out.println("Got " + this.results.getPrimes().size() + " so far..");
            System.out.println(this.results.getPrimes());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        while(this.results.getSize() < 100){
            System.out.println("Got " + this.results.getSize() + " so far..");
            // System.out.println(this.results.getPrimes());
            this.results.print();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
