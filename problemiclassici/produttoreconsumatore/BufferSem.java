package problemiclassici.produttoreconsumatore;

import java.util.concurrent.Semaphore;

public class BufferSem extends AbstractBufferCondiviso{
    private Semaphore ciSonoElementi=new Semaphore(0);
    private Semaphore ciSonoPostiVuoti;
    private Semaphore mutex=new Semaphore(1);

    public BufferSem(int N){
        super(N);
        ciSonoPostiVuoti=new Semaphore(N);
    }

    @Override
    public void put(int i){
        try{
            ciSonoPostiVuoti.acquire();
            mutex.acquire();
            buf[in]=i;
            in=(in+1)%this.buf.length;
            mutex.release();
            ciSonoElementi.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int get() {
        int prelevato=0;
        try{
            ciSonoElementi.acquire();
            mutex.acquire();
            prelevato=buf[out];
            out=(out+1)%this.buf.length;
            mutex.release();
            ciSonoPostiVuoti.release();
            return prelevato;
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return prelevato;
    }


    public static void main(String[] args){
        BufferSem buffer=new BufferSem(10);
        buffer.test();
    }
}
