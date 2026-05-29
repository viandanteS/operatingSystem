package tracce.muratori;

import java.sql.SQLOutput;
import java.util.concurrent.TimeUnit;

public class Muratore implements Runnable{

    private static final int[] PREP_TIMES = { 5,7 };
    private static final int OPERATION_TIMES = 10 ;
    private static final int REST_TIME = 5 ;
    private int type;
    boolean disponibilitaLavori=true;
    private AbstractCasa casa;
    //5s per mattoni, 7s per cemento
    //type==0 --> muratore mattoni
    //type==1 --> muratore cemento



    public Muratore(int type,AbstractCasa casa){
        this.type=type;
        this.casa=casa;
    }



    @Override
    public void run() {

        while(disponibilitaLavori){
            try{
                TimeUnit.MILLISECONDS.sleep(PREP_TIMES[type]); //prepara materiale
                disponibilitaLavori= casa.inizia(type); //inizia a lavorare
                TimeUnit.MILLISECONDS.sleep(OPERATION_TIMES);//lavora 10s
                System.out.println("Il lavoratore "+Thread.currentThread().getName()+" ha finito di lavora ed inizia a riposare");
                TimeUnit.MILLISECONDS.sleep(REST_TIME);
            }catch(InterruptedException e){}
        }
        try {
            casa.termina(type);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
