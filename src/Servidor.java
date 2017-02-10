import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.MaquinaCliente;

public class Servidor extends UnicastRemoteObject implements ServidorIF{

	private static HashMap<String,ClienteIF> clientes = new HashMap<String,ClienteIF>();
	public Servidor() throws RemoteException{}
	
	public void mensagem(String origem, String msg) throws RemoteException {
		clientes.get(origem).showMensage(msg);    
	}
	
	public void conectar(ClienteIF cliente) throws RemoteException {
		clientes.put(cliente.getId(), cliente);
    }
	
	public void listarUsuarios(String origem) throws RemoteException{
		String mensagem = "Usuarios Online:";
		
		for (Entry<String, ClienteIF> entry : clientes.entrySet()) {
			ClienteIF cliente = entry.getValue();
            mensagem += "\n" + cliente.getNome();
        }
		clientes.get(origem).showMensage(mensagem);  
	}
	

	@Override
	public void rename(String origem,String nome) throws RemoteException {
		clientes.get(origem).setNome(nome);
	}

	/**
	 * @todo a mensagem formatada terá que seguir este padrão: <IP>:<PORTA>/~<nome_usuario> : <mensagem> <hora-data>
	 * 192.168.0.123:67890/~ana: Alguma novidade do mini-projeto? 14h31 14/06/2016
	 * @param mensagem
	 */
	
	@Override
	public void send(String origem, String mensagem) throws RemoteException {
		for (Entry<String, ClienteIF> entry : clientes.entrySet()) {
			ClienteIF cliente = entry.getValue();
			cliente.showMensage(mensagem);
        }
		
	}

	@Override
	public void sendAll(String origem, String destino, String mensagem) throws RemoteException {
		// TODO Auto-generated method stub
		
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
