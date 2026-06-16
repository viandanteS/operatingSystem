package tracce.callcenter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CallCenterSem extends CallCenter{

    private final Semaphore queue = new Semaphore(0,true); // quando c'è almeno un permesso il cliente può essere servito
    private final Semaphore[] attesaOperatori = new Semaphore[NUM_OPE]; // array di semaphore per sbloccare l'operatore che ha concluso un cliente
    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore[] attendoSoluzione = new Semaphore[NUM_CLIENTI];
    private final Semaphore possoFornire = new Semaphore(0); // pensavo potesse aiutarmi a risolvere il problema di quando non ci sono clienti e l'operatore deve attendere per averne.

    private final List<Cliente> prelievo = new LinkedList<>();

    Map<Cliente,Operatore> map = new HashMap<>();



    public CallCenterSem(int num_ope, int num_clienti) {
        super(num_ope, num_clienti);
        for(int i=0;i<num_ope;i++){
            attesaOperatori[i]=new Semaphore(0);
        }
        for(int i=0;i<num_clienti;i++){
            attendoSoluzione[i]=new Semaphore(0);
        }
    }


    @Override
    protected void richiediAssistenza() throws InterruptedException {
        queue.acquire();

        Cliente c = (Cliente) Thread.currentThread();
        mutex.acquire();
        prelievo.addLast( c );
        mutex.release();
        possoFornire.release();

        attendoSoluzione[c.getMyId()].acquire();
    }

    @Override
    protected void fornisciAssistenza() throws InterruptedException {
        //ci sono clienti in coda?
        queue.release();
        possoFornire.acquire();//?????

        Operatore op =(Operatore) Thread.currentThread();

        mutex.acquire();
        Cliente c = prelievo.removeFirst();
        map.put(c,op);
        mutex.release();

        TimeUnit.SECONDS.sleep(2);

        attendoSoluzione[c.getMyId()].release();


    }

    @Override
    protected void terminaChiamata() throws InterruptedException {
        mutex.acquire();
        Operatore opCheMiHaServito = map.remove((Cliente) Thread.currentThread());
        mutex.release();
        TimeUnit.SECONDS.sleep(1);
        attesaOperatori[opCheMiHaServito.getIdOp()].release();
    }

    @Override
    protected void prossimoCliente() throws InterruptedException {
        Operatore op =(Operatore) Thread.currentThread();
        attesaOperatori[op.getIdOp()].acquire();
    }
}
