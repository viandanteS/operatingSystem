package tracce.callcenter;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Operatore extends Thread {

    private static final int CLIENTI_PER_PAUSA=15;
    private static final int MINUTI_DI_RIPOSO=5;

    private int clientiServiti;
    private final Random rand=new Random();
    private final CallCenter cc;
    private final int idOp;

    public Operatore(CallCenter cc,int idOp){
        this.cc=cc;
        this.idOp=idOp;
        this.clientiServiti=0;
        this.setDaemon(true);

    }

    public int getIdOp(){
        return idOp;
    }
    public void run(){

        try{
            while(true) {

                cc.fornisciAssistenza();

                cc.prossimoCliente();

                if (clientiServiti == CLIENTI_PER_PAUSA) {
                    System.out.println(Thread.currentThread().getName()+": riposo.");
                    attendi(MINUTI_DI_RIPOSO);
                    clientiServiti=0;
                }
                clientiServiti++;
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
