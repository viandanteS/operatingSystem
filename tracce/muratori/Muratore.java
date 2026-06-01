package tracce.muratori;

import java.sql.SQLOutput;
import java.util.concurrent.TimeUnit;

public class Muratore extends Thread{

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
                if(!disponibilitaLavori){break;}
                TimeUnit.MILLISECONDS.sleep(OPERATION_TIMES);//lavora 10s
                casa.termina(type);
                TimeUnit.MILLISECONDS.sleep(REST_TIME);

            }catch(InterruptedException e){}
        }
        System.out.println("muratore "+ Thread.currentThread().getName()+" se ne và.");
    }
}
