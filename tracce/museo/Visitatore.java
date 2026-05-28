package tracce.museo;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Visitatore extends Thread{

    private final Random rand = new Random();
    private MuseoAbstract museo;


    public Visitatore(MuseoAbstract museo) {
        this.museo = museo;
    }


    @Override
    public void run() {
        int timeA= rand.nextInt(20,40);
        int timeD= rand.nextInt(5,8);

        try {
            museo.visitaSA();
            attendi(timeA);
            museo.terminaVisitaSA();

            museo.visitaSD();
            attendi(timeD);
            museo.terminaVisitaSD();

        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private void attendi(int time){
        try {
            TimeUnit.MICROSECONDS.sleep(time);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }


}
