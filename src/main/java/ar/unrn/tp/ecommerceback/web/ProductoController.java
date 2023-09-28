package ar.unrn.tp.ecommerceback.web;

import ar.unrn.tp.ecommerceback.api.ProductoService;
import ar.unrn.tp.ecommerceback.modelo.Cliente;
import ar.unrn.tp.ecommerceback.modelo.Producto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductoController {

    private ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("productos")
    public List<Producto> getAllProducts() {
        return this.productoService.listarProductos();
    }

    @PostMapping("productos")
    public void createProduct(@RequestBody Producto producto) {
        this.productoService.crearProducto(producto.getCodigo(), producto.getDescripcion(), producto.precio(),
                producto.getCategoria().getId(), producto.getMarca().getId());
    }

    @PutMapping("productos")
    public void updateProduct(@RequestBody Producto producto) {
        this.productoService.modificarProducto(producto.getId(), producto.getDescripcion(), producto.precio(),
                producto.getCategoria().getId());
    }
}
