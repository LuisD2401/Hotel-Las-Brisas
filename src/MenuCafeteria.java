import javax.swing.*;
import java.awt.*;

class MenuCafeteria extends JFrame {

    public MenuCafeteria() {
        setTitle("Menú Cafetería");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JButton btnComprar = new JButton("Comprar en Cafetería");
        JButton btnGestionar = new JButton("Gestionar Productos");
        JButton btnVentas = new JButton("Ver Ventas");
        JButton btnMenu = new JButton("Volver al Menú Principal");
        JButton btnSalir = new JButton("Salir");

        add(btnComprar);
        add(btnGestionar);
        add(btnVentas);
        add(btnMenu);
        add(btnSalir);

        // Abrir ventana de compras normalmente
        btnComprar.addActionListener(e -> {
            VentanaCafeteria v = new VentanaCafeteria();
            v.setVisible(true);
        });

        // Abrir solo gestión SIN crear la ventana de compras
        btnGestionar.addActionListener(e -> {
            String password = JOptionPane.showInputDialog(this, "Ingrese contraseña del recepcionista:");
            if (!"marco123".equals(password)) {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.");
                return;
            }

            VentanaCafeteria.gestionarProductosEstatico();
        });

        btnVentas.addActionListener(e -> {
            String password = JOptionPane.showInputDialog(this, "Ingrese contraseña del vendedor:");
            if (!"marco123".equals(password)) {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.");
                return;
            }
            new VentanaVentas();
        });

        btnMenu.addActionListener(e -> {
            dispose();      // ← Cierra correctamente esta ventana
            new HotelGUI(); // ← Abre solo una nueva
        });

        btnSalir.addActionListener(e -> System.exit(0));

        setVisible(true);
    }
}

