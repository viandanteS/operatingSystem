package problemiclassici.contocorrente;

import java.util.concurrent.Semaphore;

public class ContoCorrenteSem extends AbstractCC{

    private Semaphore mutex = new Semaphore(1);

    public ContoCorrenteSem(int saldoIniziale){
        super(saldoIniziale);
    }

    @Override
    public void deposita(int x) {
        try {
            mutex.acquire();
            this.saldo += x;
            mutex.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void preleva(int x) {
        try {
            mutex.acquire();
            this.saldo -= x;
            mutex.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /*
        Un solo thread alla volta potra accedere all'operazione di modifica del saldo. --> Atomica.
     */


    public static void main(String[] args) throws InterruptedException {
        final int saldoIniziale = 100000;
        final int X = 100;
        final int N = 5000;
        Correntista[] correntisti = new Correntista[200];
        ContoCorrenteSem cc = new ContoCorrenteSem(saldoIniziale);
        for (int i = 0; i < 200; i++) {
            correntisti[i] = new Correntista(cc, N, X);
            correntisti[i].start();
        }
        for (int i = 0; i < 200; i++) {
            correntisti[i].join();
        }

        if ((saldoIniziale - cc.getSaldo()) == 0) {
            System.out.println("Corretto");
        } else {
            System.out.println("Errore");
        }
    }

}
