package modelo;

public class CategoriaCliente {
	private int catCliCod;
	private String catCliDesc;
	private double catCliLimCred;
	private char catCliEstReg;

	public CategoriaCliente() {
	}

	public CategoriaCliente(int cod, String desc, double limCred, char estReg){
		catCliCod = cod;
		catCliDesc = desc;
		catCliLimCred = limCred;
		catCliEstReg = estReg;
	}

	public int getCatCliCod(){
		return catCliCod;
	}

	public void setCatCliCod(int catCliCod){
		this.catCliCod = catCliCod;
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

	@Override
	public String toString() {
		return "CategoriaCliente{" +
				"catCliCod=" + catCliCod +
				", catCliDesc='" + catCliDesc + '\'' +
				", catCliLimCred=" + catCliLimCred +
				", catCliEstReg=" + catCliEstReg +
				'}';
	}
}
