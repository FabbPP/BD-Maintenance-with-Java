package gui.consultas;

import dao.consultas.ConsultaRepresentanteDAO;
import modelo.consultas.ConsultaRepresentante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class ConsultaRepresentanteFrame extends JFrame {
    
    private JTable tablaEstadisticas;
    private DefaultTableModel tableModel;
    private JButton btnActualizar, btnSalir;
    private JLabel lblTotalRegistros, lblMontoGlobal;
    
    private ConsultaRepresentanteDAO estadisticasDAO;
    private DecimalFormat decimalFormat = new DecimalFormat("'S/'#,##0.00");
    
    public ConsultaRepresentanteFrame() {
        setTitle("Estadísticas de Representantes - Ventas por Representante");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        estadisticasDAO = new ConsultaRepresentanteDAO();
        
        initComponents();
        cargarEstadisticas();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel superior con información general
        JPanel panelInfo = new JPanel(new GridLayout(1, 2, 10, 10));
        panelInfo.setBorder(BorderFactory.createTitledBorder("Resumen General"));
        
        lblTotalRegistros = new JLabel("Total Representantes: 0");
        lblMontoGlobal = new JLabel("Monto Global: S/. 0.00");
        
        panelInfo.add(lblTotalRegistros);
        panelInfo.add(lblMontoGlobal);
        
        add(panelInfo, BorderLayout.NORTH);
        
        // Panel central con tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Estadísticas por Representante"));
        
        tableModel = new DefaultTableModel(
            new Object[]{"Código", "Nombre Representante", "Total Facturas", "Monto Total Ventas"}, 
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaEstadisticas = new JTable(tableModel);
        tablaEstadisticas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Configurar el ancho de las columnas
        tablaEstadisticas.getColumnModel().getColumn(0).setPreferredWidth(80);
        tablaEstadisticas.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaEstadisticas.getColumnModel().getColumn(2).setPreferredWidth(120);
        tablaEstadisticas.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(tablaEstadisticas);
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        add(panelTabla, BorderLayout.CENTER);
        
        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        btnActualizar = new JButton("Actualizar Datos");
        btnSalir = new JButton("Salir");
        
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarEstadisticas();
                JOptionPane.showMessageDialog(
                    ConsultaRepresentanteFrame.this,
                    "Datos actualizados correctamente.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnSalir);
        
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void cargarEstadisticas() {
        tableModel.setRowCount(0);
        
        List<ConsultaRepresentante> estadisticas = estadisticasDAO.obtenerConsultasRepresentantes();
        
        BigDecimal montoGlobal = BigDecimal.ZERO;
        int totalFacturas = 0;
        
        for (ConsultaRepresentante est : estadisticas) {
            Object[] row = {
                est.getRepCod(),
                est.getRepNom(),
                est.getTotalFacturas(),
                decimalFormat.format(est.getMontoTotalVentas() != null ? est.getMontoTotalVentas() : BigDecimal.ZERO)
            };
            tableModel.addRow(row);
            
            if (est.getMontoTotalVentas() != null) {
                montoGlobal = montoGlobal.add(est.getMontoTotalVentas());
            }
            totalFacturas += est.getTotalFacturas();
        }
        
        // Actualizar labels de resumen
        lblTotalRegistros.setText("Total Representantes: " + estadisticas.size());
        lblMontoGlobal.setText("Monto Global: " + decimalFormat.format(montoGlobal));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ConsultaRepresentanteFrame().setVisible(true);
        });
    }
}