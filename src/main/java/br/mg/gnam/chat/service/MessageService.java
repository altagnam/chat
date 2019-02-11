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

@Service
public class MessageService {
	
	
	@Autowired 
	private MessageRepository messageRepository;
	
	@Autowired 
	private UserService userService;
	
	
	public Message save (MessageDTO messageDTO) {
		Message message = new Message();
		message.setText(messageDTO.getText());
		message.setDate(new Date());
		message.setFrom(userService.porLogin(messageDTO.getFrom()));
		message.setTo(userService.porLogin(messageDTO.getTo()));
		return messageRepository.save(message);
	}
	
	
	
	public HashMap<String, Integer> countMessageNotSendByUser (User user) {
		HashMap<String, Integer> count = new HashMap<String, Integer>();
		List<Object[]> objects = messageRepository.countMessages(user.getId());
		for (Object[] object : objects) {
			count.put(object[0].toString(), Integer.parseInt(object[1].toString()));
		}
		
		return count;
	}
	
	
	public HashMap<String, Integer> countMessageNotSendByUser (Principal principal) {
		User user = userService.porLogin(principal.getName());
		return countMessageNotSendByUser(user);
	}
	
	
	public List<MessageDTO> getMessageNotRead (User from, User to){
		List<Message> messages = messageRepository.findByFromAndTo(from, to);
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
	
	
	public List<MessageDTO> getMessageNotRead (String from, String to){
		return getMessageNotRead (userService.porLogin(from), userService.porLogin(to));
	}
	
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
	
	@Transactional
	public void removeMessageNotReadUserTo(User to) {
		List<Message> messages = messageRepository.removeByTo(to);
		for (Message message : messages) {
			System.out.println(message);
		}
	}
	

}
