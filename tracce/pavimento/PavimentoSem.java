package tracce.pavimento;

import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;

public class PavimentoSem extends Pavimento{

    private final Semaphore[] lavoroDisponibileTypeT = {new Semaphore(base),new Semaphore(0)};

    private final Semaphore[] bloccoColla = new Semaphore[base];
    private final Semaphore[] bloccoPiastrelle = new Semaphore[base];

    private final Semaphore mutex = new Semaphore(1);

    private int contaFilaCollaCompletata=0;
    private int contaFilaPiastrelleCompletate=0;

    private int currentColla = 0;
    private int currentPiastrelle=0;

    private int completatiColla=0;
    private int completatiPiastrelle=0;


    private boolean terminato=false;

    public PavimentoSem(int base, int altezza){
        super(base,altezza);
        for(int i=0;i<base;i++){
            bloccoColla[i]=new Semaphore(1);
            bloccoPiastrelle[i]=new Semaphore(0);
        }
    }

    //0->Colla 1->Piastrelle
    @Override
    String inizia(int T) throws InterruptedException {

        String lavoroSulBlocco = "";

        if(T==0){
            lavoroDisponibileTypeT[T].acquire();
            if(terminato) return null;
            mutex.acquire();
            bloccoColla[currentColla].acquire();
            lavoroSulBlocco = "B_"+contaFilaCollaCompletata+"_"+currentColla;
            currentColla++;
            mutex.release();


        }else if(T==1) {
            lavoroDisponibileTypeT[T].acquire();
            if(terminato) return null;
            mutex.acquire();
            bloccoPiastrelle[currentPiastrelle].acquire();
            lavoroSulBlocco = "B_"+contaFilaPiastrelleCompletate+"_"+currentPiastrelle;
            currentPiastrelle++;
            mutex.release();
        }
        return lavoroSulBlocco;
    }

    @Override
    void finisci(int T, String B) throws InterruptedException {
        StringTokenizer st = new StringTokenizer(B,"_");

        st.nextToken();
        int riga = Integer.parseInt(st.nextToken());
        int colonna = Integer.parseInt(st.nextToken());
        mutex.acquire();
        if(T==0){
            completatiColla++;
            if(currentColla==base && completatiColla==numPiastrellistriColla) {
                lavoroDisponibileTypeT[T+1].release(base);
                currentColla=0;
                completatiColla=0;
                contaFilaCollaCompletata++;
            }
            bloccoPiastrelle[colonna].release();
        }else if(T==1){
            completatiPiastrelle++;
            if(currentPiastrelle==base && completatiPiastrelle==numPiastrellistriPiastrelle) {
                lavoroDisponibileTypeT[T-1].release(base);
                currentPiastrelle=0;
                completatiPiastrelle=0;
                contaFilaPiastrelleCompletate++;
            }
            bloccoColla[colonna].release();
        }
        if(contaFilaPiastrelleCompletate==altezza && !terminato){
            terminato=true;
            lavoroDisponibileTypeT[0].release(numPiastrellistriColla);
            lavoroDisponibileTypeT[1].release(numPiastrellistriPiastrelle);
        }
        mutex.release();

    }

    public static void main(String[] args) throws InterruptedException {
        PavimentoSem pavimento = new PavimentoSem(8,4);
        pavimento.test(8,8);
    }
}
