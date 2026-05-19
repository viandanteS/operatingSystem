package tracce.galleria;

import java.util.Random;

public abstract class GalleriaAbstract {

    protected abstract void iniziaVisita(int lingua) throws InterruptedException;
    protected abstract void esci(int lingua) throws InterruptedException;
    protected abstract void attendiVisitatori(int lingua) throws InterruptedException;
    protected abstract void terminaVisita(int lingua) throws InterruptedException;

    protected final int guide = 5;
    private Random random = new Random();
    protected void test(int guide, int visitatori) {
        for (int i = 0; i < guide; i++) {
            Guida g = new Guida(this, i);
            g.setDaemon(true);
            g.start();
        }
        for (int i = 0; i < visitatori; i++) {
            Visitatore v = new Visitatore(this, random.nextInt(guide));
            v.start();
        }
    }

}
