package tracce.prontosoccorso;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProntoSoccorsoLC extends ProntoSoccorso{


    private final Lock lock = new ReentrantLock();
    private final Condition ottieniCodice = lock.newCondition(); // condition per mettersi in attesa per l'estrazione del colore codice
    private final Condition possoVisitare = lock.newCondition(); // condition per far attendere il medico che ci sia un paziente in coda per farsi visitare
    private final Condition puoAndare = lock.newCondition(); // condition per segnalare al paziente appena visitato di sgomberare
    private final Condition accettato = lock.newCondition(); // condition per segnalare al paziente che è stato accettato in visita dal medico


    private final List<Thread> codaVerdi= new LinkedList<>();
    private final List<Thread> codaGiallo= new LinkedList<>();
    private final List<Thread> codaRosso= new LinkedList<>();

    private final Random random = new Random();

    private final List<Thread> codaColor = new LinkedList<>();

    private boolean finita = false;
    private long inVisita = -1 ;



    private boolean possoVisitare(){
        return !(codaRosso.isEmpty() && codaGiallo.isEmpty() && codaVerdi.isEmpty());
    }

    @Override
    void iniziaVisita() throws InterruptedException {
        lock.lock();
        int durataVisita;
        try{
            while(!possoVisitare()){
                possoVisitare.await();
            }
            finita=false;
            if(!codaRosso.isEmpty()){
                inVisita = codaRosso.removeFirst().threadId();
                durataVisita=random.nextInt(200,400);
            } else if (!codaGiallo.isEmpty()) {
                inVisita = codaGiallo.removeFirst().threadId();
                durataVisita=random.nextInt(150,200);
            } else {
                inVisita = codaVerdi.removeFirst().threadId();
                durataVisita=random.nextInt(100,150);
            }
        }finally {
            accettato.signal();
            lock.unlock();
        }
        System.out.println("Inizia visita "+inVisita);
        TimeUnit.MILLISECONDS.sleep(durataVisita);
    }

    @Override
    void terminaVisita() throws InterruptedException {
        lock.lock();
        try{
         finita=true;
        }finally{
            puoAndare.signal();
            lock.unlock();
        }
    }

    @Override
    void accediPaziente() throws InterruptedException {
        lock.lock();
        codaColor.add(Thread.currentThread());
        try{
            while(!codaColor.getFirst().equals(Thread.currentThread())){
                ottieniCodice.await();
            }
            int codice = random.nextInt(0,3);
            if(codice==0){
                codaRosso.add(Thread.currentThread());
            } else if (codice==1) {
                codaGiallo.add(Thread.currentThread());
            }else if(codice==2){
                codaVerdi.add(Thread.currentThread());
            }
        }finally {
            possoVisitare.signal();
            lock.unlock();
        }

        lock.lock();
        try{
            while(!(inVisita==Thread.currentThread().threadId())){
                accettato.await();
            }
        }finally{
         lock.unlock();
        }
    }

    @Override
    void esciPaziente() throws InterruptedException {
        lock.lock();
        try{
            while(!finita){
                //finche non sei tu quello che il medico sta visitando e non è terminata la visita aspetta prima di andare
                puoAndare.await();
            }
        }finally {
            codaColor.remove(Thread.currentThread());
            ottieniCodice.signalAll();
            lock.unlock();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        ProntoSoccorsoLC ps = new ProntoSoccorsoLC();
        ps.test();
    }
}
