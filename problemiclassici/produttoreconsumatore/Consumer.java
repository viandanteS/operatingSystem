package problemiclassici.produttoreconsumatore;

import java.util.Random;

public class Consumer extends Thread{

    private static AbstractBufferCondiviso buffer;
    private final int minTime=500;
    private final int maxTime=2000;
    Random rand=new Random();

    public Consumer(AbstractBufferCondiviso buffer){
        this.buffer = buffer;
    }

    public void run(){
        while(true){
            consume();
        }
    }

    private void consume() {
        try {
            System.out.println("Consuming");
            buffer.get();
            Thread.sleep(rand.nextInt((maxTime-minTime)+1+minTime));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
