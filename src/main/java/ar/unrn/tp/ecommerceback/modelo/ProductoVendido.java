package ar.unrn.tp.ecommerceback.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos_vendidos")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ProductoVendido {

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
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_venta")
    private Venta venta;

//    public ProductoVendido(String codigo, String descripcion, double precio, Categoria categoria, Marca marca) {
//
//        this.verificarProductoVendido(codigo, descripcion, precio, categoria, marca);
//        this.codigo = codigo;
//        this.descripcion = descripcion;
//        this.precio = precio;
//        this.categoria = categoria;
//        this.marca = marca;
//    }
//
//    private void verificarProductoVendido(String codigo, String descripcion, double precio, Categoria categoria, Marca marca) {
//        if (codigo == null || descripcion == null || precio <= 0 || categoria == null || marca == null) {
//            throw new IllegalArgumentException("Todos los valores del producto deben ser válidos...");
//        }
//    }

    public double precio() {
        return this.precio;
    }

    public boolean verificarMarca(Marca marca) {
        return this.marca.equals(marca);
    }

    public double descuento(double descuento) {
        return this.precio * descuento;
    }
}
