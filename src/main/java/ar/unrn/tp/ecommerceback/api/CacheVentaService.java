package ar.unrn.tp.ecommerceback.api;

import ar.unrn.tp.ecommerceback.modelo.Venta;

import java.util.List;

public interface CacheVentaService {

    void agregarVenta(Venta venta);

    void agregarUltimasVentas(List<Venta> ventas, Long idCliente);

    void limpiarCache(Long idCliente);

    List<Venta> listarVentasDeUnCliente(Long idCliente);
}
