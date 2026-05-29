package tracce.callcenter;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Operatore extends Thread {

    private static final int CLIENTI_PER_PAUSA=15;
    private static final int MINUTI_DI_RIPOSO=5;

    private int clientiServiti;
    private final Random rand=new Random();
    private final CallCenter cc;

    public Operatore(CallCenter cc){
        this.cc=cc;
        this.clientiServiti=0;
        this.setDaemon(true);

    }

    public void run(){
        int attesaSoluzione=0;

        try{
            while(true) {
                attesaSoluzione = rand.nextInt();
                cc.fornisciAssistenza();
                cc.prossimoCliente();
                if (clientiServiti == CLIENTI_PER_PAUSA) {
                    System.out.println(Thread.currentThread().getName()+": riposo.");
                    attendi(MINUTI_DI_RIPOSO);
                }
            }
        }
        catch (InterruptedException e){
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
