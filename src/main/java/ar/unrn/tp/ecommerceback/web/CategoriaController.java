package ar.unrn.tp.ecommerceback.web;

import ar.unrn.tp.ecommerceback.api.CategoriaService;
import ar.unrn.tp.ecommerceback.modelo.Categoria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoriaController {

    private CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("categorias")
    public List<Categoria> getAllCategories() {
        return this.categoriaService.categorias();
    }
}
