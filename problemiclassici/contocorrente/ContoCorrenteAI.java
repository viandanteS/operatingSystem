package problemiclassici.contocorrente;

import java.util.concurrent.atomic.AtomicInteger;

public class ContoCorrenteAI extends AbstractCC {

    private AtomicInteger deposito;

    public ContoCorrenteAI(int saldoIniziale) {
        super(saldoIniziale);
        deposito = new AtomicInteger(saldoIniziale);
    }

    @Override
    public void deposita(int x) {
        deposito.addAndGet(x);
    }

    @Override
    public void preleva(int x) {
        deposito.addAndGet(-x);
    }

    @Override
    public int getSaldo() {
        return deposito.get();
    }

    /*
        Una somma o una sottrazione come abbiamo detto non sono operazioni atomiche pertanto c'è il problema di
        RACE CONDITION. Questa cosa si può risolvere facendo in modo che alle variabili condivise si possa accedere solamente in modo atomico.
        Consideriamo l'istruzione contatore++;
        Se il Thread A e il Thread B leggono contemporaneamente "5" da contatore, entrambi calcoleranno "6" e scriveranno "6".
        Risultato? Abbiamo perso un incremento.

        Il meccanismo per risolvere è far si che le operazioni (che dietro le quinte sono scomposte) siano fatte in maniera atomica.
        Il meccanismo compareAndSet(valoreAtteso, nuovoValore) ragiona in questo modo:

        "**1) Aggiorna questa variabile al nuovoValore,
         **2) ma solo se il suo valore attuale in memoria è ancora uguale al valoreAtteso (cioè quello che avevo letto io originariamente).
         Altrimenti, non fare nulla e dimmi che hai fallito."

        public final int addAndGet(int delta){
            for(;;){
                int current = get();
                int next= current+next;
                if(compareAndSet(current,next)){ **1)
                    return next;
                }
            }
       }

       public final boolean compareAndSet(int expect, int update) {
           return unsafe.compareAndSwapInt(this, valueOffset, **2)
                                                    expect, update);
       }

    */

    public static void main(String[] args) throws InterruptedException {
        final int saldoIniziale = 100000;
        final int X = 100;
        final int N = 5000;
        Correntista[] correntisti = new Correntista[200];
        ContoCorrenteAI contoCorrenteAI = new ContoCorrenteAI(saldoIniziale);
        for (int i = 0; i < 200; i++) {
            correntisti[i] = new Correntista(contoCorrenteAI, N, X);
            correntisti[i].start();
        }
        for (int i = 0; i < 200; i++) {
            correntisti[i].join();
        }

        if ((saldoIniziale - contoCorrenteAI.getSaldo()) == 0) {
            System.out.println("Corretto");
        } else {
            System.out.println("Errore");
        }
    }
}
