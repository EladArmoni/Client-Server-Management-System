package Model;
/**
 * @author Elad_Armoni
 */
public interface Connectable {

	/**
	 * Add a message sent from a client or server to the relevant array 
	 * Please verify if the client and server are connected
	 * @param message is the message parameter
	 * @return return true if added successfully, false otherwise
	 */
    boolean messageSent(Message<?, ?, ?> message);
    
	/**
	 * Add a message received from client or server to the relevant array
	 * Please verify if the client and server are connected
	 * @param message is the message parameter
	 * @return return true if added successfully, false otherwise
	 */
    boolean onMessageReceived(Message<?, ?, ?> message);
    
    /**
     * 
     * @return return the id for the element, ip for client and ip:port for server
     */
	String getID();
}
