package modelo;

public class EstadoSistema {
    private char usuEstSistCod;
    private String usuEstSistDesc;
    private char usuEstSistEstReg;

    public EstadoSistema() {
    }

    public EstadoSistema(char cod, String desc, char estReg) {
        usuEstSistCod = cod;
        usuEstSistDesc = desc;
        usuEstSistEstReg = estReg;
    }

    public char getUsuEstSistCod() {
        return usuEstSistCod;
    }

    public void setUsuEstSistCod(char usuEstSistCod) {
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

    public String toString() {
        return "EstadoSistema {" + "usuEstSistCod = " + usuEstSistCod + ", usuEstSistDesc = " + usuEstSistDesc + ", usuEstSistEstReg = " + usuEstSistEstReg + "}";
    }
}