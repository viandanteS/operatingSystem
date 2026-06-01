package tracce.catenadimontagio;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CatenaDiMontaggioSem extends CatenaDiMontaggioA{


    private final int ATTESASX=10;
    private final int ATTESADX=15;

    private Semaphore mutex = new Semaphore(1);

    private final Semaphore[] daProdurre;

    private final Semaphore[] daConsumare;

    public CatenaDiMontaggioSem(){
        this.daProdurre=new Semaphore[2];
        this.daConsumare=new Semaphore[2];
        for(int i=0;i<2;i++){
            daProdurre[i]=new Semaphore(0);
        }
        for(int i=0;i<2;i++){
            daConsumare[i]=new Semaphore(0);
        }
    }

    @Override
    protected void richiediProduzione(int pSx, int pDx) throws InterruptedException {

        daProdurre[0].release(pSx);
        daProdurre[1].release(pDx);

        daConsumare[0].acquire(pSx);
        daConsumare[1].acquire(pDx);
        System.out.println("I pezzi per questo componente sono stati prodotti.");


    }

    @Override
    protected void produci(int tipo) throws InterruptedException {

        if(tipo==0){
            daProdurre[0].acquire();
            System.out.println("Produzione pezzo sx");
            TimeUnit.MICROSECONDS.sleep(ATTESASX);
            daConsumare[0].release();
        }else{
            daProdurre[1].acquire();
            System.out.println("Produzione pezzo dx");
            TimeUnit.MICROSECONDS.sleep(ATTESADX);
            daConsumare[1].release();
        }

    }

    @Override
    protected void assembla() throws InterruptedException {

        System.out.println("Assemblatore assembla");
        TimeUnit.SECONDS.sleep(5);

    }

    public static  void main(String[] args) throws InterruptedException {

        CatenaDiMontaggioSem cdm=new CatenaDiMontaggioSem();
        cdm.test(3,4);

    }

}
