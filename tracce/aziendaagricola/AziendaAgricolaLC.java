package tracce.aziendaagricola;



import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AziendaAgricolaLC extends AbstractAziendaAgricola {

    private int qtSacchetti=200;
    private ReentrantLock lock = new ReentrantLock();
    Condition possoPagare = lock.newCondition();
    Condition possoRitirare = lock.newCondition();
    Condition possoRicaricare = lock.newCondition();

    List<Thread> codaRitiro=new LinkedList<>();
    List<Thread> codaPagare=new LinkedList<>();

    boolean stopRitiri=false;
    boolean ritiroInCorso=false;


    @Override
    protected void paga(int N) throws InterruptedException {
        lock.lock();
        try {
            codaPagare.addLast(Thread.currentThread());

            while(!possoPagare()){
                possoPagare.await();
            }
            incasso+=N*COSTO_SACCHETTO;
            codaRitiro.add(Thread.currentThread());
            codaPagare.removeFirst();
            possoPagare.signalAll();
        }finally {
            lock.unlock();
        }

    }
    private boolean possoPagare() {
        return !codaPagare.isEmpty() && codaPagare.getFirst().equals(Thread.currentThread());
    }

    @Override
    protected void restock() throws InterruptedException {
        lock.lock();
        try {
            while (!possoRicaricare()) {
                possoRicaricare.await();
            }
            stopRitiri=!stopRitiri;
            lock.unlock();
            TimeUnit.SECONDS.sleep(5);
            lock.lock();
            qtSacchetti=200;
        } finally {
            stopRitiri=!stopRitiri;
            possoRitirare.signalAll();
            lock.unlock();

        }
    }

    private boolean possoRicaricare(){
        return !stopRitiri && !ritiroInCorso;
    }






    @Override
    protected void ritira() throws InterruptedException {
            lock.lock();

            try {
                while(!codaRitiro.getFirst().equals(Thread.currentThread()) && stopRitiri && ritiroInCorso ){
                    possoRitirare.await();
                }

                ritiroInCorso=true;
                qtSacchetti--;

                if(qtSacchetti==0){possoRicaricare.signal();}

                }
            finally {
                ritiroInCorso=false;
                lock.unlock();
                }
    }
}
