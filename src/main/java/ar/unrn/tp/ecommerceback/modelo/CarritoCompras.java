package ar.unrn.tp.ecommerceback.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CarritoCompras {

    private Cliente cliente;
    private List<Producto> productos;
    private List<Descuento> descuentos;
    private ServicioValidadorTarjeta servicioValidadorTarjeta;

    public CarritoCompras(Cliente cliente, List<Producto> productos, List<Descuento> descuentos,
                          ServicioValidadorTarjeta servicioValidadorTarjeta) {
        this.cliente = cliente;
        this.productos = productos;
        this.descuentos = descuentos;
        this.servicioValidadorTarjeta = servicioValidadorTarjeta;
        System.out.println(this.servicioValidadorTarjeta);
    }

    public double montoTotal(TarjetaCredito tarjetaCredito) {

        double montoTotal = this.calcularMontoTotal();

        double descuentoTotal = this.descuentoTotal(tarjetaCredito);

        return montoTotal -= descuentoTotal;
    }

    private double calcularMontoTotal() {
        return this.productos.stream()
                .mapToDouble(Producto::precio)
                .sum();
    }

    private double descuentoTotal(TarjetaCredito tarjetaCredito) {
        return this.descuentos.stream()
                .filter(descuento -> descuento.estaActiva(LocalDate.now()))
                .mapToDouble(descuento -> descuento.calcularDescuento(this.productos, tarjetaCredito))
                .sum();
    }

    public Venta realizarVenta(TarjetaCredito tarjetaCredito, String numeroVenta) {
        if (!this.servicioValidadorTarjeta.validarTarjeta(tarjetaCredito)) {
            throw new RuntimeException("La tarjeta falló...");
        }
        double montoTotal = this.calcularMontoTotal();
        double descuentoTotal = this.descuentoTotal(tarjetaCredito);
        montoTotal -= descuentoTotal;

        return new Venta(LocalDateTime.now(), this.cliente, this.productos, montoTotal, numeroVenta);
    }
}
