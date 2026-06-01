package tracce.funivia;

import java.util.concurrent.TimeUnit;

public class Pilota extends Thread{

    private AbstractFunivia funivia;

    public Pilota(){
        this.setDaemon(true);
    }

    public void run(){
        while(true){
            funivia.pilotaStart();
            System.out.println("La funivia è partita");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            funivia.pilotaEnd();
            System.out.println("La funivia è ritornata");
        }
    }

}
