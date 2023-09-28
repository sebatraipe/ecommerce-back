package ar.unrn.tp.ecommerceback.modelo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tarjetas_credito", uniqueConstraints = @UniqueConstraint(columnNames = "numero"))
@NoArgsConstructor
@Data
public class TarjetaCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numero;
    private String descripcion;

    public TarjetaCredito(String numero, String descripcion) {
        this.numero = numero;
        this.descripcion = descripcion;
    }

    public boolean verificarTarjeta(String tarjetaCredito) {
        return this.descripcion.equals(tarjetaCredito);
    }

    public boolean verificarNumero(String numero) {
        return this.numero.equals(numero);
    }

    public boolean verificar(String numeroTarjeta, String descripcionTarjeta) {
        return this.numero.equals(numeroTarjeta) && this.descripcion.equals(descripcionTarjeta);
    }
}
