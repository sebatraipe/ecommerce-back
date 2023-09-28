package ar.unrn.tp.ecommerceback.servicios;

import ar.unrn.tp.ecommerceback.api.VentaService;
import ar.unrn.tp.ecommerceback.exceptions.NotEmptyException;
import ar.unrn.tp.ecommerceback.modelo.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    @PersistenceContext
    private EntityManager entityManager;
    private ServicioValidadorTarjeta servicioValidadorTarjeta = new ServicioTarjetaCredito("Validar Tarjeta Online");

//    public VentaServiceImpl(EntityManagerFactory entityManagerFactory, ServicioValidadorTarjeta servicioValidadorTarjeta) {
//        this.entityManagerFactory = entityManagerFactory;
//        this.servicioValidadorTarjeta = servicioValidadorTarjeta;
//    }

    @Override
    @Transactional
    public void realizarVenta(Long idCliente, List<Long> productos, Long idTarjeta) {
        try {
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

            CarritoCompras carritoCompras = new CarritoCompras(cliente, productosListado, descuentos,
                    this.servicioValidadorTarjeta);
            Venta venta = carritoCompras.realizarVenta(tarjetaCredito);
            entityManager.persist(venta);

            entityManager.clear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
}
