package tracce.biblioteca;


import java.util.Random;

public abstract class BibliotecaAbstract {

    Random rand = new Random();

    abstract void richiestiPrestito() throws InterruptedException;
    abstract void registraPrestito() throws InterruptedException;
    abstract void esci() throws  InterruptedException;
    abstract void prossimoUtente() throws InterruptedException;


    public void test(int numUtenti){

        for(int i = 0 ; i < numUtenti ; i++){
            new Utente(rand.nextInt(0,2),this).start();
        }
        new Bibliotecario(this).start();


    }
}
