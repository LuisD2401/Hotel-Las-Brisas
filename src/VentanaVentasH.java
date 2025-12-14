import javax.swing.*;
import java.awt.*;

public class VentanaVentasH extends JFrame {

    public VentanaVentasH() {
        setTitle("Ventas de Habitaciones");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));

        if (Main.ventasHabitaciones.isEmpty()) {
            area.setText("No hay reservas registradas.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (VentaHabitacion v : Main.ventasHabitaciones) {
                sb.append(v).append("\n");
                sb.append("---------------------------------\n");
            }
            area.setText(sb.toString());
        }

        add(new JScrollPane(area));
        setVisible(true);
    }
}

