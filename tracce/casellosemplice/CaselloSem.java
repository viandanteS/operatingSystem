package tracce.casellosemplice;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class CaselloSem extends AbstractCasello{

    public CaselloSem(int tariffa, int numPorte) {
        super(tariffa, numPorte);
    }

    private Semaphore[] puoPagare;
    private Semaphore mutex;
    private Random rand=new Random();

    @Override
    protected int accoda() throws InterruptedException {
        return 0;
    }

    @Override
    protected void paga(Veicolo veicolo, int varco) {

    }

    @Override
    protected void rilascia(int varco) throws InterruptedException {

    }





   /* public static void main(String[] args) throws InterruptedException {
        CaselloSem casello=new CaselloSem(2,2);
        casello.test(10);

        System.out.println(casello.incasso);
        System.out.println("finish");
    }*/
}
