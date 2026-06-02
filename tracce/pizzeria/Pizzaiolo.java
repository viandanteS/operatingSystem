package tracce.pizzeria;

import java.util.concurrent.TimeUnit;

public class Pizzaiolo extends Thread{

    private final PizzeraAstratta pizzeria;

    public Pizzaiolo(PizzeraAstratta pizzeria){
        this.pizzeria = pizzeria;

        this.setDaemon(true);
    }


    public void run(){

        while(true){
            try {
                pizzeria.preparaPizza();

                pizzeria.serviPizza();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
