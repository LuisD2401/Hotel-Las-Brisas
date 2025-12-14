import javax.swing.*;
import java.awt.*;

public class MenuCafeteria extends JFrame {

    public MenuCafeteria() {
        setTitle("Menú Cafetería");
        setSize(1500, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===============================
        //      PANEL SUPERIOR (HEADER)
        // ===============================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(178, 224, 216));
        panelSuperior.setPreferredSize(new Dimension(1500, 150));
        panelSuperior.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // Ícono del encabezado
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/imagen/cafeteria.png"));
            Image img = icono.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel lblIcono = new JLabel(new ImageIcon(img));
            panelSuperior.add(lblIcono);
        } catch (Exception e) {
            System.out.println("⚠ Icono cafeteria.png no encontrado");
        }

        // Texto del encabezado
        JLabel lblTitulo = new JLabel("Cafetería Hotel Las Brisas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 50));
        lblTitulo.setForeground(Color.black);
        panelSuperior.add(lblTitulo);

        add(panelSuperior, BorderLayout.NORTH);

        // ===============================
        //  PANEL CENTRAL (BOTONES + FONDO DEGRADADO)
        // ===============================
        JPanel panel = crearPanelDegradado(
                new Color(0, 255, 255), // arriba cyan
                new Color(0, 150, 255)  // abajo azul
        );

        panel.setLayout(new GridLayout(5, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(60, 200, 60, 200));

        // Botones
        JButton btnComprar = crearBoton("Comprar en Cafetería", "pago.png");
        JButton btnGestionar = crearBoton("Gestionar Productos", "listadoPago.png");
        JButton btnVentas = crearBoton("Ver Ventas", "recoleccion-de-datos.png");
        JButton btnMenu = crearBoton("Volver al Menú Principal", "volver.png");
        JButton btnSalir = crearBoton("Salir", "cancelar.png");

        panel.add(btnComprar);
        panel.add(btnGestionar);
        panel.add(btnVentas);
        panel.add(btnMenu);
        panel.add(btnSalir);

        add(panel, BorderLayout.CENTER);

        // ===============================
        //            ACCIONES
        // ===============================
        btnComprar.addActionListener(e -> {
            dispose(); // Cierra esta ventana

            // Crear carrito compartido
            Carrito carrito = new Carrito();

            // Abrir ventana de categorías pasándole el carrito
            CategoriasCafeteria categorias = new CategoriasCafeteria(carrito);
            categorias.setVisible(true);

            // Abrir también la ventana del carrito
            carrito.setVisible(true);
        });


        btnGestionar.addActionListener(e -> {
            String password = JOptionPane.showInputDialog(this, "Ingrese contraseña del recepcionista:");
            if (!"brisitas2005".equals(password)) {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.");
                return;
            }
            VentanaCafeteria.gestionarProductosEstatico();
        });

        btnVentas.addActionListener(e -> {
            String password = JOptionPane.showInputDialog(this, "Ingrese contraseña del vendedor:");
            if (!"brisitas2005".equals(password)) {
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


    // ============================================================
    //      MÉTODO PARA CREAR PANEL CON FONDO DEGRADADO
    // ============================================================
    private JPanel crearPanelDegradado(Color c1, Color c2) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                int w = getWidth();
                int h = getHeight();

                GradientPaint gp = new GradientPaint(
                        0, 0, c1,     // color arriba
                        0, h, c2      // color abajo
                );

                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
    }


    // ============================================================
    // MÉTODO PARA CREAR BOTONES BONITOS CON ICONOS
    // ============================================================
    private JButton crearBoton(String texto, String iconoNombre) {
        JButton btn = new JButton(texto);

        btn.setFont(new Font("Arial", Font.BOLD, 26));

        try {
            ImageIcon imagen = new ImageIcon(getClass().getResource("/imagen/" + iconoNombre));
            Image img = imagen.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("⚠ Icono no encontrado: " + iconoNombre);
        }

        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(20);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        return btn;
    }
}
