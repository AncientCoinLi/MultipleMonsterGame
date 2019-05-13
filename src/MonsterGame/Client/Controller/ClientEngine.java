package MonsterGame.Client.Controller;

import MonsterGame.Client.View.GameUI;
import MonsterGame.Client.View.LoginUI;
import MonsterGame.Client.View.MainUI;
import MonsterGame.Client.View.RankUI;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ClientEngine {
	private static String username;
	public String serials;
	public static boolean host;
	public static int numOfPlayer = 4;
	private ClientController clientController;

	public ClientEngine(Stage stage) {
		this.clientController = new ClientController(stage);
	}

	public void setUser(String username) {
		ClientEngine.username = username;
	}

	public static String getUser() {
		return username;
	}

	public ClientController getClientController() {
		return this.clientController;
	}

	public void start() {
		this.clientController.start(this);
	}

	public void process(String str) {
		String info = "";
		String[] order = str.split(":");
		switch (order[0]) {
		case "login":
			login(order);
			break;
		case "register":
			register(order);
			break;
		case "role":
			drawRole(order);
			break;
		case "monster":
			String direction = order[1];
			this.monsterMove(direction);
			break;
		case "move":
			playerMove(order[1], order[2]);
			break;
		case "eat":
			clientController.getGameUI().eaten(order[1]);
			break;
		case "reset":
			clientController.skipToGameUI(this, getUser(), ClientEngine.numOfPlayer);
			break;
		case "win":
			if (order[1].equals(getUser())) {
				info = "Congradulations.\n" + "You win this game, " + order[1];
				clientController.showTips(info);
			}
			break;
		case "lack":
			info = "Fail to start.\n" + "Because number of players is not enough.\n" + "You have to wait more players.";
			clientController.showTips(info);
			break;
		case "setnum":
			numOfPlayer = Integer.parseInt(order[1]);
			clientController.setNumText(numOfPlayer);
			break;
		case "rank":
			showRankInfo(order);
			break;
		default:
			break;
		}
	}

	private void showRankInfo(String[] order) {
		StringBuffer rankInfo;
		rankInfo = new StringBuffer();
		rankInfo.append("Player\tWin\tTotal\tWinRate");
		for (int i = 1; i < order.length - 1; i++) {
			rankInfo.append("\n" + order[i]);
		}
		clientController.logoutRequest("null");
		clientController.skipToRankUI(this, rankInfo.toString());
	}

	private void drawRole(String[] order) {
		int x = Integer.parseInt(order[2]);
		int y = Integer.parseInt(order[3]);
		int sitId = Integer.parseInt(order[4]);
		clientController.addPlayer(order[1], x, y, sitId);
	}

	private void register(String[] order) {
		serials = order[order.length - 1];
		if (order[1].equals("succeed")) {
			clientController.showTips("Register Successfully");
			clientController.skipToMainUI(this);
		} else {
			clientController.showTips("Username Exists");
		}
		clientController.logoutRequest("null");
	}

	private void playerMove(String username, String direction) {
		clientController.playerMove(username, direction);
	}

	private void monsterMove(String direction) {
		clientController.monsterMove(direction);
	}

	private void login(String[] order) {
		serials = order[order.length - 1];
		if (order[1].equals("succeed")) {
			setUser(order[2]);
			if (order[3].equals("true"))
				host = true;
			else
				host = false;
			clientController.skipToGameUI(this, order[2], ClientEngine.numOfPlayer);
			clientController.showTips("Login Successfully");
		} else {
			clientController.showTips("Invalid Username or Password");
			clientController.skipToLoginUI(this);
			clientController.logoutRequest("null");
		}
	}

}
