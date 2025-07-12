package modelo;

public class EstadoSistema {
    private int usuEstSistCod;      
    private String usuEstSistDesc;
    private char usuEstSistEstReg;

    public EstadoSistema() {
    }

    public EstadoSistema(int cod, String desc, char estReg) {
        this.usuEstSistCod = cod;
        this.usuEstSistDesc = desc;
        this.usuEstSistEstReg = estReg;
    }

    public int getUsuEstSistCod() {
        return usuEstSistCod;
    }

    public void setUsuEstSistCod(int usuEstSistCod) {
        this.usuEstSistCod = usuEstSistCod;
    }

    public String getUsuEstSistDesc() {
        return usuEstSistDesc;
    }

    public void setUsuEstSistDesc(String usuEstSistDesc) {
        this.usuEstSistDesc = usuEstSistDesc;
    }

    public char getUsuEstSistEstReg() {
        return usuEstSistEstReg;
    }

    public void setUsuEstSistEstReg(char usuEstSistEstReg) {
        this.usuEstSistEstReg = usuEstSistEstReg;
    }

    @Override
    public String toString() {
        return "EstadoSistema{" +
                "usuEstSistCod=" + usuEstSistCod +
                ", usuEstSistDesc='" + usuEstSistDesc + '\'' +
                ", usuEstSistEstReg=" + usuEstSistEstReg +
                '}';
    }
}
