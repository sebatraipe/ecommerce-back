package ar.unrn.tp.ecommerceback.web;

import ar.unrn.tp.ecommerceback.api.ClienteService;
import ar.unrn.tp.ecommerceback.exceptions.NotNullException;
import ar.unrn.tp.ecommerceback.modelo.Cliente;
import ar.unrn.tp.ecommerceback.modelo.TarjetaCredito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/")
public class ClienteController {

    private ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("clientes")
    public List<Cliente> getAllClients() {
        return this.clienteService.listarClientes();
    }

    @PostMapping("clientes")
    public ResponseEntity<?> createClient(@RequestBody Cliente cliente) throws Exception, NotNullException {
        this.clienteService.crearCliente(cliente.getNombre(), cliente.getApellido(), cliente.getDni(),
                cliente.getEmail());
        Map<String, String> response = new HashMap<>();
        response.put("message", "El cliente se registró con éxito.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("clientes")
    public void updateClient(@RequestBody Cliente cliente) {
        clienteService.modificarCliente(cliente.getId(), cliente.getNombre(),
                cliente.getApellido());
    }

    @PostMapping("clientes/{idCliente}/addCard")
    public void addCard(@PathVariable Long idCliente, @RequestBody TarjetaCredito tarjetaCredito) {
        clienteService.agregarTarjeta(idCliente, tarjetaCredito.getNumero(),
                tarjetaCredito.getDescripcion());
    }

    @GetMapping("clientes/{idCliente}")
    public List<Cliente> getAllClientsCard(@PathVariable Long idCliente) {
        return this.clienteService.listarTarjetas(idCliente);
    }
}
