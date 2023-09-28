package ar.unrn.tp.ecommerceback.api;

import java.util.List;

public interface ClienteService {

    //Validar que el dni no se repita
    void crearCliente(String nombre, String apellido, String dni, String email);

    //Validar que sea un cliente existente
    void modificarCliente(Long idCliente, String nombre, String apellido);

    //Validar que sea un cliente existente
    void agregarTarjeta(Long idCliente, String numero, String marca);

    //Devuelve las tarjetas de un cliente especifico
    List listarTarjetas(Long idCliente);

    List listarClientes();
}
