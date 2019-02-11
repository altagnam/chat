package br.mg.gnam.chat.listener;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import br.mg.gnam.chat.model.MessageDTO;
import br.mg.gnam.chat.model.User;
import br.mg.gnam.chat.service.MessageService;
import br.mg.gnam.chat.service.UserService;

@Component
public class WebSocketEventListener {


	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserService userService;

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		Principal principal = event.getUser();
		User user = userService.porLogin(principal.getName());
		userService.userOn(principal.getName());
		simpMessagingTemplate.convertAndSend("/topic/greetings", "{\"text\": " + "\"" + user.getName() + " entrou." + "\"" + ", \"status\": 0}");

		new Thread(new Runnable() {

			@Override
			public void run() {
				List<MessageDTO> messages = messageService.getMessageNotRead(principal.getName());
				for (MessageDTO messageDTO : messages) {
					simpMessagingTemplate.convertAndSendToUser(messageDTO.getTo(), "/queue/greetings", messageDTO);
				}
				
				if (!messages.isEmpty()) {
					messageService.removeMessageNotReadUserTo(user);
				}
			}
		}).start();
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		String username = event.getUser().getName();
		User user = userService.porLogin(username);
		userService.userOff(username);
		simpMessagingTemplate.convertAndSend("/topic/greetings", "{\"text\": " + "\"" + user.getName() + " saiu." +  "\"" + ", \"status\": 1}");
	}
}