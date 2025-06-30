package modelo;

import java.math.BigDecimal;

public class Producto {
    private int prodCod;
    private int fabCod;
    private String prodDes;
    private BigDecimal prodPre;
    private int prodStock;
    private int clasProCod;
    private int uniMedProCod;
    private int dispoProdCod;
    private char prodEstReg;
    
    // Campos adicionales para mostrar descripciones
    private String fabricanteNombre;        // Para mostrar nombre del fabricante
    private String clasificacionDescripcion; // Para mostrar descripción de la clasificación
    private String unidadMedidaDescripcion;  // Para mostrar descripción de la unidad de medida
    private String disponibilidadDescripcion; // Para mostrar descripción de disponibilidad
    
    // Constructor vacío
    public Producto() {}
    
    // Constructor principal
    public Producto(int prodCod, int fabCod, String prodDes, BigDecimal prodPre,
                   int prodStock, int clasProCod, int uniMedProCod, 
                   int dispoProdCod, char prodEstReg) {
        this.prodCod = prodCod;
        this.fabCod = fabCod;
        this.prodDes = prodDes;
        this.prodPre = prodPre;
        this.prodStock = prodStock;
        this.clasProCod = clasProCod;
        this.uniMedProCod = uniMedProCod;
        this.dispoProdCod = dispoProdCod;
        this.prodEstReg = prodEstReg;
    }
    
    // Constructor con descripciones
    public Producto(int prodCod, int fabCod, String prodDes, BigDecimal prodPre,
                   int prodStock, int clasProCod, int uniMedProCod, 
                   int dispoProdCod, char prodEstReg,
                   String fabricanteNombre, String clasificacionDescripcion,
                   String unidadMedidaDescripcion, String disponibilidadDescripcion) {
        this(prodCod, fabCod, prodDes, prodPre, prodStock, clasProCod, 
             uniMedProCod, dispoProdCod, prodEstReg);
        this.fabricanteNombre = fabricanteNombre;
        this.clasificacionDescripcion = clasificacionDescripcion;
        this.unidadMedidaDescripcion = unidadMedidaDescripcion;
        this.disponibilidadDescripcion = disponibilidadDescripcion;
    }
    
    // Getters y Setters
    public int getProdCod() {
        return prodCod;
    }
    
    public void setProdCod(int prodCod) {
        this.prodCod = prodCod;
    }
    
    public int getFabCod() {
        return fabCod;
    }
    
    public void setFabCod(int fabCod) {
        this.fabCod = fabCod;
    }
    
    public String getProdDes() {
        return prodDes;
    }
    
    public void setProdDes(String prodDes) {
        this.prodDes = prodDes;
    }
    
    public BigDecimal getProdPre() {
        return prodPre;
    }
    
    public void setProdPre(BigDecimal prodPre) {
        this.prodPre = prodPre;
    }
    
    public int getProdStock() {
        return prodStock;
    }
    
    public void setProdStock(int prodStock) {
        this.prodStock = prodStock;
    }
    
    public int getClasProCod() {
        return clasProCod;
    }
    
    public void setClasProCod(int clasProCod) {
        this.clasProCod = clasProCod;
    }
    
    public int getUniMedProCod() {
        return uniMedProCod;
    }
    
    public void setUniMedProCod(int uniMedProCod) {
        this.uniMedProCod = uniMedProCod;
    }
    

    public int getDispoProdCod() {
        return dispoProdCod;
    }
    
    public void setDispoProdCod(int dispoProdCod) {
        this.dispoProdCod = dispoProdCod;
    }
    
    public char getProdEstReg() {
        return prodEstReg;
    }
    
    public void setProdEstReg(char prodEstReg) {
        this.prodEstReg = prodEstReg;
    }
    
    public String getFabricanteNombre() {
        return fabricanteNombre;
    }
    
    public void setFabricanteNombre(String fabricanteNombre) {
        this.fabricanteNombre = fabricanteNombre;
    }
    
    public String getClasificacionDescripcion() {
        return clasificacionDescripcion;
    }
    
    public void setClasificacionDescripcion(String clasificacionDescripcion) {
        this.clasificacionDescripcion = clasificacionDescripcion;
    }
    
    public String getUnidadMedidaDescripcion() {
        return unidadMedidaDescripcion;
    }
    
    public void setUnidadMedidaDescripcion(String unidadMedidaDescripcion) {
        this.unidadMedidaDescripcion = unidadMedidaDescripcion;
    }
    
    public String getDisponibilidadDescripcion() {
        return disponibilidadDescripcion;
    }
    
    public void setDisponibilidadDescripcion(String disponibilidadDescripcion) {
        this.disponibilidadDescripcion = disponibilidadDescripcion;
    }
    
    // Método para verificar si el producto está disponible
    public boolean estaDisponible() {
        return dispoProdCod == 1;
    }
    
    // Método para verificar si hay stock suficiente
    public boolean hayStockSuficiente(int cantidad) {
        return prodStock >= cantidad;
    }
    
    // Método para calcular el valor total del stock
    public BigDecimal getValorTotalStock() {
        return prodPre.multiply(BigDecimal.valueOf(prodStock));
    }
    
    @Override
    public String toString() {
        return prodCod + " - " + prodDes;
    }
}