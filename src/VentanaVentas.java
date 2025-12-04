import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class VentanaVentas extends JFrame {

    private DefaultTableModel modelo;
    private JTable tabla;

    public VentanaVentas() {
        setTitle("Ventas Cafetería");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Modelo de tabla: Tipo | Monto | Fecha y Hora | Ver Boleta | Eliminar
        modelo = new DefaultTableModel(new Object[]{"Tipo", "Monto", "Fecha y Hora", "Ver Boleta", "Eliminar"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 3;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(30);

        // Llenar tabla solo con ventas de tipo "Cafetería"
        cargarTabla();

        // Renderers y editores para botones
        tabla.getColumn("Ver Boleta").setCellRenderer(new ButtonRenderer());
        tabla.getColumn("Ver Boleta").setCellEditor(new ButtonEditor(new JCheckBox(), true));

        tabla.getColumn("Eliminar").setCellRenderer(new ButtonRenderer());
        tabla.getColumn("Eliminar").setCellEditor(new ButtonEditor(new JCheckBox(), false));

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        setVisible(true);
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Venta v : Main.ventas) {
            if (!"Cafetería".equals(v.tipo)) continue; // filtramos solo Cafetería
            String tipo = v.tipo;
            double monto = v.monto;
            String fechaHora = v.fecha.format(df);
            modelo.addRow(new Object[]{tipo, monto, fechaHora, "Ver Boleta", "Eliminar"});
        }
    }

    // Renderer para botones
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() { setOpaque(true); }
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Editor para botones
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean verBoleta; // true: ver boleta, false: eliminar
        private int row;

        public ButtonEditor(JCheckBox checkBox, boolean verBoleta) {
            super(checkBox);
            this.verBoleta = verBoleta;
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(e -> {
                if (verBoleta) {
                    mostrarBoleta(row);
                } else {
                    eliminarVenta(row);
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            button.setText((value == null) ? "" : value.toString());
            this.row = row;
            return button;
        }

        public Object getCellEditorValue() { return button.getText(); }
    }

    private void mostrarBoleta(int fila) {
        if (fila < 0 || fila >= Main.ventas.size()) return;
        // obtenemos solo ventas de cafetería
        Venta v = Main.ventas.stream().filter(venta -> "Cafetería".equals(venta.tipo)).toList().get(fila);

        StringBuilder boleta = new StringBuilder();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        boleta.append("=== BOLETA ===\n\n");
        boleta.append("Tipo: ").append(v.tipo).append("\n");
        boleta.append("Monto: $").append(String.format("%.2f", v.monto)).append("\n");
        boleta.append("Medio de pago: ").append(v.medioPago).append("\n");
        boleta.append("Fecha y hora: ").append(v.fecha.format(df)).append("\n\n");

        if (v.productos != null && v.cantidades != null && v.productos.size() == v.cantidades.size()) {
            boleta.append("Detalles:\n");
            double total = 0;
            for (int i = 0; i < v.productos.size(); i++) {
                ProductoCafeteria p = v.productos.get(i);
                int c = v.cantidades.get(i);
                double subtotal = p.precio * c;
                total += subtotal;
                boleta.append(String.format("- %s x%d = $%.2f\n", p.nombre, c, subtotal));
            }
            boleta.append("\nTotal calculado: $").append(String.format("%.2f", total)).append("\n");
        }

        JOptionPane.showMessageDialog(this, boleta.toString(), "Boleta", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarVenta(int fila) {
        if (fila < 0 || fila >= Main.ventas.size()) return;
        // obtenemos solo ventas de cafetería
        Venta v = Main.ventas.stream().filter(venta -> "Cafetería".equals(venta.tipo)).toList().get(fila);

        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar esta venta?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Main.ventas.remove(v);
            Persistencia.guardar(Main.ventas, Main.ARCHIVO_VENTAS);
            cargarTabla();
        }
    }
}




