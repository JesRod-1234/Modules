import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeDemo {

    //Thread safe
    //Tråd säker = Att vi får inte dela en variabel mellan flera trådar om vi både skriver och ->
    // -> läser på den. Man får inte dela på en variabel som ska dela värde.

    private static AtomicInteger anInt = new AtomicInteger();

    //private static int anInt = 0;

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Future<?>[]futures = new Future[10];
        for (int i = 0; i < 10; i++) {
           futures[i] = executorService.submit(() -> doSomeHeavyWork());
        }
        for (var f: futures) {
            try {
                f.get();
            } catch(InterruptedException e){
                e.printStackTrace();
            }catch ( ExecutionException e){
                e.printStackTrace();
            }
        }
        System.out.println(anInt);
    }
    private static void doSomeHeavyWork(){
        // Massa kod
        // Vänta på filinlämning

        for (int i = 0; i < 10000; i++) {


            System.out.println(Thread.currentThread().getName() + ":" + anInt);
        }

    }
}
