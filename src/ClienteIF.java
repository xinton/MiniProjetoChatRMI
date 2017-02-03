import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClienteIF  extends Remote{
	public void showMensage(String msg) throws RemoteException;   
	public String getNome() throws RemoteException;   
}
