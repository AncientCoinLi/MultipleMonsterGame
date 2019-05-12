package MonsterGame.Client.Model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Send implements Runnable {
	private Socket socket;
	private boolean stop;
	private ClientEngine clientEngine;
	
    public Send(Socket socket, ClientEngine clientEngine) {
    	this.clientEngine = clientEngine;
        this.socket = socket;
        stop = false;
    }
    
    public void stop() {
    	stop = true;
    }
    public void setStart() {
    	stop = false;
    }

    @Override
    public void run() {
    	stop = false;
        PrintWriter pWriter=null;
        Scanner scanner=null;
        try {
            pWriter=new PrintWriter(socket.getOutputStream());
            scanner=new Scanner(System.in);
            while (!stop) {
				synchronized (Unprocessed.send) {
					while(Unprocessed.send.size() == 0) {
						Unprocessed.send.wait();
					} 
					Unprocessed.send.notifyAll();
					pWriter.write(Unprocessed.send.poll()+":"+clientEngine.serials+"\n");
					pWriter.flush();
				} 
			}


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
            scanner.close();
            pWriter.close();
        }

    }
}
