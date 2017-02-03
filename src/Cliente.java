import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Cliente  extends UnicastRemoteObject implements ClienteIF{

	static Scanner kB;
	static boolean isRunning = true;
	private String nome;
	
	public Cliente (String nome) throws RemoteException {
		this.nome = nome;
	}
	
	public void showMensage(String msg) throws RemoteException {
        System.out.println("showMensage"+msg);
    }

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public static void main(String[] args) {
		kB = new Scanner(System.in);
		try {
            ConectaIF servidorConect = (ConectaIF) 
            Naming.lookup("rmi://localhost/servidor");
            servidorConect.conectar("Guest");
            
            MensagemIF servidorMensage= (MensagemIF) 
                    Naming.lookup("rmi://localhost/servidor");
            
            System.out.println("Mini Chat (Digite 'bye' pra sair)");
			String outMsg = "";
			System.out.print("Out: ");
            
            do{						
				outMsg = kB.nextLine();
				servidorMensage.mensagem(outMsg);
				System.out.print("Out: ");
				/*if (!s.isClosed()){
					dataOut.writeUTF(outMsg);
				} else {break;}*/				
			}while(!outMsg.equals("bye"));
            
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
			
	}
}