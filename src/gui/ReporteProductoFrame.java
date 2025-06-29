package gui;

import dao.ReporteProductoDAO;
import modelo.ReporteProducto;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReporteProductoFrame extends JFrame {

    private JTextField txtCodigo;
    private JTextField txtStockMinimo;
    private JTextField txtStockMaximo;
    private JTextField txtEstadoRegistro;
    private JTable tablaReportes;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, btnActualizar, btnCancelar, btnSalir;
    private JButton btnBuscarPorStockMin, btnBuscarPorStockMax, btnVerStockCritico, btnVerStockBajo;

    private ReporteProductoDAO reporteProductoDAO;
    private int flagCarFlaAct = 0;
    private String operacionActual = "";
    private int codigoSeleccionado = 0;

    public ReporteProductoFrame() {
        setTitle("Mantenimiento de Reportes de Producto");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        reporteProductoDAO = new ReporteProductoDAO();
        initComponents();
        cargarTablaReportes();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReporteProductoFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel de registro
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Reporte de Producto"));
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
        panelRegistro.add(new JLabel("Estado Registro:"), gbc);
        gbc.gridx = 3;
        txtEstadoRegistro = new JTextField(5);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        // Segunda fila
        gbc.gridx = 0; gbc.gridy = 1;
        panelRegistro.add(new JLabel("Stock Mínimo:"), gbc);
        gbc.gridx = 1;
        txtStockMinimo = new JTextField(15);
        panelRegistro.add(txtStockMinimo, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        panelRegistro.add(new JLabel("Stock Máximo:"), gbc);
        gbc.gridx = 3;
        txtStockMaximo = new JTextField(15);
        panelRegistro.add(txtStockMaximo, gbc);

        // Tercera fila - Nota sobre validaciones
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        JLabel lblValidaciones = new JLabel("* Stock mínimo debe ser menor que el máximo y ambos mayores a 0");
        lblValidaciones.setFont(lblValidaciones.getFont().deriveFont(Font.ITALIC, 10f));
        lblValidaciones.setForeground(Color.GRAY);
        panelRegistro.add(lblValidaciones, gbc);

        add(panelRegistro, BorderLayout.NORTH);

        // Panel de tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Reportes de Producto"));
        tableModel = new DefaultTableModel(new Object[]{"Código", "Stock Mín.", "Stock Máx.", "Estado", "Diferencia"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaReportes = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaReportes), BorderLayout.CENTER);

        // Listener para selección en tabla
        tablaReportes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int i = tablaReportes.getSelectedRow();
                    if (i != -1) {
                        txtCodigo.setText(tableModel.getValueAt(i, 0).toString());
                        txtStockMinimo.setText(tableModel.getValueAt(i, 1).toString());
                        txtStockMaximo.setText(tableModel.getValueAt(i, 2).toString());
                        txtEstadoRegistro.setText(tableModel.getValueAt(i, 3).toString());
                        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
                        habilitarBotonesParaSeleccion();
                    }
                }
            }
        });
        add(panelTabla, BorderLayout.CENTER);

        // Panel de botones principales
        JPanel panelBotonesPrincipales = new JPanel(new GridLayout(2, 4, 10, 10));
        btnAdicionar = new JButton("Adicionar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnCancelar = new JButton("Cancelar");
        btnInactivar = new JButton("Inactivar");
        btnReactivar = new JButton("Reactivar");
        btnActualizar = new JButton("Actualizar");
        btnSalir = new JButton("Salir");

        for (JButton b : new JButton[]{btnAdicionar, btnModificar, btnEliminar, btnCancelar, 
                                      btnInactivar, btnReactivar, btnActualizar, btnSalir}) {
            panelBotonesPrincipales.add(b);
        }

        // Panel de botones de consulta
        JPanel panelBotonesConsulta = new JPanel(new FlowLayout());
        panelBotonesConsulta.setBorder(BorderFactory.createTitledBorder("Consultas Especiales"));
        btnBuscarPorStockMin = new JButton("Buscar por Stock Mín.");
        btnBuscarPorStockMax = new JButton("Buscar por Stock Máx.");
        btnVerStockCritico = new JButton("Ver Stock Crítico");
        btnVerStockBajo = new JButton("Ver Stock Bajo");

        for (JButton b : new JButton[]{btnBuscarPorStockMin, btnBuscarPorStockMax, 
                                      btnVerStockCritico, btnVerStockBajo}) {
            panelBotonesConsulta.add(b);
        }

        // Panel sur combinado
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.add(panelBotonesConsulta, BorderLayout.NORTH);
        panelSur.add(panelBotonesPrincipales, BorderLayout.SOUTH);
        add(panelSur, BorderLayout.SOUTH);

        // Event listeners
        btnAdicionar.addActionListener(e -> comandoAdicionar());
        btnActualizar.addActionListener(e -> comandoActualizar());
        btnCancelar.addActionListener(e -> comandoCancelar());
        btnSalir.addActionListener(e -> comandoSalir());
        btnModificar.addActionListener(e -> comandoModificar());
        btnEliminar.addActionListener(e -> comandoEliminar());
        btnInactivar.addActionListener(e -> comandoInactivar());
        btnReactivar.addActionListener(e -> comandoReactivar());
        
        // Listeners para botones de consulta
        btnBuscarPorStockMin.addActionListener(e -> buscarPorStockMinimo());
        btnBuscarPorStockMax.addActionListener(e -> buscarPorStockMaximo());
        btnVerStockCritico.addActionListener(e -> verStockCritico());
        btnVerStockBajo.addActionListener(e -> verStockBajo());
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
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String stockMinStr = txtStockMinimo.getText().trim();
        String stockMaxStr = txtStockMaximo.getText().trim();
        String estadoStr = txtEstadoRegistro.getText().trim();

        if (stockMinStr.isEmpty() || stockMaxStr.isEmpty() || estadoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stockMin, stockMax;
        
        try {
            stockMin = Integer.parseInt(stockMinStr);
            stockMax = Integer.parseInt(stockMaxStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Los valores de stock deben ser números válidos.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validación adicional
        if (stockMin <= 0 || stockMax <= 0) {
            JOptionPane.showMessageDialog(this, "Los valores de stock deben ser mayores a 0.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (stockMin >= stockMax) {
            JOptionPane.showMessageDialog(this, "El stock mínimo debe ser menor que el stock máximo.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ReporteProducto reporte = new ReporteProducto();
        reporte.setReporProdMin(stockMin);
        reporte.setReporProdMax(stockMax);
        reporte.setReporProdEstReg(estadoStr.charAt(0));

        boolean exito = false;
        String mensaje = "";

        switch (operacionActual) {
            case "ADICIONAR":
                String mensajeErrorAdi = reporteProductoDAO.insertarReporte(reporte);
                if (mensajeErrorAdi == null) {
                    exito = true;
                    mensaje = "Reporte de producto registrado con éxito.";
                } else {
                    exito = false;
                    mensaje = mensajeErrorAdi;
                }
                break;
            case "MODIFICAR":
                reporte.setReporProdCod(codigoSeleccionado);
                String mensajeErrorMod = reporteProductoDAO.actualizarReporte(reporte);
                if (mensajeErrorMod == null) {
                    exito = true;
                    mensaje = "Reporte de producto modificado con éxito.";
                } else {
                    exito = false;
                    mensaje = mensajeErrorMod;
                }
                break;
            case "ELIMINAR":
                exito = reporteProductoDAO.eliminarLogicamenteReporte(codigoSeleccionado);
                mensaje = exito ? "Reporte eliminado con éxito." : "Error al eliminar reporte.";
                break;
            case "INACTIVAR":
                exito = reporteProductoDAO.inactivarReporte(codigoSeleccionado);
                mensaje = exito ? "Reporte inactivado con éxito." : "Error al inactivar reporte.";
                break;
            case "REACTIVAR":
                exito = reporteProductoDAO.reactivarReporte(codigoSeleccionado);
                mensaje = exito ? "Reporte reactivado con éxito." : "Error al reactivar reporte.";
                break;
            default:
                JOptionPane.showMessageDialog(this, "Operación no reconocida.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
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
        habilitarBotonesIniciales();
        cargarTablaReportes(); // Volver a mostrar todos los reportes
    }

    private void comandoSalir() {
        dispose();
    }

    private void comandoModificar() {
        int selectedRow = tablaReportes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para modificar.", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtStockMinimo.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtStockMaximo.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtEstadoRegistro.setText("*");
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como eliminado ('*').\nPresione 'Actualizar' para confirmar.", 
                                    "Eliminación Lógica", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaReportes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para inactivar.", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtStockMinimo.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtStockMaximo.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtEstadoRegistro.setText("I");
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        
        habilitarControles(false);
        operacionActual = "INACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como inactivo ('I').\nPresione 'Actualizar' para confirmar.", 
                                    "Inactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoReactivar() {
        int selectedRow = tablaReportes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para reactivar.", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtStockMinimo.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtStockMaximo.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtEstadoRegistro.setText("A");
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        
        habilitarControles(false);
        operacionActual = "REACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.", 
                                    "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    // Métodos de consulta especiales VISTAS
    private void buscarPorStockMinimo() {
        String input = JOptionPane.showInputDialog(this, 
                "Ingrese el rango de stock mínimo (formato: min-max):\nEjemplo: 10-50", 
                "Buscar por Stock Mínimo", JOptionPane.QUESTION_MESSAGE);
        
        if (input != null && !input.trim().isEmpty()) {
            try {
                String[] partes = input.split("-");
                if (partes.length == 2) {
                    int min = Integer.parseInt(partes[0].trim());
                    int max = Integer.parseInt(partes[1].trim());
                    cargarTablaReportesFiltrados(reporteProductoDAO.buscarReportesPorStockMinimo(min, max));
                } else {
                    JOptionPane.showMessageDialog(this, "Formato incorrecto. Use: min-max", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Los valores deben ser números válidos.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarPorStockMaximo() {
        String input = JOptionPane.showInputDialog(this, 
                "Ingrese el rango de stock máximo (formato: min-max):\nEjemplo: 100-500", 
                "Buscar por Stock Máximo", JOptionPane.QUESTION_MESSAGE);
        
        if (input != null && !input.trim().isEmpty()) {
            try {
                String[] partes = input.split("-");
                if (partes.length == 2) {
                    int min = Integer.parseInt(partes[0].trim());
                    int max = Integer.parseInt(partes[1].trim());
                    cargarTablaReportesFiltrados(reporteProductoDAO.buscarReportesPorStockMaximo(min, max));
                } else {
                    JOptionPane.showMessageDialog(this, "Formato incorrecto. Use: min-max", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Los valores deben ser números válidos.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void verStockCritico() {
        List<ReporteProducto> reportesCriticos = reporteProductoDAO.obtenerReportesStockCritico();
        cargarTablaReportesFiltrados(reportesCriticos);
        
        if (reportesCriticos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron reportes con stock crítico.", 
                                        "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Se encontraron " + reportesCriticos.size() + 
                                        " reportes con stock crítico (mín >= máx).", 
                                        "Stock Crítico", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void verStockBajo() {
        String input = JOptionPane.showInputDialog(this, 
                "Ingrese el porcentaje para considerar stock bajo:\nEjemplo: 80 (para 80%)", 
                "Ver Stock Bajo", JOptionPane.QUESTION_MESSAGE);
        
        if (input != null && !input.trim().isEmpty()) {
            try {
                double porcentaje = Double.parseDouble(input.trim());
                if (porcentaje <= 0 || porcentaje > 100) {
                    JOptionPane.showMessageDialog(this, "El porcentaje debe estar entre 1 y 100.", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                List<ReporteProducto> reportesBajos = reporteProductoDAO.obtenerReportesStockBajo(porcentaje);
                cargarTablaReportesFiltrados(reportesBajos);
                
                if (reportesBajos.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No se encontraron reportes with stock bajo al " + porcentaje + "%.", 
                                                "Información", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Se encontraron " + reportesBajos.size() + 
                                                " reportes con stock bajo al " + porcentaje + "%.", 
                                                "Stock Bajo", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El porcentaje debe ser un número válido.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarTablaReportes() {
        tableModel.setRowCount(0);
        List<ReporteProducto> lista = reporteProductoDAO.obtenerTodosReportes();
        for (ReporteProducto r : lista) {
            int diferencia = r.getReporProdMax() - r.getReporProdMin();
            tableModel.addRow(new Object[]{
                r.getReporProdCod(),
                r.getReporProdMin(),
                r.getReporProdMax(),
                r.getReporProdEstReg(),
                diferencia
            });
        }
    }

    private void cargarTablaReportesFiltrados(List<ReporteProducto> lista) {
        tableModel.setRowCount(0);
        for (ReporteProducto r : lista) {
            int diferencia = r.getReporProdMax() - r.getReporProdMin();
            tableModel.addRow(new Object[]{
                r.getReporProdCod(),
                r.getReporProdMin(),
                r.getReporProdMax(),
                r.getReporProdEstReg(),
                diferencia
            });
        }
    }

    private void habilitarControles(boolean b) {
        txtStockMinimo.setEditable(b);
        txtStockMaximo.setEditable(b);
        // txtCodigo y txtEstadoRegistro siempre permanecen no editables
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtStockMinimo.setText("");
        txtStockMaximo.setText("");
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
        
        // Botones de consulta siempre habilitados
        btnBuscarPorStockMin.setEnabled(true);
        btnBuscarPorStockMax.setEnabled(true);
        btnVerStockCritico.setEnabled(true);
        btnVerStockBajo.setEnabled(true);
    }

    private void habilitarBotonesParaOperacion() {
        btnAdicionar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnInactivar.setEnabled(false);
        btnReactivar.setEnabled(false);
        
        // Deshabilitar botones de consulta durante operaciones
        btnBuscarPorStockMin.setEnabled(false);
        btnBuscarPorStockMax.setEnabled(false);
        btnVerStockCritico.setEnabled(false);
        btnVerStockBajo.setEnabled(false);
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
        
        // Botones de consulta habilitados para selección
        btnBuscarPorStockMin.setEnabled(true);
        btnBuscarPorStockMax.setEnabled(true);
        btnVerStockCritico.setEnabled(true);
        btnVerStockBajo.setEnabled(true);
    }
}