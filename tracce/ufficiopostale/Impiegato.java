package tracce.ufficiopostale;

import java.util.concurrent.TimeUnit;

public class Impiegato extends Thread {
    private String operazione;
    private AbstractUfficioPostale uf;
    private final int aA=3;
    private final int aB=5;
    private final int aC=7;

    public Impiegato(String operazione, AbstractUfficioPostale uf) {
        this.operazione = operazione;
        this.uf = uf;
    }

    @Override
    public void run() {
        try {
            while(true){
                uf.prossimoCliente();
                uf.eseguiOperazione();
                //attendi(operazione);
            }
        }catch (InterruptedException e){}
    }

    public void attendi() throws InterruptedException{
        if(operazione.equals("A")){
            TimeUnit.MICROSECONDS.sleep(aA);
            return;
        }
        if(operazione.equals("B")){
            TimeUnit.MICROSECONDS.sleep(aB);
            return;
        }
        TimeUnit.MICROSECONDS.sleep(aC);
        return;
    }
}
