package modelo;

public class ProdUnidadMedida {
	private String uniMedProCod;
	private String uniMedProDesc;
	private char uniMedProEstReg;

	public ProdUnidadMedida() {
	}

	public ProdUnidadMedida(String cod, String desc, char estReg) {
		uniMedProCod = cod;
		uniMedProDesc = desc;
		uniMedProEstReg = estReg;
		}

	public String getUniMedProCod() {
		return uniMedProCod;
	}

	public void setUniMedProCod(String cod) {
		uniMedProCod = cod;
	}

	public String getUniMedProDesc() {
		return uniMedProDesc;
	}

	public void setUniMedProDesc(String desc) {
		uniMedProDesc = desc;
	}

	public char getUniMedProEstReg() {
		return uniMedProEstReg;
	}

	public void setUniMedProEstReg(char estReg) {
		uniMedProEstReg = estReg;
	}

	public String toString() {
		return "ProdUnidadMedida {" + "uniMedProCod = " + uniMedProCod + ", uniMedProDesc = " + uniMedProDesc + ", uniMedProEstReg = " + uniMedProEstReg + "}";
	}
}