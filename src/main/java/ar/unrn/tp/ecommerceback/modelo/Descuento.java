package ar.unrn.tp.ecommerceback.modelo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Descuentos")
@Inheritance
@NoArgsConstructor
@Data
@DiscriminatorColumn(name = "tipo_descuento")
public abstract class Descuento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "fecha_inicio")
    protected LocalDate fechaInicio;
    @Column(name = "fecha_fin")
    protected LocalDate fechaFin;
    protected double porcentaje;

    public Descuento(LocalDate fechaInicio, LocalDate fechaFin, double porcentaje) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.porcentaje = porcentaje;
    }

    protected abstract boolean estaActiva(LocalDate fecha);

    protected abstract void validarFecha(LocalDate fechaInicio, LocalDate fechaFin);

    protected abstract double calcularDescuento(List<Producto> productos, TarjetaCredito tarjetaCredito);
}
