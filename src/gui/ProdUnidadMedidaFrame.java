package gui;

import dao.ProdUnidadMedidaDAO;
import modelo.ProdUnidadMedida;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ProdUnidadMedidaFrame extends JFrame {

    private JTextField txtCodigo;
    private JTextField txtDescripcion;
    private JTextField txtEstadoRegistro;
    private JTable tablaUnidades;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnInactivar;
    private JButton btnReactivar;
    private JButton btnActualizar;
    private JButton btnCancelar;
    private JButton btnSalir;

    private ProdUnidadMedidaDAO prodUnidadMedidaDAO;
    private int flagCarFlaAct = 0;
    private String operacionActual = "";

    public ProdUnidadMedidaFrame() {
        setTitle("Mantenimiento de Unidad de Medida de Producto");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        prodUnidadMedidaDAO = new ProdUnidadMedidaDAO();
        initComponents();
        cargarTablaUnidades();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProdUnidadMedidaFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Unidad de Medida"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtCodigo = new JTextField(15);
        panelRegistro.add(txtCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtDescripcion = new JTextField(30);
        panelRegistro.add(txtDescripcion, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Estado Registro:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        txtEstadoRegistro = new JTextField(5);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        add(panelRegistro, BorderLayout.NORTH);


        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Unidades de Medida de Producto"));
        tableModel = new DefaultTableModel(new Object[]{"Código", "Descripción", "Estado"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaUnidades = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaUnidades), BorderLayout.CENTER);

        tablaUnidades.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int selectedRow = tablaUnidades.getSelectedRow();
                    if (selectedRow != -1) {
                        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
                        txtDescripcion.setText(tableModel.getValueAt(selectedRow, 1).toString());
                        txtEstadoRegistro.setText(tableModel.getValueAt(selectedRow, 2).toString());
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
        txtCodigo.setEditable(true);
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        txtCodigo.requestFocusInWindow();
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigo = txtCodigo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String estadoRegistroStr = txtEstadoRegistro.getText().trim();

        // Validar campos vacíos
        if (codigo.isEmpty() || descripcion.isEmpty() || estadoRegistroStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos (Código, Descripción, Estado Registro) son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar formato del código
        if (operacionActual.equals("ADICIONAR") || operacionActual.equals("MODIFICAR")) {
            if (!codigo.matches("UND|KG|ML")) {
                JOptionPane.showMessageDialog(this, "El Código solo puede ser 'UND', 'KG' o 'ML'.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Validar estado
        if (!estadoRegistroStr.matches("A|I|\\*")) {
            JOptionPane.showMessageDialog(this, "El Estado de Registro solo puede ser 'A' (Activo), 'I' (Inactivo) o '*' (Eliminado Lógicamente).", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        char estadoRegistro = estadoRegistroStr.charAt(0); // solo cuando ya esté validado

        ProdUnidadMedida unidad = new ProdUnidadMedida(codigo, descripcion, estadoRegistro);
        boolean exito = false;
        String mensaje = "";

        switch (operacionActual) {
            case "ADICIONAR":
                exito = prodUnidadMedidaDAO.insertarUnidad(unidad);
                mensaje = exito ? "Unidad de medida adicionada con éxito." : "Error al adicionar unidad de medida. Revise el código o restricciones.";
                break;
            case "MODIFICAR":
                exito = prodUnidadMedidaDAO.actualizarUnidad(unidad);
                mensaje = exito ? "Unidad de medida modificada con éxito." : "Error al modificar unidad de medida.";
                break;
            case "ELIMINAR":
                int confirmDelete = JOptionPane.showConfirmDialog(this, "¿Está seguro de marcar este registro como ELIMINADO LÓGICAMENTE ('*')?", "Confirmar Eliminación Lógica", JOptionPane.YES_NO_OPTION);
                if (confirmDelete == JOptionPane.YES_OPTION) {
                    exito = prodUnidadMedidaDAO.eliminarLogicamenteUnidad(codigo);
                    mensaje = exito ? "Unidad de medida eliminada lógicamente con éxito." : "Error al eliminar lógicamente unidad de medida.";
                } else {
                    mensaje = "Operación de eliminación cancelada.";
                    exito = true;
                }
                break;
            case "INACTIVAR":
                exito = prodUnidadMedidaDAO.inactivarUnidad(codigo);
                mensaje = exito ? "Unidad de medida inactivada con éxito." : "Error al inactivar unidad de medida.";
                break;
            case "REACTIVAR":
                exito = prodUnidadMedidaDAO.reactivarUnidad(codigo);
                mensaje = exito ? "Unidad de medida reactivada con éxito." : "Error al reactivar unidad de medida.";
                break;
            default:
                JOptionPane.showMessageDialog(this, "Operación no reconocida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            habilitarControles(false);
            cargarTablaUnidades();
            flagCarFlaAct = 0;
            operacionActual = "";
            habilitarBotonesIniciales();
        } else {
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void comandoCancelar() {
        limpiarCampos();
        habilitarControles(false);
        flagCarFlaAct = 0;
        operacionActual = "";
        habilitarBotonesIniciales();
        tablaUnidades.clearSelection();
    }

    private void comandoModificar() {
        int selectedRow = tablaUnidades.getSelectedRow();
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
        txtDescripcion.requestFocusInWindow();
    }

    private void comandoEliminar() {
        int selectedRow = tablaUnidades.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtDescripcion.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtEstadoRegistro.setText("*");
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará para eliminación lógica ('*').\nPresione 'Actualizar' para confirmar.", "Eliminación Lógica", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaUnidades.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para inactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
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
        int selectedRow = tablaUnidades.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para reactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
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

    private void cargarTablaUnidades() {
        tableModel.setRowCount(0);
        List<ProdUnidadMedida> unidades = prodUnidadMedidaDAO.obtenerTodasUnidades();
        for (ProdUnidadMedida um : unidades) {
            tableModel.addRow(new Object[]{um.getUniMedProCod(), um.getUniMedProDesc(), um.getUniMedProEstReg()});
        }
    }

    private void habilitarControles(boolean habilitar) {
        txtCodigo.setEditable(habilitar);
        txtDescripcion.setEditable(habilitar);
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtDescripcion.setText("");
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

    private void comandoSalir() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea salir del mantenimiento de Unidades de Medida de Producto?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

}
