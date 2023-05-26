package Model;

import java.util.Objects;
import utils.E_MessageStatus;

/**
 * 
 * @author Elad_Armoni
 *
 * @param <F> represent the object the message sent from, can be client or server
 * @param <T> represent the object the message sent to, can be client or server
 * @param <D> represent the data of the message, can be String or int
 */
public class Message<F extends Connectable,T extends Connectable, D > {
	private final int requestNumber;
	private final D data; //String or Integer
	private final F from;
	private final T to;
	private E_MessageStatus status;
	private int  replyToRequestNum;
	
	/**
	 * This message constructor create new message with the arguments
	 * The message is from client to server because the constructor don't get replyToRequestNum
	 * @param number is the id of the message
	 * @param data is the content of the message
	 * @param from is the object the message sent from
	 * @param to is the object the message sent to, by default set to PENDING
	 */
	public Message(int number, D data, F from, T to) { //message from client to server
		this.requestNumber=number;
		this.data = data;
		this.from = from;
		this.to = to;
		this.status = E_MessageStatus.PENDING;
	}
	
	/**
	  * This message constructor create new message with the arguments
	 * The message is from server to client because the constructor  get replyToRequestNum
     * @param number is the id of the message
	 * @param data is the content of the message
	 * @param from is the object the message sent from
	 * @param to is the object the message sent to, by default set to PENDING
	 * @param replyToRequestNum is the number of the request message that this message response for
	 */
	public Message(int number, D data, F from, T to, int replyToRequestNum) { //message from server to client
		this(number, data, from, to);
		this.replyToRequestNum=replyToRequestNum;
	}

	/**
	 * 
	 * @return return the object the message sent to
	 */
	public  T getTo() {
		return this.to;
	}
	
	/**
	 * 
	 * @return return the object the message get from
	 */
	public F getFrom() {
		return this.from;
	}
	
	/**
	 * 
	 * @return return the status of the message
	 */
	public E_MessageStatus getStatus() {
		return this.status;
	}

	/**
	 * set the message status to the value the method get
	 * @param status is the message status
	 */
	public void setStatus(E_MessageStatus status) {
		this.status = status;
	}

	/**
	 * 
	 * @return return the message content
	 */
	public D getData() {
		return this.data;
	}

	@Override
	public int hashCode() {
		return Objects.hash(requestNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message<?, ?, ?> other = (Message<?, ?, ?>) obj;
		return requestNumber == other.requestNumber;
	}

	@Override
	public String toString() {
		return "Message [requestNumber=" + requestNumber + ", data=" + data + ", from=" + from.getID() + ", to=" + to.getID()
				+ ", status=" + status+"]";
	}
}
