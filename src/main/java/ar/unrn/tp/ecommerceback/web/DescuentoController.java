package ar.unrn.tp.ecommerceback.web;

import ar.unrn.tp.ecommerceback.api.DescuentoService;
import ar.unrn.tp.ecommerceback.modelo.Descuento;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class DescuentoController {

    private DescuentoService descuentoService;

    public DescuentoController(DescuentoService descuentoService) {
        this.descuentoService = descuentoService;
    }

    @GetMapping("descuentos")
    private List<Descuento> getAllDiscount() {
        return this.descuentoService.descuentos();
    }
}
