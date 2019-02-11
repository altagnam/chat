package br.mg.gnam.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>Controller responsável pela tela de login.</p>
 * 
 * @author rafael.altagnam
 * @since 07/02/2019
 * @version 1.0
 */
@Controller
public class LoginController {

	/**
	 * Método responsável por direcionar o usuário para a tela de login.
	 * 
	 * @param error
	 * @param logout
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout) {
		ModelAndView model = new ModelAndView();		
		model.setViewName("login");
		return model;

	}

}
