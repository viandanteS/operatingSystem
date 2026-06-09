package tracce.muratori;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CasaSem extends AbstractCasa {


    private final Semaphore[][] pareti = new  Semaphore[2][4];
    private final Semaphore mutex = new Semaphore(1,true);
    private final int[] lavori=new int[2];

    public CasaSem(int strati) {
        super(strati);
        for(int i=0;i<pareti[0].length;i++){
            pareti[0][i] = new Semaphore(1,true);
        }
        for(int i=0;i<pareti[1].length;i++){
            pareti[1][i] = new Semaphore(0,true);
        }
        for(int i=0;i<lavori.length;i++){
            lavori[i] = strati*4;
        }
    }

    private int roundRobinMattoni=4;
    private int roundRobinCemento=4;

    private boolean lavoriTerminati(int t){
        if(lavori[t]>0) return false;
        return true;

    }

    @Override
    public boolean inizia(int t) throws InterruptedException {


        mutex.acquire();
        if(lavoriTerminati(t)) {mutex.release(); return false;}
        lavori[t]--;
        int pareteAssegnata = t==0 ? (roundRobinMattoni++%pareti[0].length) : (roundRobinCemento++%pareti[1].length);
        //System.out.println(Thread.currentThread().getName()+" type: "+ t +" Parete Assegnata: "+pareteAssegnata);
        mutex.release();



        pareti[t][pareteAssegnata].acquire(); //se lo supera vuol dire che la parete è libera ed è pronta per essere lavorata da quel tipo di muratore
        System.out.println(Thread.currentThread().getName()+" type: "+ t +" lavora su  "+pareteAssegnata);
        TimeUnit.MILLISECONDS.sleep(200);
        sbloccaSuccessivo(t,pareteAssegnata);

        return true;

    }

    private void sbloccaSuccessivo(int t,int x){
        if(t==1){
            pareti[t-1][x].release();
        }else{
            pareti[t+1][x].release();
        }
    }

    @Override
    public void termina(int t) throws InterruptedException {
        System.out.println("Terminato "+Thread.currentThread().getName());

    }


    public static void main(String[] args) throws InterruptedException {
        CasaSem casa = new CasaSem(20);
        casa.test(5,7);

    }
}