package ar.unrn.tp.ecommerceback.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@DiscriminatorValue("DP")
@NoArgsConstructor
@Data
public class DescuentoProducto extends Descuento {

    @Column(name = "marca_producto")
    private String marcaProducto;

    public DescuentoProducto(LocalDate fechaInicio, LocalDate fechaFin, double porcentaje, String marcaProducto) {
        super(fechaInicio, fechaFin, porcentaje);
        this.marcaProducto = marcaProducto;
    }

    @Override
    public double calcularDescuento(List<Producto> productos, TarjetaCredito tarjetaCredito) {
        return productos.stream()
                .filter(p -> p.verificarMarca(this.marcaProducto))
                .mapToDouble(p -> p.descuento(this.porcentaje))
                .sum();
    }

    @Override
    public boolean estaActiva(LocalDate fecha) {
        return fecha.isAfter(fechaInicio) && fecha.isBefore(fechaFin);
    }

    @Override
    public void validarFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio.isAfter(fechaFin) || fechaFin.isEqual(fechaInicio)) {
            throw new RuntimeException("Las fechas no son correctas...");
        }
    }
}
