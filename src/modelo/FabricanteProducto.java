package modelo;

public class FabricanteProducto {
    private int fabCod;
    private String fabNom;
    private char fabEstReg;
    
    // Constructor vacío
    public FabricanteProducto() {}
    
    // Constructor principal
    public FabricanteProducto(int fabCod, String fabNom, char fabEstReg) {
        this.fabCod = fabCod;
        this.fabNom = fabNom;
        this.fabEstReg = fabEstReg;
    }
    
    // Getters y Setters
    public int getFabCod() {
        return fabCod;
    }
    
    public void setFabCod(int fabCod) {
        this.fabCod = fabCod;
    }
    
    public String getFabNom() {
        return fabNom;
    }
    
    public void setFabNom(String fabNom) {
        this.fabNom = fabNom;
    }
    
    public char getFabEstReg() {
        return fabEstReg;
    }
    
    public void setFabEstReg(char fabEstReg) {
        this.fabEstReg = fabEstReg;
    }
    
    // Método para verificar si el fabricante está activo
    public boolean estaActivo() {
        return fabEstReg == 'A';
    }
    
    @Override
    public String toString() {
        return "FabricanteProducto{" +
                "fabCod=" + fabCod +
                ", fabNom='" + fabNom + '\'' +
                ", fabEstReg=" + fabEstReg +
                '}';
    }
}