package gui;

import dao.ModuloAuditoriaDAO;
import modelo.ModuloAuditoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ModuloAuditoriaFrame extends JFrame {

    private JTextField txtCodigo;
    private JTextField txtDescripcion;
    private JTextField txtEstadoRegistro;
    private JTable tablaModulos;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, btnActualizar, btnCancelar, btnSalir;

    private ModuloAuditoriaDAO moduloAuditoriaDAO;
    private int flagCarFlaAct = 0;
    private String operacionActual = "";

    public ModuloAuditoriaFrame() {
        setTitle("Mantenimiento de Módulos de Auditoría");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        moduloAuditoriaDAO = new ModuloAuditoriaDAO();
        initComponents();
        cargarTablaModulos();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ModuloAuditoriaFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Módulo de Auditoría"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        txtCodigo = new JTextField(10);
        panelRegistro.add(txtCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelRegistro.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        txtDescripcion = new JTextField(40);
        panelRegistro.add(txtDescripcion, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelRegistro.add(new JLabel("Estado Registro:"), gbc);
        gbc.gridx = 1;
        txtEstadoRegistro = new JTextField(5);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        add(panelRegistro, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Módulos de Auditoría"));
        tableModel = new DefaultTableModel(new Object[]{"Código", "Descripción", "Estado"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaModulos = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaModulos), BorderLayout.CENTER);

        tablaModulos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int i = tablaModulos.getSelectedRow();
                    txtCodigo.setText(tableModel.getValueAt(i, 0).toString());
                    txtDescripcion.setText(tableModel.getValueAt(i, 1).toString());
                    txtEstadoRegistro.setText(tableModel.getValueAt(i, 2).toString());
                    habilitarBotonesParaSeleccion();
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

    private void comandoAdicionar() {
        limpiarCampos();
        habilitarControles(true);
        txtEstadoRegistro.setText("A");
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigoStr = txtCodigo.getText().trim();
        String desc = txtDescripcion.getText().trim();
        String estadoStr = txtEstadoRegistro.getText().trim();

        if (codigoStr.isEmpty() || desc.isEmpty() || estadoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Código, descripción y estado son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int codigo;
        try {
            codigo = Integer.parseInt(codigoStr);
            if (codigo < 1) {
                JOptionPane.showMessageDialog(this, "El código debe ser mayor o igual a 1.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Código inválido. Debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ModuloAuditoria modulo = new ModuloAuditoria();
        modulo.setModAudiCod(codigo);
        modulo.setModAudiDesc(desc);
        modulo.setModAudiEstReg(estadoStr.charAt(0));

        boolean exito = false;
        String mensaje = "";

        switch (operacionActual) {
            case "ADICIONAR":
                exito = moduloAuditoriaDAO.insertarModulo(modulo);
                mensaje = exito ? "Módulo registrado con éxito." : "Error al registrar módulo.";
                break;
            case "MODIFICAR":
                exito = moduloAuditoriaDAO.actualizarModulo(modulo);
                mensaje = exito ? "Módulo modificado con éxito." : "Error al modificar módulo.";
                break;
            case "ELIMINAR":
                exito = moduloAuditoriaDAO.eliminarLogicamenteModulo(codigo);
                mensaje = exito ? "Módulo eliminado con éxito." : "Error al eliminar módulo.";
                break;
            case "INACTIVAR":
                exito = moduloAuditoriaDAO.inactivarModulo(codigo);
                mensaje = exito ? "Módulo inactivado con éxito." : "Error al inactivar módulo.";
                break;
            case "REACTIVAR":
                exito = moduloAuditoriaDAO.reactivarModulo(codigo);
                mensaje = exito ? "Módulo reactivado con éxito." : "Error al reactivar módulo.";
                break;
            default:
                JOptionPane.showMessageDialog(this, "Operación no reconocida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTablaModulos();
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
        habilitarBotonesIniciales();
    }

    private void comandoSalir() {
        dispose();
    }

    private void cargarTablaModulos() {
        tableModel.setRowCount(0);
        List<ModuloAuditoria> lista = moduloAuditoriaDAO.obtenerTodosModulos();
        for (ModuloAuditoria m : lista) {
            tableModel.addRow(new Object[]{m.getModAudiCod(), m.getModAudiDesc(), m.getModAudiEstReg()});
        }
    }

    private void habilitarControles(boolean b) {
        txtCodigo.setEditable(b);
        txtDescripcion.setEditable(b);
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtDescripcion.setText("");
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
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnInactivar.setEnabled(true);
        btnReactivar.setEnabled(true);
    }

    private void comandoModificar() {
        int selectedRow = tablaModulos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        habilitarControles(true);
        txtCodigo.setEditable(false);
        txtEstadoRegistro.setEditable(false);
        operacionActual = "MODIFICAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
    }

    private void comandoEliminar() {
        int selectedRow = tablaModulos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtDescripcion.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtEstadoRegistro.setText("*");
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como eliminado ('*').\nPresione 'Actualizar' para confirmar.", "Eliminación Lógica", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaModulos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para inactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtDescripcion.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtEstadoRegistro.setText("I");
        habilitarControles(false);
        operacionActual = "INACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como inactivo ('I').\nPresione 'Actualizar' para confirmar.", "Inactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoReactivar() {
        int selectedRow = tablaModulos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para reactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtDescripcion.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtEstadoRegistro.setText("A");
        habilitarControles(false);
        operacionActual = "REACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.", "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }
}