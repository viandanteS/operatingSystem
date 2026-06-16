package tracce.pistakart;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PistaLC extends Pista {

    private final Lock lock = new ReentrantLock();
    private final Condition possoNoleggiare = lock.newCondition();
    private final Condition possoEntrare = lock.newCondition();


    private final List<Pilota> codaNoleggioS = new LinkedList<>();
    private final List<Pilota> codaNoleggioL = new LinkedList<>();
    private final List<Pilota> codaPista = new LinkedList<>();

    private int kartSNoleggiati = 0;
    private int kartLNoleggiati = 0;
    private int kartInPista = 0;

    private boolean possoNoleggiare(int eta) {
        return (eta == 0 && kartSNoleggiati < S && codaNoleggioS.getFirst().equals(Thread.currentThread()))
                || (eta == 1 && kartLNoleggiati < L && codaNoleggioL.getFirst().equals(Thread.currentThread()));
    }


    @Override
    void noleggia() throws InterruptedException {
        lock.lock();
        Pilota pilota = (Pilota) Thread.currentThread();

        if(pilota.getEta()==0) codaNoleggioS.add(pilota);
        else codaNoleggioL.add(pilota);

        while (!possoNoleggiare(pilota.getEta())) {
            possoNoleggiare.await();
        }
        if (pilota.getEta() == 0) {
            codaNoleggioS.removeFirst();
            kartSNoleggiati++;
        } else {
            codaNoleggioL.removeFirst();
            kartLNoleggiati++;
        }

        codaPista.add(pilota);
        lock.unlock();
    }

    private boolean possoEntrare(){
        return (kartInPista<CAPIENZA_PISTA) && codaPista.getFirst().equals(Thread.currentThread());
    }

    @Override
    int entraInPista() throws InterruptedException {
        lock.lock();
        Random rand= new Random();
        while(!possoEntrare()) {
            possoEntrare.await();
        }
        codaPista.removeFirst();
        kartInPista++;

        lock.unlock();
        return rand.nextInt(3,11);
    }

    @Override
    void lasciaPista() throws InterruptedException {
        lock.lock();

        kartInPista--;
        possoEntrare.signalAll();
        lock.unlock();

    }

    @Override
    void riconsegna() throws InterruptedException {
        lock.lock();
        Pilota pilota = (Pilota) Thread.currentThread();
        if(pilota.getEta()==0) {
            kartSNoleggiati--;
        }else {
            kartLNoleggiati--;
        }
        possoNoleggiare.signalAll();
        lock.unlock();

    }

    public static void main(String[] args) {
        PistaLC pistaLC = new PistaLC();
        pistaLC.test();
    }
}
