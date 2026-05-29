package tracce.callcenter;

public abstract class CallCenter {

    private final int NUM_OPE;
    private final int NUM_CLIENTI;


    public CallCenter(int num_ope, int num_clienti) {
        NUM_OPE = num_ope;
        NUM_CLIENTI = num_clienti;
    }


    protected abstract void richiediAssistenza() throws InterruptedException;
    protected abstract void fornisciAssistenza() throws InterruptedException;
    protected abstract void terminaChiamata() throws InterruptedException;
    protected abstract void prossimoCliente() throws InterruptedException;

    public void test(int numOp,int numCli){

        for(int i=0;i<numCli;i++){
            new Cliente(this).start();
        }
        for(int i=0;i<numOp;i++){
            new Operatore(this).start();
        }

    }

}
