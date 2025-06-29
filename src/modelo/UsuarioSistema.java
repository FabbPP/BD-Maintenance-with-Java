package modelo;

public class UsuarioSistema {
    private int usuCod;
    private int repCod;
    private String usuNom;
    private String usuContr;
    private int rolUsuCod;
    private char usuEstReg;

    public UsuarioSistema() {
    }

    public UsuarioSistema(int usuCod, int repCod, String usuNom, String usuContr, int rolUsuCod, char usuEstReg) {
        this.usuCod = usuCod;
        this.repCod = repCod;
        this.usuNom = usuNom;
        this.usuContr = usuContr;
        this.rolUsuCod = rolUsuCod;
        this.usuEstReg = usuEstReg;
    }

    public int getUsuCod() {
        return usuCod;
    }

    public void setUsuCod(int usuCod) {
        this.usuCod = usuCod;
    }

    public int getRepCod() {
        return repCod;
    }

    public void setRepCod(int repCod) {
        this.repCod = repCod;
    }

    public String getUsuNom() {
        return usuNom;
    }

    public void setUsuNom(String usuNom) {
        this.usuNom = usuNom;
    }

    public String getUsuContr() {
        return usuContr;
    }

    public void setUsuContr(String usuContr) {
        this.usuContr = usuContr;
    }

    public int getRolUsuCod() {
        return rolUsuCod;
    }

    public void setRolUsuCod(int rolUsuCod) {
        this.rolUsuCod = rolUsuCod;
    }

    public char getUsuEstReg() {
        return usuEstReg;
    }

    public void setUsuEstReg(char usuEstReg) {
        this.usuEstReg = usuEstReg;
    }

    @Override
    public String toString() {
        return "UsuarioSistema{" +
                "usuCod=" + usuCod +
                ", repCod=" + repCod +
                ", usuNom='" + usuNom + '\'' +
                ", usuContr='" + usuContr + '\'' +
                ", rolUsuCod=" + rolUsuCod +
                ", usuEstReg=" + usuEstReg +
                '}';
    }
}