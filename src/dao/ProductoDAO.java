package dao;
//corregido - sin ReporProdCod
import conexion.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Producto;

public class ProductoDAO {

    public List<Producto> obtenerTodosProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.ProdCod, p.FabCod, p.ProdDes, p.ProdPre, p.ProdStock, " +
                     "p.ClasProCod, p.UniMedProCod, p.DispoProdCod, p.ProdEstReg, " +
                     "f.FabNom as FabricanteNombre, cp.ClasProDesc as ClasificacionDescripcion, " +
                     "um.UniMedProDesc as UnidadMedidaDescripcion, pd.DispoProdDesc as DisponibilidadDescripcion " +
                     "FROM PRODUCTO p " +
                     "LEFT JOIN FABRICANTE_PRODUCTO f ON p.FabCod = f.FabCod " +
                     "LEFT JOIN CLASIFICACION_PRODUCTO cp ON p.ClasProCod = cp.ClasProCod " +
                     "LEFT JOIN PROD_UNIDAD_MEDIDA um ON p.UniMedProCod = um.UniMedProCod " +
                     "LEFT JOIN PROD_DISPO pd ON p.DispoProdCod = pd.DispoProdCod " +
                     "ORDER BY p.ProdCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("ProdCod"),
                    rs.getInt("FabCod"),
                    rs.getString("ProdDes"),
                    rs.getBigDecimal("ProdPre"),
                    rs.getInt("ProdStock"),
                    rs.getInt("ClasProCod"),
                    rs.getInt("UniMedProCod"),
                    rs.getInt("DispoProdCod"),
                    rs.getString("ProdEstReg").charAt(0),
                    rs.getString("FabricanteNombre"),
                    rs.getString("ClasificacionDescripcion"),
                    rs.getString("UnidadMedidaDescripcion"),
                    rs.getString("DisponibilidadDescripcion")
                );
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los productos: " + e.getMessage());
        }
        return productos;
    }

    public Producto obtenerProductoPorCodigo(int prodCod) {
        String sql = "SELECT p.ProdCod, p.FabCod, p.ProdDes, p.ProdPre, p.ProdStock, " +
                     "p.ClasProCod, p.UniMedProCod, p.DispoProdCod, p.ProdEstReg, " +
                     "f.FabNom as FabricanteNombre, cp.ClasProDesc as ClasificacionDescripcion, " +
                     "um.UniMedProDesc as UnidadMedidaDescripcion, pd.DispoProdDesc as DisponibilidadDescripcion " +
                     "FROM PRODUCTO p " +
                     "LEFT JOIN FABRICANTE_PRODUCTO f ON p.FabCod = f.FabCod " +
                     "LEFT JOIN CLASIFICACION_PRODUCTO cp ON p.ClasProCod = cp.ClasProCod " +
                     "LEFT JOIN PROD_UNIDAD_MEDIDA um ON p.UniMedProCod = um.UniMedProCod " +
                     "LEFT JOIN PROD_DISPO pd ON p.DispoProdCod = pd.DispoProdCod " +
                     "WHERE p.ProdCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, prodCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Producto(
                    rs.getInt("ProdCod"),
                    rs.getInt("FabCod"),
                    rs.getString("ProdDes"),
                    rs.getBigDecimal("ProdPre"),
                    rs.getInt("ProdStock"),
                    rs.getInt("ClasProCod"),
                    rs.getInt("UniMedProCod"),
                    rs.getInt("DispoProdCod"),
                    rs.getString("ProdEstReg").charAt(0),
                    rs.getString("FabricanteNombre"),
                    rs.getString("ClasificacionDescripcion"),
                    rs.getString("UnidadMedidaDescripcion"),
                    rs.getString("DisponibilidadDescripcion")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener producto: " + e.getMessage());
        }
        return null;
    }

    public String insertarProducto(Producto producto) {
        String sql = "INSERT INTO PRODUCTO (FabCod, ProdDes, ProdPre, ProdStock, ClasProCod, " +
                     "UniMedProCod, DispoProdCod, ProdEstReg) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, producto.getFabCod());
            stmt.setString(2, producto.getProdDes());
            stmt.setBigDecimal(3, producto.getProdPre());
            stmt.setInt(4, producto.getProdStock());
            stmt.setInt(5, producto.getClasProCod());
            stmt.setInt(6, producto.getUniMedProCod());
            stmt.setInt(7, producto.getDispoProdCod());
            stmt.setString(8, String.valueOf(producto.getProdEstReg()));
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return null; // No error, return null to indicate success
            } else {
                return "Error desconocido al insertar el producto.";
            }
        } catch (SQLException e) {
            String errorMsg = e.getMessage().toLowerCase();
            
            if (errorMsg.contains("chk_prod_precio")) {
                return "El precio del producto debe ser mayor o igual a 0.00.";
            } else if (errorMsg.contains("chk_prod_stock")) {
                return "El stock del producto debe ser mayor o igual a 0.";
            } else if (errorMsg.contains("fk_prod_fabricante")) {
                return "El código de fabricante no existe o está inactivo.";
            } else if (errorMsg.contains("fk_prod_clasificacion")) {
                return "El código de clasificación no existe o está inactivo.";
            } else if (errorMsg.contains("fk_prod_unidad")) {
                return "El código de unidad de medida no existe o está inactivo.";
            } else if (errorMsg.contains("fk_prod_disponibilidad")) {
                return "El código de disponibilidad no existe o está inactivo.";
            } else if (errorMsg.contains("chk_prod_est")) {
                return "El estado del registro debe ser A, I o *.";
            } else {
                return "Error al insertar producto: " + e.getMessage();
            }
        }
    }

    public String actualizarProducto(Producto producto) {
        String sql = "UPDATE PRODUCTO SET FabCod = ?, ProdDes = ?, ProdPre = ?, ProdStock = ?, " +
                     "ClasProCod = ?, UniMedProCod = ?, DispoProdCod = ?, " +
                     "ProdEstReg = ? WHERE ProdCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, producto.getFabCod());
            stmt.setString(2, producto.getProdDes());
            stmt.setBigDecimal(3, producto.getProdPre());
            stmt.setInt(4, producto.getProdStock());
            stmt.setInt(5, producto.getClasProCod());
            stmt.setInt(6, producto.getUniMedProCod());
            stmt.setInt(7, producto.getDispoProdCod());
            stmt.setString(8, String.valueOf(producto.getProdEstReg()));
            stmt.setInt(9, producto.getProdCod());
            
            return (stmt.executeUpdate() > 0) ? null : "Error al actualizar producto.";
        } catch (SQLException e) {
            String errorMsg = e.getMessage().toLowerCase();
            
            if (errorMsg.contains("chk_prod_precio")) {
                return "El precio del producto debe ser mayor or igual a 0.00.";
            } else if (errorMsg.contains("chk_prod_stock")) {
                return "El stock del producto debe ser mayor o igual a 0.";
            } else if (errorMsg.contains("fk_prod_fabricante")) {
                return "El código de fabricante no existe o está inactivo.";
            } else if (errorMsg.contains("fk_prod_clasificacion")) {
                return "El código de clasificación no existe o está inactivo.";
            } else if (errorMsg.contains("fk_prod_unidad")) {
                return "El código de unidad de medida no existe o está inactivo.";
            } else if (errorMsg.contains("fk_prod_disponibilidad")) {
                return "El código de disponibilidad no existe o está inactivo.";
            } else if (errorMsg.contains("chk_prod_est")) {
                return "El estado del registro debe ser A, I o *.";
            } else {
                return "Error al actualizar producto: " + e.getMessage();
            }
        }
    }

    public boolean eliminarLogicamenteProducto(int prodCod) {
        String sql = "UPDATE PRODUCTO SET ProdEstReg = '*' WHERE ProdCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, prodCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente producto: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarProducto(int prodCod) {
        String sql = "UPDATE PRODUCTO SET ProdEstReg = 'I' WHERE ProdCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, prodCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al inactivar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarProducto(int prodCod) {
        String sql = "UPDATE PRODUCTO SET ProdEstReg = 'A' WHERE ProdCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, prodCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al reactivar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarProducto(int prodCod) {
        String sql = "DELETE FROM PRODUCTO WHERE ProdCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, prodCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    public List<Producto> obtenerProductosActivos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.ProdCod, p.FabCod, p.ProdDes, p.ProdPre, p.ProdStock, " +
                     "p.ClasProCod, p.UniMedProCod, p.DispoProdCod, p.ProdEstReg, " +
                     "f.FabNom as FabricanteNombre, cp.ClasProDesc as ClasificacionDescripcion, " +
                     "um.UniMedProDesc as UnidadMedidaDescripcion, pd.DispoProdDesc as DisponibilidadDescripcion " +
                     "FROM PRODUCTO p " +
                     "LEFT JOIN FABRICANTE_PRODUCTO f ON p.FabCod = f.FabCod " +
                     "LEFT JOIN CLASIFICACION_PRODUCTO cp ON p.ClasProCod = cp.ClasProCod " +
                     "LEFT JOIN PROD_UNIDAD_MEDIDA um ON p.UniMedProCod = um.UniMedProCod " +
                     "LEFT JOIN PROD_DISPO pd ON p.DispoProdCod = pd.DispoProdCod " +
                     "WHERE p.ProdEstReg = 'A' ORDER BY p.ProdCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("ProdCod"),
                    rs.getInt("FabCod"),
                    rs.getString("ProdDes"),
                    rs.getBigDecimal("ProdPre"),
                    rs.getInt("ProdStock"),
                    rs.getInt("ClasProCod"),
                    rs.getInt("UniMedProCod"),
                    rs.getInt("DispoProdCod"),
                    rs.getString("ProdEstReg").charAt(0),
                    rs.getString("FabricanteNombre"),
                    rs.getString("ClasificacionDescripcion"),
                    rs.getString("UnidadMedidaDescripcion"),
                    rs.getString("DisponibilidadDescripcion")
                );
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos activos: " + e.getMessage());
        }
        return productos;
    }

    public List<Producto> buscarProductosPorDescripcion(String descripcion) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.ProdCod, p.FabCod, p.ProdDes, p.ProdPre, p.ProdStock, " +
                     "p.ClasProCod, p.UniMedProCod, p.DispoProdCod, p.ProdEstReg, " +
                     "f.FabNom as FabricanteNombre, cp.ClasProDesc as ClasificacionDescripcion, " +
                     "um.UniMedProDesc as UnidadMedidaDescripcion, pd.DispoProdDesc as DisponibilidadDescripcion " +
                     "FROM PRODUCTO p " +
                     "LEFT JOIN FABRICANTE_PRODUCTO f ON p.FabCod = f.FabCod " +
                     "LEFT JOIN CLASIFICACION_PRODUCTO cp ON p.ClasProCod = cp.ClasProCod " +
                     "LEFT JOIN PROD_UNIDAD_MEDIDA um ON p.UniMedProCod = um.UniMedProCod " +
                     "LEFT JOIN PROD_DISPO pd ON p.DispoProdCod = pd.DispoProdCod " +
                     "WHERE p.ProdDes LIKE ? ORDER BY p.ProdCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + descripcion + "%";
            stmt.setString(1, searchPattern);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("ProdCod"),
                    rs.getInt("FabCod"),
                    rs.getString("ProdDes"),
                    rs.getBigDecimal("ProdPre"),
                    rs.getInt("ProdStock"),
                    rs.getInt("ClasProCod"),
                    rs.getInt("UniMedProCod"),
                    rs.getInt("DispoProdCod"),
                    rs.getString("ProdEstReg").charAt(0),
                    rs.getString("FabricanteNombre"),
                    rs.getString("ClasificacionDescripcion"),
                    rs.getString("UnidadMedidaDescripcion"),
                    rs.getString("DisponibilidadDescripcion")
                );
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos por descripción: " + e.getMessage());
        }
        return productos;
    }

    public List<Producto> buscarProductosPorFabricante(int fabCod) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.ProdCod, p.FabCod, p.ProdDes, p.ProdPre, p.ProdStock, " +
                     "p.ClasProCod, p.UniMedProCod, p.DispoProdCod, p.ProdEstReg, " +
                     "f.FabNom as FabricanteNombre, cp.ClasProDesc as ClasificacionDescripcion, " +
                     "um.UniMedProDesc as UnidadMedidaDescripcion, pd.DispoProdDesc as DisponibilidadDescripcion " +
                     "FROM PRODUCTO p " +
                     "LEFT JOIN FABRICANTE_PRODUCTO f ON p.FabCod = f.FabCod " +
                     "LEFT JOIN CLASIFICACION_PRODUCTO cp ON p.ClasProCod = cp.ClasProCod " +
                     "LEFT JOIN PROD_UNIDAD_MEDIDA um ON p.UniMedProCod = um.UniMedProCod " +
                     "LEFT JOIN PROD_DISPO pd ON p.DispoProdCod = pd.DispoProdCod " +
                     "WHERE p.FabCod = ? ORDER BY p.ProdCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, fabCod);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("ProdCod"),
                    rs.getInt("FabCod"),
                    rs.getString("ProdDes"),
                    rs.getBigDecimal("ProdPre"),
                    rs.getInt("ProdStock"),
                    rs.getInt("ClasProCod"),
                    rs.getInt("UniMedProCod"),
                    rs.getInt("DispoProdCod"),
                    rs.getString("ProdEstReg").charAt(0),
                    rs.getString("FabricanteNombre"),
                    rs.getString("ClasificacionDescripcion"),
                    rs.getString("UnidadMedidaDescripcion"),
                    rs.getString("DisponibilidadDescripcion")
                );
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos por fabricante: " + e.getMessage());
        }
        return productos;
    }

    public List<Producto> obtenerProductosDisponibles() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.ProdCod, p.FabCod, p.ProdDes, p.ProdPre, p.ProdStock, " +
                     "p.ClasProCod, p.UniMedProCod, p.DispoProdCod, p.ProdEstReg, " +
                     "f.FabNom as FabricanteNombre, cp.ClasProDesc as ClasificacionDescripcion, " +
                     "um.UniMedProDesc as UnidadMedidaDescripcion, pd.DispoProdDesc as DisponibilidadDescripcion " +
                     "FROM PRODUCTO p " +
                     "LEFT JOIN FABRICANTE_PRODUCTO f ON p.FabCod = f.FabCod " +
                     "LEFT JOIN CLASIFICACION_PRODUCTO cp ON p.ClasProCod = cp.ClasProCod " +
                     "LEFT JOIN PROD_UNIDAD_MEDIDA um ON p.UniMedProCod = um.UniMedProCod " +
                     "LEFT JOIN PROD_DISPO pd ON p.DispoProdCod = pd.DispoProdCod " +
                     "WHERE p.DispoProdCod = 1 AND p.ProdEstReg = 'A' ORDER BY p.ProdCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("ProdCod"),
                    rs.getInt("FabCod"),
                    rs.getString("ProdDes"),
                    rs.getBigDecimal("ProdPre"),
                    rs.getInt("ProdStock"),
                    rs.getInt("ClasProCod"),
                    rs.getInt("UniMedProCod"),
                    rs.getInt("DispoProdCod"),
                    rs.getString("ProdEstReg").charAt(0),
                    rs.getString("FabricanteNombre"),
                    rs.getString("ClasificacionDescripcion"),
                    rs.getString("UnidadMedidaDescripcion"),
                    rs.getString("DisponibilidadDescripcion")
                );
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos disponibles: " + e.getMessage());
        }
        return productos;
    }

    public boolean actualizarStock(int prodCod, int nuevoStock) {
        String sql = "UPDATE PRODUCTO SET ProdStock = ? WHERE ProdCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nuevoStock);
            stmt.setInt(2, prodCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }

    public boolean existeProducto(int prodCod) {
        String sql = "SELECT COUNT(*) FROM PRODUCTO WHERE ProdCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, prodCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de producto: " + e.getMessage());
        }
        return false;
    }

    public boolean validarReferenciaFabricante(int fabCod) {
        String sql = "SELECT COUNT(*) FROM FABRICANTE_PRODUCTO WHERE FabCod = ? AND FabEstReg = 'A'";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, fabCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar referencia de fabricante: " + e.getMessage());
        }
        return false;
    }

    public boolean validarReferenciaClasificacion(int clasCod) {
        String sql = "SELECT COUNT(*) FROM CLASIFICACION_PRODUCTO WHERE ClasProCod = ? AND ClasProEstReg = 'A'";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clasCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar referencia de clasificación: " + e.getMessage());
        }
        return false;
    }

    public boolean validarReferenciaUnidadMedida(int uniCod) {
        String sql = "SELECT COUNT(*) FROM PROD_UNIDAD_MEDIDA WHERE UniMedProCod = ? AND UniMedEstReg = 'A'";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, uniCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar referencia de unidad de medida: " + e.getMessage());
        }
        return false;
    }

    public boolean validarReferenciaDisponibilidad(int dispoCod) {
        String sql = "SELECT COUNT(*) FROM PROD_DISPO WHERE DispoProdCod = ? AND DispoProdEstReg = 'A'";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, dispoCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar referencia de disponibilidad: " + e.getMessage());
        }
        return false;
    }
}