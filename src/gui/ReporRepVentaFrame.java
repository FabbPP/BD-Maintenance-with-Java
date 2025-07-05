package gui;

import dao.RepVentaDAO;
import dao.ReporRepVentaDAO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.RepVenta;
import modelo.ReporRepVenta;

public class ReporRepVentaFrame extends JFrame {

    private JTextField txtCodigo;
    private JComboBox<ComboItem> cbRepresentante;
    private JTextField txtObjetivo;
    private JTextField txtVentasConcretas;
    private JTextField txtCuota;
    private JTextField txtEstadoRegistro;
    private JTable tablaReportes;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, btnActualizar, btnCancelar, btnSalir;

    private ReporRepVentaDAO reporRepVentaDAO;
    private RepVentaDAO repVentaDAO;
    private int flagCarFlaAct = 0;
    private String operacionActual = "";
    private int codigoSeleccionado = 0;
    private int repCodSeleccionado = 0;

    // Clase auxiliar para manejar items del ComboBox
    private static class ComboItem {
        private int codigo;
        private String descripcion;
        
        public ComboItem(int codigo, String descripcion) {
            this.codigo = codigo;
            this.descripcion = descripcion;
        }
        
        public int getCodigo() {
            return codigo;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
        
        @Override
        public String toString() {
            return codigo + " - " + descripcion;
        }
    }

