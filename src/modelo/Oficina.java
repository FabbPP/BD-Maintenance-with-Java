package modelo;

public class Oficina {
    private int ofiCod;
    private String ofiCiu;
    private String ofiReg;
    private String ofiDir;
    private String ofiEmp;
    private double ofiObj;
    private char ofiEstReg;

    // Constructor vacío
    public Oficina() {
    }

    // Constructor con parámetros
    public Oficina(int ofiCod, String ofiCiu, String ofiReg, String ofiDir, String ofiEmp, double ofiObj, char ofiEstReg) {
        this.ofiCod = ofiCod;
        this.ofiCiu = ofiCiu;
        this.ofiReg = ofiReg;
        this.ofiDir = ofiDir;
        this.ofiEmp = ofiEmp;
        this.ofiObj = ofiObj;
        this.ofiEstReg = ofiEstReg;
    }

    // Getters y Setters
    public int getOfiCod() {
        return ofiCod;
    }

    public void setOfiCod(int ofiCod) {
        this.ofiCod = ofiCod;
    }

    public String getOfiCiu() {
        return ofiCiu;
    }

    public void setOfiCiu(String ofiCiu) {
        this.ofiCiu = ofiCiu;
    }

    public String getOfiReg() {
        return ofiReg;
    }

    public void setOfiReg(String ofiReg) {
        this.ofiReg = ofiReg;
    }

    public String getOfiDir() {
        return ofiDir;
    }

    public void setOfiDir(String ofiDir) {
        this.ofiDir = ofiDir;
    }

    public String getOfiEmp() {
        return ofiEmp;
    }

    public void setOfiEmp(String ofiEmp) {
        this.ofiEmp = ofiEmp;
    }

    public double getOfiObj() {
        return ofiObj;
    }

    public void setOfiObj(double ofiObj) {
        this.ofiObj = ofiObj;
    }

    public char getOfiEstReg() {
        return ofiEstReg;
    }

    public void setOfiEstReg(char ofiEstReg) {
        this.ofiEstReg = ofiEstReg;
    }

    @Override
    public String toString() {
        return "Oficina{" +
                "ofiCod=" + ofiCod +
                ", ofiCiu='" + ofiCiu + '\'' +
                ", ofiReg='" + ofiReg + '\'' +
                ", ofiDir='" + ofiDir + '\'' +
                ", ofiEmp='" + ofiEmp + '\'' +
                ", ofiObj=" + ofiObj +
                ", ofiEstReg=" + ofiEstReg +
                '}';
    }
}