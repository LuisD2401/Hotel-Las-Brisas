import java.io.Serializable;

public class ProductoCafeteria implements Serializable {
    String nombre;
    double precio;
    int stock; // nueva variable

    public ProductoCafeteria(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    @Override
    public String toString() {
        return nombre + " - $" + precio + " - Stock: " + stock;
    }
}