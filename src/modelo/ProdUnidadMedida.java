package modelo;

public class ProdUnidadMedida {
    private int uniMedProCod; 
    private String uniMedProDesc;
    private char uniMedProEstReg;

    // Constructor sin parámetros
    public ProdUnidadMedida() {
    }

    // Constructor con parámetros
    public ProdUnidadMedida(int cod, String desc, char estReg) {
        this.uniMedProCod = cod;
        this.uniMedProDesc = desc;
        this.uniMedProEstReg = estReg;
    }

    // Getter y Setter para el código de la unidad de medida
    public int getUniMedProCod() {
        return uniMedProCod;
    }

    public void setUniMedProCod(int uniMedProCod) {
        this.uniMedProCod = uniMedProCod;
    }

    // Getter y Setter para la descripción de la unidad de medida
    public String getUniMedProDesc() {
        return uniMedProDesc;
    }

    public void setUniMedProDesc(String uniMedProDesc) {
        this.uniMedProDesc = uniMedProDesc;
    }

    // Getter y Setter para el estado del registro
    public char getUniMedProEstReg() {
        return uniMedProEstReg;
    }

    public void setUniMedProEstReg(char uniMedProEstReg) {
        this.uniMedProEstReg = uniMedProEstReg;
    }

    // Método toString para representar la clase en formato de cadena
    @Override
    public String toString() {
        return "ProdUnidadMedida{" +
                "uniMedProCod=" + uniMedProCod +
                ", uniMedProDesc='" + uniMedProDesc + '\'' +
                ", uniMedProEstReg=" + uniMedProEstReg +
                '}';
    }
}
