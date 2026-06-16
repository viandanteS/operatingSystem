package tracce.biblioteca;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BibliotecaLC extends BibliotecaAbstract {


    private final Lock lock = new ReentrantLock();
    private final Condition possoUscire = lock.newCondition();
    private final Condition possoRegistrare = lock.newCondition();
    private final Condition possoAndareAlProssimo = lock.newCondition();
    private final Condition puoiAvanzare = lock.newCondition();

    private boolean bancoLibero = true;


    List<Thread> codaTesserati = new LinkedList<>(); //type 1
    List<Thread> codaEsterni = new LinkedList<>(); // type 0

    List<Utente> codaUscita = new LinkedList<>();

    Map<Utente, String> registroPrestiti = new HashMap<Utente, String>();


    @Override
    void richiestiPrestito() throws InterruptedException {
        Utente thisUtente = (Utente) Thread.currentThread();
        lock.lock();
        try {
            if (thisUtente.getType() == 1) {
                codaTesserati.add(thisUtente);
            } else {
                codaEsterni.add(thisUtente);
            }

        } finally {
            possoRegistrare.signal();
            lock.unlock();
        }
    }


    private boolean possoRegistrare() {
        return !codaTesserati.isEmpty() || !codaEsterni.isEmpty();
    }

    @Override
    void registraPrestito() throws InterruptedException {
        lock.lock();
        Utente serving = null;
        try {
            while (!possoRegistrare()) {
                possoRegistrare.await();
            }
            bancoLibero = false;

            if (!codaTesserati.isEmpty()) {
                serving = (Utente) codaTesserati.removeFirst();
            } else {
                serving = (Utente) codaEsterni.removeFirst();
            }
            registroPrestiti.put(serving, serving.getCodiceLibro());

        } finally {
            codaUscita.add(serving);
            possoUscire.signalAll();
            lock.unlock();
        }
    }

    private boolean possoUscire(){
        var user = (Utente) Thread.currentThread();
        return registroPrestiti.containsKey(user) && codaUscita.getFirst() == user;
    }

    @Override
    void esci() throws InterruptedException {
        lock.lock();
        try {
            Utente u = (Utente) Thread.currentThread();
            while (!possoUscire()) {
                possoUscire.await();
            }
        } finally {
            codaUscita.removeFirst();
            bancoLibero = true;
            possoAndareAlProssimo.signal();
            lock.unlock();
        }
    }

    @Override
    void prossimoUtente() throws InterruptedException {
        lock.lock();
        try {
            while (!bancoLibero) {
                possoAndareAlProssimo.await();
            }
        } finally {
            puoiAvanzare.signalAll();
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BibliotecaLC b = new BibliotecaLC();
        b.test(25);
    }
}

