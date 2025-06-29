package gui;

import dao.RepVentaDAO;
import dao.OficinaDAO;
import dao.CargoDAO;
import modelo.Oficina;
import modelo.Cargo;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.RepVenta;

public class RepVentaFrame extends JFrame {

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtEdad;
    private JComboBox<ComboItem> cbOficinaCodigo;
    private JComboBox<ComboItem> cbCargoCodigo;
    private JTextField txtFechaContratacion;
    private JTextField txtEstadoRegistro;
    private JTable tablaRepresentantes;
    private DefaultTableModel tableModel;

    
    private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, btnActualizar, btnCancelar, btnSalir;

    private RepVentaDAO repVentaDAO;
    private OficinaDAO oficinaDAO;
    private CargoDAO cargoDAO;
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

    public RepVentaFrame() {
        setTitle("Mantenimiento de Representantes de Venta");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        repVentaDAO = new RepVentaDAO();
        oficinaDAO = new OficinaDAO();
        cargoDAO = new CargoDAO();
        initComponents();
        cargarComboBoxes();
        cargarTablaRepresentantes();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new RepVentaFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Representante de Venta"));
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
        panelRegistro.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtNombre = new JTextField(30);
        panelRegistro.add(txtNombre, gbc);

        // Segunda fila
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Edad:"), gbc);
        gbc.gridx = 1;
        txtEdad = new JTextField(10);
        panelRegistro.add(txtEdad, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        panelRegistro.add(new JLabel("Oficina:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        cbOficinaCodigo = new JComboBox<>();
        cbOficinaCodigo.setPreferredSize(new Dimension(250, cbOficinaCodigo.getPreferredSize().height));
        panelRegistro.add(cbOficinaCodigo, gbc);

        // Tercera fila
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Cargo:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        cbCargoCodigo = new JComboBox<>();
        cbCargoCodigo.setPreferredSize(new Dimension(250, cbCargoCodigo.getPreferredSize().height));
        panelRegistro.add(cbCargoCodigo, gbc);

        // Cuarta fila
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Fecha Contratación:"), gbc);
        gbc.gridx = 1;
        txtFechaContratacion = new JTextField(15);
        panelRegistro.add(txtFechaContratacion, gbc);

        gbc.gridx = 2; gbc.gridy = 3;
        panelRegistro.add(new JLabel("Estado Registro:"), gbc);
        gbc.gridx = 3;
        txtEstadoRegistro = new JTextField(5);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        // Quinta fila - Nota sobre formato de fecha
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        JLabel lblFormatoFecha = new JLabel("* Formato de fecha: yyyy-MM-dd (mayor a 2024-01-15)");
        lblFormatoFecha.setFont(lblFormatoFecha.getFont().deriveFont(Font.ITALIC, 10f));
        lblFormatoFecha.setForeground(Color.GRAY);
        panelRegistro.add(lblFormatoFecha, gbc);

        add(panelRegistro, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Representantes de Venta"));
        tableModel = new DefaultTableModel(new Object[]{"Código", "Nombre", "Edad", "Oficina", "Cargo", "Fecha Contrat.", "Estado", "Ciudad Oficina", "Descripción Cargo"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaRepresentantes = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaRepresentantes), BorderLayout.CENTER);

        tablaRepresentantes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int i = tablaRepresentantes.getSelectedRow();
                    if (i != -1) {
                        txtCodigo.setText(tableModel.getValueAt(i, 0).toString());
                        txtNombre.setText(tableModel.getValueAt(i, 1).toString());
                        txtEdad.setText(tableModel.getValueAt(i, 2).toString());
                        
                        // Seleccionar en ComboBox por código de oficina
                        int codigoOficina = Integer.parseInt(tableModel.getValueAt(i, 3).toString());
                        seleccionarEnComboBoxPorCodigo(cbOficinaCodigo, codigoOficina);
                        
                        // Seleccionar en ComboBox por código de cargo
                        int codigoCargo = Integer.parseInt(tableModel.getValueAt(i, 4).toString());
                        seleccionarEnComboBoxPorCodigo(cbCargoCodigo, codigoCargo);
                        
                        txtFechaContratacion.setText(tableModel.getValueAt(i, 5).toString());
                        txtEstadoRegistro.setText(tableModel.getValueAt(i, 6).toString());
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
        // Cargar oficinas
        List<Oficina> oficinas = oficinaDAO.obtenerOficinasActivas();
        cbOficinaCodigo.removeAllItems();
        cbOficinaCodigo.addItem(new ComboItem(0, "-- Seleccionar Oficina --"));
        for (Oficina oficina : oficinas) {
            cbOficinaCodigo.addItem(new ComboItem(oficina.getOfiCod(), oficina.getOfiCiu()));
        }

        // Cargar cargos
        List<Cargo> cargos = cargoDAO.obtenerCargosActivos();
        cbCargoCodigo.removeAllItems();
        cbCargoCodigo.addItem(new ComboItem(0, "-- Seleccionar Cargo --"));
        for (Cargo cargo : cargos) {
            cbCargoCodigo.addItem(new ComboItem(cargo.getCarCod(), cargo.getCarDesc()));
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
        cbOficinaCodigo.setSelectedIndex(0);
        cbCargoCodigo.setSelectedIndex(0);
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = txtNombre.getText().trim();
        String edadStr = txtEdad.getText().trim();
        String fechaStr = txtFechaContratacion.getText().trim();
        String estadoStr = txtEstadoRegistro.getText().trim();

        if (nombre.isEmpty() || edadStr.isEmpty() || fechaStr.isEmpty() || estadoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar selección de oficina
        ComboItem oficinaSeleccionada = (ComboItem) cbOficinaCodigo.getSelectedItem();
        if (oficinaSeleccionada == null || oficinaSeleccionada.getCodigo() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una oficina.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar selección de cargo
        ComboItem cargoSeleccionado = (ComboItem) cbCargoCodigo.getSelectedItem();
        if (cargoSeleccionado == null || cargoSeleccionado.getCodigo() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cargo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int edad, ofiCodigo, carCodigo;
        LocalDate fechaContratacion;
        
        try {
            edad = Integer.parseInt(edadStr);
            fechaContratacion = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "La fecha debe estar en formato yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener códigos de los ComboBox
        ofiCodigo = oficinaSeleccionada.getCodigo();
        carCodigo = cargoSeleccionado.getCodigo();

        RepVenta rep = new RepVenta();
        rep.setRepNom(nombre);
        rep.setRepEdad(edad);
        rep.setOfiCod(ofiCodigo);
        rep.setCarCod(carCodigo);
        rep.setRepCon(fechaContratacion);
        rep.setRepEstReg(estadoStr.charAt(0));

        boolean exito = false;
        String mensaje = "";

        switch (operacionActual) {
            case "ADICIONAR":
                String mensajeErrorAdi = repVentaDAO.insertarRepresentante(rep);
                if (mensajeErrorAdi == null) {
                    // La inserción fue exitosa
                    exito= true;
                    mensaje = "Representante registrado con éxito.";
                } else {
                    exito = false;
                    mensaje = mensajeErrorAdi;
                }
                break;
            case "MODIFICAR":
                rep.setRepCod(codigoSeleccionado);
                String mensajeErrorMod = repVentaDAO.actualizarRepresentante(rep);
                if (mensajeErrorMod == null) {
                    exito = true;
                    mensaje = "Representante modificado con éxito.";
                } else {
                    exito = false;
                    mensaje = mensajeErrorMod;
                }
                break;
            case "ELIMINAR":
                exito = repVentaDAO.eliminarLogicamenteRepresentante(codigoSeleccionado);
                mensaje = exito ? "Representante eliminado con éxito." : "Error al eliminar representante.";
                break;
            case "INACTIVAR":
                exito = repVentaDAO.inactivarRepresentante(codigoSeleccionado);
                mensaje = exito ? "Representante inactivado con éxito." : "Error al inactivar representante.";
                break;
            case "REACTIVAR":
                exito = repVentaDAO.reactivarRepresentante(codigoSeleccionado);
                mensaje = exito ? "Representante reactivado con éxito." : "Error al reactivar representante.";
                break;
            default:
                JOptionPane.showMessageDialog(this, "Operación no reconocida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTablaRepresentantes();
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

    private void cargarTablaRepresentantes() {
        tableModel.setRowCount(0);
        List<RepVenta> lista = repVentaDAO.obtenerTodosRepresentantes();
        for (RepVenta r : lista) {
            tableModel.addRow(new Object[]{
                r.getRepCod(), 
                r.getRepNom(), 
                r.getRepEdad(), 
                r.getOfiCod(), 
                r.getCarCod(), 
                r.getRepCon().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), 
                r.getRepEstReg(),
                r.getOficinaDescripcion() != null ? r.getOficinaDescripcion() : "",
                r.getCargoDescripcion() != null ? r.getCargoDescripcion() : ""
            });
        }
    }

    private void habilitarControles(boolean b) {
        txtNombre.setEditable(b);
        txtEdad.setEditable(b);
        cbOficinaCodigo.setEnabled(b);
        cbCargoCodigo.setEnabled(b);
        txtFechaContratacion.setEditable(b);
        // txtCodigo y txtEstadoRegistro siempre permanecen no editables
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtEdad.setText("");
        cbOficinaCodigo.setSelectedIndex(0);
        cbCargoCodigo.setSelectedIndex(0);
        txtFechaContratacion.setText("");
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
        int selectedRow = tablaRepresentantes.getSelectedRow();
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
        int selectedRow = tablaRepresentantes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtNombre.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtEdad.setText(tableModel.getValueAt(selectedRow, 2).toString());
        
        // Seleccionar en ComboBox por código
        int codigoOficina = Integer.parseInt(tableModel.getValueAt(selectedRow, 3).toString());
        seleccionarEnComboBoxPorCodigo(cbOficinaCodigo, codigoOficina);
        
        int codigoCargo = Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString());
        seleccionarEnComboBoxPorCodigo(cbCargoCodigo, codigoCargo);
        
        txtFechaContratacion.setText(tableModel.getValueAt(selectedRow, 5).toString());
        txtEstadoRegistro.setText("*");
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como eliminado ('*').\nPresione 'Actualizar' para confirmar.", "Eliminación Lógica", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaRepresentantes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para inactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtNombre.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtEdad.setText(tableModel.getValueAt(selectedRow, 2).toString());
        
        // Seleccionar en ComboBox por código
        int codigoOficina = Integer.parseInt(tableModel.getValueAt(selectedRow, 3).toString());
        seleccionarEnComboBoxPorCodigo(cbOficinaCodigo, codigoOficina);
        
        int codigoCargo = Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString());
        seleccionarEnComboBoxPorCodigo(cbCargoCodigo, codigoCargo);
        
        txtFechaContratacion.setText(tableModel.getValueAt(selectedRow, 5).toString());
        txtEstadoRegistro.setText("I");
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        habilitarControles(false);
        operacionActual = "INACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como inactivo ('I').\nPresione 'Actualizar' para confirmar.", "Inactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoReactivar() {
        int selectedRow = tablaRepresentantes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para reactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtNombre.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtEdad.setText(tableModel.getValueAt(selectedRow, 2).toString());
        
        // Seleccionar en ComboBox por código
        int codigoOficina = Integer.parseInt(tableModel.getValueAt(selectedRow, 3).toString());
        seleccionarEnComboBoxPorCodigo(cbOficinaCodigo, codigoOficina);
        
        int codigoCargo = Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString());
        seleccionarEnComboBoxPorCodigo(cbCargoCodigo, codigoCargo);
        
        txtFechaContratacion.setText(tableModel.getValueAt(selectedRow, 5).toString());
        txtEstadoRegistro.setText("A");
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        habilitarControles(false);
        operacionActual = "REACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.", "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }
}