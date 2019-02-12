package br.mg.gnam.chat.model;

/**
 * <p>Entidade de transponte. Responsável por enviar os dados referente a uma mensagem 
 * para a camada de visão.</p> 
 * @author rafael.altagnam
 * @since 07/02/2019
 * @version 1.0
 */
public class MessageDTO {

	/**
	 * Login do usuário que enviou a mensagem
	 */
	private String from;
	
	/**
	 *  Nome do usuário enviou a mensagem
	 */
	private String fromName;

	/**
	 * Login do usuário que receberá a mensagem
	 */
	private String to;	
	
	/**
	 * Nome do usuário que irá receber a mensagem
	 */
	private String toName;

	/**
	 * Conteúdo da mensagem enviada pelo usuário
	 */
	private String text;
	
	/**
	 *  Long representado os miliessegundos de quando a mensagem foi enviada.
	 */
	private long date;

	
	public MessageDTO() {
		
	}


	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}


	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}


	/**
	 * @return the fromName
	 */
	public String getFromName() {
		return fromName;
	}


	/**
	 * @param fromName the fromName to set
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}


	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}


	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}


	/**
	 * @return the toName
	 */
	public String getToName() {
		return toName;
	}


	/**
	 * @param toName the toName to set
	 */
	public void setToName(String toName) {
		this.toName = toName;
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
	public long getDate() {
		return date;
	}


	/**
	 * @param date the date to set
	 */
	public void setDate(long date) {
		this.date = date;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MessageDTO [from=" + from + ", fromName=" + fromName + ", to=" + to + ", toName=" + toName + "]";
	}

	

}
