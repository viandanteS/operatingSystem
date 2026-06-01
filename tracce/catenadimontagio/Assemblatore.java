package tracce.catenadimontagio;

import java.util.concurrent.TimeUnit;

public class Assemblatore extends Thread {

    private final CatenaDiMontaggioA cdm;
    private final int[] codaDx;
    private final int[] codaSx;

    public Assemblatore(int[] codaDx,int codaSx[],CatenaDiMontaggioA cdm){
        this.codaDx=codaDx;
        this.codaSx=codaSx;
        this.cdm=cdm;

    }

    public void run(){

        try{
            for(int i=0;i<codaDx.length;i++){
                System.out.format("Assemblatore richiede pezzi per %d° articolo\n",i+1);
                cdm.richiediProduzione(codaSx[i],codaDx[i]);

                cdm.assembla();




            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }

    }



}
