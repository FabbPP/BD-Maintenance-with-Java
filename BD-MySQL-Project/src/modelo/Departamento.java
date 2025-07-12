package modelo;

public class Departamento{
	private int depCod;
	private String depNom;
	private char depEstReg;

	public Departamento() {
	}

	public Departamento(int cod, String nom, char estReg) {
		depCod = cod;
		depNom = nom;
		depEstReg = estReg;
	}

	public int getDepCod() {
		return depCod;
	}

	public void setDepCod(int cod) {
		depCod = cod;
	}

	public String getDepNom() {
		return depNom;
	}

	public void setDepNom(String nom) {
		depNom = nom;
	}

	public char getDepEstReg() {
		return depEstReg;
	}

	public void setDepEstReg(char estReg) {
		depEstReg = estReg;
	}

	public String toString() {
		return "Departamento {" +
                "DepCod = " + depCod +
                ", DepNom = '" + depNom + '\'' +
		", DepEstReg =" + depEstReg +
                '}';
	}
}