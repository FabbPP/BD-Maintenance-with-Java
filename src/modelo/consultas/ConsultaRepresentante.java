package modelo.consultas;

import java.math.BigDecimal;

public class ConsultaRepresentante {
    private int repCod;
    private String repNom;
    private int totalFacturas;
    private BigDecimal montoTotalVentas;
    
    public ConsultaRepresentante() {
    }
    
    public ConsultaRepresentante(int repCod, String repNom, int totalFacturas, BigDecimal montoTotalVentas) {
        this.repCod = repCod;
        this.repNom = repNom;
        this.totalFacturas = totalFacturas;
        this.montoTotalVentas = montoTotalVentas;
    }
    
    public int getRepCod() {
        return repCod;
    }
    
    public void setRepCod(int repCod) {
        this.repCod = repCod;
    }
    
    public String getRepNom() {
        return repNom;
    }
    
    public void setRepNom(String repNom) {
        this.repNom = repNom;
    }
    
    public int getTotalFacturas() {
        return totalFacturas;
    }
    
    public void setTotalFacturas(int totalFacturas) {
        this.totalFacturas = totalFacturas;
    }
    
    public BigDecimal getMontoTotalVentas() {
        return montoTotalVentas;
    }
    
    public void setMontoTotalVentas(BigDecimal montoTotalVentas) {
        this.montoTotalVentas = montoTotalVentas;
    }
    
    @Override
    public String toString() {
        return "Consulta Representante{" +
                "repCod=" + repCod +
                ", repNom='" + repNom + '\'' +
                ", totalFacturas=" + totalFacturas +
                ", montoTotalVentas=" + montoTotalVentas +
                '}';
    }
}