package modelo;

public class Cliente {
    private int cliCod;
    private String cliEmp;
    private Integer repCod;
    private String cliLim;
    private String cliNom;
    private String cliApePat;
    private String cliApeMat;
    private Integer ciuCod;
    private String cliDirDetalle;
    private Long cliTel;
    private String cliCor;
    private int catCliCod;
    private char cliEstReg;
    
    // Campos adicionales para mostrar descripciones
    private String representanteNombre;     // Para mostrar nombre del representante
    private String categoriaDescripcion;    // Para mostrar descripción de la categoría
    private String ciudadNombre;            // Para mostrar nombre de la ciudad
    
    // Constructor vacío
    public Cliente() {}
    
    // Constructor principal
    public Cliente(int cliCod, String cliEmp, Integer repCod, String cliLim,
                   String cliNom, String cliApePat, String cliApeMat,
                   Integer ciuCod, String cliDirDetalle, Long cliTel,
                   String cliCor, int catCliCod, char cliEstReg) {
        this.cliCod = cliCod;
        this.cliEmp = cliEmp;
        this.repCod = repCod;
        this.cliLim = cliLim;
        this.cliNom = cliNom;
        this.cliApePat = cliApePat;
        this.cliApeMat = cliApeMat;
        this.ciuCod = ciuCod;
        this.cliDirDetalle = cliDirDetalle;
        this.cliTel = cliTel;
        this.cliCor = cliCor;
        this.catCliCod = catCliCod;
        this.cliEstReg = cliEstReg;
    }
    
    // Constructor con descripciones
    public Cliente(int cliCod, String cliEmp, Integer repCod, String cliLim,
                   String cliNom, String cliApePat, String cliApeMat,
                   Integer ciuCod, String cliDirDetalle, Long cliTel,
                   String cliCor, int catCliCod, char cliEstReg,
                   String representanteNombre, String categoriaDescripcion,
                   String ciudadNombre) {
        this(cliCod, cliEmp, repCod, cliLim, cliNom, cliApePat, cliApeMat,
             ciuCod, cliDirDetalle, cliTel, cliCor, catCliCod, cliEstReg);
        this.representanteNombre = representanteNombre;
        this.categoriaDescripcion = categoriaDescripcion;
        this.ciudadNombre = ciudadNombre;
    }
    
    // Getters y Setters
    public int getCliCod() {
        return cliCod;
    }
    
    public void setCliCod(int cliCod) {
        this.cliCod = cliCod;
    }
    
    public String getCliEmp() {
        return cliEmp;
    }
    
    public void setCliEmp(String cliEmp) {
        this.cliEmp = cliEmp;
    }
    
    public Integer getRepCod() {
        return repCod;
    }
    
    public void setRepCod(Integer repCod) {
        this.repCod = repCod;
    }
    
    public String getCliLim() {
        return cliLim;
    }
    
    public void setCliLim(String cliLim) {
        this.cliLim = cliLim;
    }
    
    public String getCliNom() {
        return cliNom;
    }
    
    public void setCliNom(String cliNom) {
        this.cliNom = cliNom;
    }
    
    public String getCliApePat() {
        return cliApePat;
    }
    
    public void setCliApePat(String cliApePat) {
        this.cliApePat = cliApePat;
    }
    
    public String getCliApeMat() {
        return cliApeMat;
    }
    
    public void setCliApeMat(String cliApeMat) {
        this.cliApeMat = cliApeMat;
    }
    
    public Integer getCiuCod() {
        return ciuCod;
    }
    
    public void setCiuCod(Integer ciuCod) {
        this.ciuCod = ciuCod;
    }
    
    public String getCliDirDetalle() {
        return cliDirDetalle;
    }
    
    public void setCliDirDetalle(String cliDirDetalle) {
        this.cliDirDetalle = cliDirDetalle;
    }
    
    public Long getCliTel() {
        return cliTel;
    }
    
    public void setCliTel(Long cliTel) {
        this.cliTel = cliTel;
    }
    
    public String getCliCor() {
        return cliCor;
    }
    
    public void setCliCor(String cliCor) {
        this.cliCor = cliCor;
    }
    
    public int getCatCliCod() {
        return catCliCod;
    }
    
    public void setCatCliCod(int catCliCod) {
        this.catCliCod = catCliCod;
    }
    
    public char getCliEstReg() {
        return cliEstReg;
    }
    
    public void setCliEstReg(char cliEstReg) {
        this.cliEstReg = cliEstReg;
    }
    
    public String getRepresentanteNombre() {
        return representanteNombre;
    }
    
    public void setRepresentanteNombre(String representanteNombre) {
        this.representanteNombre = representanteNombre;
    }
    
    public String getCategoriaDescripcion() {
        return categoriaDescripcion;
    }
    
    public void setCategoriaDescripcion(String categoriaDescripcion) {
        this.categoriaDescripcion = categoriaDescripcion;
    }
    
    public String getCiudadNombre() {
        return ciudadNombre;
    }
    
    public void setCiudadNombre(String ciudadNombre) {
        this.ciudadNombre = ciudadNombre;
    }
    
    // Método para obtener el nombre completo del cliente
    public String getNombreCompleto() {
        return cliNom + " " + cliApePat + " " + cliApeMat;
    }
    
    @Override
    public String toString() {
        return "Cliente{" +
                "cliCod=" + cliCod +
                ", cliEmp='" + cliEmp + '\'' +
                ", repCod=" + repCod +
                ", cliLim='" + cliLim + '\'' +
                ", cliNom='" + cliNom + '\'' +
                ", cliApePat='" + cliApePat + '\'' +
                ", cliApeMat='" + cliApeMat + '\'' +
                ", ciuCod=" + ciuCod +
                ", cliDirDetalle='" + cliDirDetalle + '\'' +
                ", cliTel=" + cliTel +
                ", cliCor='" + cliCor + '\'' +
                ", catCliCod=" + catCliCod +
                ", cliEstReg=" + cliEstReg +
                '}';
    }
}