package problemiclassici.contocorrente;

public class ContoCorrenteNTS extends AbstractCC {

    public ContoCorrenteNTS(int saldo) {
        super(saldo);
    }

    @Override
    public void deposita(int x) {
        this.saldo += x;
    }

    @Override
    public void preleva(int x) {
        this.saldo -= x;
    }


    public static void main(String[] args) throws InterruptedException {
        final int saldoIniziale=100000;
        final int X=100;
        final int N=5000;
        Correntista[] correntisti = new Correntista[200];
        ContoCorrenteNTS contoCorrenteNTS = new ContoCorrenteNTS(saldoIniziale);
        for(int i=0;i<200;i++) {
            correntisti[i] = new Correntista(contoCorrenteNTS,N,X);
            correntisti[i].start();
        }

        for(int i=0;i<200;i++) {
            correntisti[i].join();
        }

        if ((saldoIniziale - contoCorrenteNTS.getSaldo()) == 0) {
            System.out.println("Corretto");
        } else {
            System.out.println("Errore");
        }



    }

}
