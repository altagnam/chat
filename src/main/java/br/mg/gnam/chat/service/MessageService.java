package br.mg.gnam.chat.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.mg.gnam.chat.model.Message;
import br.mg.gnam.chat.model.MessageDTO;
import br.mg.gnam.chat.model.User;
import br.mg.gnam.chat.repository.MessageRepository;

/**
 * <p>Serviço responsável por manipular as mensagens do sistema.</p> 
 * @author rafael.altagnam
 * @since 07/02/2019
 * @version 1.0
 */
@Service
public class MessageService {
	
	/**
	 * Repositorio de mensagem
	 */
	@Autowired 
	private MessageRepository messageRepository;
	
	/**
	 * Serviço responsável pelos usuários
	 */
	@Autowired 
	private UserService userService;
	
	
	/**
	 * Preenche o campo FROM do DTO com as informações do usuário
	 * @param principal
	 * @param messageDTO
	 * @throws Exception
	 */
	public void fillMessageFrom (Principal principal, MessageDTO messageDTO) throws Exception {
		User userFrom = userService.porLogin(principal.getName());
		messageDTO.setFrom(userFrom.getLogin());
		messageDTO.setFromName(userFrom.getName());
		messageDTO.setDate(new Date().getTime());
	}
	
	
	/**
	 * Salva uma mensagem na base de dados
	 * @param messageDTO
	 * @return
	 */
	public Message save (MessageDTO messageDTO) {
		Message message = new Message();
		message.setText(messageDTO.getText());
		message.setDate(new Date());
		message.setFrom(userService.porLogin(messageDTO.getFrom()));
		message.setTo(userService.porLogin(messageDTO.getTo()));
		return messageRepository.save(message);
	}
	
	
	/**
	 * Retorna um HashMap contendo a quantidade de mensagens que um usuário 
	 * recebeu quando estava offline.
	 * 
	 * @param user
	 * @return
	 */
	public HashMap<String, Integer> countMessageNotSendByUser (User user) {
		HashMap<String, Integer> count = new HashMap<String, Integer>();
		List<Object[]> objects = messageRepository.countMessages(user.getId());
		for (Object[] object : objects) {
			count.put(object[0].toString(), Integer.parseInt(object[1].toString()));
		}
		
		return count;
	}
	
	
	/**
	 * Retorna um HashMap contendo a quantidade de mensagens que um usuário 
	 * recebeu quando estava offline.
	 * 
	 * @param principal
	 * @return
	 */
	public HashMap<String, Integer> countMessageNotSendByUser (Principal principal) {
		User user = userService.porLogin(principal.getName());
		return countMessageNotSendByUser(user);
	}	
	

	/**
	 * Retorna as mensagens que foram enviados para um usuário quando estava offline.
	 * @param to
	 * @return
	 */
	public List<MessageDTO> getMessageNotRead (String to){
		User user =  userService.porLogin(to);
		List<Message> messages = messageRepository.findByTo(user);
		List<MessageDTO> list = new ArrayList<>();
		for (Message message : messages) {
			MessageDTO dto = new MessageDTO();
			dto.setDate(message.getDate().getTime());
			dto.setFrom(message.getFrom().getLogin());
			dto.setFromName(message.getFrom().getName());
			dto.setTo(message.getTo().getLogin());
			dto.setToName(message.getTo().getName());
			dto.setText(message.getText());
			list.add(dto);
		}		
		return list;	
	}
	
	
	/**
	 * Função responsável por remover as mensagens da base de dados.
	 * Quando um usuário recebe a mensagem que não foram lidas, tais mensagens poderão ser removidas.
	 * @param to
	 */
	@Transactional
	public void removeMessageNotReadUserTo(User to) {
		List<Message> messages = messageRepository.removeByTo(to);
		for (Message message : messages) {
			System.out.println(message);
		}
	}
	

}
