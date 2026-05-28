package tracce.museo;

import java.util.concurrent.Semaphore;

public class MuseoSem extends MuseoAbstract {


    Semaphore codaSalaA = new Semaphore(40,true);
    Semaphore codaSalaD = new Semaphore(5,true);



    @Override
    protected void visitaSA() throws InterruptedException {
        codaSalaA.acquire();
    }

    @Override
    protected void terminaVisitaSA() throws InterruptedException {
        codaSalaA.release();
    }

    @Override
    protected void visitaSD() throws InterruptedException {
        codaSalaD.acquire();
    }

    @Override
    protected void terminaVisitaSD() throws InterruptedException {

        codaSalaD.release();

    }
}
