package modelo;

public class Ciudad{
	private int ciuCod;
	private int regCod;
	private String ciuNom;
	private char ciuEstReg;

	public Ciudad() {
	}

	public Ciudad(int cod, int rCod, String nom, char estReg) {
		ciuCod = cod;
		regCod = rCod;
		ciuNom = nom;
		ciuEstReg = estReg;
	}

	public int getCiuCod() {
		return ciuCod;
	}

	public void setCiuCod(int cod) {
		ciuCod = cod;
	}

	public int getRegCod() {
		return regCod;
	}

	public void setRegCod(int rCod) {
		regCod = rCod;
	}

	public String getCiuNom() {
		return ciuNom;
	}

	public void setCiuNom(String ciuNom) {
		ciuNom = ciuNom;
	}

	public char getCiuEstReg() {
		return ciuEstReg;
	}

	public void setCiuEstReg(char estReg) {
		ciuEstReg = estReg;
	}

	public String toString() {
		return "Ciudad {" +
			"ciuCod = " + ciuCod +
			", regCod = " + regCod +
			", ciuNom = '" + ciuNom + '\'' +
			", ciuEstReg = '" + ciuEstReg + '\'' +
			'}';
	}
}