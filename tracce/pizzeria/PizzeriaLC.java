package tracce.pizzeria;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PizzeriaLC extends PizzeraAstratta{

    Random rand = new Random();
    private Lock lock = new ReentrantLock();
    Condition possoEntrare = lock.newCondition();
    Condition possoPreparare = lock.newCondition();
    Condition possoMangiare = lock.newCondition();

    List<Thread> codaEntrata = new LinkedList<>();

    boolean pronta = false;
    boolean tavoloLibero = true;
    int postiOccupati=0;
    int clientiFinito=0;

    @Override
    protected void entra() throws InterruptedException {
        lock.lock();
        try{
            codaEntrata.add(Thread.currentThread());
            while (!possoEntrare()) {
                possoEntrare.await();
            }
            codaEntrata.remove(Thread.currentThread());
            postiOccupati++;
            if (postiOccupati == POSTI_A_SEDERE) {
                tavoloLibero = false;
                possoPreparare.signal();
            }
        }finally{
            lock.unlock();
        }
    }

    private boolean possoEntrare(){
        return codaEntrata.getFirst() == Thread.currentThread() && tavoloLibero;
    }

    @Override
    protected void mangiaPizza() throws InterruptedException {
        lock.lock();
        try{
            while(!possoMangiare()){
                possoMangiare.await();
            }
        }finally {
            lock.unlock();
        }
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(5000,10000));

        lock.lock();
        System.out.println(Thread.currentThread().getName()+" ha finito e se ne va. ");
        try{
            clientiFinito++;
            if(clientiFinito == POSTI_A_SEDERE){ //per controllare quanti hanno finito e si sono alzati
                postiOccupati=0;
                tavoloLibero = true;
                pronta = false;
                possoEntrare.signalAll();
            }
        }finally {
            lock.unlock();
        }
    }

    private boolean possoMangiare(){
        return pronta;
    }


    @Override
    protected void preparaPizza() throws InterruptedException {
        lock.lock();
        try{

            while(!possoPreparare()){
                possoPreparare.await();
            }
            System.out.println("Pizzaiolo prepara la pizza");

        }finally{
            lock.unlock();
        }
        TimeUnit.SECONDS.sleep(4);

    }

    private boolean possoPreparare(){ //il pizzaiolo non può più preparare una volta che la pizza è pronta
        return !tavoloLibero && postiOccupati==POSTI_A_SEDERE && !pronta ;
    }

    @Override
    protected void serviPizza() throws InterruptedException {
        lock.lock();
        try{
            System.out.println("Pizzaiolo serve la pizza");
            clientiFinito=0;
            pronta = true;

        }finally {
            possoMangiare.signalAll();
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        PizzeriaLC pizzeria = new PizzeriaLC();
        int numClienti = 30;
        pizzeria.test(numClienti);
    }
}
