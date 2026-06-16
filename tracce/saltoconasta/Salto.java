package tracce.saltoconasta;

public abstract class Salto {

    protected final int NUM_SALTATORI=20;

    abstract void inizio(Saltatore s) throws InterruptedException;
    abstract int arrivo(Saltatore s) throws InterruptedException;

    abstract boolean successivo() throws InterruptedException;


    public void test(){

        for (int i =0; i<NUM_SALTATORI;i++){
            new Saltatore(i,this).start();
        }
        new Giudice(this).start();

    }
}
