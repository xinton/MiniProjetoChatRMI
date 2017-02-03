package model;

import java.net.InetAddress;
import java.net.Socket;

public class MaquinaCliente {
	private Socket socketCliente;
	private String nome;
	private String ip;
	private int porta;

	public MaquinaCliente(Socket socketCliente){
		this.socketCliente = socketCliente;
		this.nome = socketCliente.getInetAddress().getHostName();
		this.ip = socketCliente.getInetAddress().getHostAddress();
		this.porta = socketCliente.getLocalPort();
	}
	
	
	public Socket getSocketCliente() {
		return socketCliente;
	}


	public void setSocketCliente(Socket socketCliente) {
		this.socketCliente = socketCliente;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public int getPorta() {
		return porta;
	}


	public void setPorta(int porta) {
		this.porta = porta;
	}


	@Override
	public String toString() {
		return "MaquinaCliente [socketCliente=" + socketCliente + ", nome=" + nome + ", ip=" + ip + ", porta=" + porta
				+ "]";
	}
}
