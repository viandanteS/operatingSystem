package problemiclassici.produttoreconsumatore;

import java.util.Random;

public class Producer extends Thread{

    private final Random generator = new Random();
    private AbstractBufferCondiviso buffer;
    private static final int MAX_RANDOM=10;


    public Producer(AbstractBufferCondiviso buffer){
        this.buffer = buffer;
    }

    public void run(){
        while(true){
            try{
                buffer.put(generator.nextInt(MAX_RANDOM));
                System.out.println("Produced");
                Thread.sleep(1000);
            }catch(InterruptedException e){}
        }
    }
}
