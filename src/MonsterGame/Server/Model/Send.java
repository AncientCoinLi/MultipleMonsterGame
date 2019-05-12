package MonsterGame.Server.Model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class Send implements Runnable {
	public static HashMap<String, Socket> allSocket;
	public Send(HashMap<String, Socket> allSocket, ServerEngine serverEngine) {
		Send.allSocket = allSocket;
	}

	@Override
	public void run() {
		PrintWriter pWriter = null;
		String str = null;
		try {
			while (true) {
				synchronized (Unprocessed.send) {
					while (Unprocessed.send.size() == 0) {
						Unprocessed.send.wait();
					}
					Unprocessed.send.notifyAll();
					Message m;
					m = Unprocessed.send.poll();
					str = m.toString();
					if (m.socket != null) {
						String tmp = "";
						for (String v : Send.allSocket.keySet()) {
							if (allSocket.get(v).equals(m.socket))
								tmp = v;
						}
						if (!m.getSocket().isClosed()) {
							pWriter = new PrintWriter(m.getSocket().getOutputStream());
							pWriter.write(str + ":" + tmp + "\n");
							pWriter.flush();
						} else {
							allSocket.remove(tmp);
						}
					} else {
						Socket s;
						String var = "";
						for (String v : allSocket.keySet()) {
							s = allSocket.get(v);
							if (s.isClosed()) {
								var = v;
								continue;
							}
							pWriter = new PrintWriter(s.getOutputStream());
							pWriter.write(str + ":" + v + "\n");
							pWriter.flush();
						}
						allSocket.remove(var);
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// scanner.close();
			// pWriter.close();
		}
	}

}
