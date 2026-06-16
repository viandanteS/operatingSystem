package tracce.casadicura;



public abstract class CasaDiCuraAbstract {

    protected abstract void chiamaEIniziaOperazione() throws InterruptedException;
    protected abstract void fineOperazione() throws InterruptedException;
    protected abstract void pazienteEntra() throws InterruptedException;
    protected abstract void pazienteEsci() throws InterruptedException;

    public void test(){
        for(int i=0;i<12;i++){
            new Paziente(this).start();
        }
        new Medico(this).start();
    }

}
