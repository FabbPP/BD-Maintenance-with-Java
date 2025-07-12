package modelo;

import java.math.BigDecimal;

public class DetalleFactura{
    private int facCod;
    private int proCod;
    private int detCan;
    private BigDecimal detPre;
    private BigDecimal detSub;
    private char detEstReg;

    public DetalleFactura() {
    }

    public DetalleFactura(int cod, int pCod, int can, BigDecimal pre, BigDecimal sub, char estReg) {
        facCod = cod;
        proCod = pCod;
        detCan = can;
        detPre = pre;
        detSub = sub;
        detEstReg = estReg;
    }

    public int getFacCod() {
        return facCod;
    }

    public void setFacCod(int cod) {
        facCod = cod;
    }

    public int getProCod() {
        return proCod;
    }

    public void setProCod(int pCod) {
        proCod = pCod;
    }

    public int getDetCan() {
        return detCan;
    }

    public void setDetCan(int can) {
        detCan = can;
    }

    public BigDecimal getDetPre() {
        return detPre;
    }

    public void setDetPre(BigDecimal pre) {
        detPre = pre;
    }

    public BigDecimal getDetSub() {
        return detSub;
    }

    public void setDetSub(BigDecimal sub) {
        detSub = sub;
    }

    public char getDetEstReg() {
        return detEstReg;
    }

    public void setDetEstReg(char estReg) {
        detEstReg = estReg;
    }

    public String toString() {
        return "DetalleFactura{" +
               "facCod=" + facCod +
               ", proCod=" + proCod +
               ", detCan=" + detCan +
               ", detPre=" + detPre +
               ", detSub=" + detSub +
               ", DetEstReg =" + detEstReg +
               '}';
    }
}