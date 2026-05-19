package tracce.casellosemplice;

public abstract class AbstractCasello {

    protected final int TARIFFA,N;
    protected int incasso=0;


    public AbstractCasello(int tariffa, int numPorte){
        TARIFFA=tariffa;
        N=numPorte;
    }


    protected abstract int accoda() throws InterruptedException;

    protected abstract void paga(Veicolo v, int varco);

    protected abstract void rilascia(int varco) throws InterruptedException;


    public void test(int nVeicoli){
        Veicolo[] veicoli=new Veicolo[nVeicoli];
        for(int i=0;i<nVeicoli;i++){
            veicoli[i]=new Veicolo(this);
            veicoli[i].start();
        }


        for(int i=0;i<nVeicoli;i++){
            try {
                veicoli[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(incasso);
        System.out.println("finish");
    }
}
