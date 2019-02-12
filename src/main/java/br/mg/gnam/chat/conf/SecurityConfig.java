package br.mg.gnam.chat.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.mg.gnam.chat.service.UserDetailsServiceImpl;

/**
 * <p>Classe de configuração para habilitar Spring security.</p>
 * 
 * @author rafael.altagnam
 * @since 06/02/2019
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * Service utilizado para autenticar o usuário com o login e senha
	 * informados de acordo com as informações salvas no banco de dados.
	 */
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	/**
	 * Bean responsável pela criptografia da senha
	 * @return
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	/**
	 * Bean utilizado para autenticar o usuário com o o login e senha
	 * informada através do método <code>autologin</code>, da classe {@link SecurityServiceImpl}
	 * @see SecurityServiceImpl
	 */	
	@Override
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * Bean responsável por permitir autenticar o usuário com o o login e senha
	 * informado atraves da página de login. 
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

	/**
	 * Configura as permissões do sistema.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().maximumSessions(1);
		http.csrf().disable();
		http.authorizeRequests()
				.antMatchers("/cadastro.html", "/main.js").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/api/*").permitAll()
				.antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
				.antMatchers("/chat/**").authenticated()
				.antMatchers("/user/all", "/user/self").authenticated().and()
				.formLogin().loginPage("/login").permitAll()			
				.defaultSuccessUrl("/chat/index.html").and().logout().permitAll().and()
				.logout().permitAll();

	}
}
