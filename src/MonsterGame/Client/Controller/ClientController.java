package MonsterGame.Client.Controller;

import MonsterGame.Client.Model.Comm;
import MonsterGame.Client.Model.Role;
import MonsterGame.Client.Model.Unprocessed;
import MonsterGame.Client.View.GameUI;
import MonsterGame.Client.View.LoginUI;
import MonsterGame.Client.View.MainUI;
import MonsterGame.Client.View.RankUI;
import MonsterGame.Client.View.RegisterUI;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ClientController {

	private Stage stage;
	private GameUI gameUI;
	
	public ClientController(Stage stage) {
		this.stage = stage;
	}

	public void loginRequest(String username, String password) {
		Comm.start();
		synchronized (Unprocessed.send) {
			Unprocessed.send.notifyAll();
			Unprocessed.send.add("login:" + username + ":" + password);
		}
	}

	public void logoutRequest(String username) {
		synchronized (Unprocessed.send) {
			Unprocessed.send.notifyAll();
			Unprocessed.send.add("logout:" + username);
		}
		this.gameUI = null;
		Comm.stop();
	}

	public void moveRequest(String username, String direction) {
		synchronized (Unprocessed.send) {
			Unprocessed.send.notifyAll();
			Unprocessed.send.add("move:" + username + ":" + direction);
		}
	}

	public void rankRequest() {
		Comm.start();
		synchronized (Unprocessed.send) {
			Unprocessed.send.notifyAll();
			Unprocessed.send.add("rank");
		}
		this.gameUI = null;
	}

	public void resetRequest() {
		synchronized (Unprocessed.send) {
			Unprocessed.send.notifyAll();
			Unprocessed.send.add("reset");
		}
	}

	public void startRequest() {
		synchronized (Unprocessed.send) {
			Unprocessed.send.notifyAll();
			Unprocessed.send.add("start");
		}
	}

	public void registerRequest(String username, String password) {
		Comm.start();
		synchronized (Unprocessed.send) {
			Unprocessed.send.notifyAll();
			Unprocessed.send.add("register:" + username + ":" + password);
		}
	}

	public void setNumOfPlayerRequest(String info) {
		synchronized (Unprocessed.send) {
			Unprocessed.send.notifyAll();
			Unprocessed.send.add(info);
		}
	}

	public void start(ClientEngine engine) {
		Platform.runLater(() -> {
			stage.setScene(new Scene(new MainUI(engine)));
			stage.show();
		});
	}

	public void skipToRankUI(ClientEngine engine, String string) {
		Platform.runLater(() -> {
			stage.setScene(new Scene(new RankUI(engine, string)));
		});
	}

	public void skipToMainUI(ClientEngine engine) {
		Platform.runLater(() -> {
			stage.setScene(new Scene(new MainUI(engine)));
		});
	}

	public void skipToLoginUI(ClientEngine engine) {
		Platform.runLater(() -> {
			stage.setScene(new Scene(new LoginUI(engine)));
		});
	}
	

	public void skipToRegisterUI(ClientEngine engine) {
		Platform.runLater(() -> {
			stage.setScene(new Scene(new RegisterUI(engine)));
		});
	}
	
	public void showTips(String str) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Tips");
			alert.setHeaderText(null);
			alert.setContentText(str);
			alert.showAndWait();
		});
	}

	public void skipToGameUI(ClientEngine engine, String username, int numOfPlayer) {
		Platform.runLater(() -> {
			this.gameUI = new GameUI(engine, username, numOfPlayer);
			Scene scene = new Scene(this.gameUI);
			scene.setOnKeyPressed(e -> {
				Role player = this.gameUI.getPlayer(username);
				if(player != null) {
					moveRequest(username, moveDirection(e.getCode()));
				}
			});
			stage.setOnCloseRequest(e -> {
				this.logoutRequest(username);
				gameUI.removePlayer(username);
			});
			stage.setScene(scene);
		});
	}
	
	private String moveDirection(KeyCode key) {
		String direction = "";
		switch (key) {
		case W:
		case UP:
			direction = "up";
			break;
		case S:
		case DOWN:
			direction = "down";
			break;
		case A:
		case LEFT:
			direction = "left";
			break;
		case D:
		case RIGHT:
			direction = "right";
			break;
		default:
			break;
		}
		return direction;
	}

	public void addPlayer(String username, int x, int y, int sitId) {
		Platform.runLater(() -> {
			if (this.gameUI != null) {
				this.gameUI.addPlayer(username, x, y, sitId);
			}
		});
	}

	public GameUI getGameUI() {
		return this.gameUI;
	}

	public void setNumText(int numOfPlayer) {
		Platform.runLater(() -> {
			this.gameUI.setNumText(ClientEngine.numOfPlayer);
		});		
	}

	public void playerMove(String username, String direction) {
		Platform.runLater(() -> {
			Role player = gameUI.getPlayers().get(username);
			if (player != null)
				player.move(direction);
		});		
	}

	public void monsterMove(String direction) {
		Platform.runLater(() -> {
			gameUI.getMonster().move(direction);
		});		
	}

	
	
	

}
