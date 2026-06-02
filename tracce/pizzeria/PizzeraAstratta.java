package tracce.pizzeria;

public abstract class PizzeraAstratta {

    protected final int POSTI_A_SEDERE = 5;



    protected abstract void entra() throws InterruptedException;
    protected abstract void mangiaPizza() throws InterruptedException;
    protected abstract void preparaPizza() throws InterruptedException;
    protected abstract void serviPizza() throws InterruptedException;


    public void test(int numClienti){

        for(int i = 0; i < numClienti; i++){
            new Cliente(this).start();
        }
        new Pizzaiolo(this).start();

    }
}
