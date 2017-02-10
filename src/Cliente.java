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
	private String nome;
	private String id;
	
	public Cliente (String id, String nome) throws RemoteException {
		this.id = id;
		this.nome = nome;
	}
	
	public void showMensage(String msg) throws RemoteException {
        System.out.println(msg);
    }
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() throws RemoteException{
		return nome;
	}

	public void setNome(String nome) throws RemoteException{
		this.nome = nome;
	}
	
	public static void main(String[] args) {		
		try {
			kB = new Scanner(System.in);
            ServidorIF servidor = (ServidorIF )Naming.lookup("rmi://localhost/servidor");
            Cliente cliente = new Cliente("1","Guest");
            servidor.conectar(cliente);
			String outMsg = "";		
            
           do{	
        	   
        	   System.out.println(
       				"Mini Chat (Digite 'bye' pra sair)"
       				+ "\n [1]Listar usuarios"
       				+ "\n [2]Renomear"
       				+ "\n [3]Enviar para o chat"
       				+ "\n [4]Mensagem privada");
        	   System.out.print("Out: ");
				outMsg = kB.nextLine();
				
				switch(outMsg){
				case "1":
					servidor.listarUsuarios(cliente.getId());
					break;

				case "2":
					System.out.println("Insira um nome de usuario!");
					outMsg = kB.nextLine();
					servidor.rename(cliente.getId(),outMsg);
					break;

				case "3":
					System.out.println("Escreva uma mensagem!");
					outMsg = kB.nextLine();
					servidor.mensagem(cliente.getId(),outMsg);
					break;

				case "4":
					break;

				default:
					System.out.println("Comando invalido!");
					break;
				}
				
				
				
			}while(!outMsg.equals("bye"));
            
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
			
	}

	

	
}