package sisop04.es41;
import java.util.concurrent.Semaphore;

public class Printer {

    private static Semaphore semaphore = new Semaphore(0);

    static class P1 extends Thread {
        public void run() {
            System.out.print("A ");
            semaphore.release();
        }
    }

    static class P2 extends Thread {
        public void run() {
            try {
                semaphore.acquire();
                System.out.println("B");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {

        for(int i=0;i<100;i++) {
            new P2().start();
            new P1().start();
        }
        /* questo modo comunque non garantisce che vengano stampati A B A B A B sempre..
            questo garantisce solamente che B non possa mai stampare prima che un A abbia fatto una release.
            ma ciò non toglie che più thread "A" possano fare più stampe e release di fila.
         */
    }



}
