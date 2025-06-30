package dao;

import conexion.ConexionBD;
import modelo.DetalleFactura;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleFacturaDAO{

    public List<DetalleFactura> obtenerTodosDetalles() {
        List<DetalleFactura> detalles = new ArrayList<>();
        String sql = "SELECT FacCod, ProCod, DetCan, DetPre, DetSub, DetEstReg FROM DETALLES";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                DetalleFactura det = new DetalleFactura();
                det.setFacCod(rs.getInt("FacCod"));
                det.setProCod(rs.getInt("ProCod"));
                det.setDetCan(rs.getInt("DetCan"));
                det.setDetPre(rs.getBigDecimal("DetPre"));
                det.setDetSub(rs.getBigDecimal("DetSub"));
                det.setDetEstReg(rs.getString("DetEstReg").charAt(0));
                detalles.add(det);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los detalles de factura: " + e.getMessage());
        }
        return detalles;
    }

    public DetalleFactura obtenerDetallePorPK(int facCod, int proCod) {
        String sql = "SELECT FacCod, ProCod, DetCan, DetPre, DetSub, DetEstReg FROM DETALLES WHERE FacCod = ? AND ProCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, facCod);
            pstmt.setInt(2, proCod);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    DetalleFactura det = new DetalleFactura();
                    det.setFacCod(rs.getInt("FacCod"));
                    det.setProCod(rs.getInt("ProCod"));
                    det.setDetCan(rs.getInt("DetCan"));
                    det.setDetPre(rs.getBigDecimal("DetPre"));
                    det.setDetSub(rs.getBigDecimal("DetSub"));
                    det.setDetEstReg(rs.getString("DetEstReg").charAt(0));
                    return det;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener detalle de factura por PK: " + e.getMessage());
        }
        return null;
    }

    public boolean insertarDetalle(DetalleFactura detalle) {
        String sql = "INSERT INTO DETALLES (FacCod, ProCod, DetCan, DetPre, DetSub, DetEstReg) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, detalle.getFacCod());
            pstmt.setInt(2, detalle.getProCod());
            pstmt.setInt(3, detalle.getDetCan());
            pstmt.setBigDecimal(4, detalle.getDetPre());
            pstmt.setBigDecimal(5, detalle.getDetSub());
            pstmt.setString(6, String.valueOf(departamento.getDepEstReg()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar detalle de factura: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarDetalle(DetalleFactura detalle) {
        String sql = "UPDATE DETALLES SET DetCan = ?, DetPre = ?, DetSub = ?, DetEstReg = ? WHERE FacCod = ? AND ProCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, detalle.getDetCan());
            pstmt.setBigDecimal(2, detalle.getDetPre());
            pstmt.setBigDecimal(3, detalle.getDetSub());
            pstmt.setString(4, String.valueOf(departamento.getDepEstReg()));
            pstmt.setInt(5, detalle.getFacCod());
            pstmt.setInt(6, detalle.getProCod());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar detalle de factura: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogicamenteDetalle(int facCod, int proCod) {
        String sql = "UPDATE DEPARTAMENTO SET DetEstReg = '*' WHERE FacCod = ? AND ProCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, facCod);
            pstmt.setInt(2, proCod);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar detalle de factura: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarDetalle(int codigo) {
        String sql = "UPDATE DETALLES SET DetEstReg = 'I' WHERE FacCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al inactivar detalle de factura: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarDetalle(int codigo) {
        String sql = "UPDATE DETALLES SET DetEstReg = 'A' WHERE FacCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al reactivar detalle de factura: " + e.getMessage());
            return false;
        }
    }
}