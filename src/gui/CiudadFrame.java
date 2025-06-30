package gui;

import dao.CiudadDAO;
import dao.RegionDAO;
import modelo.Ciudad;
import modelo.Region;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

public class CiudadFrame extends JFrame{

    private JTextField txtCodigo;
    private JComboBox<String> cmbRegion;
    private JTextField txtNombre;
    private JTextField txtEstadoRegistro;
    private JTable tablaCiudades;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, btnActualizar, btnCancelar, btnSalir;

    private CiudadDAO ciudadDAO;
    private RegionDAO regionDAO;
    private int flagCarFlaAct = 0;
    private String operacionActual = "";

    private List<Region> listaRegiones;

    public CiudadFrame() {
        setTitle("Mantenimiento de Ciudad");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        ciudadDAO = new CiudadDAO();
        regionDAO = new RegionDAO();
        initComponents();
        cargarRegionesEnComboBox();
        cargarTablaCiudades();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CiudadFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel de Registro
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Ciudad"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtCodigo = new JTextField(10);
        txtCodigo.setEditable(false);
        panelRegistro.add(txtCodigo, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Región:"), gbc);
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 1.0;
        cmbRegion = new JComboBox<>();
        panelRegistro.add(cmbRegion, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        txtNombre = new JTextField(50);
        panelRegistro.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Estado Registro:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        txtEstadoRegistro = new JTextField(1);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        add(panelRegistro, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Ciudades"));
        tableModel = new DefaultTableModel(new Object[]{"Código Ciu.", "Cód. Región", "Nombre Ciu.", "Estado"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCiudades = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaCiudades), BorderLayout.CENTER);

        tablaCiudades.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int i = tablaCiudades.getSelectedRow();
                    if (i != -1) {
                        txtCodigo.setText(tableModel.getValueAt(i, 0).toString());
                        int regCod = Integer.parseInt(tableModel.getValueAt(i, 1));
                        String regNom = getRegionNombre(regCod);
                        cmbRegion.setSelectedItem(regNom);

                        txtNombre.setText(tableModel.getValueAt(i, 2).toString());
                        txtEstadoRegistro.setText(tableModel.getValueAt(i, 3).toString());
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

    private void cargarRegionesEnComboBox() {
        listaRegiones = regionDAO.obtenerTodasRegiones();
        cmbRegion.removeAllItems();
        for (Region reg : listaRegiones) {
            cmbRegion.addItem(reg.getRegNom() + " (ID: " + reg.getRegCod() + ")");
        }
    }

    private int getRegionCodigo(String nombreConId) {
        if (nombreConId == null || nombreConId.isEmpty()) return -1;
        try {
            int startIndex = nombreConId.indexOf("(ID: ") + 5;
            int endIndex = nombreConId.indexOf(")", startIndex);
            return Integer.parseInt(nombreConId.substring(startIndex, endIndex));
        } catch (Exception e) {
            System.err.println("Error al parsear ID de región: " + e.getMessage());
            return -1;
        }
    }

    private String getRegionNombre(int codigo) {
        for (Region reg : listaRegiones) {
            if (reg.getRegCod() == codigo) {
                return reg.getRegNom() + " (ID: " + reg.getRegCod() + ")";
            }
        }
        return "";
    }

    private void comandoAdicionar() {
        limpiarCampos();
        habilitarControles(true);
        txtEstadoRegistro.setText("A");
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        cmbRegion.requestFocusInWindow();
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreCiudad = txtNombre.getText().trim();
        String estadoRegistro = txtEstadoRegistro.getText().trim();
        int regCod = getRegionCodigo(cmbRegion.getSelectedItem().toString());

        if (regCod == -1 || nombreCiudad.isEmpty() || estadoRegistro.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios. Asegúrese de seleccionar una región válida.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!estadoRegistro.matches("A|I|\\*")) {
             JOptionPane.showMessageDialog(this, "El Estado de Registro solo puede ser 'A', 'I' o '*'.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
             return;
        }

        Ciudad ciudad = new Ciudad();
        ciudad.setRegCod(regCod);
        ciudad.setCiuNom(nombreCiudad);
        ciudad.setCiuEstReg(estadoRegistro.charAt(0));

        boolean exito = false;
        String mensaje = "";

        switch (operacionActual) {
            case "ADICIONAR":
                exito = ciudadDAO.insertarCiudad(ciudad);
                mensaje = exito ? "Ciudad adicionada con éxito." : "Error al adicionar ciudad.";
                break;
            case "MODIFICAR":
                ciudad.setCiuCod(Integer.parseInt(txtCodigo.getText()));
                exito = ciudadDAO.actualizarCiudad(ciudad);
                mensaje = exito ? "Ciudad modificada con éxito." : "Error al modificar ciudad.";
                break;
            case "ELIMINAR":
                int confirmDelete = JOptionPane.showConfirmDialog(this, "¿Está seguro de marcar este registro como ELIMINADO LÓGICAMENTE ('*')?", "Confirmar Eliminación Lógica", JOptionPane.YES_NO_OPTION);
                if (confirmDelete == JOptionPane.YES_OPTION) {
                    exito = ciudadDAO.eliminarLogicamenteCiudad(Integer.parseInt(txtCodigo.getText()));
                    mensaje = exito ? "Ciudad eliminada lógicamente con éxito." : "Error al eliminar lógicamente ciudad.";
                } else {
                    mensaje = "Operación de eliminación cancelada.";
                    exito = true;
                }
                break;
            case "INACTIVAR":
                exito = ciudadDAO.inactivarCiudad(Integer.parseInt(txtCodigo.getText()));
                mensaje = exito ? "Ciudad inactivada con éxito." : "Error al inactivar ciudad.";
                break;
            case "REACTIVAR":
                exito = ciudadDAO.reactivarCiudad(Integer.parseInt(txtCodigo.getText()));
                mensaje = exito ? "Ciudad reactivada con éxito." : "Error al reactivar ciudad.";
                break;
            default:
                JOptionPane.showMessageDialog(this, "Operación no reconocida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            habilitarControles(false);
            cargarTablaCiudades();
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
        tablaCiudades.clearSelection();
    }

    private void comandoSalir() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea salir del mantenimiento de Ciudades?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private void cargarTablaCiudades() {
        tableModel.setRowCount(0);
        List<Ciudad> ciudades = ciudadDAO.obtenerTodasCiudades();
        for (Ciudad ciu : ciudades) {
            tableModel.addRow(new Object[]{
                ciu.getCiuCod(),
                ciu.getRegCod(),
                ciu.getCiuNom(),
                ciu.getCiuEstReg()
            });
        }
    }

    private void habilitarControles(boolean b) {
        txtCodigo.setEditable(false);
        cmbRegion.setEnabled(b);
        txtNombre.setEditable(b);
        txtEstadoRegistro.setEditable(false);
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        cmbRegion.setSelectedIndex(0);
        txtNombre.setText("");
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

    private void comandoModificar(){
        int selectedRow = tablaCiudades.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        habilitarControles(true);
        txtCodigo.setEditable(false);
        cmbRegion.setEnabled(true);
        txtEstadoRegistro.setEditable(false);
        operacionActual = "MODIFICAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        txtNombre.requestFocusInWindow();
    }

    private void comandoEliminar() {
        int selectedRow = tablaCiudades.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        cmbRegion.setSelectedItem(getRegionNombre((int)tableModel.getValueAt(selectedRow, 1)));
        txtNombre.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtEstadoRegistro.setText("*");
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará para eliminación lógica ('*').\nPresione 'Actualizar' para confirmar.", "Eliminación Lógica", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaCiudades.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para inactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        cmbRegion.setSelectedItem(getRegionNombre((int)tableModel.getValueAt(selectedRow, 1)));
        txtNombre.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtEstadoRegistro.setText("I");
        habilitarControles(false);
        operacionActual = "INACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como inactivo ('I').\nPresione 'Actualizar' para confirmar.", "Inactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoReactivar() {
        int selectedRow = tablaCiudades.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para reactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        cmbRegion.setSelectedItem(getRegionNombre((int)tableModel.getValueAt(selectedRow, 1)));
        txtNombre.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtEstadoRegistro.setText("A");
        habilitarControles(false);
        operacionActual = "REACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.", "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }
}
