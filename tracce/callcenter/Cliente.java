package tracce.callcenter;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Cliente extends Thread {

    private final CallCenter cc;
    private final Random rand=new Random();
    private final int myId;

    public Cliente(CallCenter cc,int myId) {
        this.cc = cc;
        this.myId=myId;
    }
    public int getMyId() {
        return myId;
    }

    public void run() {
        int time= rand.nextInt(2,6);
        try {

            cc.richiediAssistenza();
            attendi(time);
            cc.terminaChiamata();

        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    private void attendi(int time){
        try{
            TimeUnit.SECONDS.sleep(time);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
