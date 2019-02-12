package br.mg.gnam.chat.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import br.mg.gnam.chat.model.MessageDTO;
import br.mg.gnam.chat.service.MessageService;
import br.mg.gnam.chat.service.UserService;

/**
 * <p>Controller responsável por enviar as mensagens aos usuários.</p> 
 * @author rafael.altagnam
 * @since 06/02/2019
 * @version 1.0
 */
@Controller
public class MessageController {
	
	public static final String DESTINATION = "/queue/greetings";
	
	/**
	 * Envia as mensagens aos destinarios
	 */
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	/**
	 * Serviço responsável por prover informações dos usuários.
	 */
	@Autowired 
	private UserService userService;
	
	/**
	 * Serviço responsável por prover informações das mensagens.
	 */
	@Autowired 
	private MessageService messageService;

	
	/**
	 * Envia mensagem para a propria pessoa que maandou e para o destinatario, caso esta on-line
	 * @param principal - usuário logado
	 * @param message - mensagem que sera enviada.
	 * @throws Exception
	 */
	@MessageMapping("/message")
	public void message(Principal principal, MessageDTO message) throws Exception {
		messageService.fillMessageFrom(principal, message);
		sendMessage(message);
	}
	
	
	/**
	 * Envia as mensagens para o destinatário e o proprio usuário que mandou
	 * @param message
	 */
	private void sendMessage (MessageDTO message) {
		
		/*
		 * Send message to owner
		 */
		simpMessagingTemplate.convertAndSendToUser(message.getFrom(), DESTINATION, message);
		
		/*
		 * Receiver is on? If yes, the message will be send to him else the message will save in bd 
		 * to after be send
		 */
		if (!userService.isUserOnWebSocket(message.getTo())) {
			messageService.save(message);
		
		}else {
			simpMessagingTemplate.convertAndSendToUser(message.getTo(), DESTINATION, message);
		}		
	}
}