    public ReporRepVentaFrame() {
        setTitle("Mantenimiento de Reportes de Representantes de Venta");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        reporRepVentaDAO = new ReporRepVentaDAO();
        repVentaDAO = new RepVentaDAO();
        initComponents();
        cargarComboBoxes();
        cargarTablaReportes();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new ReporRepVentaFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Reporte de Representante de Venta"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Primera fila
        gbc.gridx = 0; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Código Reporte:"), gbc);
        gbc.gridx = 1;
        txtCodigo = new JTextField(10);
        txtCodigo.setEditable(false); // El código es AUTO_INCREMENT
        panelRegistro.add(txtCodigo, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Representante:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        cbRepresentante = new JComboBox<>();
        cbRepresentante.setPreferredSize(new Dimension(300, cbRepresentante.getPreferredSize().height));
        panelRegistro.add(cbRepresentante, gbc);

        // Segunda fila
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Meta Objetivo:"), gbc);
        gbc.gridx = 1;
        txtObjetivo = new JTextField(15);
        panelRegistro.add(txtObjetivo, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        panelRegistro.add(new JLabel("Ventas Concretas:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtVentasConcretas = new JTextField(10);
        panelRegistro.add(txtVentasConcretas, gbc);

        // Tercera fila
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Cuota:"), gbc);
        gbc.gridx = 1;
        txtCuota = new JTextField(10);
        panelRegistro.add(txtCuota, gbc);

        gbc.gridx = 2; gbc.gridy = 2;
        panelRegistro.add(new JLabel("Estado Registro:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtEstadoRegistro = new JTextField(5);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        // Cuarta fila - Notas
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        JLabel lblNotas = new JLabel("* Ventas Concretas y Cuota deben ser números >= 0. Meta Objetivo en formato decimal (ej: 15000.50)");
        lblNotas.setFont(lblNotas.getFont().deriveFont(Font.ITALIC, 10f));
        lblNotas.setForeground(Color.GRAY);
        panelRegistro.add(lblNotas, gbc);

        add(panelRegistro, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Reportes de Representantes de Venta"));
        tableModel = new DefaultTableModel(new Object[]{"Código Reporte", "Código Rep.", "Nombre Representante", "Meta Objetivo", "Ventas Concretas", "Cuota", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaReportes = new JTable(tableModel);
        tablaReportes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelTabla.add(new JScrollPane(tablaReportes), BorderLayout.CENTER);

        tablaReportes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int i = tablaReportes.getSelectedRow();
                    if (i != -1) {
                        cargarDatosDeTabla(i);
                        habilitarBotonesParaSeleccion();
                    }
                }
            }
        });
        add(panelTabla, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        btnAdicionar = new JButton("Adicionar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnInactivar = new JButton("Inactivar");
        btnReactivar = new JButton("Reactivar");
        btnActualizar = new JButton("Actualizar");
        btnCancelar = new JButton("Cancelar");
        btnSalir = new JButton("Salir");

        panelBotones.add(btnAdicionar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnInactivar);
        panelBotones.add(btnReactivar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnSalir);
        
        add(panelBotones, BorderLayout.SOUTH);

        // Agregar listeners a los botones
        btnAdicionar.addActionListener(e -> comandoAdicionar());
        btnModificar.addActionListener(e -> comandoModificar());
        btnEliminar.addActionListener(e -> comandoEliminar());
        btnInactivar.addActionListener(e -> comandoInactivar());
        btnReactivar.addActionListener(e -> comandoReactivar());
        btnActualizar.addActionListener(e -> comandoActualizar());
        btnCancelar.addActionListener(e -> comandoCancelar());
        btnSalir.addActionListener(e -> comandoSalir());
    }

    private void cargarComboBoxes() {
        // Cargar representantes activos
        List<RepVenta> representantes = repVentaDAO.obtenerRepresentantesActivos();
        cbRepresentante.removeAllItems();
        cbRepresentante.addItem(new ComboItem(0, "-- Seleccionar Representante --"));
        for (RepVenta rep : representantes) {
            cbRepresentante.addItem(new ComboItem(rep.getRepCod(), rep.getRepNom()));
        }
    }

    private void seleccionarEnComboBoxPorCodigo(JComboBox<ComboItem> comboBox, int codigo) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            ComboItem item = comboBox.getItemAt(i);
            if (item.getCodigo() == codigo) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        comboBox.setSelectedIndex(0); // Si no encuentra, selecciona el primer item
    }

    private void comandoAdicionar() {
        limpiarCampos();
        habilitarControles(true);
        txtEstadoRegistro.setText("A");
        txtVentasConcretas.setText("0");
        txtCuota.setText("0");
        cbRepresentante.setSelectedIndex(0);
        cbRepresentante.requestFocus();
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
    }

    private void comandoModificar() {
        int selectedRow = tablaReportes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Verificar que el registro no esté eliminado
        String estado = tableModel.getValueAt(selectedRow, 6).toString();
        if ("*".equals(estado)) {
            JOptionPane.showMessageDialog(this, "No se puede modificar un registro eliminado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        habilitarControles(true);
        cbRepresentante.setEnabled(false); // No permitir cambiar el representante en modificación
        operacionActual = "MODIFICAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
    }

    private void comandoEliminar() {
        int selectedRow = tablaReportes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Verificar que el registro no esté ya eliminado
        String estado = tableModel.getValueAt(selectedRow, 6).toString();
        if ("*".equals(estado)) {
            JOptionPane.showMessageDialog(this, "El registro ya está eliminado.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int respuesta = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea eliminar este reporte?\nEsta acción marcará el registro como eliminado ('*').", 
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION);
            
        if (respuesta == JOptionPane.YES_OPTION) {
            boolean exito = reporRepVentaDAO.eliminarLogicamenteReporte(codigoSeleccionado, repCodSeleccionado);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Reporte eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTablaReportes();
                comandoCancelar();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el reporte.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void comandoInactivar() {
        int selectedRow = tablaReportes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para inactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String estado = tableModel.getValueAt(selectedRow, 6).toString();
        if ("I".equals(estado)) {
            JOptionPane.showMessageDialog(this, "El registro ya está inactivo.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if ("*".equals(estado)) {
            JOptionPane.showMessageDialog(this, "No se puede inactivar un registro eliminado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int respuesta = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea inactivar este reporte?", 
            "Confirmar Inactivación", 
            JOptionPane.YES_NO_OPTION);
            
        if (respuesta == JOptionPane.YES_OPTION) {
            boolean exito = reporRepVentaDAO.inactivarReporte(codigoSeleccionado, repCodSeleccionado);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Reporte inactivado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTablaReportes();
                comandoCancelar();
            } else {
                JOptionPane.showMessageDialog(this, "Error al inactivar el reporte.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void comandoReactivar() {
        int selectedRow = tablaReportes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para reactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String estado = tableModel.getValueAt(selectedRow, 6).toString();
        if ("A".equals(estado)) {
            JOptionPane.showMessageDialog(this, "El registro ya está activo.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if ("*".equals(estado)) {
            JOptionPane.showMessageDialog(this, "No se puede reactivar un registro eliminado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int respuesta = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea reactivar este reporte?", 
            "Confirmar Reactivación", 
            JOptionPane.YES_NO_OPTION);
            
        if (respuesta == JOptionPane.YES_OPTION) {
            boolean exito = reporRepVentaDAO.reactivarReporte(codigoSeleccionado, repCodSeleccionado);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Reporte reactivado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTablaReportes();
                comandoCancelar();
            } else {
                JOptionPane.showMessageDialog(this, "Error al reactivar el reporte.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar campos obligatorios
        if (!validarCampos()) {
            return;
        }

        // Crear objeto reporte con los datos
        ReporRepVenta reporte = crearReporteDesdeFormulario();
        if (reporte == null) {
            return; // Error en la creación del objeto
        }

        boolean exito = false;
        String mensaje = "";

        switch (operacionActual) {
            case "ADICIONAR":
                String mensajeError = reporRepVentaDAO.insertarReporte(reporte);
                if (mensajeError == null) {
                    exito = true;
                    mensaje = "Reporte registrado con éxito.";
                } else {
                    exito = false;
                    mensaje = mensajeError;
                }
                break;
                
            case "MODIFICAR":
                reporte.setRepoRepVentCod(codigoSeleccionado);
                String mensajeErrorMod = reporRepVentaDAO.actualizarReporte(reporte);
                if (mensajeErrorMod == null) {
                    exito = true;
                    mensaje = "Reporte modificado con éxito.";
                } else {
                    exito = false;
                    mensaje = mensajeErrorMod;
                }
                break;
                
            default:
                JOptionPane.showMessageDialog(this, "Operación no reconocida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTablaReportes();
            comandoCancelar();
        } else {
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        String objetivoStr = txtObjetivo.getText().trim();
        String ventasConcretasStr = txtVentasConcretas.getText().trim();
        String cuotaStr = txtCuota.getText().trim();

        if (objetivoStr.isEmpty() || ventasConcretasStr.isEmpty() || cuotaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar selección de representante
        ComboItem representanteSeleccionado = (ComboItem) cbRepresentante.getSelectedItem();
        if (representanteSeleccionado == null || representanteSeleccionado.getCodigo() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un representante.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar formato numérico
        try {
            BigDecimal objetivo = new BigDecimal(objetivoStr);
            if (objetivo.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "La meta objetivo debe ser mayor o igual a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            int ventasConcretas = Integer.parseInt(ventasConcretasStr);
            if (ventasConcretas < 0) {
                JOptionPane.showMessageDialog(this, "Las ventas concretas deben ser >= 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            int cuota = Integer.parseInt(cuotaStr);
            if (cuota < 0) {
                JOptionPane.showMessageDialog(this, "La cuota debe ser >= 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Verifique que los valores numéricos sean válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private ReporRepVenta crearReporteDesdeFormulario() {
        try {
            ComboItem representanteSeleccionado = (ComboItem) cbRepresentante.getSelectedItem();
            
            ReporRepVenta reporte = new ReporRepVenta();
            reporte.setRepCod(representanteSeleccionado.getCodigo());
            reporte.setRepoRepVentObj(new BigDecimal(txtObjetivo.getText().trim()));
            reporte.setRepoRepVentNum(Integer.parseInt(txtVentasConcretas.getText().trim()));
            reporte.setRepoRepVentCuo(Integer.parseInt(txtCuota.getText().trim()));
            reporte.setRepoRepVentEstReg(txtEstadoRegistro.getText().trim().charAt(0));
            
            return reporte;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear el objeto reporte: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void comandoCancelar() {
        limpiarCampos();
        habilitarControles(false);
        operacionActual = "";
        flagCarFlaAct = 0;
        codigoSeleccionado = 0;
        repCodSeleccionado = 0;
        habilitarBotonesIniciales();
        tablaReportes.clearSelection();
    }

    private void comandoSalir() {
        if (flagCarFlaAct == 1) {
            int respuesta = JOptionPane.showConfirmDialog(this, 
                "Hay una operación pendiente. ¿Desea salir sin guardar?", 
                "Confirmar Salida", 
                JOptionPane.YES_NO_OPTION);
            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }
        }
        dispose();
    }

    private void cargarTablaReportes() {
        tableModel.setRowCount(0);
        List<ReporRepVenta> lista = reporRepVentaDAO.obtenerTodosReportes();
        
        for (ReporRepVenta r : lista) {
            tableModel.addRow(new Object[]{
                r.getRepoRepVentCod(),
                r.getRepCod(),
                r.getRepresentanteNombre() != null ? r.getRepresentanteNombre() : "N/A",
                r.getRepoRepVentObj(),
                r.getRepoRepVentNum(),
                r.getRepoRepVentCuo(),
                r.getRepoRepVentEstReg()
            });
        }
    }

    private void cargarDatosDeTabla(int selectedRow) {
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        
        // Seleccionar representante por código
        int codigoRep = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
        seleccionarEnComboBoxPorCodigo(cbRepresentante, codigoRep);
        
        txtObjetivo.setText(tableModel.getValueAt(selectedRow, 3).toString());
        txtVentasConcretas.setText(tableModel.getValueAt(selectedRow, 4).toString());
        txtCuota.setText(tableModel.getValueAt(selectedRow, 5).toString());
        txtEstadoRegistro.setText(tableModel.getValueAt(selectedRow, 6).toString());
        
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        repCodSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
    }

    private void habilitarControles(boolean habilitado) {
        cbRepresentante.setEnabled(habilitado);
        txtObjetivo.setEditable(habilitado);
        txtVentasConcretas.setEditable(habilitado);
        txtCuota.setEditable(habilitado);
        // txtCodigo y txtEstadoRegistro siempre permanecen no editables
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        if (cbRepresentante.getItemCount() > 0) {
            cbRepresentante.setSelectedIndex(0);
        }
        txtObjetivo.setText("");
        txtVentasConcretas.setText("");
        txtCuota.setText("");
        txtEstadoRegistro.setText("");
    }

    private void habilitarBotonesIniciales() {
        btnAdicionar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnInactivar.setEnabled(false);
        btnReactivar.setEnabled(false);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnSalir.setEnabled(true);
    }

    private void habilitarBotonesParaOperacion() {
        btnAdicionar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnInactivar.setEnabled(false);
        btnReactivar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnSalir.setEnabled(false);
    }

    private void habilitarBotonesParaSeleccion() {
        btnAdicionar.setEnabled(false);
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnInactivar.setEnabled(true);
        btnReactivar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnSalir.setEnabled(true);
    }
}