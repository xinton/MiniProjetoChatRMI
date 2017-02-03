import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.MaquinaCliente;


public class ServerListener extends Thread{
	private List<MaquinaCliente> maquinasClientes = new ArrayList<MaquinaCliente>();
	private MaquinaCliente maquinaCliente;
	Socket s;

	public ServerListener(MaquinaCliente maquinaCliente, List<MaquinaCliente> maquinasClientes) {
		this.maquinaCliente = maquinaCliente;
		this.maquinasClientes = maquinasClientes;
	}

	public void run(){
		try {

			DataOutputStream dataOutput = new DataOutputStream(this.maquinaCliente.getSocketCliente().getOutputStream());
			String msg = "";
			do{
				DataInputStream dataIn = new DataInputStream(this.maquinaCliente.getSocketCliente().getInputStream());
				String dadosDoCliente = dataIn.readUTF();
				String output[] = dadosDoCliente.split(" ");
				String op = output[0];
				switch(op){
				case "bye":
					msg = "fim";
					dataOutput.writeUTF(msg);
					this.maquinasClientes.remove(this.maquinaCliente);
					this.maquinaCliente.getSocketCliente().close();
					break;

				case "list":
					dataOutput.writeUTF(this.listarUsuarios());
					break;
					
				case "rename":
					if (output.length == 1) {
						dataOutput.writeUTF("Insira um nome de usuário!");
						break;
					}
					String nomeUsuario = dadosDoCliente.substring(7,dadosDoCliente.length());
					
					if (!nomeUsuario.trim().contains(" ")) {
						dataOutput.writeUTF(this.renameUsuario(nomeUsuario));
						break;
					}

					if (nomeUsuario.trim().contains(" ")) {
						dataOutput.writeUTF("Nome de usuario invalido, favor remover espacos em branco que separa os nomes!");
						break;
					}
					
				case "send":
					if (output.length == 1) {
						dataOutput.writeUTF("Insira o comando '-all' ou '-user'!");
						break;
					}
					if (output.length == 2) {
						dataOutput.writeUTF("Escreva uma mensagem!");
						break;
					}
					if (dadosDoCliente.substring(5,9).equals("-all")) {
						this.sendAll(dadosDoCliente.substring(10,dadosDoCliente.length()));
						break;
					}
					/**
					 * @todo aqui eu limitei que o usuario nao poderia separar seu nome.
					 */
					if (dadosDoCliente.substring(5,10).equals("-user")) {
						String arrayDados[] = dadosDoCliente.substring(11,dadosDoCliente.length()).split(" ");
						String nome = arrayDados[0];
						String mensagem = dadosDoCliente.substring(12+nome.length(),dadosDoCliente.length());
						this.sendUser(nome, mensagem);
						break;
					}
					
				default:
					dataOutput.writeUTF("Comando inválido!");
					break;
				}		
			}while(!this.maquinaCliente.getSocketCliente().isClosed());			
			this.maquinaCliente.getSocketCliente().close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author mayer
	 * @return String
	 */
	public String listarUsuarios(){
		String mensagem = "Usuarios Online:";

		for(MaquinaCliente maquinaCliente: this.maquinasClientes) {
			mensagem += "\n" + maquinaCliente.getNome();
		}

		return mensagem;
	}

	/**
	 * @author mayer
	 * @return String
	 */
	public String renameUsuario(String novoNome){

		if (!this.isNomeExistente(novoNome)) {
			this.maquinaCliente.setNome(novoNome);
			return "Usuário Renomeado com sucesso!";
		}

		return "Nome de usuário já existente!";
	}
	/**
	 * @author mayer
	 * @param novoNome
	 * @return boolean
	 */
	private boolean isNomeExistente(String novoNome){

		for (MaquinaCliente maquinaCliente: this.maquinasClientes) {

			if (maquinaCliente.getNome().equals(novoNome)) {
				return true;
			}
		}

		return false;
	}

	public MaquinaCliente findCliente(String nomeUser) {
		for (MaquinaCliente maquinaCliente: this.maquinasClientes) {

			if (maquinaCliente.getNome().equals(nomeUser)) {
				return maquinaCliente;
			}
		}

		return null;
	}

	public void sendUser(String nome, String mensagem) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss yyyy/MM/dd");
		Date date = new Date();
		String mensagemFormatada = this.maquinaCliente.getIp()+
				":"+
				this.maquinaCliente.getPorta()+
				"/~"+
				this.maquinaCliente.getNome()+
				":"+
				mensagem+
				" "+dateFormat.format(date)
				; 
		MaquinaCliente cliente = this.findCliente(nome);

		if (cliente != null) {
			try {
				DataOutputStream dataOutput = new DataOutputStream(cliente.getSocketCliente().getOutputStream());
				dataOutput.writeUTF(mensagemFormatada);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @todo a mensagem formatada terá que seguir este padrão: <IP>:<PORTA>/~<nome_usuario> : <mensagem> <hora-data>
	 * 192.168.0.123:67890/~ana: Alguma novidade do mini-projeto? 14h31 14/06/2016
	 * @todo criar validação caso o usuario não mande nenhum texto
	 * @param mensagem
	 */
	public void sendAll(String mensagem) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss yyyy/MM/dd");
		Date date = new Date();
		
		String mensagemFormatada = this.maquinaCliente.getIp()+
				":"+
				this.maquinaCliente.getPorta()+
				"/~"+
				this.maquinaCliente.getNome()+
				":"+
				mensagem+
				" "+dateFormat.format(date)
				;
		for (MaquinaCliente maquinaClienteOnline: this.maquinasClientes) {
			try {
				if (!maquinaClienteOnline.equals(this.maquinaCliente)) {
					DataOutputStream dataOutput = new DataOutputStream(maquinaClienteOnline.getSocketCliente().getOutputStream());
					dataOutput.writeUTF(mensagemFormatada);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
