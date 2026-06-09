package tracce.funivia;

public abstract class AbstractFunivia {

    abstract void pilotaStart() throws InterruptedException;
    abstract void pilotaEnd() throws InterruptedException;
    abstract void turistaSali(int t) throws InterruptedException;
    abstract void turistaScendi(int t) throws InterruptedException;

    void test(){

        for (int i = 0; i<18; i++){
            new Turista(0,this).start();
        }
        for (int i = 0; i<9; i++){
            new Turista(1,this).start();
        }
        new Pilota(this).start();
    }



}
