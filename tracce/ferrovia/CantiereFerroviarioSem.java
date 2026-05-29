package tracce.ferrovia;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class CantiereFerroviarioSem extends CantiereFerroviarioA{


    Semaphore mutex = new Semaphore(1);

    Semaphore semTraverse = new Semaphore(numBinari);
    Semaphore semRotaie = new Semaphore(0);


    Queue<Integer> bXTraverse = new LinkedList<>();
    Queue<Integer> bXRotaie= new LinkedList<>();

    Map<Long,Integer> opBinarioOccupato = new HashMap<>();


    public CantiereFerroviarioSem(int numBinari){
        super(numBinari);
        for(int i=0; i<numBinari; i++){
            bXTraverse.add(i);
        }
    }

    @Override
    protected void lavora(int t) throws InterruptedException {
        long thisOp = Thread.currentThread().getId();

        if(t==0){
            //qua può acquisire solo se ci sono permessi sul semaforo ( se ci sono permessi allora c'è almeno un elemento in coda )
            semTraverse.acquire();
            mutex.acquire();
            int binarioDaLavorare = bXTraverse.poll();
            opBinarioOccupato.put(thisOp, binarioDaLavorare);
            mutex.release();

        }
        else {

            semRotaie.acquire();
            mutex.acquire();
            int binarioDaLavorare = bXRotaie.poll();
            opBinarioOccupato.put(thisOp, binarioDaLavorare);
            mutex.release();


        }

    }

    @Override
    protected void termina(int t) throws InterruptedException {
        long thisOp = Thread.currentThread().getId();


        if(t==0){
            //il numero di elementi presenti nella coda deve essere uguale al numero di permessi del semaforo relativo a ciascuna coda
            // quindi quando l'operatore termina mette il binario su cui ha lavorato nella coda opposta e segnala all'operaio dell'altro tipo che può ora estrarre dalla coda
            mutex.acquire();
            bXRotaie.add(opBinarioOccupato.get(thisOp));
            opBinarioOccupato.remove(thisOp);
            semRotaie.release();
            mutex.release();


        }else {
            mutex.acquire();
            int binarioXCodaTraverse = opBinarioOccupato.get(thisOp);
            opBinarioOccupato.remove(thisOp);
            bXTraverse.add(binarioXCodaTraverse);
            semTraverse.release();
            mutex.release();

        }

    }




    public static void main(String[] args) throws InterruptedException {
        CantiereFerroviarioSem cf = new CantiereFerroviarioSem(3);
        cf.test(4,4);
    }


}
