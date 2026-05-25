package tracce.aziendaagricola;



public abstract class AbstractAziendaAgricola {


    protected static int COSTO_SACCHETTO=3;
    protected int incasso=0;




    protected abstract void paga(int N) throws InterruptedException;
    protected abstract void restock()  throws InterruptedException;
    protected abstract void ritira()  throws InterruptedException;


    public void test(int N) throws InterruptedException {

        new Magazziniere(this).start();
        Cliente[] clienti=new  Cliente[N];
        for(int i=0;i<N;i++){
            clienti[i]=new Cliente(this,""+i);
            clienti[i].start();
        }


        for(int i=0;i<N;i++){
            clienti[i].join();
        }
        System.out.println("Sacchetti richiesti "+Cliente.getContador());
        System.out.println("L'incasso è "+incasso);


    }
}
