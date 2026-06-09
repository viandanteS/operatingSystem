package tracce.supermercato;

public abstract class CassaAbstract {


    protected abstract void  svuotaCarrello(int N) throws InterruptedException;
    protected abstract void scansiona() throws InterruptedException;
    protected abstract void paga(int N) throws InterruptedException;
    protected abstract void prossimoCliente() throws InterruptedException;


    public void test(int numClienti){
        for(int i=0; i<numClienti; i++){
            Cliente c = new Cliente(this);
            c.start();
            System.out.println(c.getName());
        }
        new Cassiere(this);
    }
}
