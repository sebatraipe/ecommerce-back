package ar.unrn.tp.ecommerceback;

import ar.unrn.tp.ecommerceback.api.ClienteService;
import ar.unrn.tp.ecommerceback.api.ProductoService;
import ar.unrn.tp.ecommerceback.api.VentaService;
import ar.unrn.tp.ecommerceback.servicios.VentaServiceImpl;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootApplication
public class EcommerceBackApplication {

    @Autowired
    EntityManagerFactory entityManagerFactory;

    public static void main(String[] args) {
        SpringApplication.run(EcommerceBackApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*");
            }
        };
    }
}
