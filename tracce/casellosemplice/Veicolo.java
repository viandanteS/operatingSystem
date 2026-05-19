package tracce.casellosemplice;

import java.util.Random;
import java.util.UUID;

public class Veicolo extends Thread{

    private final int UID= UUID.randomUUID().hashCode();
    private AbstractCasello casello;
    private final static int MAX_KM=100,MIN_KM=50;
    private int x=new Random().nextInt(MAX_KM-MIN_KM+1)+MIN_KM;


    public int getX() {
        return x;
    }

    public Veicolo(AbstractCasello casello){
        this.casello=casello;
    }


    private void percorri() throws InterruptedException {
        Thread.sleep(40);
    }

    public void run(){
        try {
            System.out.println(String.format("Il veicolo %d startau",UID));
            for(int i=0;i<x;i++){
                percorri();
            }
            int varco=casello.accoda();
            System.out.println(String.format("Il veicolo %d si accoda al varco %d",UID,varco));
            casello.paga(this,varco);
            System.out.println(String.format("Il veicolo %d paga",UID));
            casello.rilascia(varco);
            //System.out.println(String.format("Il veicolo %d rilascia il varco %d",UID,varco));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
