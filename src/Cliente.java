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
	static Scanner entrada;
	private String nome;
	private String id;

	public Cliente (String nome) throws RemoteException {
		this.id = nome;
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
			entrada = new Scanner(System.in);
			ServidorIF servidor = (ServidorIF )Naming.lookup("rmi://localhost/servidor");
			String outMsg = "";		
			System.out.println("Bem vindo ao chat, favor inserir seu nome sem espaços para logar!");
			String mensagemEntrada = entrada.nextLine();
			
			while(!servidor.isUsuarioValido(mensagemEntrada) || mensagemEntrada.contains(" ") || mensagemEntrada.isEmpty() || mensagemEntrada.length() == 0) {
				if (!servidor.isUsuarioValido(mensagemEntrada)) {
					System.out.println("Usuário já existente, insira outro nome!");
					mensagemEntrada = entrada.nextLine();
				} else {
					System.out.println("Você inseriu espaços, favor inserir seu nome sem espaços!");
					mensagemEntrada = entrada.nextLine();
				}
			}
			Cliente cliente = new Cliente(mensagemEntrada);
			servidor.conectar(cliente);
			kB = new Scanner(System.in);

			do {	
				System.out.println(
						"Mini Chat (Digite 'bye' pra sair)"
								+ "\n [1]Listar usuarios"
								+ "\n [2]Renomear"
								+ "\n [3]Enviar para o grupo"
								+ "\n [4]Mensagem privada");
				System.out.print("Out: ");
				outMsg = kB.nextLine();

				switch(outMsg) {
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
					servidor.sendAll(cliente.getId(),outMsg);
					break;

				case "4":
					System.out.println("Escreva o nome do usuario!");
					String nomeUsuario = kB.nextLine();
					System.out.println("Escreva uma mensagem!");
					outMsg = kB.nextLine();
					servidor.send(cliente.id, nomeUsuario, outMsg);
					break;
				case "bye":
					break;

				default:
					System.out.println("Comando invalido!");
					break;
				}
			} while(!outMsg.equals("bye"));
		} catch (NotBoundException | MalformedURLException | RemoteException ex) {
			Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
		}			
	}
}