import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompraCafeteria implements Serializable {
    List<ProductoCafeteria> productos = new ArrayList<>();

    public void agregarProducto(ProductoCafeteria p) {
        productos.add(p);
    }

    public double calcularTotal() {
        return productos.stream().mapToDouble(p -> p.precio).sum();
    }

    public void mostrarCompra() {
        System.out.println("\n----- Compra en Cafetería -----");
        for (ProductoCafeteria p : productos) {
            System.out.println("- " + p.nombre + ": $" + p.precio);
        }
        System.out.println("Total: $" + calcularTotal());
    }
}

