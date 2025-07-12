package dao;

import conexion.ConexionBD;
import modelo.CategoriaCliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaClienteDAO {

    public List<CategoriaCliente> obtenerTodasCategorias() {
        List<CategoriaCliente> categorias = new ArrayList<>();
        String sql = "SELECT CatCliCod, CatCliDesc, CatCliLimCred, CatCliEstReg FROM clascliente";

        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CategoriaCliente categoria = new CategoriaCliente();
                categoria.setCatCliCod(rs.getInt("CatCliCod"));
                categoria.setCatCliDesc(rs.getString("CatCliDesc"));
                categoria.setCatCliLimCred(rs.getDouble("CatCliLimCred"));
                categoria.setCatCliEstReg(rs.getString("CatCliEstReg").charAt(0));
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las categorías de cliente: " + e.getMessage());
        }

        return categorias;
    }

    public CategoriaCliente obtenerCategoriaPorCodigo(int codigo) {
        String sql = "SELECT CatCliCod, CatCliDesc, CatCliLimCred, CatCliEstReg FROM clascliente WHERE CatCliCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    CategoriaCliente categoria = new CategoriaCliente();
                    categoria.setCatCliCod(rs.getInt("CatCliCod"));
                    categoria.setCatCliDesc(rs.getString("CatCliDesc"));
                    categoria.setCatCliLimCred(rs.getDouble("CatCliLimCred"));
                    categoria.setCatCliEstReg(rs.getString("CatCliEstReg").charAt(0));
                    return categoria;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener categoría por código: " + e.getMessage());
        }

        return null;
    }

    public boolean insertarCategoria(CategoriaCliente categoria) {
        String sql = "INSERT INTO clascliente (CatCliDesc, CatCliLimCred, CatCliEstReg) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, categoria.getCatCliDesc());
            pstmt.setDouble(2, categoria.getCatCliLimCred());
            pstmt.setString(3, String.valueOf(categoria.getCatCliEstReg()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        categoria.setCatCliCod(generatedKeys.getInt(1)); // Asignar la PK generada al objeto
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al insertar categoría de cliente: " + e.getMessage());
            e.printStackTrace(); // Para ver más detalles del error
            return false;
        }
    }

    public boolean actualizarCategoria(CategoriaCliente categoria) {
        String sql = "UPDATE clascliente SET CatCliDesc = ?, CatCliLimCred = ?, CatCliEstReg = ? WHERE CatCliCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, categoria.getCatCliDesc());
            pstmt.setDouble(2, categoria.getCatCliLimCred());
            pstmt.setString(3, String.valueOf(categoria.getCatCliEstReg()));
            pstmt.setInt(4, categoria.getCatCliCod());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar categoría de cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogicamenteCategoria(int codigo) {
        String sql = "UPDATE clascliente SET CatCliEstReg = '*' WHERE CatCliCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente categoría: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarCategoria(int codigo) {
        String sql = "UPDATE clascliente SET CatCliEstReg = 'I' WHERE CatCliCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al inactivar categoría: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarCategoria(int codigo) {
        String sql = "UPDATE clascliente SET CatCliEstReg = 'A' WHERE CatCliCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al reactivar categoría: " + e.getMessage());
            return false;
        }
    }
}
