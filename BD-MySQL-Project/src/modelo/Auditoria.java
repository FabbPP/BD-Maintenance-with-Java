package modelo;

import java.sql.Date;
import java.sql.Time;

public class Auditoria {
    private int audiCod;
    private int usuCod;
    private Date audiFecha;
    private Time audiHora;
    private String audiDescri;
    private String audiDet;
    private int modAudiCod;
    private char audiEstReg;
    
    // Campos adicionales para mostrar descripciones
    private String usuarioNombre;       // Para mostrar nombre del usuario
    private String moduloDescripcion;   // Para mostrar descripción del módulo
    
    // Constructor vacío
    public Auditoria() {}
    
    // Constructor principal
    public Auditoria(int audiCod, int usuCod, Date audiFecha, Time audiHora,
                    String audiDescri, String audiDet, int modAudiCod, char audiEstReg) {
        this.audiCod = audiCod;
        this.usuCod = usuCod;
        this.audiFecha = audiFecha;
        this.audiHora = audiHora;
        this.audiDescri = audiDescri;
        this.audiDet = audiDet;
        this.modAudiCod = modAudiCod;
        this.audiEstReg = audiEstReg;
    }
    
    // Constructor con descripciones
    public Auditoria(int audiCod, int usuCod, Date audiFecha, Time audiHora,
                    String audiDescri, String audiDet, int modAudiCod, char audiEstReg,
                    String usuarioNombre, String moduloDescripcion) {
        this(audiCod, usuCod, audiFecha, audiHora, audiDescri, audiDet, modAudiCod, audiEstReg);
        this.usuarioNombre = usuarioNombre;
        this.moduloDescripcion = moduloDescripcion;
    }
    
    // Getters y Setters
    public int getAudiCod() {
        return audiCod;
    }
    
    public void setAudiCod(int audiCod) {
        this.audiCod = audiCod;
    }
    
    public int getUsuCod() {
        return usuCod;
    }
    
    public void setUsuCod(int usuCod) {
        this.usuCod = usuCod;
    }
    
    public Date getAudiFecha() {
        return audiFecha;
    }
    
    public void setAudiFecha(Date audiFecha) {
        this.audiFecha = audiFecha;
    }
    
    public Time getAudiHora() {
        return audiHora;
    }
    
    public void setAudiHora(Time audiHora) {
        this.audiHora = audiHora;
    }
    
    public String getAudiDescri() {
        return audiDescri;
    }
    
    public void setAudiDescri(String audiDescri) {
        this.audiDescri = audiDescri;
    }
    
    public String getAudiDet() {
        return audiDet;
    }
    
    public void setAudiDet(String audiDet) {
        this.audiDet = audiDet;
    }
    
    public int getModAudiCod() {
        return modAudiCod;
    }
    
    public void setModAudiCod(int modAudiCod) {
        this.modAudiCod = modAudiCod;
    }
    
    public char getAudiEstReg() {
        return audiEstReg;
    }
    
    public void setAudiEstReg(char audiEstReg) {
        this.audiEstReg = audiEstReg;
    }
    
    public String getUsuarioNombre() {
        return usuarioNombre;
    }
    
    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }
    
    public String getModuloDescripcion() {
        return moduloDescripcion;
    }
    
    public void setModuloDescripcion(String moduloDescripcion) {
        this.moduloDescripcion = moduloDescripcion;
    }
    
    // Método para obtener fecha y hora completa como string
    public String getFechaHoraCompleta() {
        return audiFecha.toString() + " " + audiHora.toString();
    }
    
    // Método para verificar si el registro está activo
    public boolean estaActivo() {
        return audiEstReg == 'A';
    }
    
    @Override
    public String toString() {
        return "Auditoria{" +
                "audiCod=" + audiCod +
                ", usuCod=" + usuCod +
                ", audiFecha=" + audiFecha +
                ", audiHora=" + audiHora +
                ", audiDescri='" + audiDescri + '\'' +
                ", audiDet='" + audiDet + '\'' +
                ", modAudiCod=" + modAudiCod +
                ", audiEstReg=" + audiEstReg +
                '}';
    }
}