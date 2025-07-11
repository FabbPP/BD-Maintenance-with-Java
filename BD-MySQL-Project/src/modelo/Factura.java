package modelo;

import java.math.BigDecimal;
import java.sql.Date;

public class Factura{
	private int facCod;
	private int cliCod;
	private int repCod;
	private BigDecimal facImp;
	private int facAño;
	private int facMes;
	private int facDia;
	private Date facPlazoPago;
	private Date facFechPago;
	private char facEstReg;
	private int facEstado; // Nuevo campo: 0=Generada, 1=Parcial, 2=Completa, 9=Cancelada

	public Factura() {
	}

	public Factura(int cod, int cCod, int rCod, BigDecimal imp, int año, int mes, int dia, Date plazoPago, Date fechPago, char estReg) {
		facCod = cod;
		cliCod = cCod;
		repCod = rCod;
		facImp = imp;
		facAño = año;
		facMes = mes;
		facDia = dia;
		facPlazoPago = plazoPago;
		facFechPago = fechPago;
		facEstReg = estReg;
		facEstado = 0; // Por defecto: Generada
	}

	// Constructor completo con el nuevo campo
	public Factura(int cod, int cCod, int rCod, BigDecimal imp, int año, int mes, int dia, Date plazoPago, Date fechPago, char estReg, int estado) {
		facCod = cod;
		cliCod = cCod;
		repCod = rCod;
		facImp = imp;
		facAño = año;
		facMes = mes;
		facDia = dia;
		facPlazoPago = plazoPago;
		facFechPago = fechPago;
		facEstReg = estReg;
		facEstado = estado;
	}

	public int getFacCod() {
		return facCod;
	}

	public void setFacCod(int cod) {
		this.facCod = cod;
	}

	public int getCliCod() {
		return cliCod;
	}

	public void setCliCod(int cCod) {
		cliCod = cCod;
	}

	public int getRepCod() {
		return repCod;
	}

	public void setRepCod(int rCod) {
		repCod = rCod;
	}

	public BigDecimal getFacImp() {
		return facImp;
	}

	public void setFacImp(BigDecimal imp) {
		facImp = imp;
	}

	public int getFacAño() {
		return facAño;
	}

	public void setFacAño(int año) {
		facAño = año;
	}

	public int getFacMes() {
		return facMes;
	}

	public void setFacMes(int mes) {
		facMes = mes;
	}

	public int getFacDia() {
		return facDia;
	}

	public void setFacDia(int dia) {
		facDia = dia;
	}

	public Date getFacPlazoPago() {
		return facPlazoPago;
	}

	public void setFacPlazoPago(Date plazoPago) {
		facPlazoPago = plazoPago;
	}

	public Date getFacFechPago() {
		return facFechPago;
	}

	public void setFacFechPago(Date fechPago) {
		facFechPago = fechPago;
	}

	public char getFacEstReg() {
		return facEstReg;
	}

	public void setFacEstReg(char estReg) {
		facEstReg = estReg;
	}

	public int getFacEstado() {
		return facEstado;
	}

	public void setFacEstado(int estado) {
		facEstado = estado;
	}

	// Método helper para obtener la descripción del estado
	public String getFacEstadoDescripcion() {
		switch (facEstado) {
			case 0: return "Generada";
			case 1: return "Parcial";
			case 2: return "Completa";
			case 9: return "Cancelada";
			default: return "Desconocido";
		}
	}

	public String toString() {
		return "Factura{" +
			"facCod=" + facCod +
			", cliCod=" + cliCod +
			", repCod=" + repCod +
			", facImp=" + facImp +
			", facAño=" + facAño +
			", facMes=" + facMes +
			", facDia=" + facDia +
			", facPlazoPago=" + facPlazoPago +
			", facFechPago=" + facFechPago +
			", facEstReg = '" + facEstReg + '\'' +
			", facEstado=" + facEstado +
			'}';
	}
}