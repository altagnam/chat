package br.mg.gnam.chat.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.mg.gnam.chat.model.Message;
import br.mg.gnam.chat.model.User;

@Repository
public interface MessageRepository  extends JpaRepository<Message, Long>  {
	
	public List<Message> findByFrom(User user);
	
	public List<Message> findByTo(User user);	
	
	public List<Message> findByFromAndTo(User from, User to);
	
	@Query(value = "SELECT U.CPF, COUNT(*) FROM MESSAGE M INNER JOIN USER U ON (U.ID = M.USER_FROM) WHERE M.USER_TO = ?1 GROUP BY USER_FROM", nativeQuery = true)
	public List<Object[]> countMessages (Long idUser);	
	
	public List<Message> removeByTo(User user);

}
