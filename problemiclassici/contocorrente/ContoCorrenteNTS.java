package problemiclassici.contocorrente;

public class ContoCorrenteNTS extends AbstractCC {

    public ContoCorrenteNTS(int saldo) {
        super(saldo);
    }

    @Override
    public void deposita(int x) {
        this.saldo += x;
    }
    /*Questa operazione in memoria equivale a fare
    * reg1 <- saldo
    * reg2 <- x
    * reg1 <- saldo+x
    * saldo= reg1 */

    @Override
    public void preleva(int x) {
        this.saldo -= x;
    }
    /*Questa operazione in memoria equivale a fare
     * reg3 <- saldo
     * reg4 <- -x
     * reg3 <- saldo+x
     * saldo= reg3 */


    /* Quando i correntisti vengono schedulati c'è possibilità che le istruzioni base "macchina" si interfoglino,
        sovrascrivendo valori presenti nei registri.
        In questo caso l'operazione critica è nel fatto che le istruzioni dove viene sovrascritto il saldo potrebbero essere schedulate una dopo l'altra
        alterando permanentemente e non correttametne il valore assunto.
     */


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
