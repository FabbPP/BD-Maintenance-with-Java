package gui;

import dao.FacturaDAO;
import dao.ClienteDAO;
import dao.RepVentaDAO;
import modelo.Factura;
import modelo.Cliente;
import modelo.RepVenta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class FacturaFrame extends JFrame{

    private JTextField txtCodigo;
    private JComboBox<String> cmbCliente;
    private JComboBox<String> cmbRepresentante;
    private JTextField txtImporteTotal;
    private JTextField txtAño;
    private JTextField txtMes;
    private JTextField txtDia;
    private JTextField txtPlazoPago;
    private JTextField txtFechaPago;
    private JTextField txtEstadoRegistro;
    private JTable tablaFacturas;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, btnActualizar, btnCancelar, btnSalir;

    private FacturaDAO facturaDAO;
    private ClienteDAO clienteDAO;
    private RepVentaDAO repVentaDAO;
    private List<Cliente> listaClientes;
    private List<RepVenta> listaRepresentantes;
    
    private int flagCarFlaAct = 0;
    private String operacionActual = "";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public FacturaFrame() {
        setTitle("Mantenimiento de Factura");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        facturaDAO = new FacturaDAO();
        clienteDAO = new ClienteDAO();
        repVentaDAO = new RepVentaDAO();
        
        initComponents();
        cargarDatosComboBox();
        cargarTablaFacturas();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FacturaFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Factura"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Código Factura:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtCodigo = new JTextField(10);
        txtCodigo.setEditable(false);
        panelRegistro.add(txtCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        cmbCliente = new JComboBox<>();
        cmbCliente.setPreferredSize(new Dimension(300, 25));
        panelRegistro.add(cmbCliente, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Representante:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        cmbRepresentante = new JComboBox<>();
        cmbRepresentante.setPreferredSize(new Dimension(300, 25));
        panelRegistro.add(cmbRepresentante, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Importe Total:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        txtImporteTotal = new JTextField(15);
        panelRegistro.add(txtImporteTotal, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Año:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0;
        txtAño = new JTextField(5);
        panelRegistro.add(txtAño, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Mes:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0;
        txtMes = new JTextField(5);
        panelRegistro.add(txtMes, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Día:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0;
        txtDia = new JTextField(5);
        panelRegistro.add(txtDia, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Plazo Pago (AAAA-MM-DD):"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 1.0;
        txtPlazoPago = new JTextField(15);
        panelRegistro.add(txtPlazoPago, gbc);

        gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Fecha Pago (AAAA-MM-DD):"), gbc);
        gbc.gridx = 3; gbc.gridy = 3; gbc.weightx = 1.0;
        txtFechaPago = new JTextField(15);
        panelRegistro.add(txtFechaPago, gbc);

        gbc.gridx = 2; gbc.gridy = 4; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Estado Registro:"), gbc);
        gbc.gridx = 3; gbc.gridy = 4; gbc.weightx = 1.0;
        txtEstadoRegistro = new JTextField(1);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        add(panelRegistro, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Facturas"));
        tableModel = new DefaultTableModel(new Object[]{"Cód. Factura", "Cliente", "Representante", "Importe", "Año", "Mes", "Día", "Plazo Pago", "Fecha Pago", "Estado"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaFacturas = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaFacturas), BorderLayout.CENTER);

        tablaFacturas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int i = tablaFacturas.getSelectedRow();
                    if (i != -1) {
                        txtCodigo.setText(tableModel.getValueAt(i, 0).toString());
                        
                        // Buscar y seleccionar cliente en el combo
                        String clienteNombre = tableModel.getValueAt(i, 1).toString();
                        for (int j = 0; j < cmbCliente.getItemCount(); j++) {
                            if (cmbCliente.getItemAt(j).equals(clienteNombre)) {
                                cmbCliente.setSelectedIndex(j);
                                break;
                            }
                        }
                        
                        // Buscar y seleccionar representante en el combo
                        String repNombre = tableModel.getValueAt(i, 2).toString();
                        for (int j = 0; j < cmbRepresentante.getItemCount(); j++) {
                            if (cmbRepresentante.getItemAt(j).equals(repNombre)) {
                                cmbRepresentante.setSelectedIndex(j);
                                break;
                            }
                        }
                        
                        txtImporteTotal.setText(tableModel.getValueAt(i, 3).toString());
                        txtAño.setText(tableModel.getValueAt(i, 4).toString());
                        txtMes.setText(tableModel.getValueAt(i, 5).toString());
                        txtDia.setText(tableModel.getValueAt(i, 6).toString());
                        txtPlazoPago.setText(tableModel.getValueAt(i, 7) != null ? dateFormat.format((Date) tableModel.getValueAt(i, 7)) : "");
                        txtFechaPago.setText(tableModel.getValueAt(i, 8) != null ? dateFormat.format((Date) tableModel.getValueAt(i, 8)) : "");
                        txtEstadoRegistro.setText(tableModel.getValueAt(i, 9).toString());
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

    private void cargarDatosComboBox() {
        // Cargar clientes activos
        listaClientes = clienteDAO.obtenerClientesActivos();
        cmbCliente.removeAllItems();
        cmbCliente.addItem("Seleccionar Cliente...");
        for (Cliente cliente : listaClientes) {
            cmbCliente.addItem(cliente.getCliNom() + " " + cliente.getCliApePat() + " " + cliente.getCliApeMat() + " (Cod: " + cliente.getCliCod() + ")");
        }
        
        // Cargar todos los representantes
        listaRepresentantes = repVentaDAO.obtenerTodosRepresentantes();
        cmbRepresentante.removeAllItems();
        cmbRepresentante.addItem("Seleccionar Representante...");
        for (RepVenta rep : listaRepresentantes) {
            cmbRepresentante.addItem(rep.getRepNom() + " (Cod: " + rep.getRepCod() + ")");
        }
    }

    private int obtenerCodigoClienteSeleccionado() {
        int index = cmbCliente.getSelectedIndex();
        if (index <= 0) return -1;
        return listaClientes.get(index - 1).getCliCod();
    }

    private int obtenerCodigoRepresentanteSeleccionado() {
        int index = cmbRepresentante.getSelectedIndex();
        if (index <= 0) return -1;
        return listaRepresentantes.get(index - 1).getRepCod();
    }

    private void comandoAdicionar() {
        limpiarCampos();
        habilitarControles(true);
        txtEstadoRegistro.setText("A");
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        cmbCliente.requestFocusInWindow();
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int cliCod = obtenerCodigoClienteSeleccionado();
            int repCod = obtenerCodigoRepresentanteSeleccionado();
            
            if (cliCod == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (repCod == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un representante.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            BigDecimal importe = new BigDecimal(txtImporteTotal.getText().trim());
            int año = Integer.parseInt(txtAño.getText().trim());
            int mes = Integer.parseInt(txtMes.getText().trim());
            int dia = Integer.parseInt(txtDia.getText().trim());
            Date plazoPago = txtPlazoPago.getText().trim().isEmpty() ? null : Date.valueOf(txtPlazoPago.getText().trim());
            Date fechaPago = txtFechaPago.getText().trim().isEmpty() ? null : Date.valueOf(txtFechaPago.getText().trim());
            String estadoRegistro = txtEstadoRegistro.getText().trim();

            if (importe.compareTo(BigDecimal.ZERO) < 0) { // chk_fac_importe_check
                JOptionPane.showMessageDialog(this, "El importe total no puede ser negativo.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (año < 2000) { // chk_fac_año_check
                JOptionPane.showMessageDialog(this, "El año debe ser 2000 o posterior.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (mes < 1 || mes > 12) { // chk_fac_mes_check
                JOptionPane.showMessageDialog(this, "El mes debe estar entre 1 y 12.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (dia < 1 || dia > 31) { // chk_fac_dia_check
                JOptionPane.showMessageDialog(this, "El día debe estar entre 1 y 31.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!estadoRegistro.matches("A|I|\\*")) {
                JOptionPane.showMessageDialog(this, "El Estado de Registro solo puede ser 'A', 'I' o '*'.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Factura factura = new Factura();
            factura.setCliCod(cliCod);
            factura.setRepCod(repCod);
            factura.setFacImp(importe);
            factura.setFacAño(año);
            factura.setFacMes(mes);
            factura.setFacDia(dia);
            factura.setFacPlazoPago(plazoPago);
            factura.setFacFechPago(fechaPago);
            factura.setFacEstReg(estadoRegistro.charAt(0));

            boolean exito = false;
            String mensaje = "";

            switch (operacionActual) {
                case "ADICIONAR":
                    exito = facturaDAO.insertarFactura(factura);
                    mensaje = exito ? "Factura adicionada con éxito." : "Error al adicionar factura.";
                    break;
                case "MODIFICAR":
                    factura.setFacCod(Integer.parseInt(txtCodigo.getText().trim()));
                    exito = facturaDAO.actualizarFactura(factura);
                    mensaje = exito ? "Factura modificada con éxito." : "Error al modificar factura.";
                    break;
                case "ELIMINAR":
                    int confirm = JOptionPane.showConfirmDialog(this,  "¿Está seguro de marcar este registro como ELIMINADO LÓGICAMENTE ('*')?", "Confirmar Eliminación Lógica", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        exito = facturaDAO.eliminarLogicamenteFactura(Integer.parseInt(txtCodigo.getText()));
                        mensaje = exito ? "Factura eliminada físicamente con éxito." : "Error al eliminar factura.";
                    } else {
                        mensaje = "Operación de eliminación cancelada.";
                        exito = true;
                    }
                    break;
                case "INACTIVAR":
                    exito = facturaDAO.inactivarFactura(Integer.parseInt(txtCodigo.getText()));
                    mensaje = exito ? "Factura inactivada con éxito." : "Error al inactivar factura.";
                    break;
                case "REACTIVAR":
                    exito = facturaDAO.reactivarFactura(Integer.parseInt(txtCodigo.getText()));
                    mensaje = exito ? "Factura reactivada con éxito." : "Error al reactivar factura.";
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Operación no reconocida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            if (exito) {
                JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                habilitarControles(false);
                cargarTablaFacturas();
                flagCarFlaAct = 0;
                operacionActual = "";
                habilitarBotonesIniciales();
            } else {
                JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error de formato numérico. Asegúrese de que todos los campos numéricos contengan valores válidos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error de formato de fecha. Use el formato AAAA-MM-DD.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
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
        tablaFacturas.clearSelection();
    }

    private void comandoSalir() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea salir del mantenimiento de Facturas?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private void cargarTablaFacturas() {
        tableModel.setRowCount(0);
        List<Factura> facturas = facturaDAO.obtenerTodasFacturas();
        for (Factura fac : facturas) {
            // Buscar nombre del cliente
            String nombreCliente = "Cliente no encontrado";
            for (Cliente cliente : listaClientes) {
                if (cliente.getCliCod() == fac.getCliCod()) {
                    nombreCliente = cliente.getCliNom() + " " + cliente.getCliApePat() + " " + cliente.getCliApeMat();
                    break;
                }
            }
            
            // Buscar nombre del representante
            String nombreRepresentante = "Representante no encontrado";
            for (RepVenta rep : listaRepresentantes) {
                if (rep.getRepCod() == fac.getRepCod()) {
                    nombreRepresentante = rep.getRepNom();
                    break;
                }
            }
            
            tableModel.addRow(new Object[]{
                fac.getFacCod(),
                nombreCliente,
                nombreRepresentante,
                fac.getFacImp(),
                fac.getFacAño(),
                fac.getFacMes(),
                fac.getFacDia(),
                fac.getFacPlazoPago(),
                fac.getFacFechPago(),
                fac.getFacEstReg()
            });
        }
    }

    private void habilitarControles(boolean b) {
        txtCodigo.setEditable(false);
        cmbCliente.setEnabled(b);
        cmbRepresentante.setEnabled(b);
        txtImporteTotal.setEditable(b);
        txtAño.setEditable(b);
        txtMes.setEditable(b);
        txtDia.setEditable(b);
        txtPlazoPago.setEditable(b);
        txtFechaPago.setEditable(b);
        txtEstadoRegistro.setEditable(false);
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        cmbCliente.setSelectedIndex(0);
        cmbRepresentante.setSelectedIndex(0);
        txtImporteTotal.setText("");
        txtAño.setText("");
        txtMes.setText("");
        txtDia.setText("");
        txtPlazoPago.setText("");
        txtFechaPago.setText("");
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
        int selectedRow = tablaFacturas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        habilitarControles(true);
        txtCodigo.setEditable(false);
        txtEstadoRegistro.setEditable(false);
        operacionActual = "MODIFICAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        cmbCliente.requestFocusInWindow();
    }

    private void comandoEliminar() {
        int selectedRow = tablaFacturas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        cargarDatosDeFilaSeleccionada(selectedRow);
        txtEstadoRegistro.setText("*");
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se eliminará FÍSICAMENTE.\nPresione 'Actualizar' para confirmar.", "Confirmar Eliminación", JOptionPane.WARNING_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaFacturas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para inactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        cargarDatosDeFilaSeleccionada(selectedRow);
        txtEstadoRegistro.setText("I");
        habilitarControles(false);
        operacionActual = "INACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como inactivo ('I').\nPresione 'Actualizar' para confirmar.", "Inactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoReactivar() {
        int selectedRow = tablaFacturas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para reactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        cargarDatosDeFilaSeleccionada(selectedRow);
        txtEstadoRegistro.setText("A");
        habilitarControles(false);
        operacionActual = "REACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.", "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cargarDatosDeFilaSeleccionada(int selectedRow) {
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        
        // Buscar y seleccionar cliente en el combo
        String clienteNombre = tableModel.getValueAt(selectedRow, 1).toString();
        for (int j = 0; j < cmbCliente.getItemCount(); j++) {
            if (cmbCliente.getItemAt(j).contains(clienteNombre.split(" ")[0])) {
                cmbCliente.setSelectedIndex(j);
                break;
            }
        }
        
        // Buscar y seleccionar representante en el combo
        String repNombre = tableModel.getValueAt(selectedRow, 2).toString();
        for (int j = 0; j < cmbRepresentante.getItemCount(); j++) {
            if (cmbRepresentante.getItemAt(j).contains(repNombre)) {
                cmbRepresentante.setSelectedIndex(j);
                break;
            }
        }
        
        txtImporteTotal.setText(tableModel.getValueAt(selectedRow, 3).toString());
        txtAño.setText(tableModel.getValueAt(selectedRow, 4).toString());
        txtMes.setText(tableModel.getValueAt(selectedRow, 5).toString());
        txtDia.setText(tableModel.getValueAt(selectedRow, 6).toString());
        txtPlazoPago.setText(tableModel.getValueAt(selectedRow, 7) != null ? dateFormat.format((Date) tableModel.getValueAt(selectedRow, 7)) : "");
        txtFechaPago.setText(tableModel.getValueAt(selectedRow, 8) != null ? dateFormat.format((Date) tableModel.getValueAt(selectedRow, 8)) : "");
        txtEstadoRegistro.setText(tableModel.getValueAt(selectedRow, 9).toString());
    }
}