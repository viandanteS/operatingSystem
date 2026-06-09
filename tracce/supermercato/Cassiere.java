package tracce.supermercato;

public class Cassiere extends Thread {

    private final CassaAbstract cassa;
    private int index=0;
    public Cassiere(CassaAbstract cassa) {
        this.cassa = cassa;
        this.setDaemon(true);
        this.start();
    }
    public int  getIndex() {
        return index;
    }
    public void setIndex(int index){
        this.index=index;
    }


    public void run() {

        while (true) {
            try{
                cassa.scansiona();

                cassa.prossimoCliente();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
