package tracce.aziendaagricola;

public class AziendaAgricolaMain {

    public static void main(String[] args) throws InterruptedException {

        AziendaAgricolaSem aa=new AziendaAgricolaSem();

        aa.test(120);
        System.out.println(aa.getSacchettiRichiesti());

    }

}
