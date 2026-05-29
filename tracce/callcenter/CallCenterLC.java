package tracce.callcenter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CallCenterLC extends CallCenter {

    private final ReentrantLock lock = new ReentrantLock();

    // N.B. TRACCIA: Commento per ogni condition
    // Usata dai clienti per attendere finché non vengono presi in carico e la soluzione è pronta
    private final Condition condClienti = lock.newCondition();

    // Usata dagli operatori per attendere nuovi clienti in coda o che il cliente finisca di applicare la soluzione
    private final Condition condOperatori = lock.newCondition();

    private final LinkedList<Thread> coda = new LinkedList<>();

    // Mappe di stato per tenere traccia delle specifiche chiamate
    private final Map<Thread, Thread> operatoriClienti = new HashMap<>(); // Mappa Operatore -> Cliente
    private final Map<Thread, Boolean> soluzionePronta = new HashMap<>(); // Cliente -> ha ricevuto la soluzione?
    private final Map<Thread, Boolean> chiamataTerminata = new HashMap<>(); // Cliente -> ha applicato e finito?

    public CallCenterLC(int num_ope, int num_clienti) {
        super(num_ope, num_clienti);
    }

    @Override
    public void richiediAssistenza() throws InterruptedException {
        lock.lock();
        try {
            Thread io = Thread.currentThread();
            coda.add(io);
            soluzionePronta.put(io, false);
            chiamataTerminata.put(io, false);

            System.out.println(io.getName() + " in coda per richiedere assistenza.");
            condOperatori.signalAll(); // Sveglia un eventuale operatore in attesa di clienti
            // Il cliente attende non solo di essere estratto, ma che la sua soluzione sia PRONTA
            while (!soluzionePronta.get(io)) {
                condClienti.await();
            }
            System.out.println(io.getName() + " ha ricevuto la soluzione. Inizia ad applicarla...");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void fornisciAssistenza() throws InterruptedException {
        lock.lock();
        Thread cliente;
        try {
            while (coda.isEmpty()) {
                condOperatori.await();
            }
            cliente = coda.removeFirst();
            operatoriClienti.put(Thread.currentThread(), cliente);
            System.out.println(Thread.currentThread().getName() + " prende in carico " + cliente.getName());
        } finally {
            // RILASCIO IL LOCK per simulare l'elaborazione (1-10 min) senza bloccare il sistema
            lock.unlock();
        }

        // Fuori dal lock: l'operatore cerca la soluzione
        int tempoSoluzione = ThreadLocalRandom.current().nextInt(1, 11);
        TimeUnit.SECONDS.sleep(tempoSoluzione); // Simuliamo minuti in secondi

        // RIACQUISISCO IL LOCK per aggiornare lo stato e attendere in linea
        lock.lock();
        try {
            soluzionePronta.put(cliente, true);
            condClienti.signalAll(); // Risveglio il cliente: la soluzione è pronta!

            // L'operatore resta "in linea" finché il cliente non riattacca
            while (!chiamataTerminata.get(cliente)) {
                condOperatori.await();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void terminaChiamata() throws InterruptedException {
        lock.lock();
        try {
            Thread io = Thread.currentThread();
            chiamataTerminata.put(io, true);
            System.out.println(io.getName() + " ha applicato la soluzione e riattacca.");

            // Sveglia l'operatore che era in attesa in linea
            condOperatori.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void prossimoCliente() throws InterruptedException {
        lock.lock();
        try {
            Thread operatore = Thread.currentThread();
            Thread cliente = operatoriClienti.remove(operatore);

            // Pulizia delle mappe per liberare memoria
            if (cliente != null) {
                soluzionePronta.remove(cliente);
                chiamataTerminata.remove(cliente);
            }
            System.out.println(operatore.getName() + " ha chiuso la pratica ed è di nuovo libero.");
        } finally {
            lock.unlock();
        }
    }
}