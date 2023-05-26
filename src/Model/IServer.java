package Model;

/**
 * @author Elad_Armoni
 */
public interface IServer extends Connectable{

    /**
     * Register the Client to this server
     * @param client is client parameter
     * @return return true if registers successfully, false otherwise 
     */
    boolean register(IClient client);

    /**
     * 
     * @return true if there are connected for clients , false otherwise
     */
    boolean isOnline();

    /**
     * this method un-registers the client
     * @return true if the un-registers successfully,false otherwise.
     */
    boolean disconnectClient(IClient client);

}
