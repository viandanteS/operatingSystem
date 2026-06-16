package tracce.aereoporto;


import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class BancoCheckInSem extends BancoCheckIn{

    private Semaphore queue = new Semaphore(1,true);
    private Semaphore consegnaCartaImbarco = new Semaphore(0);
    private Semaphore passeggeroAlBanco = new Semaphore(0);
    private Semaphore bancoLibero = new Semaphore(0);

    private boolean cartaImbarcoRicevuta;
    private int numBagagliPassegero;
    @Override
    void deponeBagagli(int N) throws InterruptedException {
        queue.acquire();
        cartaImbarcoRicevuta = false;
        numBagagliPassegero = N;
        //passegero depone i bagagli
        passeggeroAlBanco.release();
        consegnaCartaImbarco.acquire();
    }

    @Override
    void pesaERegistra() throws InterruptedException {

        passeggeroAlBanco.acquire();
        System.out.println("Serving a new passenger...");
        TimeUnit.MILLISECONDS.sleep(numBagagliPassegero*100);
        System.out.println("luggage registered...");
        consegnaCartaImbarco.release();
        //una volta pesato, e registrato i bagli attendendo 10*N, rilascio un permesso su semaforo
        // bloccante alla fine di deponeBagagli tipo consegna carta d'imbarco
    }

    //il metodo si attiva solo dopo che tutti i bagagli sono stati registrati
    @Override
    void riceviCartaImbarco() throws InterruptedException {

        cartaImbarcoRicevuta = true;
        System.out.println(Thread.currentThread().getName()+" Carta imbarco ricevuta. Check-in completato");

        bancoLibero.release();
    }

    @Override
    void prossimoPasseggero() throws InterruptedException {
        bancoLibero.acquire();
        queue.release();
    }

    public static void main(String[] args) throws InterruptedException {
        BancoCheckInSem bancoCheckIn = new BancoCheckInSem();
        bancoCheckIn.test();

    }
}
