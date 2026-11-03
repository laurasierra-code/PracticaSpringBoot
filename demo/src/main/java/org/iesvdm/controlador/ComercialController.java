package org.iesvdm.controlador;

import java.util.List;

import org.iesvdm.modelo.Comercial;
import org.iesvdm.service.ComercialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ComercialController {

    private final ComercialService comercialService;

    public ComercialController(ComercialService comercialService) {
        this.comercialService = comercialService;
    }

    @GetMapping("/comerciales")
    public String listar(Model model) {
        List<Comercial> listaComerciales = comercialService.listAll();
        model.addAttribute("listaComerciales", listaComerciales);

        return "comerciales";
    }

    @GetMapping("/comerciales/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("comercial", new Comercial());
        model.addAttribute("titulo", "Crear comercial");
        return "comercial-form";
    }

    @PostMapping("/comerciales/guardar")
    public String guardar(@ModelAttribute Comercial comercial) {
        if (comercial.getId() == 0) {
            comercialService.create(comercial);
        } else {
            comercialService.update(comercial);
        }
        return "redirect:/comerciales";
    }

    @GetMapping("/comerciales/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        Comercial comercial = comercialService.find(id)
                .orElseThrow(() -> new IllegalArgumentException("Comercial no encontrado: " + id));
        model.addAttribute("comercial", comercial);
        model.addAttribute("titulo", "Editar comercial");
        return "comercial-form";
    }

    @GetMapping("/comerciales/{id}")
    public String detalle(@PathVariable int id, Model model) {
        Comercial comercial = comercialService.find(id)
                .orElseThrow(() -> new IllegalArgumentException("Comercial no encontrado: " + id));
        model.addAttribute("comercial", comercial);
        return "comercial-detalle";
    }

    @PostMapping("/comerciales/borrar/{id}")
    public String borrar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        if (comercialService.hasPedidos(id)) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar el comercial porque tiene pedidos asociados.");
            return "redirect:/comerciales";
        }
        comercialService.delete(id);
        return "redirect:/comerciales";
    }
}