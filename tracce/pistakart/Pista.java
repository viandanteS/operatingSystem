package tracce.pistakart;

public abstract class Pista {


    protected final int S = 4, L = 8, CAPIENZA_PISTA = 6;

    abstract void noleggia() throws InterruptedException;

    abstract int entraInPista() throws InterruptedException;

    abstract void lasciaPista() throws InterruptedException;

    abstract void riconsegna() throws InterruptedException;

    public void test() {

        for (int i = 0; i < 20; i++) {
            new Pilota(1, this).start();
        }

        for (int i = 0; i < 5; i++) {
            new Pilota(0, this).start();
        }
    }

}
