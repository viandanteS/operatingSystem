package tracce.aziendaagricola;


import java.util.concurrent.Semaphore;

public class AziendaAgricolaSem extends AbstractAziendaAgricola{


    private Semaphore possoPagare = new Semaphore(1,true);
    private Semaphore possoRitirare = new Semaphore(200);
    private Semaphore possoRestockare= new Semaphore(0);
    private Semaphore mutexRitiro= new Semaphore(1,true);

    private int qtSacchetti=200;

    @Override
    protected void paga(int N) throws InterruptedException {

        try{
            possoPagare.acquire();
            incasso+=COSTO_SACCHETTO*N;

        }finally {
            possoPagare.release();
        }

    }

    @Override
    protected void restock() throws InterruptedException {
        try {
            possoRestockare.acquire();
            qtSacchetti=200;
        }
        finally {
            possoRitirare.release(200);
        }
    }

    @Override
    protected void ritira() throws InterruptedException {
        try{
            possoRitirare.acquire();
            mutexRitiro.acquire();
            qtSacchetti--;
            if(qtSacchetti==0){possoRestockare.release();}
        }
        finally {
            mutexRitiro.release();
        }

    }
}
