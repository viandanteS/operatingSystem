package tracce.pistakart;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Pilota extends Thread{

    private final int eta; //0 minorenne 1 maggiorenne
    private final Pista pista;

    public Pilota(int eta, Pista pista) {
        this.eta = eta;
        this.pista = pista;
    }

    public int getEta() {
        return eta;
    }

    public void run(){
        try{
            Random random = new Random();
            pista.noleggia();

            int numGiri=pista.entraInPista();
            System.out.println(Thread.currentThread().getName()+" entra in pista! "+numGiri);
            for(int i = 0 ; i < numGiri ; i++){
                System.out.println(Thread.currentThread().getName()+": giro: "+(i+1)+"/"+numGiri);
                TimeUnit.SECONDS.sleep(random.nextInt(1,3));
            }

            pista.lasciaPista();

            pista.riconsegna();
            System.out.println(Thread.currentThread().getName()+" termina.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
