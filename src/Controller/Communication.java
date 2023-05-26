package Controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import Model.Client;
import Model.IClient;
import Model.IServer;
import Model.Message;
import Model.Server;
/**
 * This Communication object ~ represents the class system
 * 
 * @author Elad_Armoni
 */
public class Communication {

	// -------------------------------Class Members------------------------------
	//<IP,Client>
	private Map<String, IClient> clients;
	
	//<IP, <PORT,SERVER>
	//dns.get("10.0.0.4").get(50) -> Server at port 10.0.0.4:50
	private Map<String,Map<Integer,IServer>> dns;
	
	//All messages
	private Map<Integer, Message<?,?,?>> messages;

	// -------------------------------Constructor------------------------------
	
	/**
	 * The constructor create new Communication
	 * Initialise the clients, dns and messages hashmaps to an empty hashmaps
	 */
	public Communication() {
		this.clients=new HashMap<>();
		this.dns=new HashMap<>();
		this.messages=new HashMap<>();
	}
	// -----------------------------------------Getters--------------------------------------

	/**
	 * 
	 * @return return the clients hashmap
	 */
	public Map<String, IClient> getClients() {
		return clients;
	}
	
	/**
	 * 
	 * @return return the dns hashmap
	 */
	public Map<String, Map<Integer, IServer>> getDns() {
		return dns;
	}
	
	/**
	 * 
	 * @return the messages hashmap
	 */
	public Map<Integer, Message<?,?,?>> getMessages() {
		return messages;
	}
	
	// -------------------------------Add && Remove Methods------------------------------
	/**
	 * this method adds new IP to map DNS,
	 * only if all the parameters are valid and the ip doesn't already exist. 
	 * @param ip is the ip parameter
	 * @return return true if added successfully . false otherwise.
	 */
	public boolean addIP(String ip) {
		if(ip!=null && ip!="" && !this.dns.containsKey (ip)) {
			Map<Integer, IServer> server=new HashMap<>();
			this.dns.put(ip, server);
			return true;
		}
		return false;//IP already exist
	}
	/**
	 * this method adds new server to IP, 
	 * only if all the parameters are valid and IP exist in map DNS and the server doesn't already exist in IP.
	 * @param ip ip parameter of server
	 * @param port parameter of server
	 * @return return true if added successfully . false otherwise.
	 */
	public boolean addServerToIP(String ip,int port) {
		if(ip!=null && ip!="" && port>0 && this.dns.containsKey (ip) && !this.dns.get(ip).containsKey(port)) {
			this.dns.get(ip).put(port, new Server(ip, port));
			return true;
		}
		return false;
	}
	/**
	 * this method adds new client, if is not already exist.
	 * only if all the parameters are valid and the client doesn't already exist.
	 * @param ip of client
	 * @return return true if added successfully . false otherwise.
	 */
	public boolean addClient(String ip) {
		if(ip!=null && ip!="" && !this.clients.containsKey(ip)) {
			this.clients.put(ip, new Client(ip));
			return true;
		}
		return false;
	}

	/**
	 * this method connect client to server, 
	 * only if all the parameters are valid and the client already exist in the system
	 * use suggestServer function to get relevant server.
	 * @param ipClient ip of client
	 * @return return IServer if connected successfully . false otherwise.
	 */
	
	public IServer connectClientToServer(String ipClient) {
		IServer server=suggestServer();
		if(ipClient!=null && ipClient!="" && server!=null && this.clients.containsKey(ipClient) && this.clients.get(ipClient).connectTo(server) && server.register(this.clients.get(ipClient))) 
			return server; 
		return null;
	}
	
	/**
	 * This method finds and returns the server with minimum clients connected with it,
	 * if there are more than one server the method returns the first server. 
	 * @return return IServer if found, null otherwise 
	 */
	private IServer suggestServer() {
		int min=utils.Constants.MAX_CLIENTS_CONNECTED_TO_ONE_SERVER;
		IServer minServer=null;
		for(String ip : this.dns.keySet()) {
			Map<Integer, IServer> portIServer = dns.get(ip);
			for(int port:portIServer.keySet()) {
				IServer currIServer=portIServer.get(port);
				if(((Server)currIServer).getClientsSize()<min) {
					min=((Server)currIServer).getClientsSize();
					minServer=currIServer;
				}
			}
		}
		return minServer;
	}
	
