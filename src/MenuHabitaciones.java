import javax.swing.*;
import java.awt.*;

class MenuHabitaciones extends JFrame {

    public MenuHabitaciones() {
        setTitle("Menú Habitaciones");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JButton btnReservar = new JButton("Reservar Habitación");
        JButton btnGestionar = new JButton("Gestionar Habitaciones");
        JButton btnVentas = new JButton("Ver Ventas");
        JButton btnMenu = new JButton("Volver al Menú Principal");
        JButton btnSalir = new JButton("Salir");

        add(btnReservar);
        add(btnGestionar);
        add(btnVentas);
        add(btnMenu);
        add(btnSalir);

        // Eventos
        btnReservar.addActionListener(e -> new VentanaReserva());

        btnGestionar.addActionListener(e -> {
            String password = JOptionPane.showInputDialog(this, "Ingrese contraseña del recepcionista:");
            if (!"marco123".equals(password)) {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.");
                return;
            }
            new HotelGUI().gestionarHabitaciones(); //Necesita que hagas pública la función
        });

        btnVentas.addActionListener(e -> {
            String password = JOptionPane.showInputDialog(this, "Ingrese contraseña del recepcionista:");
            if (!"marco123".equals(password)) {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.");
                return;
            }
            new VentanaVentas();
        });

        btnMenu.addActionListener(e -> {
            dispose();
            new HotelGUI();
        });

        btnSalir.addActionListener(e -> System.exit(0));

        setVisible(true);
    }
}
