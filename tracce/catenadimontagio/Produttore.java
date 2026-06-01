package tracce.catenadimontagio;

public class Produttore extends Thread{

    private int typeProduttore;
    private CatenaDiMontaggioA cdm;

    public Produttore(int typeProduttore, CatenaDiMontaggioA cdm){
        this.typeProduttore = typeProduttore;
        this.cdm = cdm;
        this.setDaemon(true);
    }


    public void run(){
        try{
            while(true){

                cdm.produci(typeProduttore);

            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }


}
