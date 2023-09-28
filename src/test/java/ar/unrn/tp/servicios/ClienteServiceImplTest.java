package ar.unrn.tp.servicios;

import ar.unrn.tp.ecommerceback.api.ClienteService;
import ar.unrn.tp.ecommerceback.servicios.ClienteServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
public class ClienteServiceImplTest {

    private ClienteServiceImpl clienteService;

    @Autowired
    private EntityManagerFactory entityManagerFactory;  // Inyecta el EntityManagerFactory

    private EntityManager entityManager;  // EntityManager para usar en las pruebas

    @BeforeEach
    public void setUp() {
        entityManager = entityManagerFactory.createEntityManager();  // Crea un EntityManager
        clienteService = new ClienteServiceImpl(entityManager);  // Pasa el EntityManager a tu servicio
    }

    @Test
    @Transactional
    public void testCrearCliente() {

        String nombre = "Test";
        String apellido = "ApellidoTest";
        String dni = "DNITest";
        String email = "correoTest";

        assertDoesNotThrow(() -> clienteService.crearCliente(nombre, apellido, dni, email));
    }

}
