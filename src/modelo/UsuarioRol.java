package modelo;

public class UsuarioRol {
    private int rolUsuCod;
    private String rolUsuDesc;
    private char rolUsuProEstReg;

    public UsuarioRol() {}

    public UsuarioRol(String rolUsuDesc, char rolUsuProEstReg) {
        this.rolUsuDesc = rolUsuDesc;
        this.rolUsuProEstReg = rolUsuProEstReg;
    }

    public int getRolUsuCod() {
        return rolUsuCod;
    }

    public void setRolUsuCod(int rolUsuCod) {
        this.rolUsuCod = rolUsuCod;
    }

    public String getRolUsuDesc() {
        return rolUsuDesc;
    }

    public void setRolUsuDesc(String rolUsuDesc) {
        this.rolUsuDesc = rolUsuDesc;
    }

    public char getRolUsuProEstReg() {
        return rolUsuProEstReg;
    }

    public void setRolUsuProEstReg(char rolUsuProEstReg) {
        this.rolUsuProEstReg = rolUsuProEstReg;
    }

    @Override
    public String toString() {
        return "UsuarioRol{" +
                "rolUsuCod=" + rolUsuCod +
                ", rolUsuDesc='" + rolUsuDesc + '\'' +
                ", rolUsuProEstReg=" + rolUsuProEstReg +
                '}';
    }
}
