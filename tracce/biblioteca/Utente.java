package tracce.biblioteca;

import java.security.SecureRandom;

public class Utente extends Thread {

    private final int type;
    private final BibliotecaAbstract biblioteca;
    private final SecureRandom randomStringGenerator = new SecureRandom();
    private final String codiceLibro;

    public Utente(int type, BibliotecaAbstract biblioteca) {
        this.biblioteca = biblioteca;
        this.type = type;
        int codiceLibroInt = randomStringGenerator.nextInt(1000, 10000);
        this.codiceLibro = String.valueOf(codiceLibroInt);
    }

    public String getCodiceLibro() {
        return codiceLibro;
    }

    public int getType() {
        return type;
    }

    public void run() {
        try {
            biblioteca.richiestiPrestito();
            System.out.println("Utente " + Thread.currentThread().getName() + " attende approvazione registrazione");
            biblioteca.esci();
            System.out.println("Utente " + Thread.currentThread().getName() + " se ne va");

        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }


}
