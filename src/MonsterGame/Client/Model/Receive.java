package MonsterGame.Client.Model;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Receive implements Runnable {
	private static Socket socket;
	private boolean stop;
	private ClientEngine clientEngine;
	
	public Receive(Socket socket, ClientEngine clientEngine) {
		Receive.socket = socket;
		this.clientEngine = clientEngine;
		this.stop = false;
	}

	@Override
	public void run() {
		Scanner scanner = null;
		stop = false;
		try {
			if (!socket.isClosed()) {
				scanner = new Scanner(socket.getInputStream());
				while (scanner.hasNext() && !stop) {
					String str = scanner.nextLine();
					Unprocessed.addReceive(str, clientEngine);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}

	}

	public void stop() {
		// socket.close();
		stop = true;
	}
}
