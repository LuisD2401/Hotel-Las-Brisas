import java.io.Serializable;

public class ProductoCafeteria implements Serializable {
    String nombre;
    double precio;
    int stock;
    String categoria;// nueva variable

    public ProductoCafeteria(String nombre, double precio, int stock, String categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria.trim().toLowerCase();
    }

    @Override
    public String toString() {
        return nombre + " - $" + precio + " - Stock: " + stock + " - " + categoria;
    }
}
