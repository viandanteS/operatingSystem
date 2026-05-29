package tracce.muratori;


import java.util.concurrent.Semaphore;

public class CasaSem extends AbstractCasa{

    private static final int LATI=4;
    private Semaphore mutexPossoLavorare = new Semaphore(1,true);
    private int counter=0;
    private int latiEseguiti=0;
    private Semaphore codaMattoni= new Semaphore(nM,true);
    private Semaphore codaCemento= new Semaphore(nC,true);

    public CasaSem(int i, int i1, int i2) {
        super(i,i1,i2);

    }


    @Override
    public void termina(int type) throws InterruptedException {

    }

    @Override
    public boolean inizia(int type) throws InterruptedException {


        if(turnoMattoni){

        }



        if(type==0){
            codaMattoni.acquire();
            mutexPossoLavorare.acquire();

            counter ++;
            if(counter<20){
                if(latiEseguiti!=LATI){counter=0;}
                codaCemento.release();
                mutexPossoLavorare.release();
                return true;
            }else{
                return false;
            }


        }else {
            codaCemento.acquire();
            mutexPossoLavorare.acquire();

        }




    }



    public static void main(String[] args) throws InterruptedException {

        CasaSem casa = new CasaSem(20,5,7);
        casa.test();

    }


}
