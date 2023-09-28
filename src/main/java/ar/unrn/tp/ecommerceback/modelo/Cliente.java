package ar.unrn.tp.ecommerceback.modelo;

import ar.unrn.tp.ecommerceback.exceptions.NotNullException;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "clientes", uniqueConstraints = @UniqueConstraint(columnNames = {"dni"}))
@NoArgsConstructor
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente")
    private List<TarjetaCredito> tarjetasCredito;

    public Cliente(String nombre, String apellido, String dni, String email, List<TarjetaCredito> tarjetasCredito) {
        this.validarCliente(nombre, apellido, dni, email, tarjetasCredito);
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.tarjetasCredito = tarjetasCredito;
    }

    public Cliente(String nombre, String apellido, String dni, String email) {
        this.validarCliente(nombre, apellido, dni, email);
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
    }

    private void validarCliente(String nombre, String apellido, String dni, String email, List<TarjetaCredito> tarjetaCreditos) {
        if (nombre == null || apellido == null || dni == null || email == null || tarjetaCreditos.isEmpty()){
            throw new RuntimeException("Todos los valores del cliente deben ser validos...");
        }
    }
    private void validarCliente(String nombre, String apellido, String dni, String email) {
        if (nombre == null || apellido == null || dni == null || email == null){
            throw new NotNullException("Todos los valores del cliente deben ser validos.");
        }
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    private void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void modificarDatos(String nombre, String apellido) {
        this.setNombre(nombre);
        this.setApellido(apellido);
    }

    public void addTarjeta(TarjetaCredito tarjetaCredito) {
        this.tarjetasCredito.add(tarjetaCredito);
    }

    public boolean seLlama(String nombre) {
        return this.nombre.equals(nombre);
    }

    public boolean suDniEs(String dni) {
        return this.dni.equals(dni);
    }

    public boolean suTarjetaEs(String tarjetaNumero) {
        return this.tarjetasCredito.stream()
                .anyMatch(tarjetaCredito -> tarjetaCredito.verificarNumero(tarjetaNumero));
    }

    public boolean suApellidoEs(String apellido) {
        return this.apellido.equals(apellido);
    }

    public boolean tieneTarjeta(String numeroTarjeta, String descripcionTarjeta) {
        return this.tarjetasCredito.stream()
                .anyMatch(tarjetaCredito -> tarjetaCredito.verificar(numeroTarjeta, descripcionTarjeta));
    }

    public List<TarjetaCredito> tarjetas() {
        return this.tarjetasCredito;
    }
}
