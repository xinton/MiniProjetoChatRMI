import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServidorIF extends Remote{
	public void conectar(ClienteIF cliente) throws RemoteException;
	public void mensagem(String origem, String mensagem) throws RemoteException;
	
	public void listarUsuarios(String origem) throws RemoteException;
	public void rename(String origem, String nome) throws RemoteException;
	public void sendAll(String origem, String mensagem) throws RemoteException;
	public void send(String origem,String destino, String mensagem) throws RemoteException;
}
