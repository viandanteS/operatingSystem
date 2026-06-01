package tracce.muratori;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class CasaSem extends AbstractCasa{



    //private Semaphore[] mutexPossoLavorare = new Semaphore[LATI]; //se la iesima parete è in una delle due code allora nell'iesimo semaforo c'è un permesso

//    private Semaphore pareti = new Semaphore(4,true); // quando occupo una parete con job type qualsiasi faccio acquire, quando finisco release

    private Semaphore semMutex = new Semaphore(1);

    private Semaphore jobMattoni = new Semaphore(0);//protegge l'estrazione dalla coda dei mattoni
    private Semaphore jobCemento = new Semaphore(0);

    Queue<Integer> paretiPerMattoni=new LinkedList<>();
    private int[] mattoniPiazzati=new int[LATI];
    Queue<Integer> paretiPerCemento=new LinkedList<>();

    Map<Long,Integer> muratoreMuro = new HashMap<Long,Integer>();

    public CasaSem(int i, int nM, int nC) {
        super(i,nM,nC);
        for(int x=0;x<LATI;x++){
        //    mutexPossoLavorare[x] = new Semaphore(1,true);
            paretiPerMattoni.offer(x);
            jobMattoni.release();
        }

    }

    //se c'è una parete in coda allora me la piglio, e la aggiungo al registro delle lavorazioni
    @Override
    public boolean inizia(int type) throws InterruptedException {

        long thisMuratore = Thread.currentThread().getId();
        boolean ret=true;
        if(type==0){

//            pareti.acquire();
            jobMattoni.acquire();
            semMutex.acquire();
            if(paretiFinite()){
                semMutex.release();
                return !ret; }
            int pareteDaLavorare =  paretiPerMattoni.poll();
            mattoniPiazzati[pareteDaLavorare]++;
            //mutexPossoLavorare[pareteDaLavorare].acquire();
            muratoreMuro.put(thisMuratore,pareteDaLavorare);
            System.out.println(Thread.currentThread().getName()+" muratore lavora su parete "+pareteDaLavorare);
            semMutex.release();

        }else{

//            pareti.acquire();
            jobCemento.acquire();
            semMutex.acquire();
            if(paretiFinite()){
                semMutex.release();
                return !ret; }
            int pareteDaLavorare =  paretiPerCemento.poll();
            muratoreMuro.put(thisMuratore,pareteDaLavorare);
            System.out.println(Thread.currentThread().getName()+" muratore lavora su parete "+pareteDaLavorare);
            semMutex.release();
        }


        return ret;
    }

    private boolean paretiFinite(){
        for (int i=0;i<LATI;i++){
            if(mattoniPiazzati[i]!=N){return false;}
        }
        System.out.println("pareti finite");
        return true;
    }

    @Override
    public void termina(int type) throws InterruptedException {

        long thisMuratore = Thread.currentThread().getId();

        semMutex.acquire();
        int pareteDaLiberare= muratoreMuro.remove(thisMuratore);

        if(type==0){
            paretiPerCemento.offer(pareteDaLiberare);
            //mutexPossoLavorare[pareteDaLiberare].release();
            jobCemento.release();
    //        pareti.release();
        }else{
            paretiPerMattoni.offer(pareteDaLiberare);
            jobMattoni.release();
    //        pareti.release();
        }
        System.out.println(Thread.currentThread().getId()+" termina e riposa");
        semMutex.release();

    }


    public static void main(String[] args) throws InterruptedException {

        CasaSem casa = new CasaSem(20,5,7);
        casa.test();

    }


}
