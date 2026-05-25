package tracce.aziendaagricola;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Cliente extends Thread{

    private AbstractAziendaAgricola aziendaAgricola;
    private static AtomicInteger contador = new AtomicInteger(0);
    Random rand = new Random();

    public Cliente(AbstractAziendaAgricola aziendaAgricola,String id){
        super(id);
        this.aziendaAgricola = aziendaAgricola;
    }

    public void run(){
        final int N=rand.nextInt(1,10);
        contador.addAndGet(N);
        try {
            aziendaAgricola.paga(N);
            System.out.println("Cliente"+this.getId()+" ritira");
            for (int i=0;i<N;i++){
                aziendaAgricola.ritira();
                TimeUnit.SECONDS.sleep(1);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public static int getContador(){
        return contador.get();
    }


}
