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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
@Api(value = "user", description = "Informação sobre os usuários cadastrados")
public class UsersController {
	
	@Autowired
	private UserService service;
	
	@ApiOperation(value = "Registra um usuário")
	@PostMapping
	public User cadastrar (@RequestBody User user) {
		return service.save(user);
	}
	
	@GetMapping(path = "/self")
	public User getUserConnected (Principal principal) {
		return service.porLogin(principal.getName());
	}

	@GetMapping(path = "/all")
	public List<User> getUsers(Principal principal) {
		return service.todosMenos(principal.getName());
	}

}
