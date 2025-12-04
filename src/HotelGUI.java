import javax.swing.*;
import java.awt.*;

public class HotelGUI extends JFrame {

    public HotelGUI() {
        setTitle("APLICACIÓN HOTEL LAS BRISAS");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1, 20, 20));

        JButton btnHabitaciones = new JButton("Gestión de Habitaciones");
        JButton btnCafeteria = new JButton("Cafetería");

        btnHabitaciones.setFont(new Font("Arial", Font.BOLD, 18));
        btnCafeteria.setFont(new Font("Arial", Font.BOLD, 18));

        add(btnHabitaciones);
        add(btnCafeteria);

        // Eventos
        btnHabitaciones.addActionListener(e -> new MenuHabitaciones());
        btnCafeteria.addActionListener(e -> new MenuCafeteria());

        setVisible(true);
    }
}

