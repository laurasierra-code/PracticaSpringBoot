package org.iesvdm.demo;


import java.util.Optional;

import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.Cliente;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController; //rest controller no tiene plantilla html, es un controlador que devuelve datos en formato JSON o XML, no HTML. Para devolver HTML se usa @Controller y se necesita una plantilla html (thymeleaf, freemarker, etc)

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner; //interfaz que permite ejecutar código al iniciar la aplicación Spring Boot. Se implementa el método run().

@SpringBootApplication(scanBasePackages = "org.iesvdm") // indica cual es la clase que inicia la aplicación Spring Boot. Esta anotación
						// es un atajo de @Configuration, @EnableAutoConfiguration y @ComponentScan.
@RestController // permite que sea accesinle desde una URL.
@Slf4j
public class DemoApplication implements CommandLineRunner {

	// Version 1: inyeccion automatica por atributo.
	@Autowired
	private ClienteDAO clienteDAO;

	@Autowired
	private ComercialDAO comercialDAO;

	/*
	 * Version 2: inyeccion automatica por constructor.
	 * Se deja comentada porque la version activa utiliza @Autowired.
	 *
	 * private final ClienteDAO clienteDAO;
	 *
	 * public DemoApplication(ClienteDAO clienteDAO) {
	 *     this.clienteDAO = clienteDAO;
	 * }
	 */

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/cliente")
	public String home() {
		return "Hello, World!";
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("***********");
		log.info("Prueba de arranque de la aplicación Spring Boot");
		log.info("***********");

		clienteDAO.getAll().forEach(c -> log.info("Cliente: {}", c));

		int id = 1;
		Optional<Cliente> cliente = clienteDAO.find(id);
		if (cliente.isPresent()) {
			log.info("Cliente con id {}: {}", id, cliente.get());

			String nombreOld = cliente.get().getNombre();

			cliente.get().setNombre("Jose M");

			clienteDAO.update(cliente.get());

			log.info("Cliente con id {} actualizado: {}", id, clienteDAO.find(id).get());
			
			clienteDAO.getAll().forEach(client -> log.info("Cliente: {}", client));

			cliente.get().setNombre(nombreOld);

			clienteDAO.update(cliente.get());
			
			log.info("Cliente con id {} restaurado: {}", id, clienteDAO.find(id).get());
		} else {
			log.info("No se encontró cliente con id {}", id);
		}

		log.info("Pruebas de ComercialDAO");
		comercialDAO.getAll().forEach(comercial -> log.info("Comercial: {}", comercial));

		Comercial comercialPrueba = new Comercial(0, "Comercial", "Prueba", null, 0.10f);
		comercialDAO.create(comercialPrueba);
		log.info("Comercial creado: {}", comercialPrueba);

		Optional<Comercial> comercialEncontrado = comercialDAO.find(comercialPrueba.getId());
		if (comercialEncontrado.isPresent()) {
			log.info("Comercial encontrado: {}", comercialEncontrado.get());

			comercialPrueba.setComision(0.20f);
			comercialDAO.update(comercialPrueba);
			log.info("Comercial actualizado: {}", comercialDAO.find(comercialPrueba.getId()).orElseThrow());
		} else {
			log.warn("No se encontró el comercial de prueba con id {}", comercialPrueba.getId());
		}

		comercialDAO.delete(comercialPrueba.getId());
		log.info("Comercial de prueba eliminado: {}", comercialPrueba.getId());
	}
}
