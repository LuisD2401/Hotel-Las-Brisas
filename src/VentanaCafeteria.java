import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VentanaCafeteria extends JFrame {

    private ArrayList<JSpinner> spinners = new ArrayList<>();
    private JPanel panelProductos;
    private JLabel lblTotal;

    public VentanaCafeteria() {
        setTitle("Cafetería");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));

        lblTotal = new JLabel("Total: $0.0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTotal, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(panelProductos);
        add(scroll, BorderLayout.CENTER);

        JButton btnComprar = new JButton("Comprar");
        add(btnComprar, BorderLayout.SOUTH);

        cargarProductos();

        btnComprar.addActionListener(e -> {
            double total = 0;
            ArrayList<ProductoCafeteria> productosComprados = new ArrayList<>();

            for (int i = 0; i < spinners.size(); i++) {
                int cantidad = (int) spinners.get(i).getValue();
                ProductoCafeteria p = Main.productos.get(i);
                if (cantidad > 0) {
                    p.stock -= cantidad;
                    total += p.precio * cantidad;
                    productosComprados.add(p);
                }
            }

            if (total == 0) {
                JOptionPane.showMessageDialog(this, "Seleccione al menos un producto.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Guardar stock actualizado
            Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);

            // Procesar pago
            Venta v = Main.procesarPagoGUI(total, "Cafetería");

            JOptionPane.showMessageDialog(this,
                    "Compra realizada correctamente.\nTotal: $" + total + "\nVenta registrada:\n" + v);

            // Recargar productos y actualizar la interfaz
            cargarProductos();
        });

        setVisible(true);
    }

    private void cargarProductos() {
        panelProductos.removeAll();
        spinners.clear();

        // Mostrar solo productos con stock > 0
        for (ProductoCafeteria p : Main.productos) {
            if (p.stock <= 0) continue; // ignorar productos agotados

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lblNombre = new JLabel(p.nombre + " - $" + p.precio + " - Stock: " + p.stock);
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, p.stock, 1));
            spinners.add(spinner);

            spinner.addChangeListener(e -> actualizarTotal());

            panel.add(lblNombre);
            panel.add(new JLabel("Cantidad:"));
            panel.add(spinner);
            panelProductos.add(panel);
        }

        panelProductos.revalidate();
        panelProductos.repaint();
        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = 0;
        for (int i = 0; i < spinners.size(); i++) {
            int cantidad = (int) spinners.get(i).getValue();
            ProductoCafeteria p = Main.productos.get(i);
            total += cantidad * p.precio;
        }
        lblTotal.setText("Total: $" + total);
    }
}


