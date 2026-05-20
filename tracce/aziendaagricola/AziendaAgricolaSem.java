package tracce.aziendaagricola;


import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AziendaAgricolaSem extends AbstractAziendaAgricola{

    private Semaphore mutexAcquisto = new Semaphore(1,true);
    private Semaphore possoRestockare = new Semaphore(0);
    private Semaphore mutexContatore = new Semaphore(1);
    private Semaphore possoRitirare = new Semaphore(1,true);
    private Semaphore sacchiDisponibili = new Semaphore(200);
    private int qtSacchetti = 200;

    private static AtomicInteger counter = new AtomicInteger(0);

    public int getSacchettiRichiesti(){
        return counter.get();
    }

    @Override
    protected void paga(int N) throws InterruptedException {
        mutexAcquisto.acquire();
        System.out.format("Pagati %d sacchetti\n",N);
        incasso+=N*COSTO_SACCHETTO;
        counter.addAndGet(N);
        mutexAcquisto.release();
    }

    @Override
    protected void restock() throws InterruptedException {
        possoRestockare.acquire();
        System.out.println("Restocking in corso...");
        TimeUnit.SECONDS.sleep(1);
        mutexContatore.acquire();
        qtSacchetti += 200;
        mutexContatore.release();
        sacchiDisponibili.release(200);
    }

    @Override
    public void ritira(int N) throws InterruptedException {
        possoRitirare.acquire();
        for (int i=N;i>0;i--){
            sacchiDisponibili.acquire();
            mutexContatore.acquire();
            qtSacchetti--;
            System.out.println(qtSacchetti);
            if(qtSacchetti==0){possoRestockare.release();}
            mutexContatore.release();
            TimeUnit.MILLISECONDS.sleep(10);
        }
        possoRitirare.release();
    }

}
