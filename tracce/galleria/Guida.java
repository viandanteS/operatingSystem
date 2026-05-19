package tracce.galleria;

public class Guida extends Thread {

    private int lingua;
    private GalleriaAbstract g;

    public Guida(GalleriaAbstract g, int lingua){
        this.g = g;
        this.lingua = lingua;
    }

    public void run(){
        try {
            while(true) {
                g.attendiVisitatori(lingua);
                g.terminaVisita(lingua);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
