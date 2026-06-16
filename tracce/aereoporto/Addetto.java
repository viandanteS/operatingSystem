package tracce.aereoporto;

public class Addetto extends Thread{

    private BancoCheckIn bancoCheckIn;

    public Addetto(BancoCheckIn bancoCheckIn){
        this.bancoCheckIn = bancoCheckIn;
        this.setDaemon(true);
    }
    @Override
    public void run(){
        while(true){
            try{

                bancoCheckIn.pesaERegistra();

                bancoCheckIn.prossimoPasseggero();
            }catch(InterruptedException e){}
        }
    }

}
