package tracce.ufficiopostale;

public class Cliente extends Thread {
    private AbstractUfficioPostale uf;
    private String operazione;

    public Cliente(AbstractUfficioPostale uf, String operazione) {
        this.uf = uf;
        this.operazione = operazione;
    }

    public void run() {
        try{
            if(!uf.ritiraTicket(operazione)){
                System.out.println("Il cliente npn può entrare perchè sono finiti i ticket");
                return;
            }
            uf.attendiSportello();
            System.out.println("Il cliente ha lasciato l'ufficio");
        }catch (Exception e){}
    }
}
