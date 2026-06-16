package tracce.casadicura;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CasaDiCuraLC extends CasaDiCuraAbstract{

    private final Lock lock = new ReentrantLock();
    private final Condition possoOperare = lock.newCondition(); //condition per sbloccare il medico quando il paziente è stato chiamato ed è in sala operatoria
    private final Condition possoLasciareSala = lock.newCondition();//condition per rilasciare il paziente quando l'operazione è conclusa
    private final Condition chiamato  = lock.newCondition();
    private final Condition codaEntrata = lock.newCondition();


    private final List<Long> codaOperazioni = new LinkedList<>();
    private int pazientiInSalaAttesa = 0;



    private boolean puoEntrare=false;
    private boolean pazienteInSalaOperatoria=false;
    private boolean operazioneTerminata=false;

    private boolean possoOperare(){
        return pazienteInSalaOperatoria && !operazioneTerminata;
    }

    @Override
    protected void chiamaEIniziaOperazione() throws InterruptedException {
        lock.lock();

        puoEntrare=true;
        chiamato.signalAll();

        while(!possoOperare()){
            possoOperare.await();
        }

        System.out.println("Surgeon: inizio operazione");
        lock.unlock();

    }

    @Override
    protected void fineOperazione() throws InterruptedException {
        lock.lock();
        operazioneTerminata=true;
        possoLasciareSala.signal();
        lock.unlock();
    }

    private boolean chiamato(){
        return Thread.currentThread().getId()==codaOperazioni.getFirst() && puoEntrare;
    }

    @Override
    protected void pazienteEntra() throws InterruptedException {
        lock.lock();

        while(pazientiInSalaAttesa >=3){
            codaEntrata.await();
        }

        codaOperazioni.addLast(Thread.currentThread().getId());
        pazientiInSalaAttesa++;

        while(!chiamato()){
            chiamato.await();
        }
        puoEntrare=false;
        pazientiInSalaAttesa--;
        pazienteInSalaOperatoria=true;
        possoOperare.signal();

        lock.unlock();
    }


    @Override
    protected void pazienteEsci() throws InterruptedException {
        lock.lock();

        while(!operazioneTerminata){
            possoLasciareSala.await();
        }
        operazioneTerminata=false;
        pazienteInSalaOperatoria=false;
        codaOperazioni.removeFirst();
        codaEntrata.signal();
        lock.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        CasaDiCuraLC casaDiCuraLC = new CasaDiCuraLC();
        casaDiCuraLC.test();
    }
}
