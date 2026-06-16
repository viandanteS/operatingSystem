package tracce.saltoconasta;

import java.util.concurrent.TimeUnit;

public class Giudice extends Thread {

    private final Salto salto;

    public Giudice(Salto salto) {
        this.salto = salto;
    }

    public void run() {
        boolean condition = true;
        while (condition){
            try {

                condition=salto.successivo();
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
