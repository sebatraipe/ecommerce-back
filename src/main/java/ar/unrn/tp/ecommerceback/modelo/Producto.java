package ar.unrn.tp.ecommerceback.modelo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos", uniqueConstraints = @UniqueConstraint(columnNames = "codigo"))
@NoArgsConstructor
@Data
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private String descripcion;
    private double precio;
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca marca;

    public Producto(String codigo, String descripcion, double precio, Categoria categoria, Marca marca) {

        this.verificarProducto(codigo, descripcion, precio, categoria, marca);
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.marca = marca;
    }

    private void verificarProducto(String codigo, String descripcion, double precio, Categoria categoria, Marca marca) {
        if (codigo == null || descripcion == null || precio <= 0 || categoria == null || marca == null) {
            throw new RuntimeException("Todos los valores del producto deben ser válidos...");
        }
    }

    public double precio() {
        return this.precio;
    }

    public boolean verificarMarca(String marcaProducto) {
        return this.marca.verificarMarca(marcaProducto);
    }

    public double descuento(double porcentaje) {
        return this.precio * porcentaje;
    }

    public ProductoVendido obtenerProductoVendido(Venta venta) {
        return new ProductoVendido(null, this.codigo, this.descripcion, this.precio, this.categoria, this.marca, venta);
    }

    private void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    private void setPrecio(double precio) {
        this.precio = precio;
    }

    private void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void modificarDatos(String descripcion, double precio, Categoria categoria) {
        this.setDescripcion(descripcion);
        this.setPrecio(precio);
        this.setCategoria(categoria);
    }
}
