package tracce.aziendaagricola;

public class Magazziniere extends Thread{

    private AbstractAziendaAgricola aziendaAgricola;

    public Magazziniere(AbstractAziendaAgricola aziendaAgricola){
        this.aziendaAgricola = aziendaAgricola;
        this.setDaemon(true);
    }

    public void run(){
        try {
            while(true) {
                aziendaAgricola.restock();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }


}
