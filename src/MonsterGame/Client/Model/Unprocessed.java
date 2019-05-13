package MonsterGame.Client.Model;

import java.util.LinkedList;

import MonsterGame.Client.Controller.ClientEngine;

public class Unprocessed {

	public static LinkedList<String> send;
	public static LinkedList<String> receive;
	
	public static void addSend(String str, ClientEngine clientEngine) {
		send.add(str);
	}
	
	public static void addReceive(String str, ClientEngine clientEngine) {
		receive.add(str);
		clientEngine.process(receive.poll());
	}
	
	static {
		send = new LinkedList<>();
		receive = new LinkedList<>();
	}

}