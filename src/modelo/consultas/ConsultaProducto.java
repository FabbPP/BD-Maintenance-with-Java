package modelo.consultas;

import java.math.BigDecimal;

public class  ConsultaProducto {
    private int clasProCod;
    private String clasProDesc;
    private int totalProductos;
    private BigDecimal precioPromedio;
    private BigDecimal precioMaximo;
    private int stockMinimo;
    
    public  ConsultaProducto() {
    }
    
    public  ConsultaProducto(int clasProCod, String clasProDesc, int totalProductos, 
                               BigDecimal precioPromedio, BigDecimal precioMaximo, int stockMinimo) {
        this.clasProCod = clasProCod;
        this.clasProDesc = clasProDesc;
        this.totalProductos = totalProductos;
        this.precioPromedio = precioPromedio;
        this.precioMaximo = precioMaximo;
        this.stockMinimo = stockMinimo;
    }
    
    public int getClasProCod() {
        return clasProCod;
    }
    
    public void setClasProCod(int clasProCod) {
        this.clasProCod = clasProCod;
    }
    
    public String getClasProDesc() {
        return clasProDesc;
    }
    
    public void setClasProDesc(String clasProDesc) {
        this.clasProDesc = clasProDesc;
    }
    
    public int getTotalProductos() {
        return totalProductos;
    }
    
    public void setTotalProductos(int totalProductos) {
        this.totalProductos = totalProductos;
    }
    
    public BigDecimal getPrecioPromedio() {
        return precioPromedio;
    }
    
    public void setPrecioPromedio(BigDecimal precioPromedio) {
        this.precioPromedio = precioPromedio;
    }
    
    public BigDecimal getPrecioMaximo() {
        return precioMaximo;
    }
    
    public void setPrecioMaximo(BigDecimal precioMaximo) {
        this.precioMaximo = precioMaximo;
    }
    
    public int getStockMinimo() {
        return stockMinimo;
    }
    
    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
    
    @Override
    public String toString() {
        return "ConsultasProducto{" +
                "clasProCod=" + clasProCod +
                ", clasProDesc='" + clasProDesc + '\'' +
                ", totalProductos=" + totalProductos +
                ", precioPromedio=" + precioPromedio +
                ", precioMaximo=" + precioMaximo +
                ", stockMinimo=" + stockMinimo +
                '}';
    }
}