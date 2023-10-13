package ar.unrn.tp.ecommerceback.api;

import ar.unrn.tp.ecommerceback.modelo.Producto;

import java.util.List;

public interface ProductoService {

    //Validar que sea una categoria existente y que codigo no se repita
    void crearProducto(String codigo, String descripcion, double precio, Long idCategoria, Long idMarca);

    //Validar que sea un producto existente
    void modificarProducto(Long idProducto, String descripcion, double precio, Long idCategoria, String marca,
                           int version);

    //Devuelve todos los productos
    List listarProductos();

    Producto getProduct(Long id);
}
