import javax.swing.*;
import java.awt.*;

public class CategoriasCafeteria extends JFrame {
    private Carrito carrito = new Carrito(); // carrito compartido

    public CategoriasCafeteria(Carrito carrito) {
        this .carrito = carrito;
        setTitle("Categorías Cafetería");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel central para los botones de categorías
        JPanel panelCategorias = new JPanel(new GridLayout(2, 3, 20, 20));
        panelCategorias.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Agregar botones de categorías
        agregarBotonCategoria(panelCategorias, "Bebidas Calientes");
        agregarBotonCategoria(panelCategorias, "Bebidas Frías");
        agregarBotonCategoria(panelCategorias, "Repostería/Panadería");
        agregarBotonCategoria(panelCategorias, "Snacks");
        agregarBotonCategoria(panelCategorias, "Postres");
        agregarBotonCategoria(panelCategorias, "Dulces");

        add(panelCategorias, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelSur = new JPanel();

        // Botón Ver Carrito
        JButton btnVerCarrito = new JButton("Ver Carrito");
        btnVerCarrito.setFont(new Font("Arial", Font.BOLD, 20));
        btnVerCarrito.addActionListener(e -> {
            carrito.setVisible(true); // muestra la ventana del carrito
        });
        panelSur.add(btnVerCarrito);

        // Botón Volver al menú principal
        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 20));
        btnVolver.addActionListener(e -> {
            dispose(); // cierra la ventana actual
            new MenuCafeteria(); // abre el menú principal de cafetería
        });
        panelSur.add(btnVolver);

        add(panelSur, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void agregarBotonCategoria(JPanel panel, String categoria) {
        final JFrame padre = this; // necesario para lambda
        JButton btn = new JButton(categoria);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setBackground(new Color(180, 220, 240));
        btn.setPreferredSize(new Dimension(180, 180));
        btn.setFocusPainted(false);

        btn.addActionListener(e -> {
            // Abrimos la ventana de productos pasando el carrito existente y la ventana padre
            new VentanaCafeteria(categoria, padre, carrito);
            padre.setVisible(false);
        });

        panel.add(btn);
    }
}

