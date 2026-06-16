package tracce.pavimento;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Piastrellistra extends Thread{

    private final Random rand = new Random();
    private final int type;
    private final Pavimento pavimento;

    public Piastrellistra(int type, Pavimento pavimento) {
        this.type = type;
        this.pavimento = pavimento;
    }

    public void run(){
        String bloccoLavorato=" ";
        while(bloccoLavorato!=null){
            try {
                TimeUnit.SECONDS.sleep(1);
                bloccoLavorato = pavimento.inizia(type);
                if(bloccoLavorato==null){return;}
                if(type==0) TimeUnit.SECONDS.sleep(rand.nextInt(4,6));
                else TimeUnit.SECONDS.sleep(rand.nextInt(2,3));
                pavimento.finisci(type,bloccoLavorato);
                System.out.println(Thread.currentThread().getName()+" "+type+" finisce "+bloccoLavorato + " e si riposa.");
                TimeUnit.SECONDS.sleep(1);
            }catch (InterruptedException ie){
                ie.printStackTrace();
            }
        }

    }


}
