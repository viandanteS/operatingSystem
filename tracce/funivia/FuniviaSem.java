package tracce.funivia;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class FuniviaSem extends AbstractFunivia{

    private Semaphore[] code; //code d'ingresso per gestire round robing
    private Semaphore mutex = new Semaphore(1); // mutua esclusione
    private Semaphore puoiPartire = new Semaphore(0); //semaforo per segnalare al pilota che la cabina ha 6 utenze
    private Semaphore possoScendere = new Semaphore(0); // semaforo per segnalare ai turisti che possono scendere una volta in cima
    private Semaphore tuttiScesi = new Semaphore(0); //per segnalare al pilota che tutti sono scesi e può scendere a valle

    private int currentTypeServing = 0;
    private int seduti;
    private int scesi;

    private List<Thread> turistiInCorso = new ArrayList<Thread>();


    public FuniviaSem(){
        code = new Semaphore[2];
        code[0] = new Semaphore(6,true);
        code[1] = new Semaphore(0,true);
    }

    @Override
    void pilotaStart() throws InterruptedException {

        puoiPartire.acquire();
        System.out.println("Funivia partita! Si sale in cima!");
        if(currentTypeServing == 0) System.out.println("Corsa a piedi");
        else System.out.println("Corsa bici");

        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("Funivia in cima");
    }

    @Override
    void pilotaEnd() throws InterruptedException {
        turistiInCorso.forEach((Thread t)-> {System.out.println(t.getName()); possoScendere.release();});
        turistiInCorso.clear();

        tuttiScesi.acquire();
        System.out.println("La funivia ritorna a valle..");

        TimeUnit.SECONDS.sleep(1);

        System.out.println("La funivia è a valle..");
        if(currentTypeServing==0){
            code[1].release(3);
            currentTypeServing=1;
        }
        else{
            code[0].release(6);
            currentTypeServing=0;
        }

    }

    @Override
    void turistaSali(int t) throws InterruptedException {

        code[t].acquire();
        mutex.acquire();
        if(t == 0){
            seduti++;}
        else{
            seduti +=2;}
        turistiInCorso.add(Thread.currentThread());
        if(seduti ==6){
            seduti=0;
            puoiPartire.release();
        }
        mutex.release();
    }

    @Override
    void turistaScendi(int t) throws InterruptedException {
        possoScendere.acquire();
        mutex.acquire();
        if(t == 0){
            scesi++;}
        else{
            scesi +=2;}
        if(scesi ==6){
            scesi = 0;
            tuttiScesi.release();
        }
        mutex.release();
    }


    public static void main(String[] args) throws InterruptedException {
        FuniviaSem f = new FuniviaSem();
        f.test();

    }
}


