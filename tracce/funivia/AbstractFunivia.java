package tracce.funivia;

public abstract class AbstractFunivia {

    protected static final int TURISTA_A_PIEDI = 0;
    protected static final int TURISTA_IN_BICI = 1;
    protected static final int POSTI_FUNIVIA = 6;

    abstract void pilotaStart();
    abstract void pilotaEnd();
    abstract void turistaSali(int t);
    abstract void turistaScendi(int t);

    void test(int numTuristi){

        for (int i = 0; i<numTuristi; i++){

        }


    }



}
