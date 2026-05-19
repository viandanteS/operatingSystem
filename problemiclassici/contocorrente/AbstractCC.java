package problemiclassici.contocorrente;

import java.sql.SQLOutput;

public abstract class AbstractCC {

    int saldo;


    public AbstractCC(int saldo){
        this.saldo=saldo;
    }

    public abstract void deposita(int x);
    public abstract void preleva(int x);

    protected int getSaldo() {
        return saldo;
    }


}
