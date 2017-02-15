import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor extends UnicastRemoteObject implements ServidorIF{

	private static HashMap<String,ClienteIF> clientes = new HashMap<String,ClienteIF>();

	public Servidor() throws RemoteException {}

	public void mensagem(String origem, String msg) throws RemoteException {
		clientes.get(origem).showMensage(msg);    
	}

	public void conectar(ClienteIF cliente) throws RemoteException {
		clientes.put(cliente.getId(), cliente);
	}
	
	public void desconectar(String id) throws RemoteException {
		clientes.remove(id);
	}

	public void listarUsuarios(String origem) throws RemoteException{
		String mensagem = "Usuarios Online: \n";

		for (Entry<String, ClienteIF> entry : clientes.entrySet()) {
			ClienteIF cliente = entry.getValue();
			mensagem += cliente.getNome() + "\n";
		}
		clientes.get(origem).showMensage(mensagem);  
	}


	@Override
	public void rename(String origem,String nome) throws RemoteException {
		if(this.isNomeValido(nome)) {
			clientes.get(origem).setNome(nome);
			clientes.get(origem).showMensage("Usuario renomeado com sucesso! \n");
		} else {
			clientes.get(origem).showMensage("Nome de usuário já existente \n");
		}
	}
	private boolean isNomeValido(String nome) {
		for (Entry<String, ClienteIF> entry : clientes.entrySet()) {

			if(entry.getKey().equals(nome)) {
				return false;
			}
		}

		return true;
	}
	@Override
	public boolean isUsuarioValido(String nome) throws RemoteException {
		for (Entry<String, ClienteIF> entry : clientes.entrySet()) {

			if(entry.getKey().equals(nome)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void sendAll(String origem, String mensagem) throws RemoteException { 
		DateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
		Date date = new Date();
		
		for (Entry<String, ClienteIF> entry : clientes.entrySet()) {
			if(!entry.getKey().equals(origem)) {
				String mensagemFormatada = "/~" + origem + " : " + mensagem + " "+ dateFormat.format(date);
				ClienteIF cliente = entry.getValue();
				cliente.showMensage(mensagemFormatada);
			}
		}

	}
	public ClienteIF findCliente(String nome) {
		for (Entry<String, ClienteIF> entry : clientes.entrySet()) {

			if(entry.getKey().equals(nome)) {
				ClienteIF cliente = entry.getValue();

				return cliente;
			}
		}

		return null;
	}
	@Override
	public void send(String origem, String destino, String mensagem) throws RemoteException {
		if(this.findCliente(destino) != null) {
			if(destino.equals(origem)){
				clientes.get(origem).showMensage("Usuario destino não pode ser o mesmo usuario de origem! \n");
			} else {
				DateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
				String mensagemFormatada = "/~" + origem + " : " + mensagem + " " + dateFormat.format(new Date());
				clientes.get(destino).showMensage(mensagemFormatada);
			}
			
		} else {
			clientes.get(origem).showMensage("Usuário inexistente \n");
		}
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
