package tracce.aereoporto;

public abstract class BancoCheckIn {

    abstract void deponeBagagli(int N) throws InterruptedException;

    abstract void pesaERegistra() throws InterruptedException;

    abstract void riceviCartaImbarco() throws InterruptedException;

    abstract void prossimoPasseggero() throws InterruptedException;


    public void test() {

        for (int i = 0; i < 40; i++){
            new Passeggero(this).start();
        }
        new Addetto(this).start();


    }
}
