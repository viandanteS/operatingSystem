package tracce.galleria;

public class Visitatore extends Thread {
    GalleriaAbstract g;
    private final int lingua;

    public Visitatore(GalleriaAbstract g,int lingua) {
        this.lingua = lingua;
        this.g = g;
    }


    public void run() {
        try{
            g.iniziaVisita(lingua);
            g.terminaVisita(lingua);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
