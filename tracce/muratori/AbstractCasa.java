package tracce.muratori;

public abstract class AbstractCasa {
    protected static final int LATI=4;
    final int N,nM,nC;

    public AbstractCasa(int N, int nM, int nC){
        this.N=N;
        this.nM = nM;
        this.nC = nC;
    }


    public abstract void termina(int type) throws InterruptedException;

    public abstract boolean inizia(int type) throws InterruptedException;


    public void test(){

        for(int i=0;i<nM;i++){
            new Muratore(0,this).start();

        }
        for(int i=0;i<nC;i++){
            new Muratore(1,this).start();
        }

    }
}