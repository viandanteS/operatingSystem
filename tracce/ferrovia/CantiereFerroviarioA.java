package tracce.ferrovia;

public abstract class CantiereFerroviarioA {

    protected final int numBinari;
    protected int numOpRot,numOpTrav;

    public CantiereFerroviarioA(int numBinari){
        this.numBinari = numBinari;
    }

    protected abstract void lavora(int t) throws InterruptedException;
    protected abstract void termina(int t) throws InterruptedException;

    public void test(int opRotaie,int opTraverse){

        numOpRot=opRotaie; numOpTrav=opTraverse;


        for(int i = 0; i < opRotaie; i++){
            new Operaio(1,this).start();
        }
        for(int i = 0; i < opTraverse; i++){
            new Operaio(0,this).start();
        }

    }


}
