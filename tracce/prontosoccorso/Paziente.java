package tracce.prontosoccorso;

public class Paziente extends Thread{

    private final ProntoSoccorso ps;

    public Paziente(ProntoSoccorso ps){
        this.ps = ps;
    }


    public void run(){
        try{
            ps.accediPaziente();

            ps.esciPaziente();
            System.out.println(Thread.currentThread().getName()+ " se ne va.");
        }
         catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
