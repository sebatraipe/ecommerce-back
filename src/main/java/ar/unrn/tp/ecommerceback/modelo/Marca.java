package ar.unrn.tp.ecommerceback.modelo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "marcas", uniqueConstraints = @UniqueConstraint(columnNames = "nombre"))
@NoArgsConstructor
@Data
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    public boolean verificarMarca(String marcaProducto) {
        return this.nombre.equals(marcaProducto);
    }
}
