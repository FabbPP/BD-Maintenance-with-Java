package modelo;

public class Cargo {
    private int carCod;
    private String carDesc;
    private double carSue;
    private char carEstReg;

    // Constructor por defecto
    public Cargo() {}

    // Constructor con parámetros
    public Cargo(int carCod, String carDesc, double carSue, char carEstReg) {
        this.carCod = carCod;
        this.carDesc = carDesc;
        this.carSue = carSue;
        this.carEstReg = carEstReg;
    }

    // Getters y Setters
    public int getCarCod() {
        return carCod;
    }

    public void setCarCod(int carCod) {
        this.carCod = carCod;
    }

    public String getCarDesc() {
        return carDesc;
    }

    public void setCarDesc(String carDesc) {
        this.carDesc = carDesc;
    }

    public double getCarSue() {
        return carSue;
    }

    public void setCarSue(double carSue) {
        this.carSue = carSue;
    }

    public char getCarEstReg() {
        return carEstReg;
    }

    public void setCarEstReg(char carEstReg) {
        this.carEstReg = carEstReg;
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "carCod=" + carCod +
                ", carDesc='" + carDesc + '\'' +
                ", carSue=" + carSue +
                ", carEstReg=" + carEstReg +
                '}';
    }
}