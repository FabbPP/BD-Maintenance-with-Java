package gui;

import dao.ProductoDAO;
import dao.FabricanteProductoDAO;
import dao.ClasificacionProductoDAO;
import dao.ProdUnidadMedidaDAO;
import dao.DisponibilidadProductoDAO;
import modelo.Producto;
import modelo.FabricanteProducto;
import modelo.ClasificacionProducto;
import modelo.ProdUnidadMedida;
import modelo.DisponibilidadProducto;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProductoFrame extends JFrame {

    private JTextField txtCodigo;
    private JTextField txtDescripcion;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JTextField txtEstadoRegistro;
    private JComboBox<FabricanteProducto> cmbFabricante;
    private JComboBox<ClasificacionProducto> cmbClasificacion;
    private JComboBox<ProdUnidadMedida> cmbUnidadMedida;
    private JComboBox<DisponibilidadProducto> cmbDisponibilidad;
    private JTable tablaProductos;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, 
                   btnActualizar, btnCancelar, btnSalir, btnBuscar;
    private JTextField txtBuscar;

    private ProductoDAO productoDAO;
    private FabricanteProductoDAO fabricanteDAO;

    
    private int flagCarFlaAct = 0;
    private String operacionActual = "";
    private int codigoSeleccionado = 0;

    public ProductoFrame() {
        setTitle("Mantenimiento de Productos");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        productoDAO = new ProductoDAO();
        fabricanteDAO = new FabricanteProductoDAO();
        
        initComponents();
        cargarCombos();
        cargarTablaProductos();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductoFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel de registro
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Producto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Primera fila
        gbc.gridx = 0; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        txtCodigo = new JTextField(10);
        txtCodigo.setEditable(false);
        panelRegistro.add(txtCodigo, gbc);

        gbc.gridx = 2;
        panelRegistro.add(new JLabel("Estado Registro:"), gbc);
        gbc.gridx = 3;
        txtEstadoRegistro = new JTextField(5);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        // Segunda fila
        gbc.gridx = 0; gbc.gridy = 1;
        panelRegistro.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtDescripcion = new JTextField(40);
        panelRegistro.add(txtDescripcion, gbc);

        // Tercera fila
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        txtPrecio = new JTextField(10);
        panelRegistro.add(txtPrecio, gbc);

        gbc.gridx = 2;
        panelRegistro.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 3;
        txtStock = new JTextField(10);
        panelRegistro.add(txtStock, gbc);

        // Cuarta fila
        gbc.gridx = 0; gbc.gridy = 3;
        panelRegistro.add(new JLabel("Fabricante:"), gbc);
        gbc.gridx = 1;
        cmbFabricante = new JComboBox<>();
        panelRegistro.add(cmbFabricante, gbc);

        gbc.gridx = 2;
        panelRegistro.add(new JLabel("Clasificación:"), gbc);
        gbc.gridx = 3;
        cmbClasificacion = new JComboBox<>();
        panelRegistro.add(cmbClasificacion, gbc);

        // Quinta fila
        gbc.gridx = 0; gbc.gridy = 4;
        panelRegistro.add(new JLabel("Unidad Medida:"), gbc);
        gbc.gridx = 1;
        cmbUnidadMedida = new JComboBox<>();
        panelRegistro.add(cmbUnidadMedida, gbc);

        gbc.gridx = 2;
        panelRegistro.add(new JLabel("Disponibilidad:"), gbc);
        gbc.gridx = 3;
        cmbDisponibilidad = new JComboBox<>();
        panelRegistro.add(cmbDisponibilidad, gbc);

        // Sexta fila - Nota sobre estados
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 4;
        JLabel lblEstados = new JLabel("* Estados: A = Activo, I = Inactivo, * = Eliminado");
        lblEstados.setFont(lblEstados.getFont().deriveFont(Font.ITALIC, 10f));
        lblEstados.setForeground(Color.GRAY);
        panelRegistro.add(lblEstados, gbc);

        add(panelRegistro, BorderLayout.NORTH);

        // Panel de búsqueda y tabla
        JPanel panelCentral = new JPanel(new BorderLayout());
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Buscar por descripción:"));
        txtBuscar = new JTextField(20);
        panelBusqueda.add(txtBuscar);
        btnBuscar = new JButton("Buscar");
        panelBusqueda.add(btnBuscar);
        panelCentral.add(panelBusqueda, BorderLayout.NORTH);

        // Panel de tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Productos"));
        tableModel = new DefaultTableModel(new Object[]{
            "Código", "Descripción", "Precio", "Stock", "Fabricante", 
            "Clasificación", "Unidad Medida", "Disponibilidad", "Estado"
        }, 0) {
            public boolean isCellEditable(int row, int col) { 
                return false; 
            }
        };
        tablaProductos = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        panelCentral.add(panelTabla, BorderLayout.CENTER);

        // Listener para selección en tabla
        tablaProductos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int i = tablaProductos.getSelectedRow();
                    if (i != -1) {
                        cargarDatosSeleccionados(i);
                        habilitarBotonesParaSeleccion();
                    }
                }
            }
        });

        add(panelCentral, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(2, 5, 10, 10));
        btnAdicionar = new JButton("Adicionar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnInactivar = new JButton("Inactivar");
        btnReactivar = new JButton("Reactivar");
        btnActualizar = new JButton("Actualizar");
        btnCancelar = new JButton("Cancelar");
        btnSalir = new JButton("Salir");

        for (JButton b : new JButton[]{btnAdicionar, btnModificar, btnEliminar, 
                                     btnInactivar, btnReactivar, btnActualizar, 
                                     btnCancelar, btnSalir}) {
            panelBotones.add(b);
        }
        add(panelBotones, BorderLayout.SOUTH);

        // Listeners de botones
        btnAdicionar.addActionListener(e -> comandoAdicionar());
        btnActualizar.addActionListener(e -> comandoActualizar());
        btnCancelar.addActionListener(e -> comandoCancelar());
        btnSalir.addActionListener(e -> comandoSalir());
        btnModificar.addActionListener(e -> comandoModificar());
        btnEliminar.addActionListener(e -> comandoEliminar());
        btnInactivar.addActionListener(e -> comandoInactivar());
        btnReactivar.addActionListener(e -> comandoReactivar());
        btnBuscar.addActionListener(e -> comandoBuscar());
    }

    private void cargarCombos() {
        // Cargar fabricantes activos
        cmbFabricante.removeAllItems();
        List<FabricanteProducto> fabricantes = fabricanteDAO.obtenerFabricantesActivos();
        for (FabricanteProducto f : fabricantes) {
            cmbFabricante.addItem(f);
        }
    }

    private void cargarDatosSeleccionados(int fila) {
        txtCodigo.setText(tableModel.getValueAt(fila, 0).toString());
        txtDescripcion.setText(tableModel.getValueAt(fila, 1).toString());
        txtPrecio.setText(tableModel.getValueAt(fila, 2).toString());
        txtStock.setText(tableModel.getValueAt(fila, 3).toString());
        txtEstadoRegistro.setText(tableModel.getValueAt(fila, 8).toString());
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(fila, 0).toString());

        // Seleccionar en combos
        String fabricante = tableModel.getValueAt(fila, 4).toString();
        String clasificacion = tableModel.getValueAt(fila, 5).toString();
        String unidadMedida = tableModel.getValueAt(fila, 6).toString();
        String disponibilidad = tableModel.getValueAt(fila, 7).toString();

        seleccionarEnCombo(cmbFabricante, fabricante);
        seleccionarEnCombo(cmbClasificacion, clasificacion);
        seleccionarEnCombo(cmbUnidadMedida, unidadMedida);
        seleccionarEnCombo(cmbDisponibilidad, disponibilidad);
    }

    private void seleccionarEnCombo(JComboBox combo, String texto) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).toString().equals(texto)) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void comandoAdicionar() {
        limpiarCampos();
        habilitarControles(true);
        txtEstadoRegistro.setText("A");
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        txtDescripcion.requestFocus();
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarCampos()) {
            return;
        }

        Producto producto = crearProductoDesdeFormulario();
        boolean exito = false;
        String mensaje = "";

        switch (operacionActual) {
            case "ADICIONAR":
                String mensajeErrorAdi = productoDAO.insertarProducto(producto);
                if (mensajeErrorAdi == null) {
                    exito = true;
                    mensaje = "Producto registrado con éxito.";
                } else {
                    exito = false;
                    mensaje = mensajeErrorAdi;
                }
                break;
                
            case "MODIFICAR":
                producto.setProdCod(codigoSeleccionado);
                String mensajeErrorMod = productoDAO.actualizarProducto(producto);
                if (mensajeErrorMod == null) {
                    exito = true;
                    mensaje = "Producto modificado con éxito.";
                } else {
                    exito = false;
                    mensaje = mensajeErrorMod;
                }
                break;
                
            case "ELIMINAR":
                exito = productoDAO.eliminarLogicamenteProducto(codigoSeleccionado);
                mensaje = exito ? "Producto eliminado con éxito." : "Error al eliminar producto.";
                break;
                
            case "INACTIVAR":
                exito = productoDAO.inactivarProducto(codigoSeleccionado);
                mensaje = exito ? "Producto inactivado con éxito." : "Error al inactivar producto.";
                break;
                
            case "REACTIVAR":
                exito = productoDAO.reactivarProducto(codigoSeleccionado);
                mensaje = exito ? "Producto reactivado con éxito." : "Error al reactivar producto.";
                break;
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTablaProductos();
            comandoCancelar();
        } else {
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        String descripcion = txtDescripcion.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String stockStr = txtStock.getText().trim();

        if (descripcion.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            BigDecimal precio = new BigDecimal(precioStr);
            if (precio.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "El precio debe ser mayor o igual a 0.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int stock = Integer.parseInt(stockStr);
            if (stock < 0) {
                JOptionPane.showMessageDialog(this, "El stock debe ser mayor o igual a 0.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El stock debe ser un número entero válido.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (cmbFabricante.getSelectedItem() == null || 
            cmbClasificacion.getSelectedItem() == null ||
            cmbUnidadMedida.getSelectedItem() == null || 
            cmbDisponibilidad.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar todos los valores en los combos.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private Producto crearProductoDesdeFormulario() {
        FabricanteProducto fabricante = (FabricanteProducto) cmbFabricante.getSelectedItem();
        ClasificacionProducto clasificacion = (ClasificacionProducto) cmbClasificacion.getSelectedItem();
        ProdUnidadMedida unidadMedida = (ProdUnidadMedida) cmbUnidadMedida.getSelectedItem();
        DisponibilidadProducto disponibilidad = (DisponibilidadProducto) cmbDisponibilidad.getSelectedItem();

        return new Producto(
            0, // ProdCod será asignado por la BD
            fabricante.getFabCod(),
            txtDescripcion.getText().trim(),
            new BigDecimal(txtPrecio.getText().trim()),
            Integer.parseInt(txtStock.getText().trim()),
            clasificacion.getClasProCod(),
            unidadMedida.getUniMedProCod(),
            null, // ReporProdCod puede ser null
            disponibilidad.getDispoProdCod(),
            txtEstadoRegistro.getText().charAt(0),
            fabricante.getFabNom(),
            clasificacion.getClasProDesc(),
            unidadMedida.getUniMedProDesc(),
            disponibilidad.getDispoProdDesc()
        );
    }

    private void comandoCancelar() {
        limpiarCampos();
        habilitarControles(false);
        operacionActual = "";
        flagCarFlaAct = 0;
        codigoSeleccionado = 0;
        habilitarBotonesIniciales();
    }

    private void comandoSalir() {
        dispose();
    }

    private void comandoModificar() {
        int selectedRow = tablaProductos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para modificar.", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        habilitarControles(true);
        operacionActual = "MODIFICAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        txtDescripcion.requestFocus();
    }

    private void comandoEliminar() {
        int selectedRow = tablaProductos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        txtEstadoRegistro.setText("*");
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, 
            "El registro se marcará como eliminado ('*').\nPresione 'Actualizar' para confirmar.", 
            "Eliminación Lógica", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaProductos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para inactivar.", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtEstadoRegistro.setText("I");
        habilitarControles(false);
        operacionActual = "INACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, 
            "El registro se marcará como inactivo ('I').\nPresione 'Actualizar' para confirmar.", 
            "Inactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoReactivar() {
        int selectedRow = tablaProductos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para reactivar.", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtEstadoRegistro.setText("A");
        habilitarControles(false);
        operacionActual = "REACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, 
            "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.", 
            "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoBuscar() {
        String textoBusqueda = txtBuscar.getText().trim();
        if (textoBusqueda.isEmpty()) {
            cargarTablaProductos();
        } else {
            cargarTablaProductosFiltrados(textoBusqueda);
        }
    }

    private void cargarTablaProductos() {
        tableModel.setRowCount(0);
        List<Producto> lista = productoDAO.obtenerTodosProductos();
        for (Producto p : lista) {
            tableModel.addRow(new Object[]{
                p.getProdCod(),
                p.getProdDes(),
                p.getProdPre(),
                p.getProdStock(),
                p.getFabricanteNombre(),
                p.getClasificacionDescripcion(),
                p.getUnidadMedidaDescripcion(),
                p.getDisponibilidadDescripcion(),
                p.getProdEstReg()
            });
        }
    }

    private void cargarTablaProductosFiltrados(String descripcion) {
        tableModel.setRowCount(0);
        List<Producto> lista = productoDAO.buscarProductosPorDescripcion(descripcion);
        for (Producto p : lista) {
            tableModel.addRow(new Object[]{
                p.getProdCod(),
                p.getProdDes(),
                p.getProdPre(),
                p.getProdStock(),
                p.getFabricanteNombre(),
                p.getClasificacionDescripcion(),
                p.getUnidadMedidaDescripcion(),
                p.getDisponibilidadDescripcion(),
                p.getProdEstReg()
            });
        }
    }

    private void habilitarControles(boolean b) {
        txtDescripcion.setEditable(b);
        txtPrecio.setEditable(b);
        txtStock.setEditable(b);
        cmbFabricante.setEnabled(b);
        cmbClasificacion.setEnabled(b);
        cmbUnidadMedida.setEnabled(b);
        cmbDisponibilidad.setEnabled(b);
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtEstadoRegistro.setText("");
        if (cmbFabricante.getItemCount() > 0) cmbFabricante.setSelectedIndex(0);
        if (cmbClasificacion.getItemCount() > 0) cmbClasificacion.setSelectedIndex(0);
        if (cmbUnidadMedida.getItemCount() > 0) cmbUnidadMedida.setSelectedIndex(0);
        if (cmbDisponibilidad.getItemCount() > 0) cmbDisponibilidad.setSelectedIndex(0);
    }

    private void habilitarBotonesIniciales() {
        btnAdicionar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnInactivar.setEnabled(false);
        btnReactivar.setEnabled(false);
        btnBuscar.setEnabled(true);
        btnSalir.setEnabled(true);
    }

    private void habilitarBotonesParaOperacion() {
        btnAdicionar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnInactivar.setEnabled(false);
        btnReactivar.setEnabled(false);
        btnBuscar.setEnabled(false);
        btnSalir.setEnabled(true);
    }

    private void habilitarBotonesParaSeleccion() {
        btnAdicionar.setEnabled(true);
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnInactivar.setEnabled(true);
        btnReactivar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnBuscar.setEnabled(true);
        btnSalir.setEnabled(true);
    }
}