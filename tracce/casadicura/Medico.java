package tracce.casadicura;

import java.util.concurrent.TimeUnit;

public class Medico extends Thread {

    private CasaDiCuraAbstract casaDiCura;

    public Medico(CasaDiCuraAbstract casaDiCura) {
        this.casaDiCura = casaDiCura;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("preparazione sala..");
                TimeUnit.SECONDS.sleep(2);
                casaDiCura.chiamaEIniziaOperazione();
                System.out.println("Surgeon is working...");
                TimeUnit.MILLISECONDS.sleep(300);
                casaDiCura.fineOperazione();

            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
