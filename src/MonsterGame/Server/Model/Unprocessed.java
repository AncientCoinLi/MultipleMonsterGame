package MonsterGame.Server.Model;

import java.net.Socket;
import java.util.LinkedList;

import MonsterGame.Server.Controller.ServerEngine;

public class Unprocessed {

	private static LinkedList<String> receive;
	public static LinkedList<Message> send;

	public static void addReceive(String str, Socket socket, ServerEngine serverEngine) throws Exception {
		receive.add(str);
		serverEngine.process(str, socket);
		receive.poll();
	}

	public static void addSend(String str, Socket socket) {
		send.add(new Message(str, socket));
	}

	public static void addSend(String str) {
		send.add(new Message(str));
	}

	static {
		receive = new LinkedList<String>();
		send = new LinkedList<Message>();
	}

}

class Message {
	Socket socket;
	String str;

	public Message(String str) {
		this.str = str;
		socket = null;
	}

	public Message(String str, Socket socket) {
		this.str = str;
		this.socket = socket;
	}

	@Override
	public String toString() {
		return str;
	}

	public Socket getSocket() {
		return this.socket;
	}
}
