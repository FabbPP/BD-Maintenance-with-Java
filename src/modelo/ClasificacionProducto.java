package modelo;

public class ClasificacionProducto {
    private char clasProCod;
    private String clasProDesc;
    private char clasProEstReg;

    public ClasificacionProducto() {
    }

    public ClasificacionProducto(char clasProCod, String clasProDesc, char clasProEstReg) {
        this.clasProCod = clasProCod;
        this.clasProDesc = clasProDesc;
        this.clasProEstReg = clasProEstReg;
    }

    public char getClasProCod() {
        return clasProCod;
    }

    public void setClasProCod(char clasProCod) {
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
