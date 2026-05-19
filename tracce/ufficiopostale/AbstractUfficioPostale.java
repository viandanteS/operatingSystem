package tracce.ufficiopostale;

import java.util.Random;

public abstract class AbstractUfficioPostale {

        protected int numFile=3;
        protected int numTicket = 50;
        protected int[] ticketRimanenti = new int[numFile];

        public AbstractUfficioPostale() {
            for(int i=0; i<numFile; i++){
                ticketRimanenti[i]=numTicket;
            }
        }

        protected String operazioni = "ABC";
        protected Impiegato[] impiegati = new Impiegato[numFile];

        public abstract boolean ritiraTicket(String operazione) throws InterruptedException;
        public abstract void attendiSportello() throws InterruptedException;
        public abstract void prossimoCliente() throws InterruptedException;
        public abstract void eseguiOperazione() throws InterruptedException;


        public void test(int numClienti)throws InterruptedException{
            for(int i=0; i<numClienti; i++){
                String operazione = ""+operazioni.charAt(new Random().nextInt(operazioni.length()));
                Cliente c = new Cliente(this,operazione);
                c.start();
            }
            for(int i=0;i<numFile;i++){
                impiegati[i] = new Impiegato((""+operazioni.charAt(i)),this);
                impiegati[i].setDaemon(true);
                impiegati[i].start();
            }

        }

}
