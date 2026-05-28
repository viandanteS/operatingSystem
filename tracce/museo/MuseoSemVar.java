package tracce.museo;

import java.util.concurrent.Semaphore;

public class MuseoSemVar extends MuseoAbstract{

    int NUM_RICHIESTO = 5;

    Semaphore codaSalaA = new Semaphore(40,true); // semaforo su cui fare la coda per visitare la sala A
    Semaphore codaSalaD = new Semaphore(5, true); // semaforo per mettere in coda ed attendere
    Semaphore possoEntrareD= new Semaphore(0); // semaforo per attendere di essere il numero esatto prima di visitare
    Semaphore mutex = new Semaphore(1);
    Semaphore mutex2=  new Semaphore(1);
    int numFineVisita = 0;
    int numVisitatoriInCoda = 0;

    @Override
    protected void visitaSA() throws InterruptedException {
        codaSalaA.acquire();
        System.out.format("Il visitatore %s visita sala Archeologica\n", Thread.currentThread().getName());
    }

    @Override
    protected void terminaVisitaSA() {
        System.out.format("Il visitatore %s ha finito la visita alla sala archeologica\n", Thread.currentThread().getName());
        codaSalaA.release();
    }

    @Override
    protected void visitaSD() throws InterruptedException {

        codaSalaD.acquire();

        mutex2.acquire();
        numVisitatoriInCoda++;
        if(numVisitatoriInCoda == NUM_RICHIESTO){
            numVisitatoriInCoda = 0;
            possoEntrareD.release(5);
        }
        mutex2.release();

        possoEntrareD.acquire();//se supero questo vuol dire che siamo almeno 5 in coda
        System.out.format("Il visitatore %s visita dama\n", Thread.currentThread().getName());
    }

    @Override
    protected void terminaVisitaSD() throws InterruptedException {

        mutex.acquire();
        numFineVisita++;

        if(numFineVisita==NUM_RICHIESTO){
            numFineVisita=0;
            codaSalaD.release(5);
        }
        System.out.format("Il visitatore %s ha finito la visita alla sala della dama\n", Thread.currentThread().getName());
        mutex.release();

    }

    public static void main(String[] args) throws InterruptedException {

        MuseoSemVar museo = new MuseoSemVar();
        museo.test(100);

    }

}
