package tracce.funivia;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FuniviaLC extends AbstractFunivia{

    Lock lock = new ReentrantLock();
    Condition possoSalire= lock.newCondition();
    Condition possoScendere = lock.newCondition();
    Condition pilotaPuoPartire = lock.newCondition();
    Condition pilotaPuoScendere = lock.newCondition();

    List<Thread> turistiOnBoard = new ArrayList<Thread>();

    int saliti=0;
    int scesi=0;
    int turnoCorrente=0;
    boolean funiviaAValle = true;
    boolean discesaAutorizzata = false;


    private String getTurnoCorrente(){
        return turnoCorrente == 0 ? "Turno a piedi" : "Turno in bici";
    }

    @Override
    void pilotaStart() throws InterruptedException {
        lock.lock();
        try{
            while(!prontiAPartire()){
                pilotaPuoPartire.await();
            }
            funiviaAValle=false;
            scesi=0;
            System.out.println("Funivia parte: "+ getTurnoCorrente());
        }finally {
            lock.unlock();
        }
    }
    private boolean prontiAPartire(){
        return saliti==6;
    }


    @Override
    void pilotaEnd() throws InterruptedException {
        lock.lock();
        try{

            for(Thread t:turistiOnBoard){
                System.out.println(t.getName()+ " scende.");
            }
            turistiOnBoard.clear();
            discesaAutorizzata=true;
        }finally {
            possoScendere.signalAll();
            lock.unlock();
        }

        System.out.println("Funivia torna a valle");
        TimeUnit.SECONDS.sleep(2);

        lock.lock();
        try{
            while(scesi!=6){
                pilotaPuoScendere.await();
            }
            discesaAutorizzata=false;
            scesi=0;
            saliti=0;
            funiviaAValle=true;
            if(turnoCorrente==0)turnoCorrente=1; else turnoCorrente=0;
            System.out.println("Funivia tornata a valle");
        }finally {
            possoSalire.signalAll();
            lock.unlock();
        }

    }

    @Override
    void turistaSali(int t) throws InterruptedException {
        lock.lock();
        try{
            while(!possoSalire(t)){
                possoSalire.await();
            }
            if(t==0)saliti++;
            else saliti+=2;

            System.out.println(Thread.currentThread().getName()+"sale a bordo !");
            turistiOnBoard.add(Thread.currentThread());
        }finally {
            if(saliti==6) pilotaPuoPartire.signal();
            lock.unlock();
        }
    }

    private boolean possoSalire(int t){
        return funiviaAValle && turnoCorrente == t && saliti<6;
    }

    @Override
    void turistaScendi(int t) throws InterruptedException {
        lock.lock();
        try{
            while(!possoScendere()){
                possoScendere.await();
            }

            if(t==0){scesi++;}
            else scesi+=2;


        }finally {
            pilotaPuoScendere.signal();
            lock.unlock();
        }
    }
    private boolean possoScendere(){
        return discesaAutorizzata;
    }

    public static void main(String[] args) {
        FuniviaLC f = new FuniviaLC();
        f.test();
    }
}
