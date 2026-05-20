package tracce.aziendaagricola;

import java.util.Random;

public class Cliente extends Thread{

    private AbstractAziendaAgricola aziendaAgricola;
    Random rand = new Random();

    public Cliente(AbstractAziendaAgricola aziendaAgricola,String id){
        super(id);
        this.aziendaAgricola = aziendaAgricola;
    }

    public void run(){
        final int N=rand.nextInt(1,10);
        try {
            aziendaAgricola.paga(N);
            //System.out.println("Cliente"+this.getId()+" ritira");
            aziendaAgricola.ritira(N);
            //System.out.println("Cliente"+this.getId()+" termina");
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
