import javax.swing.*;
import java.awt.*;

public class HotelGUI extends JFrame {

    public HotelGUI() {
        setTitle("APLICACION HOTEL LAS BRISAS");
        setSize(1500, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ======================
        //     PANEL SUPERIOR
        // ======================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setPreferredSize(new Dimension(1500, 400));
        panelSuperior.setBackground(Color.cyan);
        panelSuperior.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // Logo del hotel
        ImageIcon iconoHotel = new ImageIcon(getClass().getResource("/imagen/hotel-las-brisas.png"));
        Image imgEscalada = iconoHotel.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        JLabel lblHotel = new JLabel(new ImageIcon(imgEscalada));
        panelSuperior.add(lblHotel);

        add(panelSuperior, BorderLayout.NORTH);


        // ======================
        //     PANEL CENTRAL
        // ======================
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridLayout(2, 1, 20, 40));
        panelCentral.setBackground(Color.cyan);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(80, 300, 80, 300)); // Empuja más abajo

        // ----- Icono Habitaciones -----
        ImageIcon iconHab = new ImageIcon(getClass().getResource("/imagen/habitacion.png"));
        Image iconHabEsc = iconHab.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        JButton btnHabitaciones = new JButton("Habitaciones", new ImageIcon(iconHabEsc));
        btnHabitaciones.setFont(new Font("Arial", Font.BOLD, 22));
        btnHabitaciones.setHorizontalAlignment(SwingConstants.LEFT);
        btnHabitaciones.setIconTextGap(15);

        // ----- Icono Cafetería -----
        ImageIcon iconCafe = new ImageIcon(getClass().getResource("/imagen/cafeteria.png"));
        Image iconCafeEsc = iconCafe.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        JButton btnCafeteria = new JButton("Cafetería", new ImageIcon(iconCafeEsc));
        btnCafeteria.setFont(new Font("Arial", Font.BOLD, 22));
        btnCafeteria.setHorizontalAlignment(SwingConstants.LEFT);
        btnCafeteria.setIconTextGap(15);

        // Agregar al panel central
        panelCentral.add(btnHabitaciones);
        panelCentral.add(btnCafeteria);

        add(panelCentral, BorderLayout.CENTER);


        // ======================
        //      EVENTOS
        // ======================
        btnHabitaciones.addActionListener(e -> {
            dispose();              // cierra HotelGUI
            new MenuHabitaciones(); // abre Habitaciones
        });

        btnCafeteria.addActionListener(e -> {
            dispose();
            new MenuCafeteria();
        });

        setVisible(true);
    }
}

