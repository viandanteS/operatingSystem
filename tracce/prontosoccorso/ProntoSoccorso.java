package tracce.prontosoccorso;

public abstract class ProntoSoccorso {

    abstract void iniziaVisita() throws InterruptedException;
    abstract void terminaVisita() throws InterruptedException;
    abstract void accediPaziente() throws InterruptedException;
    abstract void esciPaziente() throws InterruptedException;


    public void test(){

        for(int i = 0; i<30;i++){
            new Paziente(this).start();
        }
        Medico m = new Medico(this);
        m.start();
    }
}
