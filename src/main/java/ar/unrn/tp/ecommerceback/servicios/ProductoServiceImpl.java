package ar.unrn.tp.ecommerceback.servicios;

import ar.unrn.tp.ecommerceback.api.ProductoService;
import ar.unrn.tp.ecommerceback.modelo.Categoria;
import ar.unrn.tp.ecommerceback.modelo.Marca;
import ar.unrn.tp.ecommerceback.modelo.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    @PersistenceContext
    private EntityManager entityManager;

//    public ProductoServiceImpl(EntityManagerFactory entityManagerFactory) {
//        this.entityManagerFactory = entityManagerFactory;
//    }

    @Override
    @Transactional
    public void crearProducto(String codigo, String descripcion, double precio, Long idCategoria, Long idMarca) {
        try {
            Categoria categoria = entityManager.find(Categoria.class, idCategoria);
            if (categoria == null)
                throw new RuntimeException("Categoria no encontrada...");
            Marca marca = entityManager.find(Marca.class, idMarca);
            if (marca == null)
                throw new RuntimeException("Marca no encontrada...");
            Producto producto = new Producto(codigo, descripcion, precio, categoria, marca);
            entityManager.persist(producto);
            entityManager.clear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modificarProducto(Long idProducto, String descripcion, double precio, Long idCategoria) {
        try {
            Producto producto = entityManager.find(Producto.class, idProducto);
            if (producto == null)
                throw new RuntimeException("Producto no encontrado...");
            Categoria categoria = entityManager.find(Categoria.class, idCategoria);
            if (categoria == null)
                throw new RuntimeException("Categoria no encontrada...");

            producto.modificarDatos(descripcion, precio, categoria);
            entityManager.clear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public List listarProductos() {
        try {
            List<Producto> productos = entityManager.createQuery(
                    "select p from Producto p", Producto.class).getResultList();

            if (productos == null) {
                //Ver Optional
                throw new RuntimeException("No hay productos...");
            }
            this.entityManager.clear();
            return productos;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
