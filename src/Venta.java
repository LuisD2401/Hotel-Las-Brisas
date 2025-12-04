import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta implements Serializable {

    double monto;
    String tipo;
    String medioPago;
    String tipoDocumento;
    LocalDateTime fecha;
    int numeroHabitacion;

    public List<ProductoCafeteria> productos;
    public List<Integer> cantidades;

    // Constructor original
    public Venta(double monto, String tipo, String medioPago, String tipoDocumento, int numeroHabitacion) {
        this.monto = monto;
        this.tipo = tipo;
        this.medioPago = medioPago;
        this.tipoDocumento = tipoDocumento;
        this.fecha = LocalDateTime.now();
        this.numeroHabitacion = numeroHabitacion;
        this.productos = new ArrayList<>();
        this.cantidades = new ArrayList<>();
    }

    // Constructor sin habitación
    public Venta(double monto, String tipo, String medioPago, String tipoDocumento) {
        this(monto, tipo, medioPago, tipoDocumento, -1);
    }

    // ✔ NUEVO CONSTRUCTOR para cafetería o reservas con detalle
    public Venta(double monto, String tipo, String medioPago, String tipoDocumento,
                 int numeroHabitacion, List<ProductoCafeteria> productos, List<Integer> cantidades) {

        this.monto = monto;
        this.tipo = tipo;
        this.medioPago = medioPago;
        this.tipoDocumento = tipoDocumento;
        this.fecha = LocalDateTime.now();
        this.numeroHabitacion = numeroHabitacion;

        this.productos = new ArrayList<>(productos);
        this.cantidades = new ArrayList<>(cantidades);
    }

    @Override
    public String toString() {
        return tipo + " - $" + monto + " - " + medioPago + " - " + tipoDocumento + " - " + fecha;
    }
}


