package gui;

import dao.OficinaDAO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.Oficina;

public class OficinaFrame extends JFrame {

    private JTextField txtCodigo;
    private JTextField txtCiudad;
    private JTextField txtRegion;
    private JTextField txtDireccion;
    private JTextField txtEmpresa;
    private JTextField txtObjetivo;
    private JTextField txtEstadoRegistro;
    private JTable tablaOficinas;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, btnActualizar, btnCancelar, btnSalir;

    private OficinaDAO oficinaDAO;
    private int flagCarFlaAct = 0;
    private String operacionActual = "";
    private int codigoSeleccionado = 0;

    public OficinaFrame() {
        setTitle("Mantenimiento de Oficinas");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        oficinaDAO = new OficinaDAO();
        initComponents();
        cargarTablaOficinas();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new OficinaFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Oficina"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Código
        gbc.gridx = 0; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        txtCodigo = new JTextField(10);
        txtCodigo.setEditable(false); // El código es AUTO_INCREMENT
        panelRegistro.add(txtCodigo, gbc);

        // Ciudad
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Ciudad:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtCiudad = new JTextField(30);
        panelRegistro.add(txtCiudad, gbc);

        // Región
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Región:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        txtRegion = new JTextField(20);
        panelRegistro.add(txtRegion, gbc);

        // Dirección
        gbc.gridx = 0; gbc.gridy = 3;
        panelRegistro.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        txtDireccion = new JTextField(40);
        panelRegistro.add(txtDireccion, gbc);

        // Empresa
        gbc.gridx = 0; gbc.gridy = 4;
        panelRegistro.add(new JLabel("Empresa:"), gbc);
        gbc.gridx = 1;
        txtEmpresa = new JTextField(30);
        panelRegistro.add(txtEmpresa, gbc);

        // Objetivo
        gbc.gridx = 0; gbc.gridy = 5;
        panelRegistro.add(new JLabel("Objetivo Ventas:"), gbc);
        gbc.gridx = 1;
        txtObjetivo = new JTextField(15);
        panelRegistro.add(txtObjetivo, gbc);

        // Estado Registro
        gbc.gridx = 0; gbc.gridy = 6;
        panelRegistro.add(new JLabel("Estado Registro:"), gbc);
        gbc.gridx = 1;
        txtEstadoRegistro = new JTextField(5);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        add(panelRegistro, BorderLayout.NORTH);

        // Panel de tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Oficinas"));
        tableModel = new DefaultTableModel(new Object[]{"Código", "Ciudad", "Región", "Dirección", "Empresa", "Objetivo", "Estado"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaOficinas = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaOficinas), BorderLayout.CENTER);

        tablaOficinas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int i = tablaOficinas.getSelectedRow();
                    txtCodigo.setText(tableModel.getValueAt(i, 0).toString());
                    txtCiudad.setText(tableModel.getValueAt(i, 1).toString());
                    txtRegion.setText(tableModel.getValueAt(i, 2).toString());
                    txtDireccion.setText(tableModel.getValueAt(i, 3).toString());
                    txtEmpresa.setText(tableModel.getValueAt(i, 4).toString());
                    txtObjetivo.setText(tableModel.getValueAt(i, 5).toString());
                    txtEstadoRegistro.setText(tableModel.getValueAt(i, 6).toString());
                    codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
                    habilitarBotonesParaSeleccion();
                }
            }
        });
        add(panelTabla, BorderLayout.CENTER);

        // Panel de botones
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

        // Eventos de botones
        btnAdicionar.addActionListener(e -> comandoAdicionar());
        btnActualizar.addActionListener(e -> comandoActualizar());
        btnCancelar.addActionListener(e -> comandoCancelar());
        btnSalir.addActionListener(e -> comandoSalir());
        btnModificar.addActionListener(e -> comandoModificar());
        btnEliminar.addActionListener(e -> comandoEliminar());
        btnInactivar.addActionListener(e -> comandoInactivar());
        btnReactivar.addActionListener(e -> comandoReactivar());
    }

    private void comandoAdicionar() {
        limpiarCampos();
        habilitarControles(true);
        txtEstadoRegistro.setText("A");
        txtObjetivo.setText("0.00");
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String ciudad = txtCiudad.getText().trim();
        String region = txtRegion.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String empresa = txtEmpresa.getText().trim();
        String objetivoStr = txtObjetivo.getText().trim();
        String estadoStr = txtEstadoRegistro.getText().trim();

        if (ciudad.isEmpty() || region.isEmpty() || estadoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ciudad, región y estado son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double objetivo;
        try {
            objetivo = Double.parseDouble(objetivoStr);
            if (objetivo < 0) {
                JOptionPane.showMessageDialog(this, "El objetivo debe ser mayor o igual a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Objetivo inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Oficina oficina = new Oficina();
        oficina.setOfiCiu(ciudad);
        oficina.setOfiReg(region);
        oficina.setOfiDir(direccion);
        oficina.setOfiEmp(empresa);
        oficina.setOfiObj(objetivo);
        oficina.setOfiEstReg(estadoStr.charAt(0));

        boolean exito = false;
        String mensaje = "";

        switch (operacionActual) {
            case "ADICIONAR":
                exito = oficinaDAO.insertarOficina(oficina);
                mensaje = exito ? "Oficina registrada con éxito." : "Error al registrar oficina.";
                break;
            case "MODIFICAR":
                oficina.setOfiCod(codigoSeleccionado);
                exito = oficinaDAO.actualizarOficina(oficina);
                mensaje = exito ? "Oficina modificada con éxito." : "Error al modificar oficina.";
                break;
            case "ELIMINAR":
                exito = oficinaDAO.eliminarLogicamenteOficina(codigoSeleccionado);
                mensaje = exito ? "Oficina eliminada con éxito." : "Error al eliminar oficina.";
                break;
            case "INACTIVAR":
                exito = oficinaDAO.inactivarOficina(codigoSeleccionado);
                mensaje = exito ? "Oficina inactivada con éxito." : "Error al inactivar oficina.";
                break;
            case "REACTIVAR":
                exito = oficinaDAO.reactivarOficina(codigoSeleccionado);
                mensaje = exito ? "Oficina reactivada con éxito." : "Error al reactivar oficina.";
                break;
            default:
                JOptionPane.showMessageDialog(this, "Operación no reconocida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTablaOficinas();
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
        habilitarBotonesIniciales();
    }

    private void comandoSalir() {
        dispose();
    }

    private void cargarTablaOficinas() {
        tableModel.setRowCount(0);
        List<Oficina> lista = oficinaDAO.obtenerTodasOficinas();
        for (Oficina o : lista) {
            tableModel.addRow(new Object[]{
                o.getOfiCod(), 
                o.getOfiCiu(), 
                o.getOfiReg(), 
                o.getOfiDir(), 
                o.getOfiEmp(), 
                o.getOfiObj(), 
                o.getOfiEstReg()
            });
        }
    }

    private void habilitarControles(boolean b) {
        txtCiudad.setEditable(b);
        txtRegion.setEditable(b);
        txtDireccion.setEditable(b);
        txtEmpresa.setEditable(b);
        txtObjetivo.setEditable(b);
        // txtCodigo y txtEstadoRegistro siempre permanecen no editables
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtCiudad.setText("");
        txtRegion.setText("");
        txtDireccion.setText("");
        txtEmpresa.setText("");
        txtObjetivo.setText("");
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
        int selectedRow = tablaOficinas.getSelectedRow();
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
        int selectedRow = tablaOficinas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtCiudad.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtRegion.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtDireccion.setText(tableModel.getValueAt(selectedRow, 3).toString());
        txtEmpresa.setText(tableModel.getValueAt(selectedRow, 4).toString());
        txtObjetivo.setText(tableModel.getValueAt(selectedRow, 5).toString());
        txtEstadoRegistro.setText("*");
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como eliminado ('*').\nPresione 'Actualizar' para confirmar.", "Eliminación Lógica", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaOficinas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para inactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtCiudad.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtRegion.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtDireccion.setText(tableModel.getValueAt(selectedRow, 3).toString());
        txtEmpresa.setText(tableModel.getValueAt(selectedRow, 4).toString());
        txtObjetivo.setText(tableModel.getValueAt(selectedRow, 5).toString());
        txtEstadoRegistro.setText("I");
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        habilitarControles(false);
        operacionActual = "INACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como inactivo ('I').\nPresione 'Actualizar' para confirmar.", "Inactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoReactivar() {
        int selectedRow = tablaOficinas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para reactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtCiudad.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtRegion.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtDireccion.setText(tableModel.getValueAt(selectedRow, 3).toString());
        txtEmpresa.setText(tableModel.getValueAt(selectedRow, 4).toString());
        txtObjetivo.setText(tableModel.getValueAt(selectedRow, 5).toString());
        txtEstadoRegistro.setText("A");
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        habilitarControles(false);
        operacionActual = "REACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.", "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }
}