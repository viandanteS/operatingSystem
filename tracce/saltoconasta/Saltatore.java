package tracce.saltoconasta;

import java.sql.Time;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Saltatore extends Thread {

    private final int id;
    private final Salto salto;
    private final Random random = new Random();
    public Saltatore(int id, Salto salto) {
        this.id = id;
        this.salto = salto;
    }

    public int getIdSaltatore(){return id;}

    public void run() {
        try{
            salto.inizio(this);

            System.out.println("Turno "+id);
            TimeUnit.MILLISECONDS.sleep(random.nextInt(150,650));
            int posizione = salto.arrivo(this);

            System.out.println(id+" Posizione temporanea: "+posizione);

        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }



}
