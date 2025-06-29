package gui;

import dao.AuditoriaDAO;
import dao.UsuarioSistemaDAO;
import dao.ModuloAuditoriaDAO;
import modelo.Auditoria;
import modelo.UsuarioSistema;
import modelo.ModuloAuditoria;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AuditoriaFrame extends JFrame {

    private JTextField txtCodigo;
    private JComboBox<ComboItem> cbUsuarioCodigo;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTextField txtDescripcion;
    private JTextArea txtDetalle;
    private JComboBox<ComboItem> cbModuloCodigo;
    private JTextField txtEstadoRegistro;
    private JTable tablaAuditorias;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, btnActualizar, btnCancelar, btnSalir;

    private AuditoriaDAO auditoriaDAO;
    private UsuarioSistemaDAO usuarioDAO;
    private ModuloAuditoriaDAO moduloAuditoriaDAO;
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

    public AuditoriaFrame() {
        setTitle("Mantenimiento de Auditorías");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        auditoriaDAO = new AuditoriaDAO();
        usuarioDAO = new UsuarioSistemaDAO();
        moduloAuditoriaDAO = new ModuloAuditoriaDAO();
        initComponents();
        cargarComboBoxes();
        cargarTablaAuditorias();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new AuditoriaFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Auditoría"));
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
        panelRegistro.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        cbUsuarioCodigo = new JComboBox<>();
        cbUsuarioCodigo.setPreferredSize(new Dimension(250, cbUsuarioCodigo.getPreferredSize().height));
        panelRegistro.add(cbUsuarioCodigo, gbc);

        // Segunda fila
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        txtFecha = new JTextField(15);
        panelRegistro.add(txtFecha, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        panelRegistro.add(new JLabel("Hora:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtHora = new JTextField(15);
        panelRegistro.add(txtHora, gbc);

        // Tercera fila
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtDescripcion = new JTextField();
        panelRegistro.add(txtDescripcion, gbc);

        // Cuarta fila
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Detalle:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        txtDetalle = new JTextArea(3, 20);
        txtDetalle.setLineWrap(true);
        txtDetalle.setWrapStyleWord(true);
        JScrollPane scrollDetalle = new JScrollPane(txtDetalle);
        scrollDetalle.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panelRegistro.add(scrollDetalle, gbc);

        // Quinta fila
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelRegistro.add(new JLabel("Módulo:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1.0;
        cbModuloCodigo = new JComboBox<>();
        cbModuloCodigo.setPreferredSize(new Dimension(250, cbModuloCodigo.getPreferredSize().height));
        panelRegistro.add(cbModuloCodigo, gbc);

        gbc.gridx = 3; gbc.gridwidth = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0;
        txtEstadoRegistro = new JTextField(5);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        // Sexta fila - Notas sobre formato
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 4;
        JLabel lblFormatos = new JLabel("* Formato fecha: yyyy-MM-dd | Formato hora: HH:mm:ss");
        lblFormatos.setFont(lblFormatos.getFont().deriveFont(Font.ITALIC, 10f));
        lblFormatos.setForeground(Color.GRAY);
        panelRegistro.add(lblFormatos, gbc);

        add(panelRegistro, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Auditorías"));
        tableModel = new DefaultTableModel(new Object[]{"Código", "Usuario", "Fecha", "Hora", "Descripción", "Módulo", "Estado", "Nombre Usuario", "Descripción Módulo"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaAuditorias = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaAuditorias), BorderLayout.CENTER);

        tablaAuditorias.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int i = tablaAuditorias.getSelectedRow();
                    if (i != -1) {
                        txtCodigo.setText(tableModel.getValueAt(i, 0).toString());
                        
                        // Seleccionar en ComboBox por código de usuario
                        int codigoUsuario = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                        seleccionarEnComboBoxPorCodigo(cbUsuarioCodigo, codigoUsuario);
                        
                        txtFecha.setText(tableModel.getValueAt(i, 2).toString());
                        txtHora.setText(tableModel.getValueAt(i, 3).toString());
                        txtDescripcion.setText(tableModel.getValueAt(i, 4).toString());
                        
                        // Obtener el detalle completo del objeto
                        int codigoAuditoria = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
                        Auditoria auditoriaCompleta = auditoriaDAO.obtenerAuditoriaPorCodigo(codigoAuditoria);
                        if (auditoriaCompleta != null) {
                            txtDetalle.setText(auditoriaCompleta.getAudiDet() != null ? auditoriaCompleta.getAudiDet() : "");
                        }
                        
                        // Seleccionar en ComboBox por código de módulo
                        int codigoModulo = Integer.parseInt(tableModel.getValueAt(i, 5).toString());
                        seleccionarEnComboBoxPorCodigo(cbModuloCodigo, codigoModulo);
                        
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
        // Cargar usuarios (asumiendo que tienes un método similar en UsuarioDAO)
        try {
            List<UsuarioSistema> usuarios = usuarioDAO.obtenerTodosUsuarios();
            cbUsuarioCodigo.removeAllItems();
            cbUsuarioCodigo.addItem(new ComboItem(0, "-- Seleccionar Usuario --"));
            for (UsuarioSistema usuario : usuarios) {
                cbUsuarioCodigo.addItem(new ComboItem(usuario.getUsuCod(), usuario.getUsuNom()));
            }
        } catch (Exception e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
        }

        // Cargar módulos de auditoría (asumiendo que tienes un método similar en ModuloAuditoriaDAO)
        try {
            List<ModuloAuditoria> modulos = moduloAuditoriaDAO.obtenerTodosModulos();
            cbModuloCodigo.removeAllItems();
            cbModuloCodigo.addItem(new ComboItem(0, "-- Seleccionar Módulo --"));
            for (ModuloAuditoria modulo : modulos) {
                cbModuloCodigo.addItem(new ComboItem(modulo.getModAudiCod(), modulo.getModAudiDesc()));
            }
        } catch (Exception e) {
            System.err.println("Error al cargar módulos de auditoría: " + e.getMessage());
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
        cbUsuarioCodigo.setSelectedIndex(0);
        cbModuloCodigo.setSelectedIndex(0);
        
        // Establecer fecha y hora actuales por defecto
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();
        txtFecha.setText(fechaActual.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        txtHora.setText(horaActual.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String descripcion = txtDescripcion.getText().trim();
        String detalle = txtDetalle.getText().trim();
        String fechaStr = txtFecha.getText().trim();
        String horaStr = txtHora.getText().trim();
        String estadoStr = txtEstadoRegistro.getText().trim();

        if (descripcion.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty() || estadoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los campos descripción, fecha, hora y estado son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar selección de usuario
        ComboItem usuarioSeleccionado = (ComboItem) cbUsuarioCodigo.getSelectedItem();
        if (usuarioSeleccionado == null || usuarioSeleccionado.getCodigo() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar selección de módulo
        ComboItem moduloSeleccionado = (ComboItem) cbModuloCodigo.getSelectedItem();
        if (moduloSeleccionado == null || moduloSeleccionado.getCodigo() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un módulo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date fecha;
        Time hora;
        
        try {
            LocalDate localDate = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            fecha = Date.valueOf(localDate);
            
            LocalTime localTime = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
            hora = Time.valueOf(localTime);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "La fecha debe estar en formato yyyy-MM-dd y la hora en formato HH:mm:ss.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Auditoria auditoria = new Auditoria();
        auditoria.setUsuCod(usuarioSeleccionado.getCodigo());
        auditoria.setAudiFecha(fecha);
        auditoria.setAudiHora(hora);
        auditoria.setAudiDescri(descripcion);
        auditoria.setAudiDet(detalle);
        auditoria.setModAudiCod(moduloSeleccionado.getCodigo());
        auditoria.setAudiEstReg(estadoStr.charAt(0));

        boolean exito = false;
        String mensaje = "";

        switch (operacionActual) {
            case "ADICIONAR":
                String mensajeErrorAdi = auditoriaDAO.insertarAuditoria(auditoria);
                if (mensajeErrorAdi == null) {
                    exito = true;
                    mensaje = "Auditoría registrada con éxito.";
                } else {
                    exito = false;
                    mensaje = mensajeErrorAdi;
                }
                break;
            case "MODIFICAR":
                auditoria.setAudiCod(codigoSeleccionado);
                String mensajeErrorMod = auditoriaDAO.actualizarAuditoria(auditoria);
                if (mensajeErrorMod == null) {
                    exito = true;
                    mensaje = "Auditoría modificada con éxito.";
                } else {
                    exito = false;
                    mensaje = mensajeErrorMod;
                }
                break;
            case "ELIMINAR":
                exito = auditoriaDAO.eliminarLogicamenteAuditoria(codigoSeleccionado);
                mensaje = exito ? "Auditoría eliminada con éxito." : "Error al eliminar auditoría.";
                break;
            case "INACTIVAR":
                exito = auditoriaDAO.inactivarAuditoria(codigoSeleccionado);
                mensaje = exito ? "Auditoría inactivada con éxito." : "Error al inactivar auditoría.";
                break;
            case "REACTIVAR":
                exito = auditoriaDAO.reactivarAuditoria(codigoSeleccionado);
                mensaje = exito ? "Auditoría reactivada con éxito." : "Error al reactivar auditoría.";
                break;
            default:
                JOptionPane.showMessageDialog(this, "Operación no reconocida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTablaAuditorias();
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

    private void cargarTablaAuditorias() {
        tableModel.setRowCount(0);
        List<Auditoria> lista = auditoriaDAO.obtenerTodasAuditorias();
        for (Auditoria a : lista) {
            tableModel.addRow(new Object[]{
                a.getAudiCod(),
                a.getUsuCod(),
                a.getAudiFecha().toString(),
                a.getAudiHora().toString(),
                a.getAudiDescri(),
                a.getModAudiCod(),
                a.getAudiEstReg(),
                "", // Nombre de usuario - se podría obtener con join
                ""  // Descripción de módulo - se podría obtener con join
            });
        }
    }

    private void habilitarControles(boolean b) {
        cbUsuarioCodigo.setEnabled(b);
        txtFecha.setEditable(b);
        txtHora.setEditable(b);
        txtDescripcion.setEditable(b);
        txtDetalle.setEditable(b);
        cbModuloCodigo.setEnabled(b);
        // txtCodigo y txtEstadoRegistro siempre permanecen no editables
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        cbUsuarioCodigo.setSelectedIndex(0);
        txtFecha.setText("");
        txtHora.setText("");
        txtDescripcion.setText("");
        txtDetalle.setText("");
        cbModuloCodigo.setSelectedIndex(0);
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
        int selectedRow = tablaAuditorias.getSelectedRow();
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
        int selectedRow = tablaAuditorias.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        cargarDatosDesdeTabla(selectedRow);
        txtEstadoRegistro.setText("*");
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como eliminado ('*').\nPresione 'Actualizar' para confirmar.", "Eliminación Lógica", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaAuditorias.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para inactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        cargarDatosDesdeTabla(selectedRow);
        txtEstadoRegistro.setText("I");
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        habilitarControles(false);
        operacionActual = "INACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como inactivo ('I').\nPresione 'Actualizar' para confirmar.", "Inactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoReactivar() {
        int selectedRow = tablaAuditorias.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para reactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        cargarDatosDesdeTabla(selectedRow);
        txtEstadoRegistro.setText("A");
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        habilitarControles(false);
        operacionActual = "REACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.", "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cargarDatosDesdeTabla(int selectedRow) {
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        
        // Seleccionar en ComboBox por código de usuario
        int codigoUsuario = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
        seleccionarEnComboBoxPorCodigo(cbUsuarioCodigo, codigoUsuario);
        
        txtFecha.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtHora.setText(tableModel.getValueAt(selectedRow, 3).toString());
        txtDescripcion.setText(tableModel.getValueAt(selectedRow, 4).toString());
        
        // Obtener el detalle completo del objeto
        int codigoAuditoria = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        Auditoria auditoriaCompleta = auditoriaDAO.obtenerAuditoriaPorCodigo(codigoAuditoria);
        if (auditoriaCompleta != null) {
            txtDetalle.setText(auditoriaCompleta.getAudiDet() != null ? auditoriaCompleta.getAudiDet() : "");
        }
        
        // Seleccionar en ComboBox por código de módulo
        int codigoModulo = Integer.parseInt(tableModel.getValueAt(selectedRow, 5).toString());
        seleccionarEnComboBoxPorCodigo(cbModuloCodigo, codigoModulo);
        
        txtEstadoRegistro.setText(tableModel.getValueAt(selectedRow, 6).toString());
    }
}