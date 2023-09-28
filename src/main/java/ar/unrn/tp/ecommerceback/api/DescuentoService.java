package ar.unrn.tp.ecommerceback.api;

import java.time.LocalDate;
import java.util.List;

public interface DescuentoService {

    //Validar que la fecha no se superpongan
    void crearDescuentoSobreTotal(String marcaTarjeta, LocalDate fechaDesde,
                                  LocalDate fechaHasta, double porcentaje);

    //Validar que la fecha no se superpongan
    void crearDescuento(String marcaProducto, LocalDate fechaDesde, LocalDate fechaHasta, double porcentaje);

    List descuentos();
}
