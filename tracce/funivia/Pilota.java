package tracce.funivia;

import java.util.concurrent.TimeUnit;

public class Pilota extends Thread{

    private AbstractFunivia funivia;

    public Pilota(AbstractFunivia funivia)
    {
        this.funivia=funivia;
        this.setDaemon(true);
    }

    public void run(){
        while(true){
            try {
                funivia.pilotaStart();


                funivia.pilotaEnd();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
