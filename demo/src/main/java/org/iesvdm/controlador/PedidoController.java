package org.iesvdm.controlador;

import java.util.List;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.service.ClienteService;
import org.iesvdm.service.ComercialService;
import org.iesvdm.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/pedidos")
@Controller
public class PedidoController {

	private final PedidoService pedidoService;
	private final ClienteService clienteService;
	private final ComercialService comercialService;

	public PedidoController(PedidoService pedidoService, ClienteService clienteService,
			ComercialService comercialService) {
		this.pedidoService = pedidoService;
		this.clienteService = clienteService;
		this.comercialService = comercialService;
	}

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("listaPedidos", pedidoService.listAllDetalles());
		return "pedidos";
	}

	@GetMapping("/crear")
	public String mostrarFormularioCrear(Model model) {
		model.addAttribute("pedido", new Pedido());
		cargarRelaciones(model);
		model.addAttribute("titulo", "Crear pedido");
		return "pedido-form";
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute Pedido pedido, Model model) {
		StringBuilder error = new StringBuilder();
		if (clienteService.find(pedido.getClienteId()).isEmpty()) {
			error.append("El cliente seleccionado no existe. ");
		}
		if (comercialService.find(pedido.getComercialId()).isEmpty()) {
			error.append("El comercial seleccionado no existe.");
		}
		if (error.length() > 0) {
			model.addAttribute("error", error.toString());
			cargarRelaciones(model);
			model.addAttribute("titulo", pedido.getId() == 0 ? "Crear pedido" : "Editar pedido");
			return "pedido-form";
		}

		if (pedido.getId() == 0) {
			pedidoService.create(pedido);
		} else {
			pedidoService.update(pedido);
		}
		return "redirect:/pedidos";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		Pedido pedido = pedidoService.find(id)
				.orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + id));
		model.addAttribute("pedido", pedido);
		cargarRelaciones(model);
		model.addAttribute("titulo", "Editar pedido");
		return "pedido-form";
	}

	@GetMapping("/{id}")
	public String detalle(@PathVariable int id, Model model) {
		model.addAttribute("pedido", pedidoService.findDetalle(id)
				.orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + id)));
		return "pedido-detalle";
	}

	@PostMapping("/borrar/{id}")
	public String borrar(@PathVariable int id) {
		pedidoService.delete(id);
		return "redirect:/pedidos";
	}

	private void cargarRelaciones(Model model) {
		List<Cliente> listaClientes = clienteService.listAll();
		List<Comercial> listaComerciales = comercialService.listAll();
		model.addAttribute("listaClientes", listaClientes);
		model.addAttribute("listaComerciales", listaComerciales);
	}
}
