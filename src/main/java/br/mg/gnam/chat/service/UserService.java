package br.mg.gnam.chat.service;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.mg.gnam.chat.model.User;
import br.mg.gnam.chat.repository.UserRepository;

/**
 * <p>Serviço responsável por manipular as informações dos usuários cadastrados no sistema.</p> 
 * @author rafael.altagnam
 * @since 06/02/2019
 * @version 1.0
 */
@Service
public class UserService {

	/**
	 * ROLE padrão para todos os usuários
	 */
	private static final String ROLE_USER = "USER";
	
	/**
	 * Repositorio de dados do usuário
	 */
	@Autowired
	private UserRepository userRepository;

	/**
	 * Bean responsável pela criptografia da senha
	 */
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	/**
	 * Cache com os usuários conectados no websocket
	 */
	private HashMap<String, Boolean> usersConnectedChat = new  HashMap<String, Boolean>();
	
	
	/**
	 * Verifica se um determinado login esta conectado ao chat
	 * @param login
	 * @return
	 */
	public boolean isUserOnWebSocket(String login) {
		return usersConnectedChat.containsKey(login);
	}	
	
	/**
	 * Indica que um determinado login esta conectado ao chat 
	 * @param login
	 */
	public void userOnWebSocket(String login) {
		usersConnectedChat.put(login, true);
	}
	
	/**
	 * Indica que um determinado login nao esta conectado ao chat
	 * @param login
	 */
	public void userOffWebSocket(String login) {
		usersConnectedChat.remove(login);
	}	

	/**
	 * Salva um usuário na base de dados
	 * 
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public User save(User user) throws Exception {
		user.validate();		
		if (porLogin(user.getLogin()) != null) {
			throw new Exception("Login já cadastrado. Informe um login diferente.");
		}
		
		if (porNome(user.getName()) != null) {
			throw new Exception("Nome já cadastrado. Informe um nome diferente.");
		}
		
		user.setRole(ROLE_USER);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	/**
	 * Remove um usuário  da base
	 * @param user
	 */
	public void delete(User user) {
		userRepository.delete(user);
	}

	/**
	 * Retorna a lista de usuarios cadastrados 
	 * @return
	 */
	public List<User> todos() {
		return userRepository.findAll();
	}
	
	
	/**
	 * Retorna a lista de usuarios cadastrados menos o passado como parametro 
	 * @return
	 */
	public List<User> todosMenos(String login) {
		User user = userRepository.findByLogin(login);
		List<User> users = userRepository.findAllLess(user);
		users.forEach(u ->{
			u.setStatus(isUserOnWebSocket(u.getLogin()));
		});
		return users;
	}

	/**
	 * Retorna uma usuário de acordo com o nome
	 * 
	 * @param name
	 * @return
	 */
	public User porNome(String name) {
		return userRepository.findByName(name);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public User porLogin(String login) {
		User user = userRepository.findByLogin(login);
		return user;
	}

}
