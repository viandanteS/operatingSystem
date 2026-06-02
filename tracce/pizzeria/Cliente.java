package tracce.pizzeria;

public class Cliente extends Thread{

    private final PizzeraAstratta pizzeria;

    public Cliente(PizzeraAstratta pizzeria) {
        this.pizzeria = pizzeria;
    }


    public void run(){

        try {
            pizzeria.entra();

            pizzeria.mangiaPizza();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

}
