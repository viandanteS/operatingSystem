package tracce.ferrovia;

import java.util.concurrent.TimeUnit;

public class Operaio extends Thread{

    private int type;
    private CantiereFerroviarioA cf;

    public Operaio(int type,CantiereFerroviarioA cf){
        this.type = type;
        this.cf = cf;
    }



    public void run(){

        try{
            while(true){
                System.out.println("Operaio tipo "+type+" "+this.threadId()+" prepara");
                if(type == 0){
                    attendi(2); }
                else{
                    attendi(3);}
                cf.lavora(type);
                cf.termina(type);
                System.out.format("Operaio %d termina e riposa \n ", this.threadId());
                attendi(1);

            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void attendi(int time) throws InterruptedException{

        TimeUnit.SECONDS.sleep(time);


    }


}
