package MonsterGame;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;

import MonsterGame.Server.Controller.ServerEngine;
import MonsterGame.Server.Model.Receive;
import MonsterGame.Server.Model.Send;

public class ServerStartup {
	private static HashMap<String, Socket> allSocket = new HashMap<>();
	public static Thread send;

	public static void main(String[] args) throws IOException {
		System.out.println(Inet4Address.getLocalHost().getHostAddress().toString());
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(8888);
		ServerEngine serverEngine = new ServerEngine();

		while (true) {
			System.out.println("waiting");
			Socket socket = serverSocket.accept();
			// generate a random int as mark of socket
			Random r = new Random();
			r.setSeed(System.currentTimeMillis());
			String serials = r.nextLong() + "";
			allSocket.put(serials, socket);

			System.out.println("connected");
			if (allSocket.size() == 1) {
				send = new Thread(new Send(allSocket, serverEngine));
				send.start();
			}
			new Thread(new Receive(socket, serverEngine)).start();
		}
	}

}
