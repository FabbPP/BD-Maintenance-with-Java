package modelo;

import java.time.LocalDate;

public class RepVenta {
    private int repCod;
    private String repNom;
    private int repEdad;
    private int ofiCod;
    private int carCod;
    private LocalDate repCon;
    private char repEstReg;
    
    private String oficinaDescripcion; // Para mostrar ciudad de la oficina
    private String cargoDescripcion;   // Para mostrar descripción del cargo

    public RepVenta() {}

    public RepVenta(int repCod, String repNom, int repEdad, int ofiCod, int carCod, 
                    LocalDate repCon, char repEstReg) {
        this.repCod = repCod;
        this.repNom = repNom;
        this.repEdad = repEdad;
        this.ofiCod = ofiCod;
        this.carCod = carCod;
        this.repCon = repCon;
        this.repEstReg = repEstReg;
    }

    // Constructor con descripciones
    public RepVenta(int repCod, String repNom, int repEdad, int ofiCod, int carCod, 
                    LocalDate repCon, char repEstReg, String oficinaDescripcion, 
                    String cargoDescripcion) {
        this(repCod, repNom, repEdad, ofiCod, carCod, repCon, repEstReg);
        this.oficinaDescripcion = oficinaDescripcion;
        this.cargoDescripcion = cargoDescripcion;
    }

    // Getters y Setters
    public int getRepCod() {
        return repCod;
    }

    public void setRepCod(int repCod) {
        this.repCod = repCod;
    }

    public String getRepNom() {
        return repNom;
    }

    public void setRepNom(String repNom) {
        this.repNom = repNom;
    }

    public int getRepEdad() {
        return repEdad;
    }

    public void setRepEdad(int repEdad) {
        this.repEdad = repEdad;
    }

    public int getOfiCod() {
        return ofiCod;
    }

    public void setOfiCod(int ofiCod) {
        this.ofiCod = ofiCod;
    }

    public int getCarCod() {
        return carCod;
    }

    public void setCarCod(int carCod) {
        this.carCod = carCod;
    }

    public LocalDate getRepCon() {
        return repCon;
    }

    public void setRepCon(LocalDate repCon) {
        this.repCon = repCon;
    }

    public char getRepEstReg() {
        return repEstReg;
    }

    public void setRepEstReg(char repEstReg) {
        this.repEstReg = repEstReg;
    }

    public String getOficinaDescripcion() {
        return oficinaDescripcion;
    }

    public void setOficinaDescripcion(String oficinaDescripcion) {
        this.oficinaDescripcion = oficinaDescripcion;
    }

    public String getCargoDescripcion() {
        return cargoDescripcion;
    }

    public void setCargoDescripcion(String cargoDescripcion) {
        this.cargoDescripcion = cargoDescripcion;
    }

    @Override
    public String toString() {
        return "RepVenta{" +
                "repCod=" + repCod +
                ", repNom='" + repNom + '\'' +
                ", repEdad=" + repEdad +
                ", ofiCod=" + ofiCod +
                ", carCod=" + carCod +
                ", repCon=" + repCon +
                ", repEstReg=" + repEstReg +
                '}';
    }
}