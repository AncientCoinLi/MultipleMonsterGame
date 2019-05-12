package MonsterGame.Server.Model;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Receive implements Runnable {
	private Socket socket;
	private ServerEngine serverEngine;

	public Receive(Socket socket, ServerEngine serverEngine) {
		this.socket = socket;
		this.serverEngine = serverEngine;
	}

	@Override
	public void run() {
		Scanner scanner = null;
		String currStr = null;
		try {
			scanner = new Scanner(socket.getInputStream());
			while (scanner.hasNext()) {
				currStr = scanner.nextLine();
				Unprocessed.addReceive(currStr, socket, serverEngine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}
}
