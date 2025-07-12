package modelo;

public class Region{
	private int regCod;
	private int depCod;
	private String regNom;
	private char regEstReg;

	public Region() {
	}

	public Region(int cod, int dCod, String nom, char estReg) {
		regCod = cod;
		depCod = dCod;
		regNom = nom;
		regEstReg = estReg;
	}

	public int getRegCod() {
		return regCod;
	}

	public void setRegCod(int cod) {
		regCod = cod;
	}

	public int getDepCod() {
		return depCod;
	}

	public void setDepCod(int dCod) {
		depCod = dCod;
	}

	public String getRegNom() {
		return regNom;
	}

	public void setRegNom(String nom) {
		regNom = nom;
	}

	public char getRegEstReg() {
		return regEstReg;
	}

	public void setRegEstReg(char estReg) {
		regEstReg = estReg;
	}

	public String toString() {
		return "Región { " +
			"regCod = " + regCod +
			", depCod = " + depCod +
			", regNom = '" + regNom + '\'' +
			", regEstReg = '" + regEstReg + '\'' +
			'}';
	}
}