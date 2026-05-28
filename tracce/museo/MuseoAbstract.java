package tracce.museo;

public abstract class MuseoAbstract {

    protected abstract void visitaSA() throws InterruptedException;
    protected abstract void terminaVisitaSA() throws InterruptedException;
    protected abstract void visitaSD() throws InterruptedException;
    protected abstract void terminaVisitaSD() throws InterruptedException;


    public void test(int numVisitatori){

        for(int i=0;i<numVisitatori;i++){
            new Visitatore(this).start();
        }

    }
}
