package tracce.saltoconasta;

import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;

public class SaltoSem extends Salto {

    private final Semaphore[] semaphoreJumper = new Semaphore[NUM_SALTATORI];
    private final Semaphore syncSem = new Semaphore(0);


    private final Random random = new Random();
    private int currentJumper = 0;

    private final TreeMap<Double, Integer> classifica = new TreeMap<>();

    public SaltoSem() {
        for (int i = 0; i < NUM_SALTATORI; i++) {
            semaphoreJumper[i] = new Semaphore(0);
        }
    }

    @Override
    void inizio(Saltatore s) throws InterruptedException {

        int id = s.getIdSaltatore();
        semaphoreJumper[id].acquire();

    }

    @Override
    int arrivo(Saltatore s) throws InterruptedException {

        double altezzaSaltoS = random.nextDouble(4.5, 6.5);
        classifica.put(altezzaSaltoS, s.getIdSaltatore());

        int i = 0;
        for (Double key : classifica.keySet()) {
            if (classifica.get(key).equals(s.getIdSaltatore())) break;
            i++;
        }

        syncSem.release();
        return i;
    }

    @Override
    boolean successivo() throws InterruptedException {
        if (currentJumper == NUM_SALTATORI) {

            System.out.println("Salti Terminati!");
            System.out.println("CLASSIFICA");
            int i = 0;
            for (Double key : classifica.keySet().stream().toList().reversed()) {
                System.out.println(++i + "° id: " + classifica.get(key) + " altezza: " + key);
            }
            return false;
        }
        semaphoreJumper[currentJumper].release();
        syncSem.acquire();

        currentJumper++;

        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        SaltoSem s = new SaltoSem();
        s.test();
    }
}
