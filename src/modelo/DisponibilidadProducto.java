package modelo;

public class DisponibilidadProducto {
    private int dispoProdCod; 
    private String dispoProdDesc;
    private char dispoProdEstReg;

    // Constructor vacío
    public DisponibilidadProducto() {
    }

    // Constructor con parámetros
    public DisponibilidadProducto(int dispoProdCod, String dispoProdDesc, char dispoProdEstReg) {
        this.dispoProdCod = dispoProdCod;
        this.dispoProdDesc = dispoProdDesc;
        this.dispoProdEstReg = dispoProdEstReg;
    }

    // Getters y Setters
    public int getDispoProdCod() {
        return dispoProdCod;
    }

    public void setDispoProdCod(int dispoProdCod) {
        this.dispoProdCod = dispoProdCod;
    }

    public String getDispoProdDesc() {
        return dispoProdDesc;
    }

    public void setDispoProdDesc(String dispoProdDesc) {
        this.dispoProdDesc = dispoProdDesc;
    }

    public char getDispoProdEstReg() {
        return dispoProdEstReg;
    }

    public void setDispoProdEstReg(char dispoProdEstReg) {
        this.dispoProdEstReg = dispoProdEstReg;
    }

    @Override
    public String toString() {
        return "DisponibilidadProducto{" +
                "dispoProdCod=" + dispoProdCod +
                ", dispoProdDesc='" + dispoProdDesc + '\'' +
                ", dispoProdEstReg=" + dispoProdEstReg +
                '}';
    }
}
