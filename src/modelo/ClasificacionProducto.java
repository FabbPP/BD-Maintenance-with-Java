package modelo;

public class ClasificacionProducto {
    private int clasProCod; 
    private String clasProDesc;
    private char clasProEstReg;

    public ClasificacionProducto() {
    }

    public ClasificacionProducto(String clasProDesc, char clasProEstReg) {
        this.clasProDesc = clasProDesc;
        this.clasProEstReg = clasProEstReg;
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

    public char getClasProEstReg() {
        return clasProEstReg;
    }

    public void setClasProEstReg(char clasProEstReg) {
        this.clasProEstReg = clasProEstReg;
    }

    @Override
    public String toString() {
        return "ClasificacionProducto{" +
                "clasProCod=" + clasProCod +
                ", clasProDesc='" + clasProDesc + '\'' +
                ", clasProEstReg=" + clasProEstReg +
                '}';
    }
}
