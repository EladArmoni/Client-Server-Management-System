package Model;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This Class represent a client computer
 * @author Elad_Armoni
 *
 */
public class Client implements IClient{
	// -------------------------------Class Members------------------------------
	private static int arc = 0;
	private int number;
	private String ip;
	private IServer connectToServer;
	private ArrayList<Message<?, ?, ?>> sentRequest;
	private ArrayList<Message<?, ?, ?>> receivedResponses;
	
	// -------------------------------Constructors------------------------------
	/**
	 * Client constructor gets the ip and create a new client with this ip
	 * First client does not connect to server so connectToServer is null
	 * @param iP the ip parameter
	 */
	public Client(String iP) {
		number = ++arc;
		this.ip=iP;
		this.connectToServer=null;
		this.sentRequest=new ArrayList<>();
		this.receivedResponses=new ArrayList<>();
	}
	// -----------------------------------------Setters&&Getters--------------------------------------
	
	@Override
	public String getID() {
		return this.ip;
	}
	
	/**
	 * 
	 * @return the server that this client connected to
	 */
	public IServer getConnectedToServer() {
		return this.connectToServer;
	}
	
	// -------------------------------All Methods------------------------------
	
	@Override
	public boolean messageSent(Message<?, ?, ?> message) {
		if((this.connectToServer).equals(message.getTo())) {
			return this.sentRequest.add(message);
		}
		return false;
	}

	@Override
	public boolean onMessageReceived(Message<?, ?, ?> message) {
		if((this.connectToServer).equals(message.getFrom())) {
			return this.receivedResponses.add(message);
		}
			return false;
	}

	@Override
	public boolean connectTo(IServer server) {
		if(this.connectToServer==null) {
			this.connectToServer=server;
			return true;
		}
		return false; //already connected
	}

	
	@Override
	public boolean disconnect() {
		if(this.connectToServer!=null) {
			this.connectToServer=null;
			return true;
		}
		return false; //no connection to disconnect
	}
	
	// -------------------------------hashCode equals & toString------------------------------

	@Override
	public int hashCode() {
		return Objects.hash(ip);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return Objects.equals(ip, other.ip);
	}

	@Override
	public String toString() {
		return "Client [number=" + number + ", ip=" + ip + ", connectToServer=" + connectToServer + ", sentRequest="
				+ sentRequest + ", receivedResponses=" + receivedResponses + "]";
	}
}
