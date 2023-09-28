package ar.unrn.tp.ecommerceback.servicios;

import ar.unrn.tp.ecommerceback.api.DescuentoService;
import ar.unrn.tp.ecommerceback.modelo.Cliente;
import ar.unrn.tp.ecommerceback.modelo.Descuento;
import ar.unrn.tp.ecommerceback.modelo.DescuentoCompra;
import ar.unrn.tp.ecommerceback.modelo.DescuentoProducto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DescuentoServiceImpl implements DescuentoService {

    @PersistenceContext
    private EntityManager entityManager;

//    public DescuentoServiceImpl(EntityManagerFactory entityManagerFactory) {
//        this.entityManagerFactory = entityManagerFactory;
//    }

    @Override
    @Transactional
    public void crearDescuentoSobreTotal(String marcaTarjeta, LocalDate fechaDesde, LocalDate fechaHasta,
                                         double porcentaje) {
        try {

            Descuento descuentoSobreTotal = new DescuentoCompra(fechaDesde, fechaHasta, porcentaje, marcaTarjeta);
            entityManager.persist(descuentoSobreTotal);
            entityManager.clear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void crearDescuento(String marcaProducto, LocalDate fechaDesde, LocalDate fechaHasta, double porcentaje) {
        try {
            Descuento descuento = new DescuentoProducto(fechaDesde, fechaHasta, porcentaje, marcaProducto);
            entityManager.persist(descuento);
            entityManager.clear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public List descuentos() {
        try {
            return entityManager.createQuery(
                    "select d from Descuento d", Descuento.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar los descuentos", e);
        }
    }
}
