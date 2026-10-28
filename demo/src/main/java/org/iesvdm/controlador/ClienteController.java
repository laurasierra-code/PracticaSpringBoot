package org.iesvdm.controlador;

import java.util.List;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.GetMapping;


@Controller //usa templates html (thymeleaf, freemarker, etc) para devolver la respuesta al cliente. No devuelve datos en formato JSON o XML como hace @RestController

//Se puede fijar la ruta base de las peticiones  de este controador.
//Los mappings de los métodos tendrían valor /clientes como
//prefijo
//@RequestMapping ("/clientes")
public class ClienteController {

    private ClienteService clienteService;

    //Se utiliza inyeccion automatica por constructor del framework Spring.
    //Por tanto, se puede omitir la anotacion Autowired
    //@Autowired

    public ClienteController(ClienteService clienteService){
        this.clienteService = clienteService;
    }
    //@RequestMapping(value = "/clientes", method = RequestMethod.GET)
    //equivalente a la siguiente anotación
    @GetMapping("/clientes") //Al no tener ruta base para el controlador, cada método tiene que tener la ruta completa
    public String listar(Model model) {

        List<Cliente> listaClientes = clienteService.listAll();
        model.addAttribute("listaClientes", listaClientes);

        return "clientes"; //devuelve la plantilla html clientes.html que se encuentra en src/main/resources/templates
    }
}
