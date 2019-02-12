package br.mg.gnam.chat.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.util.StringUtils;

/**
 * <p>Entidade responsável por armazenar os dados referente aos usuários cadastrados  no sistema.</p> 
 * @author rafael.altagnam
 * @since 07/02/2019
 * @version 1.0
 */
@Entity
@Table(name = "USER")
public class User {

	/**
	 * ID da entidade
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Nome do usuário
	 */
	@Column(nullable = false)
	private String name;

	/**
	 * Login para que o usário possa se conectar a aplicação.
	 */
	@Column(unique = true, nullable = false)
	@NotNull
	private String login;

	/**
	 * Senha do usuário
	 */
	@Column(nullable = false)
	private String password;
	
	/**
	 * Funçao do usuário.
	 * Na primeira versão deste sistema, vamos considerar somente a função USER
	 */
	private String role;

	/**
	 * Verifica se o usuario esta conectado no websocket
	 */
	@Transient
	private boolean status;
	
	
	public User() {

	}

	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param password
	 * @param role
	 */
	public User(Long id, String name, String password, String role) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.role = role;
	}

	
	

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}	


	/**
	 * @return the status
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}


	/**
	 * Valida antes de salvar
	 * @throws Exception
	 */
	public void validate () throws Exception {
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(getLogin());
		if (m.find()) {
			throw new Exception ("Digite um login sem caracter especial. Exemplo: u123456");
		}
		
		if (StringUtils.isEmpty(login)){
			throw new Exception ("Login em branco");
		}
		
		if (getLogin().length() > 20){
			throw new Exception ("Login com mais de 20 caracteres.");
		}
		
		if (!Character.isLetter(getLogin().charAt(0))){
			throw new Exception ("O primeiro caracter do login, deve ser uma letra. Exemplo: u123456");
		}
		
		if (StringUtils.isEmpty(name)){
			throw new Exception ("Nome em branco");
		}
		
		if (StringUtils.isEmpty(password)){
			throw new Exception ("Senha em branco");
		}
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", login=" + login + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}
}
