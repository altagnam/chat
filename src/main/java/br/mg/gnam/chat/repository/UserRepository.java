package br.mg.gnam.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.mg.gnam.chat.model.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Long>  {
	
	/**
	 * Busca um usuário na base de dados pelo nome
	 * @param name
	 * @return
	 */
	public User findByName(String name);
	
	
	/**
	 * Retorna um usuário pelo login
	 * @param login
	 * @return
	 */
	public User findByLogin(String login);
	
	
	/**
	 * Retorna uma lista de usuários.
	 * Nesta lista será desonsiderado o usuário enviado como parametro para este metodo.
	 * @param user
	 * @return
	 */
	@Query(value = "SELECT u FROM User u WHERE u != :user ORDER BY u.name")
	public List<User> findAllLess(@Param(value = "user") User user);

}
