package br.mg.gnam.chat.controller;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import br.mg.gnam.chat.model.MessageDTO;
import br.mg.gnam.chat.model.User;
import br.mg.gnam.chat.service.MessageService;
import br.mg.gnam.chat.service.UserService;

@Controller
public class MessageController {
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired 
	private UserService userService;
	
	@Autowired 
	private MessageService messageService;

	
	
	@MessageMapping("/message")
	public void message(Principal principal, MessageDTO received) throws Exception {
		User userFrom = userService.porLogin(principal.getName());
		received.setFrom(userFrom.getLogin());
		received.setFromName(userFrom.getName());
		received.setDate(new Date().getTime());
		simpMessagingTemplate.convertAndSendToUser(received.getFrom(), "/queue/greetings", received);
		if (!userService.isUserOn(received.getTo())) {
			messageService.save(received);
		
		}else {
			simpMessagingTemplate.convertAndSendToUser(received.getTo(), "/queue/greetings", received);
		}	
	}
	
	
	
}
