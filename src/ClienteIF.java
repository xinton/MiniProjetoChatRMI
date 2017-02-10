import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClienteIF  extends Remote{
	public void showMensage(String msg) throws RemoteException;   
	public String getNome() throws RemoteException;   
	public void setNome(String nome) throws RemoteException;   
	
	public String getId() throws RemoteException;   
	public void setId(String id) throws RemoteException;   
}
