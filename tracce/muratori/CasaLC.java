package tracce.muratori;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CasaLC extends AbstractCasa{


    private Lock lock = new ReentrantLock();

    private Condition possoScegliere;
    private Condition[] lavoroDisponibile;


    private final LinkedList<Thread> codaScelta = new LinkedList<>();
    private final LinkedList<Thread> codaMattoni = new LinkedList<>();
    private final LinkedList<Thread> codaCemento = new LinkedList<>();

    private boolean terminato = false;
    private final boolean[] pareteLibera;
    private final int[] lastTypeWas;

    private int lastDistribuited = 3;

    private int azioni = STRATI*4;
    private final Map<Thread,Integer> muratoreParete;

    public CasaLC(int strati) {
        super(strati);
        this.muratoreParete =new HashMap<Thread,Integer>();

        pareteLibera = new boolean[4];
        lastTypeWas = new int[4];

        for(int i=0;i<4;i++){
            pareteLibera[i]=true;
            lastTypeWas[i]=1;
        }

        possoScegliere = lock.newCondition();

        lavoroDisponibile = new Condition[2];
        for(int i=0;i<lavoroDisponibile.length;i++){
            lavoroDisponibile[i]=lock.newCondition();
        }

    }

    @Override
    public boolean inizia(int t) throws InterruptedException {
        lock.lock();
        codaScelta.add(Thread.currentThread());
        try{
            while(!possoScegliere(t)){
                possoScegliere.await();
            }
            codaScelta.removeFirst();

            if(azioni<=0){lock.unlock();return false;}

            if(t==0){
                codaMattoni.add(Thread.currentThread());
            }else{
                codaCemento.add(Thread.currentThread());
            }

            while(!lavoroDisponibile(t)){
                lavoroDisponibile[t].await();
            }
            possoScegliere.signalAll();
            muratoreParete.put(Thread.currentThread(),lastDistribuited);
            pareteLibera[lastDistribuited]=false;
            azioni--;
            lastTypeWas[lastDistribuited] = t;

            lastDistribuited = (++lastDistribuited % pareteLibera.length);
        } finally {
            if(t==0){
                codaMattoni.removeFirst();
            }else{
                codaCemento.removeFirst();
            }
            lock.unlock();
        }

        System.out.println(Thread.currentThread().getName()+" "+t+ " lavora");
        TimeUnit.MILLISECONDS.sleep(500);

        lock.lock();
        pareteLibera[muratoreParete.remove(Thread.currentThread())]=true;
        if (t == 0) {
            lavoroDisponibile[t + 1].signalAll();
        } else {
            lavoroDisponibile[t - 1].signalAll();
        }

        lock.unlock();

        return true;
    }

    private boolean lavoroDisponibile(int t){
        int candidateWall = ( lastDistribuited + 1 ) % pareteLibera.length ;

        if(t==0){ return pareteLibera[candidateWall] && (lastTypeWas[candidateWall]!=t) && codaMattoni.getFirst().equals(Thread.currentThread());}
        else { return pareteLibera[candidateWall] && (lastTypeWas[candidateWall]!=t) && codaCemento.getFirst().equals(Thread.currentThread());}
    }

    private boolean possoScegliere(int t) {
        return codaScelta.getFirst().equals(Thread.currentThread()) || terminato;
    }

    @Override
    public void termina(int t) throws InterruptedException {
        System.out.println(Thread.currentThread().getName()+" termina lavoro "+t);
        terminato = true;
        possoScegliere.signalAll();
    }

    public static void main(String[] args) throws InterruptedException {
        CasaLC casa = new CasaLC(20 );
        casa.test(5,7);
    }
}
