import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MensagemIF extends Remote
{
    public void mensagem(String mensagem)
            throws RemoteException;
}