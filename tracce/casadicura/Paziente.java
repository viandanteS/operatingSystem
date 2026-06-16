package tracce.casadicura;

public class Paziente extends Thread{

    private CasaDiCuraAbstract casaDiCura;

    public Paziente(CasaDiCuraAbstract casaDiCura) {
        this.casaDiCura = casaDiCura;
    }

    @Override
    public void run() {
        try{
            casaDiCura.pazienteEntra();
            System.out.println("Paziente in sala operatoria");
            casaDiCura.pazienteEsci();
            System.out.println("Paziente "+ this.getName()+ " e' uscito.");
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

}
