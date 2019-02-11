package br.mg.gnam.chat;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.mg.gnam.chat.model.User;
import br.mg.gnam.chat.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChatApplicationTests {
	
	
	@Autowired
	private UserService userService;
	

	@Test
	public void test1_save() {
		System.out.println("\nSalvando um usuário no banco\n");
		User user = new User();
		user.setLogin("login");
		user.setName("User name");
		user.setPassword("123123");
		user.setRole("USER");
		user = userService.save(user);
		System.out.println("\nUsuário salvo\n");
		System.out.println(user.toString());
	}
	
	
	@Test
	public void test2_delete() {
		System.out.println("\nExcluindo um usuário no banco\n");
		User user = userService.porLogin("login");
		userService.delete(user);
		System.out.println("\nUsuário removido\n");
		System.out.println(user.toString());
	}

}
