package tracce.casadicura;

import java.util.concurrent.Semaphore;

public class CasaDiCuraSem extends CasaDiCuraAbstract{

    private final Semaphore pazientiInSalaAttesa = new Semaphore(3);
    private final Semaphore pazienteInSalaOperatoria = new Semaphore(1,true);
    private final Semaphore salaPronta = new Semaphore(0);
    private final Semaphore possoUscire = new Semaphore(0);
    private final Semaphore possoOperare = new Semaphore(0);


    @Override
    protected void chiamaEIniziaOperazione() throws InterruptedException {

        salaPronta.release();

        possoOperare.acquire();

    }

    @Override
    protected void fineOperazione() throws InterruptedException {
        System.out.println("Fine Operazione");
        possoUscire.release();
        pazienteInSalaOperatoria.release(); // non attende che il paziente esca dalla sala
    }

    @Override
    protected void pazienteEntra() throws InterruptedException {

        pazientiInSalaAttesa.acquire();//max 3 persone in sala d'attesa

        salaPronta.acquire();

        pazienteInSalaOperatoria.acquire(); //sospeso fin quando non entra in sala operatoria ordine FIFO

        possoOperare.release();

    }

    @Override
    protected void pazienteEsci() throws InterruptedException {

        possoUscire.acquire();
        pazientiInSalaAttesa.release();

    }

    public static void main(String[] args) throws InterruptedException {
        CasaDiCuraSem casaDiCura = new CasaDiCuraSem();
        casaDiCura.test();
    }
}
