package problemiclassici.produttoreconsumatore;

public abstract class AbstractBufferCondiviso {
    protected int[] buf;
    protected int in=0;
    protected int out=0;


    public AbstractBufferCondiviso(int N){
        buf=new int[N];
    }

    public abstract void put(int i) throws InterruptedException;
    public abstract int get() throws InterruptedException;



    protected void test(){

        for(int i=0;i<2;i++){
            new Consumer(this).start();
        }
        for(int i=0;i<10;i++) {
            new Producer(this).start();
        }
    }
}
