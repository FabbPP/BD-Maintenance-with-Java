package dao;

import conexion.ConexionBD;
import modelo.Departamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoDAO {

    public List<Departamento> obtenerTodosDepartamentos() {
        List<Departamento> departamentos = new ArrayList<>();
        String sql = "SELECT DepCod, DepNom, DepEstReg FROM DEPARTAMENTO";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Departamento dep = new Departamento(rs.getInt("DepCod"), rs.getString("DepNom"), rs.getString("DepEstReg").charAt(0));
                departamentos.add(dep);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los departamentos: " + e.getMessage());
        }
        return departamentos;
    }

    public Departamento obtenerDepartamentoPorCodigo(int depCod) {
        String sql = "SELECT DepCod, DepNom, DepEstReg FROM DEPARTAMENTO WHERE DepCod = ?";
        try (Connection conn = ConexionBD.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, depCod);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Departamento dep = new Departamento();
                    dep.setDepCod(rs.getInt("DepCod"));
                    dep.setDepNom(rs.getString("DepNom"));
                    dep.setDepEstReg(rs.getString("DepEstReg").charAt(0));
                    return dep;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener departamento por código: " + e.getMessage());
        }
        return null;
    }

    public boolean insertarDepartamento(Departamento departamento) {
        String sql = "INSERT INTO DEPARTAMENTO (DepNom, DepEstReg) VALUES (?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, departamento.getDepNom());
            pstmt.setString(2, String.valueOf(departamento.getDepEstReg()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        departamento.setDepCod(generatedKeys.getInt(1)); // Asigna el ID generado
                    }
                }
                return true;
            }
	    return false;

        } catch (SQLException e) {
            System.err.println("Error al insertar departamento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarDepartamento(Departamento departamento) {
        String sql = "UPDATE DEPARTAMENTO SET DepNom = ?, DepEstReg = ? WHERE DepCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, departamento.getDepNom());
            pstmt.setString(2, String.valueOf(departamento.getDepEstReg()));
            pstmt.setInt(3, departamento.getDepCod());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar departamento: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogicamenteDepartamento(int codigo) {
        String sql = "UPDATE DEPARTAMENTO SET DepEstReg = '*' WHERE DepCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente departamento: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarDepartamento(int codigo) {
        String sql = "UPDATE DEPARTAMENTO SET DepEstReg = 'I' WHERE DepCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al inactivar departamento: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarDepartamento(int codigo) {
        String sql = "UPDATE DEPARTAMENTO SET DepEstReg = 'A' WHERE DepCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al reactivar departamento: " + e.getMessage());
            return false;
        }
    }
}