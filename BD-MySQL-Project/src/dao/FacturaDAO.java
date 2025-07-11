package dao;

import conexion.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Factura;

public class FacturaDAO{

    public List<Factura> obtenerTodasFacturas() {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT FacCod, CliCod, RepCod, Faclmp, FacAño, FacMes, FacDia, FactPlazoPago, FactFechPago, FacEstReg, FacEstado FROM FACTURA";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Factura fac = new Factura(
                     rs.getInt("FacCod"),
                     rs.getInt("CliCod"),
                     rs.getInt("RepCod"),
                     rs.getBigDecimal("Faclmp"),
                     rs.getInt("FacAño"),
                     rs.getInt("FacMes"),
                     rs.getInt("FacDia"),
                     rs.getDate("FactPlazoPago"),
                     rs.getDate("FactFechPago"),
                     rs.getString("FacEstReg").charAt(0),
                     rs.getInt("FacEstado")
                );     
                facturas.add(fac);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las facturas: " + e.getMessage());
        }
        return facturas;
    }

    public Factura obtenerFacturaPorCodigo(int codigo) {
        String sql = "SELECT FacCod, CliCod, RepCod, Faclmp, FacAño, FacMes, FacDia, FactPlazoPago, FactFechPago, FacEstReg, FacEstado FROM FACTURA WHERE FacCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Factura fac = new Factura(
                        rs.getInt("FacCod"),
                        rs.getInt("CliCod"),
                        rs.getInt("RepCod"),
                        rs.getBigDecimal("Faclmp"),
                        rs.getInt("FacAño"),
                        rs.getInt("FacMes"),
                        rs.getInt("FacDia"),
                        rs.getDate("FactPlazoPago"),
                        rs.getDate("FactFechPago"),
                        rs.getString("FacEstReg").charAt(0),
                        rs.getInt("FacEstado")
                    );
                    return fac;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener factura por código: " + e.getMessage());
        }
        return null;
    }

    public boolean insertarFactura(Factura factura) {
        String sql = "INSERT INTO FACTURA (CliCod, RepCod, Faclmp, FacAño, FacMes, FacDia, FactPlazoPago, FactFechPago, FacEstReg, FacEstado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, factura.getCliCod());
            pstmt.setInt(2, factura.getRepCod());
            pstmt.setBigDecimal(3, factura.getFacImp());
            pstmt.setInt(4, factura.getFacAño());
            pstmt.setInt(5, factura.getFacMes());
            pstmt.setInt(6, factura.getFacDia());
            pstmt.setDate(7, factura.getFacPlazoPago());
            pstmt.setDate(8, factura.getFacFechPago());
            pstmt.setString(9, String.valueOf(factura.getFacEstReg()));
            pstmt.setInt(10, factura.getFacEstado());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        factura.setFacCod(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar factura: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarFactura(Factura factura) {
        String sql = "UPDATE FACTURA SET CliCod = ?, RepCod = ?, Faclmp = ?, FacAño = ?, FacMes = ?, FacDia = ?, FactPlazoPago = ?, FactFechPago = ?, FacEstReg = ?, FacEstado = ? WHERE FacCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, factura.getCliCod());
            pstmt.setInt(2, factura.getRepCod());
            pstmt.setBigDecimal(3, factura.getFacImp());
            pstmt.setInt(4, factura.getFacAño());
            pstmt.setInt(5, factura.getFacMes());
            pstmt.setInt(6, factura.getFacDia());
            pstmt.setDate(7, factura.getFacPlazoPago());
            pstmt.setDate(8, factura.getFacFechPago());
            pstmt.setString(9, String.valueOf(factura.getFacEstReg()));
            pstmt.setInt(10, factura.getFacEstado());
            pstmt.setInt(11, factura.getFacCod());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar factura: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogicamenteFactura(int facCod) {
        String sql = "UPDATE FACTURA SET FacEstReg = '*' WHERE FacCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, facCod);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente factura: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarFactura(int facCod) {
        String sql = "UPDATE FACTURA SET FacEstReg = 'I' WHERE FacCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, facCod);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al inactivar factura: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarFactura(int facCod) {
        String sql = "UPDATE FACTURA SET FacEstReg = 'A' WHERE FacCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, facCod);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al reactivar factura: " + e.getMessage());
            return false;
        }
    }

    public List<Factura> obtenerFacturasPorEstado(int estado) {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT FacCod, CliCod, RepCod, Faclmp, FacAño, FacMes, FacDia, FactPlazoPago, FactFechPago, FacEstReg, FacEstado FROM FACTURA WHERE FacEstado = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, estado);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Factura fac = new Factura(
                         rs.getInt("FacCod"),
                         rs.getInt("CliCod"),
                         rs.getInt("RepCod"),
                         rs.getBigDecimal("Faclmp"),
                         rs.getInt("FacAño"),
                         rs.getInt("FacMes"),
                         rs.getInt("FacDia"),
                         rs.getDate("FactPlazoPago"),
                         rs.getDate("FactFechPago"),
                         rs.getString("FacEstReg").charAt(0),
                         rs.getInt("FacEstado")
                    );     
                    facturas.add(fac);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener facturas por estado: " + e.getMessage());
        }
        return facturas;
    }
}