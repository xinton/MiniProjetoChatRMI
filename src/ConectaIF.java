import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ConectaIF extends Remote{
    public void conectar(String cliente) throws RemoteException;
}
