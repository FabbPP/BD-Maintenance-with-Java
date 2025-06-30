package gui;

import dao.DetalleFacturaDAO;
import modelo.DetalleFactura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;

public class DetalleFacturaFrame extends JFrame {

    private JTextField txtFacturaCodigo;
    private JTextField txtProductoCodigo;
    private JTextField txtCantidad;
    private JTextField txtPrecioUnitario;
    private JTextField txtSubtotal;
    private JTextField txtEstadoRegistro;
    private JTable tablaDetalles;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, btnActualizar, btnCancelar, btnSalir;

    private DetalleFacturaDAO detalleFacturaDAO;
    private int flagCarFlaAct = 0;
    private String operacionActual = "";

    public DetalleFacturaFrame() {
        setTitle("Mantenimiento de Detalles de Factura");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        detalleFacturaDAO = new DetalleFacturaDAO();
        initComponents();
        cargarTablaDetalles();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DetalleFacturaFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Detalle de Factura"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Cód. Factura:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtFacturaCodigo = new JTextField(10);
        panelRegistro.add(txtFacturaCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Cód. Producto:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtProductoCodigo = new JTextField(10);
        panelRegistro.add(txtProductoCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        txtCantidad = new JTextField(10);
        panelRegistro.add(txtCantidad, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Precio Unitario:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        txtPrecioUnitario = new JTextField(15);
        panelRegistro.add(txtPrecioUnitario, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Subtotal:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0;
        txtSubtotal = new JTextField(15);
        panelRegistro.add(txtSubtotal, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Estado Registro:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        txtEstadoRegistro = new JTextField(1);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        add(panelRegistro, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Detalles de Factura"));
        tableModel = new DefaultTableModel(new Object[]{"Cód. Factura", "Cód. Producto", "Cantidad", "Precio Unitario", "Subtotal", "Estado"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaDetalles = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaDetalles), BorderLayout.CENTER);

        tablaDetalles.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int i = tablaDetalles.getSelectedRow();
                    if (i != -1) {
                        txtFacturaCodigo.setText(tableModel.getValueAt(i, 0).toString());
                        txtProductoCodigo.setText(tableModel.getValueAt(i, 1).toString());
                        txtCantidad.setText(tableModel.getValueAt(i, 2).toString());
                        txtPrecioUnitario.setText(tableModel.getValueAt(i, 3).toString());
                        txtSubtotal.setText(tableModel.getValueAt(i, 4).toString());
                        txtEstadoRegistro.setText(tableModel.getValueAt(i, 5).toString());
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
        btnModificar.addActionListener(e -> comandoModificar());
        btnEliminar.addActionListener(e -> comandoEliminar());
        btnInactivar.addActionListener(e -> comandoInactivar());
        btnReactivar.addActionListener(e -> comandoReactivar());
        btnActualizar.addActionListener(e -> comandoActualizar());
        btnCancelar.addActionListener(e -> comandoCancelar());
        btnSalir.addActionListener(e -> comandoSalir());
    }

    private void comandoAdicionar() {
        limpiarCampos();
        habilitarControles(true);
        txtEstadoRegistro.setText("A");
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        txtFacturaCodigo.requestFocusInWindow();
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int facCod = Integer.parseInt(txtFacturaCodigo.getText().trim());
            int proCod = Integer.parseInt(txtProductoCodigo.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            BigDecimal precioUnitario = new BigDecimal(txtPrecioUnitario.getText().trim());
            BigDecimal subtotal = new BigDecimal(txtSubtotal.getText().trim());
            String estadoRegistro = txtEstadoRegistro.getText().trim();

            if (cantidad < 1) { 
                JOptionPane.showMessageDialog(this, "La cantidad debe ser al menos 1.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (precioUnitario.compareTo(new BigDecimal("0.01")) < 0) { 
                JOptionPane.showMessageDialog(this, "El precio unitario debe ser al menos 0.01.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (subtotal.compareTo(BigDecimal.ZERO) < 0) { 
                JOptionPane.showMessageDialog(this, "El subtotal no puede ser negativo.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DetalleFactura detalle = new DetalleFactura(facCod, proCod, cantidad, precioUnitario, subtotal, estadoRegistro.charAt(0));

            boolean exito = false;
            String mensaje = "";

            switch (operacionActual) {
                case "ADICIONAR":
                    exito = detalleFacturaDAO.insertarDetalle(detalle);
                    mensaje = exito ? "Detalle de factura adicionado con éxito." : "Error al adicionar detalle de factura. Puede que la combinación Factura/Producto ya exista o la FK sea inválida.";
                    break;
                case "MODIFICAR":
                    exito = detalleFacturaDAO.actualizarDetalle(detalle);
                    mensaje = exito ? "Detalle de factura modificado con éxito." : "Error al modificar detalle de factura.";
                    break;
                case "ELIMINAR":
                    int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de marcar este registro como ELIMINADO LÓGICAMENTE ('*')?", "Confirmar Eliminación Lógica", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        exito = detalleFacturaDAO.eliminarLogicamenteDetalle(facCod, proCod);
                        mensaje = exito ? "Detalle de factura eliminado físicamente con éxito." : "Error al eliminar detalle de factura.";
                    } else {
                        mensaje = "Eliminación cancelada por el usuario.";
                        exito = true;
                    }
                    break;
                case "INACTIVAR":
                    exito = detalleFacturaDAO.inactivarDetalle(Integer.parseInt(txtCodigo.getText()));
                    mensaje = exito ? "Detalle inactivado con éxito." : "Error al inactivar detalle.";
                    break;
                case "REACTIVAR":
                    exito = detalleFacturaDAO.reactivarDetalle(Integer.parseInt(txtCodigo.getText()));
                    mensaje = exito ? "Detalle reactivado con éxito." : "Error al reactivar detalle.";
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Operación no reconocida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            if (exito) {
                JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                habilitarControles(false);
                cargarTablaDetalles();
                flagCarFlaAct = 0;
                operacionActual = "";
                habilitarBotonesIniciales();
            } else {
                JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error de formato numérico. Asegúrese de que los campos de Cantidad, Precio Unitario y Subtotal contengan valores válidos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado al actualizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void comandoCancelar() {
        limpiarCampos();
        habilitarControles(false);
        flagCarFlaAct = 0;
        operacionActual = "";
        habilitarBotonesIniciales();
        tablaDetalles.clearSelection();
    }

    private void comandoSalir() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea salir del mantenimiento de Detalles de Factura?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private void cargarTablaDetalles() {
        tableModel.setRowCount(0);
        List<DetalleFactura> detalles = detalleFacturaDAO.obtenerTodosDetalles();
        for (DetalleFactura det : detalles) {
            tableModel.addRow(new Object[]{
                det.getFacCod(),
                det.getProCod(),
                det.getDetCan(),
                det.getDetPre(),
                det.getDetSub(),
                det.getDetEstReg()
            });
        }
    }

    private void habilitarControles(boolean b) {
        txtFacturaCodigo.setEditable(b);
        txtProductoCodigo.setEditable(b);
        txtCantidad.setEditable(b);
        txtPrecioUnitario.setEditable(b);
        txtSubtotal.setEditable(b);
        txtEstadoRegistro.setEditable(false);
    }

    private void limpiarCampos() {
        txtFacturaCodigo.setText("");
        txtProductoCodigo.setText("");
        txtCantidad.setText("");
        txtPrecioUnitario.setText("");
        txtSubtotal.setText("");
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

    private void comandoModificar() {
        int selectedRow = tablaDetalles.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        habilitarControles(true);
        txtFacturaCodigo.setEditable(false);
        txtProductoCodigo.setEditable(false);
        operacionActual = "MODIFICAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        txtCantidad.requestFocusInWindow();
    }

    private void comandoEliminar() {
        int selectedRow = tablaDetalles.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtFacturaCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtProductoCodigo.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtCantidad.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtPrecioUnitario.setText(tableModel.getValueAt(selectedRow, 3).toString());
        txtSubtotal.setText(tableModel.getValueAt(selectedRow, 4).toString());
        txtEstadoRegistro.setText("*");
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se eliminará FÍSICAMENTE.\nPresione 'Actualizar' para confirmar.", "Confirmar Eliminación", JOptionPane.WARNING_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaDetalles.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para inactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtFacturaCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtProductoCodigo.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtCantidad.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtPrecioUnitario.setText(tableModel.getValueAt(selectedRow, 3).toString());
        txtSubtotal.setText(tableModel.getValueAt(selectedRow, 4).toString());
        txtEstadoRegistro.setText("I");
        habilitarControles(false);
        operacionActual = "INACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como inactivo ('I').\nPresione 'Actualizar' para confirmar.", "Inactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoReactivar() {
        int selectedRow = tablaDetalles.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para reactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtFacturaCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtProductoCodigo.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtCantidad.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtPrecioUnitario.setText(tableModel.getValueAt(selectedRow, 3).toString());
        txtSubtotal.setText(tableModel.getValueAt(selectedRow, 4).toString());
        txtEstadoRegistro.setText("A");
        habilitarControles(false);
        operacionActual = "REACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.", "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }
}