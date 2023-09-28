package ar.unrn.tp.ecommerceback.modelo;

public class ServicioTarjetaCredito implements ServicioValidadorTarjeta {

    private String nombreServicio;

    public ServicioTarjetaCredito(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    @Override
    public boolean validarTarjeta(TarjetaCredito tarjetaCredito) {
        System.out.println("El " + nombreServicio + " fue exitoso para la tarjeta " + tarjetaCredito.getDescripcion());
        return true;
    }
}