	/**
	 * This method adds a new message sender by client (to server) to the system,
	 * only if all the parameters are valid, the message doesn't already exist and 
	 * both client and server already exist in the system.
	 * message must be added to all the relevant arrays.
	 * 				
	 * @param <T> is the content of data
	 * @param number of message
	 * @param clientIp ip of client
	 * @param serverIP ip of server
	 * @param serverPort port of server
	 * @param data content of message
	 * @return true if the message was added successfully,false otherwise
	 */
	public <T> boolean sendMessageByClient(int number,String clientIp,String serverIP,int serverPort,T data) {
		if (number <= 0 || clientIp == ""  || clientIp == null || serverIP == null || serverIP == "" || serverPort <= 0)
			return false;
		IClient client=this.clients.get(clientIp);
		IServer server=this.dns.get(serverIP).get(serverPort);
		Message<IClient,IServer,T> message=new Message<>(number,data, client, server);
		if(!this.messages.containsKey(number) && client!=null && server!=null && client.messageSent(message) && server.onMessageReceived(message)) {
			this.messages.put(number, message);
			return true;
		}
		return false;
	}
	
	/**
	 * This method adds a new reply message sender by server (to client) to the system,
	 * only if :
	 * 			all the parameters are valid, 
	 * 			the message doesn't already exist,
	 * 			the message number respondForNumMessage is already exist, 
	 * 			both client and server already exist in the system.
	 *          The request message for this message is already exist in the system,
	 *          and sent to client who sent request message
	 * ** Do not forget to update the status of the message according to the instructions in the document and
	 * 					message must be added to all the relevant arrays.
	 * 
	 * After successfully adding a response message from the server to the client, 
	 * the connection should be disconnected by using the method disconnectAConnectionClientToServer().
	 * @param <T> is the data of the message
	 * @param number of message
	 * @param serverIP ip of server
	 * @param serverPort port of server
	 * @param clientIp ip of client
	 * @param respondForNumMessage number of respond to message
	 * @param data content of message
	 * @return true if the message was added successfully,false otherwise
	 */
	public <T> boolean sendMessageByServer(int number,String serverIP,int serverPort,String clientIp,int respondForNumMessage,T data) {
		if (number <= 0 || clientIp == "" || clientIp == null || serverIP == null || serverIP == "" || serverPort <= 0 || respondForNumMessage <= 0)
			return false;
		IClient client=this.clients.get(clientIp);
		IServer server=this.dns.get(serverIP).get(serverPort);
		Message<IServer,IClient,T> message=new Message<>(number,data, server, client,respondForNumMessage);
		Message<?, ?, ?> requestMessage=this.messages.get(respondForNumMessage);
		if(!this.messages.containsKey(number) && requestMessage!=null &&client!=null && server!=null&& requestMessage.getTo().equals(server) 
				&& client.onMessageReceived(message) &&	server.messageSent(message)) {
			this.messages.put(number, message);
			disconnectAConnectionClientToServer(serverIP, serverPort, clientIp);
			if(data.getClass() == requestMessage.getData().getClass()) {
				message.setStatus(utils.E_MessageStatus.SUCCESS);
				requestMessage.setStatus(utils.E_MessageStatus.SUCCESS);
			}
			else {
				message.setStatus(utils.E_MessageStatus.FAILURE);
				requestMessage.setStatus(utils.E_MessageStatus.FAILURE);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Disconnect a connection between the client and the server
	 * @param serverIP
	 * @param serverPort
	 * @param clientIp
	 */
	private void disconnectAConnectionClientToServer(String serverIP,int serverPort,String clientIp) {
		IServer server=this.dns.get(serverIP).get(serverPort);
		IClient client=this.clients.get(clientIp);
		if(server!=null && client!=null) {
			server.disconnectClient(client);
			client.disconnect();
		}
	}
	
	// -------------------------------Queries------------------------------
	
	/**
	 * This query returns an array list of a messages that gets from server with status FAILURE  
	 * @return team array if found, null otherwise
	 */
	public ArrayList<Message<?,?,?>> getAllFailureMessageFromServer() {
		 ArrayList<Message<?,?,?>> failureMessages=new ArrayList<>();
		 for(Message<?,?,?> message : this.messages.values()) {
			 if(message.getTo() instanceof IClient && message.getStatus()==utils.E_MessageStatus.FAILURE) {
					failureMessages.add(message);
			 }
		 }
		if(failureMessages.size()==0)
			return null;
		return failureMessages;
	}
	
	/**
	 * This query returns an array list of a servers that is Online.  
	 * @return servers array if found, null otherwise
	 */
	public ArrayList<IServer> getAllOnlineServer() {
		 ArrayList<IServer> onlineServers=new ArrayList<>();
		 for(String ip : this.dns.keySet()) {
				Map<Integer, IServer> portIServer = dns.get(ip);
				for(int port:portIServer.keySet()) {
					IServer currIServer=portIServer.get(port);
					if(currIServer.isOnline())
						onlineServers.add(currIServer);
				}
			}
			if(onlineServers.size()==0)
				return null;
			return onlineServers;
	}
}
