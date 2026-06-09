package tracce.muratori;

public abstract class AbstractCasa{

    protected final int STRATI;

    public AbstractCasa(int strati){
        STRATI = strati;
    }


    public abstract boolean inizia(int t) throws  InterruptedException;
    public abstract void termina(int t) throws InterruptedException;


    public void test(int numeroMattoni,int numeroCemento){
        for(int i=0;i<numeroMattoni;i++){
            new Muratore(this,0).start();
        }
        for(int i=0;i<numeroCemento;i++){
            new Muratore(this,1).start();
        }
    }
}