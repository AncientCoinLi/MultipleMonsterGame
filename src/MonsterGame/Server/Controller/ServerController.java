package MonsterGame.Server.Controller;

import java.net.Socket;

import MonsterGame.Server.Model.Unprocessed;

public class ServerController {

	
	public void reply(String str, Socket socket) {
		synchronized (Unprocessed.send) {
			Unprocessed.send.notifyAll();
			Unprocessed.addSend(str, socket);
		}
	}
	
	public void reply(String str) {
		synchronized (Unprocessed.send) {
			Unprocessed.send.notifyAll();
			Unprocessed.addSend(str);
		}
	}
}
