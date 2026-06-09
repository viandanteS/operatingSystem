package tracce.supermercato;


import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CassaSem extends CassaAbstract{

    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore possoSvuotare = new Semaphore(1,true);
    private final Semaphore possoScansionare = new Semaphore(0);
    private final Semaphore puoiPagare = new Semaphore(0);
    private final Semaphore prossimo = new Semaphore(0);
    private final Random rand=new Random();

    private int prodottiSuCassa=0;
    private int conto=0;
    @Override
    protected void svuotaCarrello(int N) throws InterruptedException {

        possoSvuotare.acquire();
        mutex.acquire();
        System.out.println(Thread.currentThread().getName());
        prodottiSuCassa=0;
        conto=0;
        for(int i = 0; i < N; i++){
            System.out.println("Posiziono prodotto"+ i);
            prodottiSuCassa++;
        }
        mutex.release();
        possoScansionare.release();

    }

    @Override
    protected void scansiona() throws InterruptedException {
        System.out.println("Attesa cliente");
        possoScansionare.acquire();
        mutex.acquire();
        conto = 5*prodottiSuCassa;
        TimeUnit.MILLISECONDS.sleep(500*prodottiSuCassa);
        System.out.println("Conto del cliente "+ conto);
        mutex.release();
        puoiPagare.release();

    }

    @Override
    protected void paga(int N) throws InterruptedException {
        puoiPagare.acquire();
        mutex.acquire();
        System.out.println("Cliente paga "+conto);
        mutex.release();
        prossimo.release();


    }

    @Override
    protected void prossimoCliente() throws InterruptedException {
        prossimo.acquire();
        mutex.acquire();
        System.out.println("Cliente ha terminato!");
        mutex.release();
        possoSvuotare.release();
    }


    public static void main(String[] args) throws InterruptedException {
        CassaSem cs = new CassaSem();
        cs.test(12);
    }
}
