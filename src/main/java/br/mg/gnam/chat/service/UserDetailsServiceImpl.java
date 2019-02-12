package br.mg.gnam.chat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.mg.gnam.chat.model.User;
import br.mg.gnam.chat.repository.UserRepository;

/**
 * <p>Service responsável por permitir validar um usuário e senha de usuários com informações 
 * cadastradas no banco de dado.</p> 
 * @author rafael.altagnam
 * @since 06/02/2019
 * @version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	/**
	 * Repositorio do usuario. 
	 * Mantem os dados
	 */
	@Autowired
	private UserRepository userRepository;


	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		User user = userRepository.findByLogin(login);
		if (user == null) {
			throw new UsernameNotFoundException("No user found with username: " + login);
		}
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		return new org.springframework.security.core.userdetails.User
				(user.getLogin(),
				user.getPassword(), enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, getAuthorities(user.getRole()));
	}

	/**
	 * Retorna as ROLES do usuário.
	 * @param role
	 * @return
	 */
	private static List<GrantedAuthority> getAuthorities(String role) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role));
		return authorities;
	}

}
