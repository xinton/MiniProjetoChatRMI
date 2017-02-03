import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor extends UnicastRemoteObject implements MensagemIF, ConectaIF{

	private static List<Cliente> clientes = new ArrayList<Cliente>();
	public Servidor() throws RemoteException{}
	
	public void mensagem(String msg) throws RemoteException {
		for (Cliente cliente : Servidor.clientes){
			System.out.println(cliente.getNome());
			cliente.showMensage(msg);
		}
    }
	
	public void conectar(String cliente) throws RemoteException {
        clientes.add(new Cliente(cliente));
    }
	
	public static void main(String[] args) {
		
		try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Naming.bind("servidor", new Servidor());
        } catch (RemoteException | AlreadyBoundException | MalformedURLException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
	
	}

}
