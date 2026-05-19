package problemiclassici.contocorrente;

import java.util.concurrent.TimeUnit;
import java.util.Random;


public class Correntista extends Thread {

    private final int MIN_ATTESA= 30;
    private final int MAX_ATTESA= 60;
    private AbstractCC cc;
    private final int N, X;
    Random rand = new Random();

    public Correntista(AbstractCC cc, int N,int X) {
        if( N%2!=0 || X<=0 ) throw new RuntimeException("Errore");
        this.N=N;
        this.X=X;
        this.cc = cc;
    }

    void attesaCasuale() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(MAX_ATTESA-MIN_ATTESA+1)+MIN_ATTESA);
    }



    public void run() {

        for (int i = 0; i < N/2; i++) {
            cc.deposita(X);
            cc.preleva(X);
        }
    }


}
