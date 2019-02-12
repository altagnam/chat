package br.mg.gnam.chat.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.mg.gnam.chat.model.User;
import br.mg.gnam.chat.service.UserService;
import io.swagger.annotations.ApiOperation;

/**
 * <p>Controller responsável pela manipulação de dados dos usuários.</p> 
 * @author rafael.altagnam
 * @since 07/02/2019
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
	
	/**
	 * Service responsável por manter os dados do {@link User}.
	 */
	@Autowired
	private UserService service;
	
	
	/**
	 * Realiza o cadastro de um usuário na base de dados
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "Registra um usuário")
	@PostMapping
	public User cadastrar (@RequestBody User user) throws Exception {		
		try {
			
			return service.save(user);
		
		} catch (Exception e) {		
			throw e;
		}
	}
	
	
	/**
	 * Consulta as informacoes do usuário logado
	 * @param principal
	 * @return
	 */
	@ApiOperation(value = "Necessita estar autenticado - retorna as informações referente ao usuário logado")
	@GetMapping(path = "/self")
	public User getUserConnected (Principal principal) {
		return service.porLogin(principal.getName());
	}

	
	/**
	 * @param principal  - usuário autenticado
	 * @return
	 */
	@ApiOperation(consumes = "application/json", value = "Necessita estar autenticadao - retorna informações dos usuários cadastrados no sistema exceto o usuário logado")
	@GetMapping(path = "/all")
	public List<User> getUsers(Principal principal) {
		List<User> users = service.todosMenos(principal.getName());
		users.forEach(user ->{
			user.setPassword(null);
		});
		
		
		return users;
	}

}
