package MonsterGame.Client.Model;

import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class Comm {

	private static Socket socket;
	public static Send send;
	public static Receive receive;
	private static String ipAdd = "131.170.206.246";
	public static ClientEngine clientEngine;

	public static void start() {
		try {
			ipAdd = InetAddress.getLocalHost().getHostAddress();
			socket = new Socket(ipAdd, 8888);
			send = new Send(socket, clientEngine);
			receive = new Receive(socket, clientEngine);
			new Thread(send).start();
			new Thread(receive).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			System.out.println("Server is not running.");
			Platform.runLater(() -> {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Tips");
				alert.setHeaderText(null);
				alert.setContentText("Server is not running.");
				alert.showAndWait();
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void stop() {
		send.stop();
		receive.stop();

	}
}
