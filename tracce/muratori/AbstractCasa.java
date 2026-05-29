package tracce.muratori;

public abstract class AbstractCasa {

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
            new Thread(new Muratore(0,this)).start();
        }
        for(int i=0;i<nC;i++){
            new Thread(new Muratore(1,this)).start();
        }

    }
}