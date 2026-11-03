package org.iesvdm.controlador;

import java.util.List;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller //usa templates html (thymeleaf, freemarker, etc) para devolver la respuesta al cliente. No devuelve datos en formato JSON o XML como hace @RestController

//Se puede fijar la ruta base de las peticiones  de este controador.
//Los mappings de los métodos tendrían valor /clientes como
//prefijo
//@RequestMapping ("/clientes")
public class ClienteController {

    // Version 1: inyeccion automatica por atributo.
    @Autowired
    private ClienteService clienteService;

    /*
     * Version 2: inyeccion automatica por constructor.
     * Si se utiliza esta version, hay que comentar el atributo y @Autowired de
     * la version 1 y descomentar este constructor.
     *
     * public ClienteController(ClienteService clienteService) {
     *     this.clienteService = clienteService;
     * }
     */
    //@RequestMapping(value = "/clientes", method = RequestMethod.GET)
    //equivalente a la siguiente anotación
    @GetMapping("/clientes") //Al no tener ruta base para el controlador, cada método tiene que tener la ruta completa
    public String listar(Model model) {

        List<Cliente> listaClientes = clienteService.listAll();
        model.addAttribute("listaClientes", listaClientes);

        return "clientes"; //devuelve la plantilla html clientes.html que se encuentra en src/main/resources/templates
    }

    @GetMapping("/clientes/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("titulo", "Crear cliente");
        return "cliente-form";
    }

    @PostMapping("/clientes/guardar")
    public String guardar(@ModelAttribute Cliente cliente) {
        if (cliente.getId() == 0) {
            clienteService.create(cliente);
        } else {
            clienteService.update(cliente);
        }
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        Cliente cliente = clienteService.find(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + id));
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar cliente");
        return "cliente-form";
    }

    @GetMapping("/clientes/{id}")
    public String detalle(@PathVariable int id, Model model) {
        Cliente cliente = clienteService.find(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + id));
        model.addAttribute("cliente", cliente);
        return "cliente-detalle";
    }

    @PostMapping("/clientes/borrar/{id}")
    public String borrar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        if (clienteService.hasPedidos(id)) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar el cliente porque tiene pedidos asociados.");
            return "redirect:/clientes";
        }
        clienteService.delete(id);
        return "redirect:/clientes";
    }
}
