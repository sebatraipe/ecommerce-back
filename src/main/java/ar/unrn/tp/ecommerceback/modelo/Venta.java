package ar.unrn.tp.ecommerceback.modelo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "ventas")
@NoArgsConstructor
@Data
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_venta")
    private List<ProductoVendido> productoVendidos;
    @Column(name = "monto_total")
    private double montoTotal;

    public Venta(LocalDateTime fechaHora, Cliente cliente, List<Producto> producto, double montoTotal) {
        this.fechaHora = fechaHora;
        this.cliente = cliente;
        this.productoVendidos = this.convertirProducto(producto);
        this.montoTotal = montoTotal;
    }

    private List<ProductoVendido> convertirProducto(List<Producto> producto) {
        return producto.stream()
                .map(prod -> prod.obtenerProductoVendido(this))
                .collect(Collectors.toList());
    }

    public boolean verificarVentaPorFecha(LocalDateTime date) {
        return this.fechaHora.getDayOfMonth() == date.getDayOfMonth() &&
                this.fechaHora.getDayOfWeek() == date.getDayOfWeek() &&
                this.fechaHora.getDayOfYear() == date.getDayOfYear();
    }
}
