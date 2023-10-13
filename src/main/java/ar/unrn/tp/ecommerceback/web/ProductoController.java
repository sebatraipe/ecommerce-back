package ar.unrn.tp.ecommerceback.web;

import ar.unrn.tp.ecommerceback.api.ProductoService;
import ar.unrn.tp.ecommerceback.exceptions.ConcurrencyException;
import ar.unrn.tp.ecommerceback.modelo.Cliente;
import ar.unrn.tp.ecommerceback.modelo.Producto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> getAllProducts() {
        return this.productoService.listarProductos();
    }

    @GetMapping("/{id}")
    public Producto getProduct(@PathVariable Long id) {
        return this.productoService.getProduct(id);
    }


    @PostMapping("productos")
    public void createProduct(@RequestBody Producto producto) {
        this.productoService.crearProducto(producto.getCodigo(), producto.getDescripcion(), producto.precio(),
                producto.getCategoria().getId(), producto.getMarca().getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Producto producto) throws ConcurrencyException {
        this.productoService.modificarProducto(id, producto.getDescripcion(), producto.precio(),
                producto.getCategoria().getId(), producto.getMarca().getNombre(), producto.getVersion());
        Map<String, String> response = new HashMap<>();
        response.put("message", "El producto se modificó con éxito.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
