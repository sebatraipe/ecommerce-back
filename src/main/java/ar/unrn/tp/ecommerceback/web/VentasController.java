package ar.unrn.tp.ecommerceback.web;

import ar.unrn.tp.ecommerceback.api.VentaService;
import ar.unrn.tp.ecommerceback.exceptions.NotEmptyException;
import ar.unrn.tp.ecommerceback.modelo.Producto;
import ar.unrn.tp.ecommerceback.modelo.Venta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/")
public class VentasController {

    private VentaService ventaService;

    public VentasController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping("ventas")
    public List<Venta> getAllSales() {
        return this.ventaService.ventas();
    }

    @GetMapping("ventas/{idCard}")
    public ResponseEntity<?> makeSale(@PathVariable Long idCard, @RequestParam List<Long> productoIds,
                                                        @RequestParam Long clienteId) throws Exception, NotEmptyException {

        this.ventaService.realizarVenta(clienteId, productoIds, idCard);
        Map<String, String> response = new HashMap<>();
        response.put("message", "La venta se realizó con éxito.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("ventas/montoTotal/{idCard}")
    public double calculateAmount(@PathVariable Long idCard,
                                  @RequestParam List<Long> productoIds,
                                  @RequestParam Long clienteId) {
        return this.ventaService.calcularMonto(productoIds, idCard, clienteId);
    }
}
