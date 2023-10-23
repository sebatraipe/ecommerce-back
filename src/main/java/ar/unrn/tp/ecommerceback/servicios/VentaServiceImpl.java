package ar.unrn.tp.ecommerceback.servicios;

import ar.unrn.tp.ecommerceback.api.CacheVentaService;
import ar.unrn.tp.ecommerceback.api.VentaService;
import ar.unrn.tp.ecommerceback.exceptions.NotEmptyException;
import ar.unrn.tp.ecommerceback.modelo.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    @PersistenceContext
    private EntityManager entityManager;
    private ServicioValidadorTarjeta servicioValidadorTarjeta = new ServicioTarjetaCredito("Validar Tarjeta Online");
    private CacheVentaService cacheVentaService = new CacheVentaServiceImpl();

//    public VentaServiceImpl(EntityManagerFactory entityManagerFactory, ServicioValidadorTarjeta servicioValidadorTarjeta) {
//        this.entityManagerFactory = entityManagerFactory;
//        this.servicioValidadorTarjeta = servicioValidadorTarjeta;
//    }

    @Override
    @Transactional
    public void realizarVenta(Long idCliente, List<Long> productos, Long idTarjeta) {
        List<Descuento> descuentos = entityManager.createQuery(
                        "select d from Descuento d", Descuento.class)
                .getResultList();

        TarjetaCredito tarjetaCredito = entityManager.find(TarjetaCredito.class, idTarjeta);

        Cliente cliente = entityManager.find(Cliente.class, idCliente);

        List<Producto> productosListado = entityManager.createQuery(
                        "select p from Producto p where p.id in :productos", Producto.class)
                .setParameter("productos", productos)
                .getResultList();

        if (productosListado.isEmpty()) {
            throw new NotEmptyException("No se encontraron productos.");
        }

        NumeroSiguiente numeroSiguiente = this.obtenerNumeroSiguiente();

        //Genero el numero de venta unico N-AÑO
        String numeroVenta = numeroSiguiente.generarNumeroVenta();

        CarritoCompras carritoCompras = new CarritoCompras(cliente, productosListado, descuentos,
                this.servicioValidadorTarjeta);
        Venta venta = carritoCompras.realizarVenta(tarjetaCredito, numeroVenta);
        this.entityManager.persist(venta);
        this.incrementarNumeroActualEnLaBD(Year.now().getValue(), numeroSiguiente.recuperarSiguiente());
        this.cacheVentaService.limpiarCache(idCliente);
    }

    private void incrementarNumeroActualEnLaBD(int anio, int numeroSiguiente) {
        var query = entityManager.createQuery(
                        "update NumeroSiguiente ns set ns.actual = :numeroSiguiente where ns.anio = :anio")
                .setParameter("numeroSiguiente", numeroSiguiente)
                .setParameter("anio", anio);

        int actualizacion = query.executeUpdate();
        if (actualizacion == 0) {
            throw new RuntimeException("No se pudo actualizar el valor actual del número de la venta...");
        }
    }

    private NumeroSiguiente obtenerNumeroSiguiente() {
        int anio = Year.now().getValue();
        NumeroSiguiente numeroSiguiente = entityManager.createQuery(
                        "select ns from NumeroSiguiente ns where ns.anio = :anio", NumeroSiguiente.class)
                .setParameter("anio", anio)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

        if (numeroSiguiente == null) {
            numeroSiguiente = new NumeroSiguiente(Year.now().getValue(), 0);
            entityManager.persist(numeroSiguiente);
        }
        return numeroSiguiente;
    }

    @Override
    @Transactional
    public double calcularMonto(List<Long> productos, Long idTarjeta, Long idCliente) {
        try {
            TarjetaCredito tarjetaCredito = entityManager.find(TarjetaCredito.class, idTarjeta);
            if (tarjetaCredito == null) {
                throw new RuntimeException("La tarjeta no existe...");
            }
            List<Descuento> descuentos = entityManager.createQuery("select d from Descuento d", Descuento.class)
                    .getResultList();
            List<Producto> productosList = entityManager.createQuery(
                            "select p from Producto p where p.id in :productos", Producto.class)
                    .setParameter("productos", productos)
                    .getResultList();
            Cliente cliente = entityManager.find(Cliente.class, idCliente);
            CarritoCompras carritoCompras = new CarritoCompras(cliente, productosList, descuentos, servicioValidadorTarjeta);
            entityManager.clear();
            return carritoCompras.montoTotal(tarjetaCredito);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public List ventas() {
        try {
            List<Venta> ventas = entityManager.createQuery(
                    "select v from Venta v", Venta.class).getResultList();
            if (ventas == null)
                throw new RuntimeException("No hay ventas...");
            return ventas;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List ventasRecientes(Long idCliente) {
        List<Venta> ventas = new ArrayList<>();
        List<Venta> ventasCache = new ArrayList<>();
//        this.cacheVentaService.limpiarCache(idCliente);

        //Traigo las ventas desde caché
        ventasCache = this.cacheVentaService.listarVentasDeUnCliente(idCliente);
        if (ventasCache.size() > 0) {
            System.out.println("Tamaño venta cache: " + ventasCache.size());
            return ventasCache;
        }

        ventas.addAll(this.entityManager.createQuery("SELECT v FROM Venta v JOIN FETCH v.productoVendidos " +
                        "WHERE v.cliente.id = :idCliente ORDER BY v.fechaHora DESC LIMIT 3", Venta.class)
                .setParameter("idCliente", idCliente)
                .getResultList());
        System.out.println("Tamaño venta: " + ventas.size());

        this.cacheVentaService.agregarUltimasVentas(ventas, idCliente);
        return ventas;
    }
}
