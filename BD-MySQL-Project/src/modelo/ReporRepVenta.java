package modelo;

import java.math.BigDecimal;

public class ReporRepVenta {
    private int repoRepVentCod;
    private int repCod;
    private BigDecimal repoRepVentObj;
    private int repoRepVentNum;
    private int repoRepVentCuo;
    private char repoRepVentEstReg;
    
    private String representanteNombre;  // Para mostrar nombre del representante
    
    public ReporRepVenta() {}
    
    public ReporRepVenta(int repoRepVentCod, int repCod, BigDecimal repoRepVentObj,
                        int repoRepVentNum, int repoRepVentCuo, 
                        char repoRepVentEstReg) {
        this.repoRepVentCod = repoRepVentCod;
        this.repCod = repCod;
        this.repoRepVentObj = repoRepVentObj;
        this.repoRepVentNum = repoRepVentNum;
        this.repoRepVentCuo = repoRepVentCuo;
        this.repoRepVentEstReg = repoRepVentEstReg;
    }
    
    // Constructor con descripciones
    public ReporRepVenta(int repoRepVentCod, int repCod, BigDecimal repoRepVentObj,
                        int repoRepVentNum, int repoRepVentCuo, 
                        char repoRepVentEstReg, String representanteNombre) {
        this(repoRepVentCod, repCod, repoRepVentObj, repoRepVentNum, 
             repoRepVentCuo, repoRepVentEstReg);
        this.representanteNombre = representanteNombre;
    }
    
    // Getters y Setters
    public int getRepoRepVentCod() {
        return repoRepVentCod;
    }
    
    public void setRepoRepVentCod(int repoRepVentCod) {
        this.repoRepVentCod = repoRepVentCod;
    }
    
    public int getRepCod() {
        return repCod;
    }
    
    public void setRepCod(int repCod) {
        this.repCod = repCod;
    }
    
    public BigDecimal getRepoRepVentObj() {
        return repoRepVentObj;
    }
    
    public void setRepoRepVentObj(BigDecimal repoRepVentObj) {
        this.repoRepVentObj = repoRepVentObj;
    }
    
    public int getRepoRepVentNum() {
        return repoRepVentNum;
    }
    
    public void setRepoRepVentNum(int repoRepVentNum) {
        this.repoRepVentNum = repoRepVentNum;
    }
    
    public int getRepoRepVentCuo() {
        return repoRepVentCuo;
    }
    
    public void setRepoRepVentCuo(int repoRepVentCuo) {
        this.repoRepVentCuo = repoRepVentCuo;
    }
    
    public char getRepoRepVentEstReg() {
        return repoRepVentEstReg;
    }
    
    public void setRepoRepVentEstReg(char repoRepVentEstReg) {
        this.repoRepVentEstReg = repoRepVentEstReg;
    }
    
    public String getRepresentanteNombre() {
        return representanteNombre;
    }
    
    public void setRepresentanteNombre(String representanteNombre) {
        this.representanteNombre = representanteNombre;
    }

    
    @Override
    public String toString() {
        return "ReporRepVenta{" +
                "repoRepVentCod=" + repoRepVentCod +
                ", repCod=" + repCod +
                ", repoRepVentObj=" + repoRepVentObj +
                ", repoRepVentNum=" + repoRepVentNum +
                ", repoRepVentCuo=" + repoRepVentCuo +
                ", repoRepVentEstReg=" + repoRepVentEstReg +
                '}';
    }
}