package modelo;

public class ReporteProducto {
    private int reporProdCod;
    private int reporProdMin;
    private int reporProdMax;
    private char reporProdEstReg;
    private int prodCod;
    
    // Constructor vacío
    public ReporteProducto() {}
    
    // Constructor principal
    public ReporteProducto(int reporProdCod, int prodCod, int reporProdMin, int reporProdMax, char reporProdEstReg) {
        this.reporProdCod = reporProdCod;
        this.prodCod = prodCod;
        this.reporProdMin = reporProdMin;
        this.reporProdMax = reporProdMax;
        this.reporProdEstReg = reporProdEstReg;
    }
    
    // Getters y Setters
    public int getReporProdCod() {
        return reporProdCod;
    }
    
    public void setReporProdCod(int reporProdCod) {
        this.reporProdCod = reporProdCod;
    }
    public int getProdCod() {
        return prodCod;
    }
    
    public void setProdCod(int prodCod) {
        this.prodCod = prodCod;
    }
    
    public int getReporProdMin() {
        return reporProdMin;
    }
    
    public void setReporProdMin(int reporProdMin) {
        this.reporProdMin = reporProdMin;
    }
    
    public int getReporProdMax() {
        return reporProdMax;
    }
    
    public void setReporProdMax(int reporProdMax) {
        this.reporProdMax = reporProdMax;
    }
    
    public char getReporProdEstReg() {
        return reporProdEstReg;
    }
    
    public void setReporProdEstReg(char reporProdEstReg) {
        this.reporProdEstReg = reporProdEstReg;
    }
    
    // Método para verificar si el reporte está activo
    public boolean estaActivo() {
        return reporProdEstReg == 'A';
    }
    
    // Método para verificar si un stock está dentro del rango permitido
    public boolean stockEnRango(int stock) {
        return stock >= reporProdMin && stock <= reporProdMax;
    }
    
    // Método para verificar si el stock está por debajo del mínimo
    public boolean stockBajoMinimo(int stock) {
        return stock < reporProdMin;
    }
    
    // Método para verificar si el stock está por encima del máximo
    public boolean stockSobreMaximo(int stock) {
        return stock > reporProdMax;
    }
    
    // Método para obtener el rango de stock como string
    public String getRangoStock() {
        return "Min: " + reporProdMin + " - Max: " + reporProdMax;
    }
    
    // Método para calcular la diferencia entre max y min
    public int getRangoCapacidad() {
        return reporProdMax - reporProdMin;
    }
    
    // Método para obtener el punto medio del rango
    public int getPuntoMedio() {
        return (reporProdMax + reporProdMin) / 2;
    }
    
    @Override
    public String toString() {
        return "ReporteProducto{" +
                "prodCod=" + prodCod +
                "reporProdCod=" + reporProdCod +
                ", reporProdMin=" + reporProdMin +
                ", reporProdMax=" + reporProdMax +
                ", reporProdEstReg=" + reporProdEstReg +
                '}';
    }
}