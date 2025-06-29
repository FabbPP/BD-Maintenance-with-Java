package gui;

import dao.ReporRepVentaDAO;
import dao.RepVentaDAO;
import dao.CargoDAO;
import modelo.RepVenta;
import modelo.Cargo;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.ReporRepVenta;

public class ReporRepVentaFrame extends JFrame {

    private JTextField txtCodigo;
    private JComboBox<ComboItem> cbRepresentante;
    private JTextField txtObjetivo;
    private JTextField txtVentasConcretas;
    private JTextField txtCuota;
    private JComboBox<ComboItem> cbCargo;
    private JTextField txtEstadoRegistro;
    private JTable tablaReportes;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, btnActualizar, btnCancelar, btnSalir;

    private ReporRepVentaDAO reporRepVentaDAO;
    private RepVentaDAO repVentaDAO;
    private CargoDAO cargoDAO;
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
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        reporRepVentaDAO = new ReporRepVentaDAO();
        repVentaDAO = new RepVentaDAO();
        cargoDAO = new CargoDAO();
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
        panelRegistro.add(new JLabel("Cargo:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        cbCargo = new JComboBox<>();
        cbCargo.setPreferredSize(new Dimension(250, cbCargo.getPreferredSize().height));
        panelRegistro.add(cbCargo, gbc);

        // Cuarta fila
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Estado Registro:"), gbc);
        gbc.gridx = 1;
        txtEstadoRegistro = new JTextField(5);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        // Quinta fila - Notas
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        JLabel lblNotas = new JLabel("* Ventas Concretas y Cuota deben ser números >= 0. Meta Objetivo en formato decimal (ej: 15000.50)");
        lblNotas.setFont(lblNotas.getFont().deriveFont(Font.ITALIC, 10f));
        lblNotas.setForeground(Color.GRAY);
        panelRegistro.add(lblNotas, gbc);

        add(panelRegistro, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Reportes de Representantes de Venta"));
        tableModel = new DefaultTableModel(new Object[]{"Código Reporte", "Código Rep.", "Nombre Rep.", "Meta Objetivo", "Ventas Concretas", "Cuota", "Código Cargo", "Descripción Cargo", "Estado"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaReportes = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaReportes), BorderLayout.CENTER);

        tablaReportes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int i = tablaReportes.getSelectedRow();
                    if (i != -1) {
                        txtCodigo.setText(tableModel.getValueAt(i, 0).toString());
                        
                        // Seleccionar representante por código
                        int codigoRep = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                        seleccionarEnComboBoxPorCodigo(cbRepresentante, codigoRep);
                        
                        txtObjetivo.setText(tableModel.getValueAt(i, 3).toString());
                        txtVentasConcretas.setText(tableModel.getValueAt(i, 4).toString());
                        txtCuota.setText(tableModel.getValueAt(i, 5).toString());
                        
                        // Seleccionar cargo si existe
                        Object codigoCargoObj = tableModel.getValueAt(i, 6);
                        if (codigoCargoObj != null && !codigoCargoObj.toString().isEmpty()) {
                            int codigoCargo = Integer.parseInt(codigoCargoObj.toString());
                            seleccionarEnComboBoxPorCodigo(cbCargo, codigoCargo);
                        } else {
                            cbCargo.setSelectedIndex(0);
                        }
                        
                        txtEstadoRegistro.setText(tableModel.getValueAt(i, 8).toString());
                        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
                        repCodSeleccionado = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                        habilitarBotonesParaSeleccion();
                    }
                }
            }
        });
        add(panelTabla, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 10, 10));
        btnAdicionar = new JButton("Adicionar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnCancelar = new JButton("Cancelar");
        btnInactivar = new JButton("Inactivar");
        btnReactivar = new JButton("Reactivar");
        btnActualizar = new JButton("Actualizar");
        btnSalir = new JButton("Salir");

        for (JButton b : new JButton[]{btnAdicionar, btnModificar, btnEliminar, btnCancelar, btnInactivar, btnReactivar, btnActualizar, btnSalir}) {
            panelBotones.add(b);
        }
        add(panelBotones, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> comandoAdicionar());
        btnActualizar.addActionListener(e -> comandoActualizar());
        btnCancelar.addActionListener(e -> comandoCancelar());
        btnSalir.addActionListener(e -> comandoSalir());
        btnModificar.addActionListener(e -> comandoModificar());
        btnEliminar.addActionListener(e -> comandoEliminar());
        btnInactivar.addActionListener(e -> comandoInactivar());
        btnReactivar.addActionListener(e -> comandoReactivar());
    }

    private void cargarComboBoxes() {
        // Cargar representantes
        List<RepVenta> representantes = repVentaDAO.obtenerRepresentantesActivos();
        cbRepresentante.removeAllItems();
        cbRepresentante.addItem(new ComboItem(0, "-- Seleccionar Representante --"));
        for (RepVenta rep : representantes) {
            cbRepresentante.addItem(new ComboItem(rep.getRepCod(), rep.getRepNom()));
        }

        // Cargar cargos
        List<Cargo> cargos = cargoDAO.obtenerCargosActivos();
        cbCargo.removeAllItems();
        cbCargo.addItem(new ComboItem(0, "-- Seleccionar Cargo (Opcional) --"));
        for (Cargo cargo : cargos) {
            cbCargo.addItem(new ComboItem(cargo.getCarCod(), cargo.getCarDesc()));
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
        cbCargo.setSelectedIndex(0);
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String objetivoStr = txtObjetivo.getText().trim();
        String ventasConcretasStr = txtVentasConcretas.getText().trim();
        String cuotaStr = txtCuota.getText().trim();
        String estadoStr = txtEstadoRegistro.getText().trim();

        if (objetivoStr.isEmpty() || ventasConcretasStr.isEmpty() || cuotaStr.isEmpty() || estadoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar selección de representante
        ComboItem representanteSeleccionado = (ComboItem) cbRepresentante.getSelectedItem();
        if (representanteSeleccionado == null || representanteSeleccionado.getCodigo() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un representante.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal objetivo;
        int ventasConcretas, cuota, repCodigo;
        Integer carCodigo = null;
        
        try {
            objetivo = new BigDecimal(objetivoStr);
            ventasConcretas = Integer.parseInt(ventasConcretasStr);
            cuota = Integer.parseInt(cuotaStr);
            
            if (ventasConcretas < 0) {
                JOptionPane.showMessageDialog(this, "Las ventas concretas deben ser >= 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (cuota < 0) {
                JOptionPane.showMessageDialog(this, "La cuota debe ser >= 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Verifique que los valores numéricos sean válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener código del representante
        repCodigo = representanteSeleccionado.getCodigo();

        // Obtener código del cargo (opcional)
        ComboItem cargoSeleccionado = (ComboItem) cbCargo.getSelectedItem();
        if (cargoSeleccionado != null && cargoSeleccionado.getCodigo() != 0) {
            carCodigo = cargoSeleccionado.getCodigo();
        }

        ReporRepVenta reporte = new ReporRepVenta();
        reporte.setRepCod(repCodigo);
        reporte.setRepoRepVentObj(objetivo);
        reporte.setRepoRepVentNum(ventasConcretas);
        reporte.setRepoRepVentCuo(cuota);
        reporte.setCarCod(carCodigo);
        reporte.setRepoRepVentEstReg(estadoStr.charAt(0));

        boolean exito = false;
        String mensaje = "";

        switch (operacionActual) {
            case "ADICIONAR":
                String mensajeErrorAdi = reporRepVentaDAO.insertarReporte(reporte);
                if (mensajeErrorAdi == null) {
                    exito = true;
                    mensaje = "Reporte registrado con éxito.";
                } else {
                    exito = false;
                    mensaje = mensajeErrorAdi;
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
            case "ELIMINAR":
                exito = reporRepVentaDAO.eliminarLogicamenteReporte(codigoSeleccionado, repCodSeleccionado);
                mensaje = exito ? "Reporte eliminado con éxito." : "Error al eliminar reporte.";
                break;
            case "INACTIVAR":
                exito = reporRepVentaDAO.inactivarReporte(codigoSeleccionado, repCodSeleccionado);
                mensaje = exito ? "Reporte inactivado con éxito." : "Error al inactivar reporte.";
                break;
            case "REACTIVAR":
                exito = reporRepVentaDAO.reactivarReporte(codigoSeleccionado, repCodSeleccionado);
                mensaje = exito ? "Reporte reactivado con éxito." : "Error al reactivar reporte.";
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

    private void comandoCancelar() {
        limpiarCampos();
        habilitarControles(false);
        operacionActual = "";
        flagCarFlaAct = 0;
        codigoSeleccionado = 0;
        repCodSeleccionado = 0;
        habilitarBotonesIniciales();
    }

    private void comandoSalir() {
        dispose();
    }

    private void cargarTablaReportes() {
        tableModel.setRowCount(0);
        List<ReporRepVenta> lista = reporRepVentaDAO.obtenerTodosReportes();
        for (ReporRepVenta r : lista) {
            tableModel.addRow(new Object[]{
                r.getRepoRepVentCod(),
                r.getRepCod(),
                r.getRepresentanteNombre() != null ? r.getRepresentanteNombre() : "",
                r.getRepoRepVentObj(),
                r.getRepoRepVentNum(),
                r.getRepoRepVentCuo(),
                r.getCarCod() != null ? r.getCarCod() : "",
                r.getCargoDescripcion() != null ? r.getCargoDescripcion() : "",
                r.getRepoRepVentEstReg()
            });
        }
    }

    private void habilitarControles(boolean b) {
        cbRepresentante.setEnabled(b);
        txtObjetivo.setEditable(b);
        txtVentasConcretas.setEditable(b);
        txtCuota.setEditable(b);
        cbCargo.setEnabled(b);
        // txtCodigo y txtEstadoRegistro siempre permanecen no editables
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        cbRepresentante.setSelectedIndex(0);
        txtObjetivo.setText("");
        txtVentasConcretas.setText("");
        txtCuota.setText("");
        cbCargo.setSelectedIndex(0);
        txtEstadoRegistro.setText("");
    }

    private void habilitarBotonesIniciales() {
        btnAdicionar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnInactivar.setEnabled(false);
        btnReactivar.setEnabled(false);
    }

    private void habilitarBotonesParaOperacion() {
        btnAdicionar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnInactivar.setEnabled(false);
        btnReactivar.setEnabled(false);
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

    private void comandoModificar() {
        int selectedRow = tablaReportes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        habilitarControles(true);
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
        cargarDatosDeTabla(selectedRow);
        txtEstadoRegistro.setText("*");
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como eliminado ('*').\nPresione 'Actualizar' para confirmar.", "Eliminación Lógica", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaReportes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para inactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        cargarDatosDeTabla(selectedRow);
        txtEstadoRegistro.setText("I");
        habilitarControles(false);
        operacionActual = "INACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como inactivo ('I').\nPresione 'Actualizar' para confirmar.", "Inactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoReactivar() {
        int selectedRow = tablaReportes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para reactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        cargarDatosDeTabla(selectedRow);
        txtEstadoRegistro.setText("A");
        habilitarControles(false);
        operacionActual = "REACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.", "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cargarDatosDeTabla(int selectedRow) {
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        
        // Seleccionar representante por código
        int codigoRep = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
        seleccionarEnComboBoxPorCodigo(cbRepresentante, codigoRep);
        
        txtObjetivo.setText(tableModel.getValueAt(selectedRow, 3).toString());
        txtVentasConcretas.setText(tableModel.getValueAt(selectedRow, 4).toString());
        txtCuota.setText(tableModel.getValueAt(selectedRow, 5).toString());
        
        // Seleccionar cargo si existe
        Object codigoCargoObj = tableModel.getValueAt(selectedRow, 6);
        if (codigoCargoObj != null && !codigoCargoObj.toString().isEmpty()) {
            int codigoCargo = Integer.parseInt(codigoCargoObj.toString());
            seleccionarEnComboBoxPorCodigo(cbCargo, codigoCargo);
        } else {
            cbCargo.setSelectedIndex(0);
        }
        
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        repCodSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
    }
}