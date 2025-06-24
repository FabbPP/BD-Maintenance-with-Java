package modelo;

public class ModuloAuditoria {
    private int modAudiCod;
    private String modAudiDesc;
    private char modAudiEstReg;

    // Constructor vacío
    public ModuloAuditoria() {
    }

    // Constructor con parámetros
    public ModuloAuditoria(int modAudiCod, String modAudiDesc, char modAudiEstReg) {
        this.modAudiCod = modAudiCod;
        this.modAudiDesc = modAudiDesc;
        this.modAudiEstReg = modAudiEstReg;
    }

    // Getters y Setters
    public int getModAudiCod() {
        return modAudiCod;
    }

    public void setModAudiCod(int modAudiCod) {
        this.modAudiCod = modAudiCod;
    }

    public String getModAudiDesc() {
        return modAudiDesc;
    }

    public void setModAudiDesc(String modAudiDesc) {
        this.modAudiDesc = modAudiDesc;
    }

    public char getModAudiEstReg() {
        return modAudiEstReg;
    }

    public void setModAudiEstReg(char modAudiEstReg) {
        this.modAudiEstReg = modAudiEstReg;
    }

    @Override
    public String toString() {
        return "ModuloAuditoria{" +
                "modAudiCod=" + modAudiCod +
                ", modAudiDesc='" + modAudiDesc + '\'' +
                ", modAudiEstReg=" + modAudiEstReg +
                '}';
    }
}