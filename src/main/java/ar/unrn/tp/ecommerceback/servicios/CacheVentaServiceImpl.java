package ar.unrn.tp.ecommerceback.servicios;

import ar.unrn.tp.ecommerceback.api.CacheVentaService;
import ar.unrn.tp.ecommerceback.modelo.Venta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class CacheVentaServiceImpl implements CacheVentaService {

    Jedis jedis;

    private void setUpJedis() {
        this.jedis = new Jedis("localhost", 6379);
    }

    private void closeJedis() {
        this.jedis.close();
    }

    @Override
    public void agregarVenta(Venta venta) {

    }

    @Override
    public void agregarUltimasVentas(List<Venta> ventas, Long idCliente) {
        this.setUpJedis();
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        for (Venta venta: ventas) {
            try {
                String ventaJSON = objectMapper.writeValueAsString(venta);
                this.jedis.lpush("cliente:" + idCliente.toString() + ":ventas", ventaJSON);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        this.jedis.close();
    }

    @Override
    public void limpiarCache(Long idCliente) {
        this.setUpJedis();
        this.jedis.del("cliente:" + idCliente.toString() + ":ventas");
        this.closeJedis();
    }

    @Override
    public List<Venta> listarVentasDeUnCliente(Long idCliente) {
        this.setUpJedis();
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<String> ventasJSON = this.jedis.lrange("cliente:" + idCliente.toString() + ":ventas", 0, -1);
        List<Venta> ventas = new ArrayList<>();
        for (String ventaJSON : ventasJSON) {
            try {
                Venta venta = objectMapper.readValue(ventaJSON, Venta.class);
                ventas.add(venta);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        this.closeJedis();
        return ventas;
    }
}
