package problemiclassici.produttoreconsumatore;

import java.util.Arrays;

public abstract class AbstractBufferCondiviso {

    protected int[] buf;
    protected int lastProduced = 0;
    protected int lastConsumed=0;


    public AbstractBufferCondiviso(int N){
        buf=new int[N];
        Arrays.fill(buf,-1);

    }

    public abstract void put(int i) throws InterruptedException;
    public abstract int get() throws InterruptedException;



    protected void test(int consumers, int producers){

        for(int i=0;i<consumers;i++){
            new Consumer(this).start();
        }
        for(int i=0;i<producers;i++) {
            new Producer(this).start();
        }
    }
}
