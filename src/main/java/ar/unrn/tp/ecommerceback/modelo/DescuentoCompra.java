package ar.unrn.tp.ecommerceback.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@DiscriminatorValue("DC")
@NoArgsConstructor
@Data
public class DescuentoCompra extends Descuento {

    private String tarjeta;

    public DescuentoCompra(LocalDate fechaInicio, LocalDate fechaFin, double porcentaje, String tarjeta) {
        super(fechaInicio, fechaFin, porcentaje);
        this.tarjeta = tarjeta;
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

    public double calcularDescuento(List<Producto> productos, TarjetaCredito tarjetaCredito) {
        double montoTotal = 0;
        if (tarjetaCredito.verificarTarjeta(this.tarjeta)) {
            montoTotal = productos.stream()
                    .mapToDouble(Producto::precio)
                    .sum();
        }
        return montoTotal * this.porcentaje;
    }
}
