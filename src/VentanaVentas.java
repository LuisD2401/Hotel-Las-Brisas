import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class VentanaVentas extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;

    public VentanaVentas() {
        setTitle("Ventas del Hotel");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Columnas de la tabla
        String[] columnas = {"Tipo", "Monto", "Medio de Pago", "Documento", "Fecha", "Habitación"};

        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        cargarVentas();

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        JButton btnEliminar = new JButton("Eliminar Venta");
        add(btnEliminar, BorderLayout.SOUTH);

        // Acción al eliminar venta
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                Venta v = Main.ventas.get(fila);

                // Si es reserva, liberar habitación
                if (v.tipo.equals("Reserva") && v.numeroHabitacion != -1) {
                    for (Habitacion h : Main.habitaciones) {
                        if (h.numero == v.numeroHabitacion) {
                            h.disponible = true;
                            break;
                        }
                    }
                    Persistencia.guardar(Main.habitaciones, Main.ARCHIVO_HABITACIONES);
                }

                // Eliminar venta
                Main.ventas.remove(fila);
                modelo.removeRow(fila);
                Persistencia.guardar(Main.ventas, Main.ARCHIVO_VENTAS);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una venta para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            }
        });

        setVisible(true);
    }

    private void cargarVentas() {
        modelo.setRowCount(0); // limpiar tabla
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Venta v : Main.ventas) {
            String habitacion = v.tipo.equals("Reserva") ? String.valueOf(v.numeroHabitacion) : "-";
            Object[] fila = {
                    v.tipo,
                    "$" + v.monto,
                    v.medioPago,
                    v.tipoDocumento,
                    v.fecha.format(formato),
                    habitacion
            };
            modelo.addRow(fila);
        }
    }
}

