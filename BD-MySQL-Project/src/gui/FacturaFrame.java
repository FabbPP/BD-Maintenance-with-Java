package gui;

import dao.FacturaDAO;
import dao.ClienteDAO;
import dao.RepVentaDAO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.Factura;
import modelo.Cliente;
import modelo.RepVenta;

public class FacturaFrame extends JFrame {

    private JTextField txtCodigo;
    private JComboBox<ComboItem> cbCliente;
    private JComboBox<ComboItem> cbRepresentante;
    private JTextField txtImporte;
    private JSpinner spnAño;
    private JSpinner spnMes;
    private JSpinner spnDia;
    private JTextField txtPlazoPago;
    private JTextField txtFechaPago;
    private JTextField txtEstadoRegistro;
    private JComboBox<String> cbEstadoFactura;
    private JTable tablaFacturas;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, btnActualizar, btnCancelar, btnSalir;

    private FacturaDAO facturaDAO;
    private ClienteDAO clienteDAO;
    private RepVentaDAO repVentaDAO;
    private int flagCarFlaAct = 0;
    private String operacionActual = "";
    private int codigoSeleccionado = 0;

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

    public FacturaFrame() {
        setTitle("Mantenimiento de Facturas");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        facturaDAO = new FacturaDAO();
        clienteDAO = new ClienteDAO();
        repVentaDAO = new RepVentaDAO();
        initComponents();
        cargarComboBoxes();
        cargarTablaFacturas();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new FacturaFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Factura"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Primera fila
        gbc.gridx = 0; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        txtCodigo = new JTextField(10);
        txtCodigo.setEditable(false); // El código es AUTO_INCREMENT
        panelRegistro.add(txtCodigo, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        cbCliente = new JComboBox<>();
        cbCliente.setPreferredSize(new Dimension(250, cbCliente.getPreferredSize().height));
        panelRegistro.add(cbCliente, gbc);

        // Segunda fila
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Representante:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbRepresentante = new JComboBox<>();
        cbRepresentante.setPreferredSize(new Dimension(250, cbRepresentante.getPreferredSize().height));
        panelRegistro.add(cbRepresentante, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Importe:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtImporte = new JTextField(15);
        panelRegistro.add(txtImporte, gbc);

        // Tercera fila - Fecha
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Año:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0;
        spnAño = new JSpinner(new SpinnerNumberModel(Calendar.getInstance().get(Calendar.YEAR), 2000, 2100, 1));
        panelRegistro.add(spnAño, gbc);

        gbc.gridx = 2; gbc.gridy = 2;
        panelRegistro.add(new JLabel("Mes:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0;
        spnMes = new JSpinner(new SpinnerNumberModel(Calendar.getInstance().get(Calendar.MONTH) + 1, 1, 12, 1));
        panelRegistro.add(spnMes, gbc);

        // Cuarta fila
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Día:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0;
        spnDia = new JSpinner(new SpinnerNumberModel(Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 1, 31, 1));
        panelRegistro.add(spnDia, gbc);

        gbc.gridx = 2; gbc.gridy = 3;
        panelRegistro.add(new JLabel("Plazo Pago (YYYY-MM-DD):"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtPlazoPago = new JTextField(15);
        panelRegistro.add(txtPlazoPago, gbc);

        // Quinta fila
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Fecha Pago (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtFechaPago = new JTextField(15);
        panelRegistro.add(txtFechaPago, gbc);

        gbc.gridx = 2; gbc.gridy = 4; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Estado Factura:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        cbEstadoFactura = new JComboBox<>(new String[]{" 0 - Generada", " 1 - Parcial", " 2 - Completa", " 9 - Cancelada"});
        panelRegistro.add(cbEstadoFactura, gbc);

        // Sexta fila
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Estado Registro:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0;
        txtEstadoRegistro = new JTextField(5);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        // Séptima fila - Nota
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 4;
        JLabel lblNota = new JLabel("* Las fechas deben ingresarse en formato YYYY-MM-DD (ej: 2024-12-31)");
        lblNota.setFont(lblNota.getFont().deriveFont(Font.ITALIC, 10f));
        lblNota.setForeground(Color.GRAY);
        panelRegistro.add(lblNota, gbc);

        add(panelRegistro, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Facturas"));
        tableModel = new DefaultTableModel(new Object[]{"Código", "Cliente", "Representante", "Importe", "Año", "Mes", "Día", "Plazo Pago", "Fecha Pago", "Estado Reg", "Estado Fact", "Nombre Cliente", "Nombre Rep"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaFacturas = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaFacturas), BorderLayout.CENTER);

        tablaFacturas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int i = tablaFacturas.getSelectedRow();
                    if (i != -1) {
                        txtCodigo.setText(tableModel.getValueAt(i, 0).toString());
                        
                        // Seleccionar cliente por código
                        int codigoCli = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                        seleccionarEnComboBoxPorCodigo(cbCliente, codigoCli);
                        
                        // Seleccionar representante por código
                        int codigoRep = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
                        seleccionarEnComboBoxPorCodigo(cbRepresentante, codigoRep);
                        
                        txtImporte.setText(tableModel.getValueAt(i, 3).toString());
                        spnAño.setValue(Integer.parseInt(tableModel.getValueAt(i, 4).toString()));
                        spnMes.setValue(Integer.parseInt(tableModel.getValueAt(i, 5).toString()));
                        spnDia.setValue(Integer.parseInt(tableModel.getValueAt(i, 6).toString()));
                        
                        Object plazoPago = tableModel.getValueAt(i, 7);
                        txtPlazoPago.setText(plazoPago != null ? plazoPago.toString() : "");
                        
                        Object fechaPago = tableModel.getValueAt(i, 8);
                        txtFechaPago.setText(fechaPago != null ? fechaPago.toString() : "");
                        
                        txtEstadoRegistro.setText(tableModel.getValueAt(i, 9).toString());
                        cbEstadoFactura.setSelectedIndex(Integer.parseInt(tableModel.getValueAt(i, 10).toString()));
                        
                        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
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
        // Cargar clientes activos
        List<Cliente> clientes = clienteDAO.obtenerClientesActivos();
        cbCliente.removeAllItems();
        cbCliente.addItem(new ComboItem(0, "-- Seleccionar Cliente --"));
        for (Cliente cli : clientes) {
            cbCliente.addItem(new ComboItem(cli.getCliCod(), cli.getCliNom()));
        }

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
        cbCliente.setSelectedIndex(0);
        cbRepresentante.setSelectedIndex(0);
        cbEstadoFactura.setSelectedIndex(0);
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String importeStr = txtImporte.getText().trim();
        String estadoStr = txtEstadoRegistro.getText().trim();

        if (importeStr.isEmpty() || estadoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los campos Importe y Estado son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar selección de cliente
        ComboItem clienteSeleccionado = (ComboItem) cbCliente.getSelectedItem();
        if (clienteSeleccionado == null || clienteSeleccionado.getCodigo() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar selección de representante
        ComboItem representanteSeleccionado = (ComboItem) cbRepresentante.getSelectedItem();
        if (representanteSeleccionado == null || representanteSeleccionado.getCodigo() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un representante.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            BigDecimal importe = new BigDecimal(importeStr);
            int año = (Integer) spnAño.getValue();
            int mes = (Integer) spnMes.getValue();
            int dia = (Integer) spnDia.getValue();
            
            Date plazoPago = null;
            if (!txtPlazoPago.getText().trim().isEmpty()) {
                plazoPago = Date.valueOf(txtPlazoPago.getText().trim());
            }
            
            Date fechaPago = null;
            if (!txtFechaPago.getText().trim().isEmpty()) {
                fechaPago = Date.valueOf(txtFechaPago.getText().trim());
            }

            int estadoFactura = cbEstadoFactura.getSelectedIndex();

            Factura factura = new Factura();
            factura.setCliCod(clienteSeleccionado.getCodigo());
            factura.setRepCod(representanteSeleccionado.getCodigo());
            factura.setFacImp(importe);
            factura.setFacAño(año);
            factura.setFacMes(mes);
            factura.setFacDia(dia);
            factura.setFacPlazoPago(plazoPago);
            factura.setFacFechPago(fechaPago);
            factura.setFacEstReg(estadoStr.charAt(0));
            factura.setFacEstado(estadoFactura);

            boolean exito = false;
            String mensaje = "";

            switch (operacionActual) {
                case "ADICIONAR":
                    exito = facturaDAO.insertarFactura(factura);
                    mensaje = exito ? "Factura registrada con éxito." : "Error al registrar factura.";
                    break;
                case "MODIFICAR":
                    factura.setFacCod(codigoSeleccionado);
                    exito = facturaDAO.actualizarFactura(factura);
                    mensaje = exito ? "Factura modificada con éxito." : "Error al modificar factura.";
                    break;
                case "ELIMINAR":
                    exito = facturaDAO.eliminarLogicamenteFactura(codigoSeleccionado);
                    mensaje = exito ? "Factura eliminada con éxito." : "Error al eliminar factura.";
                    break;
                case "INACTIVAR":
                    exito = facturaDAO.inactivarFactura(codigoSeleccionado);
                    mensaje = exito ? "Factura inactivada con éxito." : "Error al inactivar factura.";
                    break;
                case "REACTIVAR":
                    exito = facturaDAO.reactivarFactura(codigoSeleccionado);
                    mensaje = exito ? "Factura reactivada con éxito." : "Error al reactivar factura.";
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Operación no reconocida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            if (exito) {
                JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTablaFacturas();
                comandoCancelar();
            } else {
                JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El importe debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
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

    private void cargarTablaFacturas() {
        tableModel.setRowCount(0);
        List<Factura> lista = facturaDAO.obtenerTodasFacturas();
        for (Factura f : lista) {
            // Obtener descripciones para mostrar en la tabla
            String nombreCliente = "";
            String nombreRep = "";
            
            // Buscar nombre del cliente
            Cliente cliente = clienteDAO.obtenerClientePorCodigo(f.getCliCod());
            if (cliente != null) {
                nombreCliente = cliente.getCliNom();
            }
            
            // Buscar nombre del representante
            RepVenta rep = repVentaDAO.obtenerRepresentantePorCodigo(f.getRepCod());
            if (rep != null) {
                nombreRep = rep.getRepNom();
            }
            
            tableModel.addRow(new Object[]{
                f.getFacCod(), 
                f.getCliCod(), 
                f.getRepCod(), 
                f.getFacImp(), 
                f.getFacAño(),
                f.getFacMes(),
                f.getFacDia(),
                f.getFacPlazoPago(),
                f.getFacFechPago(),
                f.getFacEstReg(),
                f.getFacEstado(),
                nombreCliente,
                nombreRep
            });
        }
    }

    private void habilitarControles(boolean b) {
        cbCliente.setEnabled(b);
        cbRepresentante.setEnabled(b);
        txtImporte.setEditable(b);
        spnAño.setEnabled(b);
        spnMes.setEnabled(b);
        spnDia.setEnabled(b);
        txtPlazoPago.setEditable(b);
        txtFechaPago.setEditable(b);
        cbEstadoFactura.setEnabled(b);
        // txtCodigo y txtEstadoRegistro siempre permanecen no editables
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        cbCliente.setSelectedIndex(0);
        cbRepresentante.setSelectedIndex(0);
        txtImporte.setText("");
        spnAño.setValue(Calendar.getInstance().get(Calendar.YEAR));
        spnMes.setValue(Calendar.getInstance().get(Calendar.MONTH) + 1);
        spnDia.setValue(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        txtPlazoPago.setText("");
        txtFechaPago.setText("");
        txtEstadoRegistro.setText("");
        cbEstadoFactura.setSelectedIndex(0);
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
        int selectedRow = tablaFacturas.getSelectedRow();
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
        int selectedRow = tablaFacturas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        cargarDatosSeleccionados(selectedRow);
        txtEstadoRegistro.setText("*");
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como eliminado ('*').\nPresione 'Actualizar' para confirmar.", "Eliminación Lógica", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaFacturas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para inactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        cargarDatosSeleccionados(selectedRow);
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
            JOptionPane.showMessageDialog(this, "Seleccione un registro para reactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        cargarDatosSeleccionados(selectedRow);
        txtEstadoRegistro.setText("A");
        habilitarControles(false);
        operacionActual = "REACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.", "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cargarDatosSeleccionados(int selectedRow) {
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        
        // Seleccionar cliente por código
        int codigoCli = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
        seleccionarEnComboBoxPorCodigo(cbCliente, codigoCli);
        
        // Seleccionar representante por código
        int codigoRep = Integer.parseInt(tableModel.getValueAt(selectedRow, 2).toString());
        seleccionarEnComboBoxPorCodigo(cbRepresentante, codigoRep);
        
        txtImporte.setText(tableModel.getValueAt(selectedRow, 3).toString());
        spnAño.setValue(Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString()));
        spnMes.setValue(Integer.parseInt(tableModel.getValueAt(selectedRow, 5).toString()));
        spnDia.setValue(Integer.parseInt(tableModel.getValueAt(selectedRow, 6).toString()));
        
        Object plazoPago = tableModel.getValueAt(selectedRow, 7);
        txtPlazoPago.setText(plazoPago != null ? plazoPago.toString() : "");
        
        Object fechaPago = tableModel.getValueAt(selectedRow, 8);
        txtFechaPago.setText(fechaPago != null ? fechaPago.toString() : "");
        
        cbEstadoFactura.setSelectedIndex(Integer.parseInt(tableModel.getValueAt(selectedRow, 10).toString()));
        
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
    }
}