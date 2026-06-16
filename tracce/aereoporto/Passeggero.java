package tracce.aereoporto;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Passeggero extends Thread{

    private final Random rand = new Random();
    private final int N;
    private BancoCheckIn bancoCheckIn;

    public Passeggero(BancoCheckIn bancoCheckIn){
        this.bancoCheckIn=bancoCheckIn;
        this.N = rand.nextInt(1,4) ;

    }


    public void run(){
        try{
            TimeUnit.MILLISECONDS.sleep(N*350);
            bancoCheckIn.deponeBagagli(N);

            bancoCheckIn.riceviCartaImbarco();


        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }


}
