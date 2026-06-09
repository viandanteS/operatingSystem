package tracce.funivia;

public class Turista extends Thread{
    private final int type;
    private final AbstractFunivia funivia;

    public  Turista(int type, AbstractFunivia funivia){
        this.type = type;
        this.funivia = funivia;
    }


    @Override
    public void run(){

        try{

            funivia.turistaSali(type);

            funivia.turistaScendi(type);

        }catch (InterruptedException e){
            e.printStackTrace();
        }


    }
}
