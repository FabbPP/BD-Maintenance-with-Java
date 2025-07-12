package gui.consultas;

import dao.consultas.ConsultaRepresentanteDAO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import modelo.consultas.ConsultaRepresentante;

public class ConsultaRepresentanteFrame extends JFrame {
    
    private JTable tablaEstadisticas;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
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
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return String.class;  
                    case 1: return String.class;  
                    case 2: return Integer.class; 
                    case 3: return BigDecimal.class; 
                    default: return Object.class;
                }
            }
        };
        
        tablaEstadisticas = new JTable(tableModel);
        tablaEstadisticas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Alinear todo el contenido a la izquierda
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        // Aplicar el renderizador a todas las columnas
        for (int i = 0; i < tablaEstadisticas.getColumnCount(); i++) {
            tablaEstadisticas.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }

        
        // Configurar el sorter con comparadores personalizados
        sorter = new TableRowSorter<>(tableModel);
        tablaEstadisticas.setRowSorter(sorter);
        
        // Configurar comparadores personalizados para todas las columnas
        // Columna 0: Código (String pero ordenado numéricamente)
        sorter.setComparator(0, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                try {
                    // Intentar convertir a número para ordenamiento numérico
                    Integer num1 = Integer.parseInt(String.valueOf(o1));
                    Integer num2 = Integer.parseInt(String.valueOf(o2));
                    return num1.compareTo(num2);
                } catch (NumberFormatException e) {
                    // Si no son números, ordenar como string
                    return String.valueOf(o1).compareTo(String.valueOf(o2));
                }
            }
        });
        
        // Columna 1: Nombre Representante (String)
        sorter.setComparator(1, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return String.valueOf(o1).compareTo(String.valueOf(o2));
            }
        });
        
        // Columna 2: Total Facturas (Integer)
        sorter.setComparator(2, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Integer i1 = (Integer) o1;
                Integer i2 = (Integer) o2;
                return i1.compareTo(i2);
            }
        });
        
        // Columna 3: Monto Total Ventas (BigDecimal)
        sorter.setComparator(3, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof BigDecimal && o2 instanceof BigDecimal) {
                    return ((BigDecimal) o1).compareTo((BigDecimal) o2);
                }
                return 0;
            }
        });
        
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
                est.getMontoTotalVentas() != null ? est.getMontoTotalVentas() : BigDecimal.ZERO
            };
            tableModel.addRow(row);
            
            if (est.getMontoTotalVentas() != null) {
                montoGlobal = montoGlobal.add(est.getMontoTotalVentas());
            }
            totalFacturas += est.getTotalFacturas();
        }
        
        // Configurar el renderer personalizado para mostrar formato monetario
        DefaultTableCellRenderer montoRenderer = new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof BigDecimal) {
                    setText(decimalFormat.format(value));
                } else {
                    setText(value != null ? value.toString() : "S/. 0.00");
                }
            }
        };
        montoRenderer.setHorizontalAlignment(SwingConstants.LEFT); // también alineado a la izquierda
        tablaEstadisticas.getColumnModel().getColumn(3).setCellRenderer(montoRenderer);
        
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