package br.mg.gnam.chat.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.mg.gnam.chat.model.User;
import br.mg.gnam.chat.repository.UserRepository;

@Service
public class UserService {

	private static final String ROLE_USER = "USER";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	private HashMap<String, Boolean> usersConnectedChat = new  HashMap<String, Boolean>();
	
	/**
	 * Verifica se um determinado login esta conectado ao chat
	 * @param login
	 * @return
	 */
	public boolean isUserOn(String login) {
		return usersConnectedChat.containsKey(login);
	}	
	
	/**
	 * Indica que um determinado login esta conectado ao chat 
	 * @param login
	 */
	public void userOn(String login) {
		usersConnectedChat.put(login, true);
	}
	
	/**
	 * Indica que um determinado login nao esta conectado ao chat
	 * @param login
	 */
	public void userOff(String login) {
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
		if (porLogin(user.getLogin()) != null) {
			throw new Exception("Login já cadastrado. Informe um login diferente.");
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
		return userRepository.findAllLess(user);
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
