package ar.unrn.tp.ecommerceback.servicios;

import ar.unrn.tp.ecommerceback.api.ProductoService;
import ar.unrn.tp.ecommerceback.exceptions.ConcurrencyException;
import ar.unrn.tp.ecommerceback.modelo.Categoria;
import ar.unrn.tp.ecommerceback.modelo.Marca;
import ar.unrn.tp.ecommerceback.modelo.Producto;
import jakarta.persistence.*;
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
    @Transactional
    public void modificarProducto(Long idProducto, String descripcion, double precio, Long idCategoria,
                                  String marca, int version) {

        Producto producto = entityManager.find(Producto.class, idProducto);
        if (!producto.sameVersion(version)) {
            throw new ConcurrencyException("Error, actualice la página y vuelva a intentarlo.");
        }
        Categoria categoria = entityManager.find(Categoria.class, idCategoria);
        Marca marcaProducto = this.obtenerMarca(marca);
        producto.modificarDatos(descripcion, precio, categoria, marcaProducto);
    }

    private Marca obtenerMarca(String marca) {
        Marca marcaProducto = entityManager.createQuery(
                        "select m from Marca m where m.nombre = :nombre", Marca.class)
                .setParameter("nombre", marca)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
        if (marcaProducto == null) {
            System.out.println("Entra....");
            marcaProducto = new Marca(null, marca);
            entityManager.persist(marcaProducto);
        }
        return marcaProducto;
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

    @Override
    public Producto getProduct(Long id) {
        try {
            Producto producto = entityManager.find(Producto.class, id);
            if (producto == null) {
                //Ver Optional
                throw new RuntimeException("No existe el producto...");
            }
            return producto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
