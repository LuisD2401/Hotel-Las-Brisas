import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaReserva extends JFrame {

    private JComboBox<String> comboHabitaciones;

    public VentanaReserva() {
        setTitle("Reservar Habitación");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel lblCliente = new JLabel("Nombre cliente:");
        JTextField txtCliente = new JTextField(12);

        JLabel lblDias = new JLabel("Días:");
        JTextField txtDias = new JTextField(5);

        comboHabitaciones = new JComboBox<>();
        actualizarHabitaciones();

        JButton btnReservar = new JButton("Reservar");

        add(lblCliente); add(txtCliente);
        add(lblDias); add(txtDias);
        add(new JLabel("Seleccione habitación:")); add(comboHabitaciones);
        add(btnReservar);

        btnReservar.addActionListener(e -> {
            List<Habitacion> disponibles = Main.habitacionesDisponibles();
            int indice = comboHabitaciones.getSelectedIndex();

            if (disponibles.isEmpty() || indice == -1) {
                JOptionPane.showMessageDialog(this, "No hay habitaciones disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // salir si no hay habitaciones
            }

            try {
                String cliente = txtCliente.getText();
                int dias = Integer.parseInt(txtDias.getText());

                Habitacion seleccionada = disponibles.get(indice);
                seleccionada.disponible = false;
                Persistencia.guardar(Main.habitaciones, Main.ARCHIVO_HABITACIONES);

                Reserva reserva = new Reserva(cliente, seleccionada, dias);

                // Crear venta con número de habitación
                Venta v = Main.procesarPagoGUI(reserva.total, "Reserva", new ArrayList<>(), new ArrayList<>(), seleccionada.numero);


                JOptionPane.showMessageDialog(this,
                        "Reserva realizada:\n" + reserva + "\n\nVenta:\n" + v);

                actualizarHabitaciones(); // actualizar lista tras reservar

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en los datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private void actualizarHabitaciones() {
        comboHabitaciones.removeAllItems();
        List<Habitacion> disponibles = Main.habitacionesDisponibles();

        if (disponibles.isEmpty()) {
            comboHabitaciones.addItem("No hay habitaciones disponibles");
            comboHabitaciones.setEnabled(false); // deshabilitar selección
        } else {
            comboHabitaciones.setEnabled(true); // habilitar selección
            for (Habitacion h : disponibles) {
                comboHabitaciones.addItem(h.numero + " - " + h.tipo + " - $" + h.precio);
            }
        }
    }
}

