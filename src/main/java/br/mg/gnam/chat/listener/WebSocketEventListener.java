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

/**
 * <p>Componente responsável por ficar onvindo quando um usuário conecta ou desconecta do websocket.</p> 
 * @author rafael.altagnam
 * @since 08/02/2019
 * @version 1.0
 */
@Component
public class WebSocketEventListener {

	/**
	 * Envia as mensagens aos destinarios
	 */
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	
	/**
	 * Service responsável por manipular as mensagens.
	 */
	@Autowired
	private MessageService messageService;
	
	
	/**
	 * Service responsável por recuperar as informações de um usuário 
	 */
	@Autowired
	private UserService userService;

	
	/**
	 * <p>Método acionado quando um usuário conecta ao websocket.</p>
	 * Ao conectar, é enviado uma mensagem para todos os usuários conetados alertando 
	 * que há um novo usuário online.
	 * 
	 * <p>É verificado também se foram enviadas mensagens para este usuário quando ele estava offline. 
	 * Se houver mensagens, estas serão encaminhadas ao usuário e removidas da base de dados.</p>
	 * @param event
	 */
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		Principal principal = event.getUser();
		User user = userService.porLogin(principal.getName());
		
		/*
		 * Indicando que este usuário esta on
		 */
		userService.userOnWebSocket(principal.getName());
		
		/*
		 * Alertando os demais usuários que um usuário acabou de conectar ao chat
		 */
		simpMessagingTemplate.convertAndSend("/topic/greetings", "{\"login\": \"" + user.getLogin()+ "\", \"text\": " + "\"" + user.getName() + " entrou." + "\"" + ", \"status\": 0}");

		
		/*
		 * Dispara as mensagens que foram enviadas para este usuário quando estava offline
		 */
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

	
	/**
	 * <p>Método acionado quando um usuário desconecta da chat.</p>
	 * Ao desconectar, ele é removido da sessao e é enviado uma mensagem para todos os usuários 
	 * que o usuário esta offline
	 * @param event
	 */
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		String username = event.getUser().getName();
		User user = userService.porLogin(username);
		
		/*
		 * Remove o usuário da sessao
		 */
		userService.userOffWebSocket(username);
		
		/*
		 * Notifica aos demais usuários que o usuário esta offline 
		 */
		simpMessagingTemplate.convertAndSend("/topic/greetings", "{\"login\": \"" + user.getLogin() + "\", \"text\": " + "\"" + user.getName() + " saiu." +  "\"" + ", \"status\": 1}");
	}
}