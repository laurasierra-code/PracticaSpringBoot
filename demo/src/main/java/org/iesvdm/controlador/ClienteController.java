package org.iesvdm.controlador;

import java.util.List;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import org.springframework.web.bind.annotation.GetMapping;


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
}
