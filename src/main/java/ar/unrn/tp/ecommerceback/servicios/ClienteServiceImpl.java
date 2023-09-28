package ar.unrn.tp.ecommerceback.servicios;

import ar.unrn.tp.ecommerceback.api.ClienteService;
import ar.unrn.tp.ecommerceback.modelo.Cliente;
import ar.unrn.tp.ecommerceback.modelo.TarjetaCredito;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;

@Service
public class ClienteServiceImpl implements ClienteService {

    @PersistenceContext
    private EntityManager entityManager;

    public ClienteServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void crearCliente(String nombre, String apellido, String dni, String email) {
        try {
            Cliente cliente = new Cliente(nombre, apellido, dni, email);
            this.entityManager.persist(cliente);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void modificarCliente(Long idCliente, String nombre, String apellido) {
        try {
            Cliente cliente = this.entityManager.find(Cliente.class, idCliente);
            if (cliente == null) {
                throw new RuntimeException("Cliente no encontrado...");
            }
            cliente.modificarDatos(nombre, apellido);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void agregarTarjeta(Long idCliente, String numero, String marca) {
        try {
            Cliente cliente = this.entityManager.find(Cliente.class, idCliente);
            if (cliente == null) {
                throw new RuntimeException("Cliente no encontrado...");
            }
            TarjetaCredito tarjetaCredito = new TarjetaCredito(numero, marca);
            cliente.addTarjeta(tarjetaCredito);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List listarTarjetas(Long idCliente) {
        try {
            Cliente cliente = this.entityManager.find(Cliente.class, idCliente);
            if (cliente == null) {
                throw new RuntimeException("Cliente no encontrado...");
            }

            return cliente.tarjetas();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List listarClientes() {
        try {
            return entityManager.createQuery(
                    "select c from Cliente c", Cliente.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar clientes", e);
        }
    }
}
