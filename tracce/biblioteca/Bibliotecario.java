package tracce.biblioteca;

import java.util.concurrent.TimeUnit;

public class Bibliotecario extends Thread {

    private int clientsCounter;
    private final BibliotecaAbstract biblioteca;


    public Bibliotecario(BibliotecaAbstract biblioteca) {
        this.biblioteca = biblioteca;
        this.setDaemon(true);
    }

    public void run() {

        while (true) {
            try {
                if (clientsCounter == 15) {
                    TimeUnit.SECONDS.sleep(10);
                    clientsCounter = 0;
                }
                biblioteca.registraPrestito();

                biblioteca.prossimoUtente();
                clientsCounter++;

            }catch (InterruptedException e){

            }


        }


    }
}
