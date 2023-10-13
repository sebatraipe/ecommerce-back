package ar.unrn.tp.ecommerceback.servicios;

import ar.unrn.tp.ecommerceback.api.CategoriaService;
import ar.unrn.tp.ecommerceback.modelo.Categoria;
import ar.unrn.tp.ecommerceback.modelo.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List categorias() {
        try {
            return entityManager.createQuery(
                    "select c from Categoria c", Categoria.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar las categorias.", e);
        }
    }
}
