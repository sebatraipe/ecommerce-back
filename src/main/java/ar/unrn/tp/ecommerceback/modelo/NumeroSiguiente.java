package ar.unrn.tp.ecommerceback.modelo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "numeros_siguientes")
@Data
@NoArgsConstructor
public class NumeroSiguiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int anio;
    private int actual;

    public NumeroSiguiente(int anio, int actual) {
        this.anio = anio;
        this.actual = actual;
    }

    public int recuperarSiguiente() {
        this.actual += 1;
        return this.actual;
    }

    public String generarNumeroVenta() {
        return this.actual + "-" +this.anio;
    }
}
