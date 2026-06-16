package tracce.pavimento;

public abstract class Pavimento {

    protected final int base,altezza;
    protected int numPiastrellistriColla,numPiastrellistriPiastrelle;

    protected Pavimento(int base, int altezza) {
        this.base = base;
        this.altezza = altezza;
    }


    abstract String inizia ( int T ) throws InterruptedException;
    abstract void finisci( int T, String B) throws InterruptedException;


    public void test(int colla,int piastrelle){
        this.numPiastrellistriColla = colla;
        this.numPiastrellistriPiastrelle = piastrelle;
        for(int i = 0;i<colla;i++){
            new Piastrellistra(0,this).start();
        }
        for(int i = 0;i<piastrelle;i++){
            new Piastrellistra(1,this).start();
        }
    }

}
