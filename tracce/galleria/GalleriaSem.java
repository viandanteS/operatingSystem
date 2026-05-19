package tracce.galleria;

import java.util.concurrent.Semaphore;

public class GalleriaSem extends GalleriaAbstract {

    private Semaphore mutex = new Semaphore(1);
    private Semaphore possoVisitare = new Semaphore(200);
    private Semaphore possoPartire[] = new Semaphore[super.guide];
    private Semaphore possoAccettare[] = new Semaphore[super.guide];



    @Override
    protected void iniziaVisita(int lingua) throws InterruptedException {

    }

    @Override
    protected void esci(int lingua) throws InterruptedException {

    }

    @Override
    protected void attendiVisitatori(int lingua) throws InterruptedException {

    }

    @Override
    protected void terminaVisita(int lingua) throws InterruptedException {

       // possoPartire[]
    }


    public static void main(String[] args) throws InterruptedException {


    }
}
