package problemiclassici.produttoreconsumatore;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BufferLC extends AbstractBufferCondiviso {

    private Lock lock = new ReentrantLock();
    Condition possoProdurre = lock.newCondition();
    Condition possoConsumare = lock.newCondition();



    private int counter = 0;

    public BufferLC(int N) {
        super(N);
    }

    @Override
    public void put(int i) throws InterruptedException {
        lock.lock();
        try {
            while(!possoProdurre()){
                possoProdurre.await();
            }
            buf[lastProduced] = i;
            lastProduced=(lastProduced+1)%buf.length;
            counter++;
        }finally {
            possoConsumare.signal();
            lock.unlock();
        }
    }

    private boolean possoProdurre(){
        return counter < buf.length;
    }

    @Override
    public int get() throws InterruptedException {
        lock.lock();
        int ret;
        try {
            while(!possoConsumare()){
                possoConsumare.await();
            }
            ret=buf[lastConsumed];
            lastConsumed=(lastConsumed+1)%buf.length;
        }finally {
            possoProdurre.signal();
            counter--;
            lock.unlock();
        }
        return ret;
    }
    private boolean possoConsumare(){
        return counter>0;
    }


    public static void  main(String[] args) {
        BufferLC buffer=new BufferLC(7);
        buffer.test(3,5);
    }
}
