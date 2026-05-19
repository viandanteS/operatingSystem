package tracce.casellosemplice;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CaselloLC extends AbstractCasello{

    private ReentrantLock lock=new ReentrantLock(true);
    Condition possoPagare = lock.newCondition();
    List<List<Thread>> codePorte;
    boolean[] available;

    public CaselloLC(int tariffa, int numPorte) {
        super(tariffa, numPorte);
        codePorte=new LinkedList<>();
        available= new boolean[numPorte];

        for(int i=0;i<numPorte;i++){
            codePorte.add(new LinkedList<>());
            available[i]=true;
        }

    }


    @Override
    protected int accoda(){
        lock.lock();
        int porta=new Random().nextInt(N);
        codePorte.get(porta).addLast(Thread.currentThread());
        lock.unlock();

        return porta;
    }



    private boolean possoPagare(Veicolo v,int porta) { return v.equals(codePorte.get(porta).getFirst()) && available[porta]; }
    @Override
    protected void paga(Veicolo v, int varco) {
        lock.lock();
        try
        {
            while(!possoPagare(v,varco)){
                possoPagare.await();
            }
            available[varco]=false;
            incasso=(v.getX()*TARIFFA)+incasso;
            available[varco]=true;

        }catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            codePorte.get(varco).removeFirst();
            possoPagare.signalAll();
            lock.unlock();
        }

    }

    @Override
    protected void rilascia(int varco) {
        System.out.println("Un veicolo ha abbantondato il varco "+varco);
    }


    public static void main(String[] args) throws InterruptedException {
        CaselloLC casello=new CaselloLC(2,3);
        casello.test(10);

    }
}
