package tracce.supermercato;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Cliente extends Thread {

    private final Random random = new Random();
    private final CassaAbstract cassa;
    private final int numProdotti = random.nextInt(1,20);


    public Cliente(CassaAbstract cassa) {
        this.cassa = cassa;

    }

    public void run() {
        try{
            scegliProdotti();
            cassa.svuotaCarrello(numProdotti);

            cassa.paga(numProdotti);


        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private void scegliProdotti() throws InterruptedException{
        TimeUnit.MILLISECONDS.sleep(20 * numProdotti);
    }

}
