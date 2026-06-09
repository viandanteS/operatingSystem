package tracce.prontosoccorso;

import java.util.concurrent.TimeUnit;

public class Medico extends Thread{
    private final ProntoSoccorso ps;

    public Medico(ProntoSoccorso ps){
        this.ps = ps;
        this.setDaemon(true);
    }


    public void run(){
        try{
            while(true){
                ps.iniziaVisita();

                ps.terminaVisita();
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
