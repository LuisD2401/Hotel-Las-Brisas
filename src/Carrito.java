import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Carrito extends JFrame {
    private ArrayList<ProductoCafeteria> productos = new ArrayList<>();
    private ArrayList<Integer> cantidades = new ArrayList<>();
    private JPanel panelProductos;
    private JLabel lblTotal;

    public Carrito() {
        setTitle("Carrito de Compras");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(panelProductos);
        add(scroll, BorderLayout.CENTER);

        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 22));
        add(lblTotal, BorderLayout.NORTH);

        JPanel panelSur = new JPanel();
        JButton btnFinalizar = new JButton("Finalizar Compra");
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 22));
        panelSur.add(btnFinalizar);
        add(panelSur, BorderLayout.SOUTH);

        btnFinalizar.addActionListener(e -> finalizarCompra());

        actualizarPanel();
    }

    public void agregarProducto(ProductoCafeteria p, int cantidad) {
        if (cantidad <= 0) return;

        int index = productos.indexOf(p);
        if (index >= 0) {
            cantidades.set(index, cantidades.get(index) + cantidad);
        } else {
            productos.add(p);
            cantidades.add(cantidad);
        }
        actualizarPanel();
    }

    private void actualizarPanel() {
        panelProductos.removeAll();

        for (int i = 0; i < productos.size(); i++) {
            ProductoCafeteria p = productos.get(i);
            int cant = cantidades.get(i);

            JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
            fila.add(new JLabel(p.nombre + " - $" + p.precio + " x " + cant));
            JButton btnEliminar = new JButton("Eliminar");
            int idx = i;
            btnEliminar.addActionListener(ev -> {
                productos.remove(idx);
                cantidades.remove(idx);
                actualizarPanel();
            });
            fila.add(btnEliminar);
            panelProductos.add(fila);
        }

        lblTotal.setText("Total: $" + String.format("%.2f", getTotal()));
        panelProductos.revalidate();
        panelProductos.repaint();
    }

    public double getTotal() {
        double total = 0;
        for (int i = 0; i < productos.size(); i++) {
            total += productos.get(i).precio * cantidades.get(i);
        }
        return total;
    }

    private void finalizarCompra() {
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] opciones = {"Efectivo", "Débito"};
        int metodo = JOptionPane.showOptionDialog(this, "Seleccione método de pago:", "Pago",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (metodo == -1) return; // canceló

        double total = getTotal();
        double pago = total;

        if (metodo == 0) { // efectivo
            boolean valido = false;
            while (!valido) {
                String input = JOptionPane.showInputDialog(this, "Total: $" + total + "\nIngrese monto con el que paga:");
                if (input == null) return; // canceló
                try {
                    pago = Double.parseDouble(input);
                    if (pago >= total) {
                        valido = true;
                    } else {
                        JOptionPane.showMessageDialog(this, "El monto ingresado es menor al total.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // Reducir stock
        for (int i = 0; i < productos.size(); i++) {
            ProductoCafeteria p = productos.get(i);
            p.stock = Math.max(p.stock - cantidades.get(i), 0);
        }

        Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);

        // Mostrar boleta
        StringBuilder boleta = new StringBuilder("---- BOLETA ----\n");
        for (int i = 0; i < productos.size(); i++) {
            ProductoCafeteria p = productos.get(i);
            boleta.append(p.nombre).append(" x ").append(cantidades.get(i)).append(" = $").append(p.precio * cantidades.get(i)).append("\n");
        }
        boleta.append("Total: $").append(String.format("%.2f", total)).append("\n");
        if (metodo == 0) {
            boleta.append("Pago con efectivo: $").append(String.format("%.2f", pago)).append("\n");
            boleta.append("Vuelto: $").append(String.format("%.2f", pago - total)).append("\n");
        } else {
            boleta.append("Pago con débito\n");
        }

        JOptionPane.showMessageDialog(this, boleta.toString(), "Boleta", JOptionPane.INFORMATION_MESSAGE);

        // Limpiar carrito
        productos.clear();
        cantidades.clear();
        actualizarPanel();
    }
}
