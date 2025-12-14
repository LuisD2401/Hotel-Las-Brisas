import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    public double monto;
    public String tipo;
    public String medioPago;
    public String tipoDocumento;
    public LocalDateTime fecha;
    public int numeroHabitacion;

    public List<ProductoCafeteria> productos;
    public List<Integer> cantidades;

    public Venta(double monto, String tipo, String medioPago,
                 String tipoDocumento, int numeroHabitacion,
                 List<ProductoCafeteria> productos, List<Integer> cantidades) {
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
        return tipo + " - $" + monto + " - " + medioPago + " - " + fecha;
    }
}