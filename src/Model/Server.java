package Model;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This Class represent a server computer
 * @author Elad_Armoni
 *
 */
public class Server implements IServer{
	// -------------------------------Class Members------------------------------
	private static int arc = 0;
	private final int number;
	private String ip;
	private int port;
	private ArrayList<IClient> clients;
	private ArrayList<Message<?, ?, ?>> receivedRequests;
	private ArrayList<Message<?, ?, ?>> sentResponses;

	// -------------------------------Constructors------------------------------

	/**
	 * The server constructor create new server with the ip and port arguments
	 * @param ip is the server ip
	 * @param port is the server port
	 */
	public Server(String ip,int port) {
		number = ++arc;
		this.ip=ip;
		this.port=port;
		this.clients=new ArrayList<>();
		this.receivedRequests=new ArrayList<>();
		this.sentResponses=new ArrayList<>();
	}

	// -----------------------------------------Setters&&Getters--------------------------------------

	/**
	 * 
	 * @return return the server IP
	 */
	public String getIP() {
		return this.ip;
	}

	/**
	 * 
	 * @return return the server Port
	 */
	public int getPort() {
		return this.port;
	}
	
	@Override
	public String getID() {
		return this.ip+":"+this.port;
	}
	
	/**
     * 
     * @return return the number of clients that the server connected to
     */
	public int getClientsSize() {
		return this.clients.size();
	}

	// -------------------------------All Methods------------------------------
	
	@Override
	public boolean messageSent(Message<?, ?, ?> message) {
		if(this.clients.contains(message.getTo())) {
			return this.sentResponses.add(message);
		}
		return false;
	}

	@Override
	public boolean onMessageReceived(Message<?, ?, ?> message) {
		if(this.clients.contains(message.getFrom())) {
			return this.receivedRequests.add(message);
		}
		return false;
	}

	@Override
	public boolean register(IClient client) {
		if(this.clients.size()==utils.Constants.MAX_CLIENTS_CONNECTED_TO_ONE_SERVER) {
			return false; //no place for more clients
		}
		return this.clients.add(client);
	}

	@Override
	public boolean disconnectClient(IClient client) {
		return this.clients.remove(client);
	}
	
	@Override
	public boolean isOnline() {
		return this.clients.size()!=0;
	}

	
	// -------------------------------hashCode equals & toString------------------------------
	
	@Override
	public int hashCode() {
		return Objects.hash(ip, port);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Server other = (Server) obj;
		return Objects.equals(ip, other.ip) &&  port == other.port ;
	}
	
	@Override
	public String toString() {
		return "Server [number=" + number + ", ip=" + ip + ", port=" + port + ", clients=" + this.getClientsSize()
				+ ", receivingRequest=" + receivedRequests + ", respondRequests=" + sentResponses+"]" ;
	}
}
