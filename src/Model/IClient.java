package Model;
/**
 * @author Elad_Armoni
 */
public interface IClient extends Connectable{
	/**
	 * this method connect client to given server
	 * @param server is the server parameter
	 * @return true if connect successfully, false otherwise 
	 */
    boolean connectTo(IServer server);

    /**
     * this method disconnect a connection with server
     * @return true if disconnect successfully, false otherwise
     */
    boolean disconnect();

}
