import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class VentanaVentasH extends JFrame {

    private JTable tablaVentas;
    private DefaultTableModel modelo;
    private JButton btnVer, btnCerrar;

    public VentanaVentasH() {
        setTitle("Ventas de Habitaciones");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===========================
        // Modelo y columnas de la tabla
        // ===========================
        String[] columnas = {"N° Hab", "Rut Cliente", "Fecha Reserva", "Estado", "Acción"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo la columna “Acción” (última) será editable
                return column == 4;
            }
        };


        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 18));
        tabla.setRowHeight(28);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(900, 400)); // Ajustamos el tamaño del scroll

        // Cargar ventas
        cargarVentas();

        // ===========================
        // Botones para ver boleta
        // ===========================
        tabla.getColumn("Acción").setCellRenderer(new ButtonRenderer());
        tabla.getColumn("Acción").setCellEditor(new ButtonEditor(new JCheckBox()));

        // ===========================
        // Panel de botones abajo
        // ===========================
        // ===========================
// Panel de botones abajo
// ===========================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 22));
        panelBotones.add(btnCerrar);

// NUEVO BOTON ELIMINAR
        JButton btnEliminarVenta = new JButton("Eliminar Reserva");
        btnEliminarVenta.setFont(new Font("Arial", Font.BOLD, 22));
        panelBotones.add(btnEliminarVenta);

        add(panelBotones, BorderLayout.SOUTH);

        btnCerrar.addActionListener(e -> dispose());

// Acción del botón eliminar venta
        btnEliminarVenta.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione una venta para eliminar.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro que desea eliminar esta venta?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Eliminar de la lista principal
                Main.ventasHabitaciones.remove(fila);

                // Guardar cambios si estás usando persistencia
                Persistencia.guardar(Main.ventasHabitaciones, Main.ARCHIVO_VENTAS_HABITACIONES);

                // Actualizar tabla
                cargarVentas();
            }
        });



        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        btnCerrar.addActionListener(e -> dispose());

        add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }

    // Clase interna para el botón "Ver Boleta"
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Ver Boleta");
            setFont(new Font("Arial", Font.PLAIN, 14));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setFont(new Font("Arial", Font.PLAIN, 14));

            // Acción del botón
            button.addActionListener(e -> {
                JTable table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, button);
                if (table != null) {
                    int row = table.getEditingRow();
                    // Detener edición antes de mostrar la boleta
                    fireEditingStopped();
                    verBoleta(row);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = value == null ? "Ver Boleta" : value.toString();
            button.setText(label);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }

        private void verBoleta(int row) {
            if (row < 0 || row >= Main.ventasHabitaciones.size()) return;
            VentaHabitacion venta = Main.ventasHabitaciones.get(row);
            JOptionPane.showMessageDialog(null, venta.toString(), "Boleta Reserva", JOptionPane.INFORMATION_MESSAGE);
        }
    }




    private void cargarVentas() {
        modelo.setRowCount(0);  // Limpiar la tabla

        for (VentaHabitacion v : Main.ventasHabitaciones) {
            // Agregar fila en formato adecuado
            modelo.addRow(new Object[]{
                    v.habitacion.numero,
                    v.rut,  // Mostramos el rut del cliente
                    v.getFechaVenta(),  // Usamos el método getFechaVenta()
                    v.estaActiva() ? "Activa" : "Anulada",  // Usamos el estado de la reserva
                    "Ver Boleta"  // Acción para ver boleta
            });
        }
    }



}
