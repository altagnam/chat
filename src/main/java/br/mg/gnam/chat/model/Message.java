package br.mg.gnam.chat.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <p>Entidade responsável por armazenar as mensagens enviadas pelo sistema.</p> 
 * @author rafael.altagnam
 * @since 07/02/2019
 * @version 1.0
 */
@Entity
@Table(name = "MESSAGE")
public class Message {
	

	/**
	 * ID da entidade
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	
	/**
	 * Quem enviou a mensagem.
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "USER_FROM")
	private User from;
	
	/**
	 * Para quem a mensagem foi enviada.
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "USER_TO")
	private User to;
	
	/**
	 * Conteudo da mensagem.
	 */
	@Column(name = "TEXT")
	private String text;
	
	/**
	 * Data e hora quando a mensagem foi enviada.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE")
	private Date date;

	

	public Message() {
	
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
	 * @return the from
	 */
	public User getFrom() {
		return from;
	}


	/**
	 * @param from the from to set
	 */
	public void setFrom(User from) {
		this.from = from;
	}


	/**
	 * @return the to
	 */
	public User getTo() {
		return to;
	}


	/**
	 * @param to the to to set
	 */
	public void setTo(User to) {
		this.to = to;
	}


	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}


	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}


	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}


	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}


	@Override
	public String toString() {
		return "Message [from=" + from + ", to=" + to + ", text=" + text + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
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
		Message other = (Message) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}
}
