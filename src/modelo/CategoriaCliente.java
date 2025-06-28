package modelo;

public class CategoriaCliente {
	private char catCliCod;
	private String catCliNom;
	private String catCliDesc;
	private double catCliLimCred;
	private char catCliEstReg;

	public CategoriaCliente() {
	}

	public CategoriaCliente(char cod, String nom, String desc, double limCred, char estReg){
		catCliCod = cod;
		catCliNom = nom;
		catCliDesc = desc;
		catCliLimCred = limCred;
		catCliEstReg = estReg;
	}

	public char getCatCliCod(){
		return catCliCod;
	}

	public void setCatCliCod(char catCliCod){
		this.catCliCod = catCliCod;
	}

	public String getCatCliNom(){
		return catCliNom;
	}

	public void setCatCliNom(String catCliNom){
		this.catCliNom = catCliNom;
	}

	public String getCatCliDesc(){
		return catCliDesc;
	}

	public void setCatCliDesc(String catCliDesc){
		this.catCliDesc = catCliDesc;
	}

	public double getCatCliLimCred() {
		return catCliLimCred;
	}

	public void setCatCliLimCred(double catCliLimCred) {
		this.catCliLimCred = catCliLimCred;
	}

	public char getCatCliEstReg() {
		return catCliEstReg;
	}

	public void setCatCliEstReg(char catCliEstReg) {
		this.catCliEstReg = catCliEstReg;
	}

	public String toString() {
		return "CategoriaCliente{catCliCod = " + catCliCod + ", catCliDesc = '" + catCliDesc + '\'' + ", catCliLimCred = " + catCliLimCred + ", catCliEstReg = '" + catCliEstReg + '\'' + '}';
	}
}
