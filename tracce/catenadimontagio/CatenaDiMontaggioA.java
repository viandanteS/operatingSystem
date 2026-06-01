package tracce.catenadimontagio;

public abstract class CatenaDiMontaggioA {

    private final int[] codaDx = {4,3,6};
    private final int[] codaSx = {2,4,5};



    protected abstract void richiediProduzione(int pSx,int pDx) throws InterruptedException;
    protected abstract void produci(int tipo) throws InterruptedException;
    protected abstract void assembla() throws InterruptedException;



    public void test(int nProduttoriSx,int nProduttoriDx){

        new Assemblatore(codaDx,codaSx,this).start();

        for(int i=0;i<nProduttoriDx;i++){
            new Produttore(1,this).start();
        }

        for(int i=0;i<nProduttoriSx;i++){
            new Produttore(0,this).start();
        }


    }

}
