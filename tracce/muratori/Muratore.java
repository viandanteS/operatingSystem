package tracce.muratori;


import java.util.concurrent.TimeUnit;

public class Muratore extends Thread{

    private final int type; //0 mattoni 1 cemento
    private final AbstractCasa casa;


    public Muratore(AbstractCasa casa,int type){
        this.casa = casa;
        this.type = type;
    }

    @Override
    public void run(){
        boolean notTerminato=true;
        while(notTerminato){
            try {
                prepara();

                notTerminato = casa.inizia(type);

                riposa();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            casa.termina(type);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void prepara() {
        try{
            if (type == 0) {
                TimeUnit.MILLISECONDS.sleep(500);
            } else {
                TimeUnit.MILLISECONDS.sleep(700);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private void riposa() {
        try{
            TimeUnit.MILLISECONDS.sleep(550);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }


}