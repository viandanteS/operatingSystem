package tracce.supermercato;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


/*DA FINIRE*/

public class CassaSemMultiple extends CassaAbstract {

    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore[] possoSvuotare = new Semaphore[4];
    private final Semaphore[] possoScansionare = new Semaphore[4];
    private final Semaphore[] puoiPagare = new Semaphore[4];
    private final Semaphore[] prossimo = new Semaphore[4];

    private final Semaphore possoScegliere =  new Semaphore(1,true);
    private final Map<String,Integer> threadCassaScelta = new HashMap<String,Integer>();
    private final int[] code = new int[4];


    private int[] prodottiSuCassa =new int[4];
    private int[] conto =new int[4];

    private int scegliCassa(){
        int min = code[0];
        for(int i=1; i<code.length; i++){
            if(code[i]<min){
                min=code[i];
            }
        }
        return min;
    }

    @Override
    protected void svuotaCarrello(int N) throws InterruptedException {

        possoScegliere.acquire();
        int cassaScelta = scegliCassa();
        code[cassaScelta]++;
        threadCassaScelta.put(Thread.currentThread().getName(),cassaScelta);
        possoScegliere.release();

        possoSvuotare[cassaScelta].acquire();
        mutex.acquire();
        System.out.println(Thread.currentThread().getName());

        prodottiSuCassa[cassaScelta]=0;
        conto[cassaScelta]=0;
        for(int i = 0; i < N; i++){
            System.out.println("Posiziono prodotto"+ i);
            prodottiSuCassa[cassaScelta]++;
        }
        mutex.release();
        possoScansionare[cassaScelta].release();

    }

    @Override
    protected void scansiona() throws InterruptedException {
        Cassiere c = (Cassiere) Thread.currentThread();
        int cassaScelta = c.getIndex();
        System.out.println("Cassiere "+cassaScelta+" Attesa cliente..");
        possoScansionare[cassaScelta].acquire();
        mutex.acquire();
        conto[cassaScelta] = 5*prodottiSuCassa[cassaScelta];
        TimeUnit.MILLISECONDS.sleep(500L *prodottiSuCassa[cassaScelta]);
        System.out.println("Conto del cliente "+ conto[cassaScelta]);
        mutex.release();
        puoiPagare[cassaScelta].release();

    }

    @Override
    protected void paga(int N) throws InterruptedException {

        mutex.acquire();
        int cassa=threadCassaScelta.get(Thread.currentThread().getName());
        mutex.release();

        puoiPagare[cassa].acquire();

        System.out.println("Cliente paga "+conto[cassa]);

        prossimo[cassa].release();


    }

    @Override
    protected void prossimoCliente() throws InterruptedException {

        mutex.acquire();
        int cassa = 0;
        for(String key : threadCassaScelta.keySet()){
            if(key.equals(Thread.currentThread().getName())){
                cassa=threadCassaScelta.remove(key);
                break;
            }
        }

        mutex.release();


        prossimo[cassa].acquire();

        System.out.println("Cliente ha terminato!");

        possoSvuotare[cassa].release();
    }


    public static void main(String[] args) throws InterruptedException {
        CassaSem cs = new CassaSem();
        cs.test(12);
    }
}
