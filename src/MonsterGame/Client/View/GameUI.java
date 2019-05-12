package MonsterGame.Client.View;

import java.util.HashMap;

import MonsterGame.Client.Controller.ClientController;
import MonsterGame.Client.Model.Role;
import MonsterGame.Client.Model.Cell;
import MonsterGame.Client.Model.ClientEngine;
import MonsterGame.Client.Model.Unprocessed;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameUI extends Pane {

	private final int TILE_SIZE = 50;
	private final int WIDTH = 9;
	private final int HEIGHT = 9;

	private Role monster;
	private Text numText = new Text();
	private HashMap<String, Role> players;
	private Group tileGroup;
	private Group pieceGroup;
	private BorderPane btnGroup;
	private Cell[][] board;

	public GameUI(ClientEngine clientEngine, String username, int numOfPlayer) {
		this.board = new Cell[WIDTH][HEIGHT];
		this.numText.setText("Max Player: " + numOfPlayer);

		Button startButton = new Button("Start Game");
		Button quitButton = new Button("Quit Game");
		Button logout = new Button("Logout");
		Button reset = new Button("Reset");

		players = new HashMap<>();
		monster = new Role("monster", 4, 4, 5);
		tileGroup = new Group();
		pieceGroup = new Group();
		btnGroup = new BorderPane();

		Cell cell = null;
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				if (y == 0 || y == 4 || y == 8) {
					cell = new Cell(true, x, y);
				} else if (x == 0 || x == 4 || x == 8) {
					cell = new Cell(true, x, y);
				} else {
					cell = new Cell(false, x, y);
				}
				board[x][y] = cell;
				tileGroup.getChildren().add(cell);
			}
		}
		board[4][4].setPiece(monster);
		pieceGroup.getChildren().add(monster);

		this.setPrefSize((WIDTH + 5) * TILE_SIZE, HEIGHT * TILE_SIZE);// 底板
		this.getChildren().addAll(tileGroup, pieceGroup, btnGroup);


		startButton.setPrefSize(100, 30);
		reset.setPrefSize(100, 30);
		logout.setPrefSize(100, 30);
		quitButton.setPrefSize(100, 30);

		ComboBox<Integer> numOfPlayers = new ComboBox<Integer>();
		Integer[] num = { 2, 3, 4 };
		numOfPlayers.getItems().addAll(num);
		numOfPlayers.setPrefSize(100, 30);

		numOfPlayers.setOnAction(e -> {
			numText.setText("Max Player: " + numOfPlayers.getValue());
			ClientEngine.numOfPlayer = numOfPlayers.getValue();
			String info = "setnum:" + numOfPlayers.getValue();
			clientEngine.getClientController().setNumOfPlayerRequest(info);
		});
		
		VBox vb = new VBox();
		vb.setSpacing(30);
		vb.setAlignment(Pos.CENTER);
		vb.setPadding(new Insets(80, 20, 100, 50));

		vb.getChildren().add(numText);
		if (ClientEngine.host) {
			vb.getChildren().addAll(numOfPlayers, startButton, reset);
		}
		vb.getChildren().addAll(logout, quitButton);

		btnGroup.setLayoutX((WIDTH + 0.5) * TILE_SIZE);
		btnGroup.setCenter(vb);

		reset.setOnAction(e -> {
			clientEngine.getClientController().resetRequest();
			
		});

		startButton.setOnAction(e -> {
			clientEngine.getClientController().startRequest();
		});

		logout.setOnAction(e -> {
			clientEngine.getClientController().logoutRequest(username);
			this.removePlayer(username);
			clientEngine.getClientController().skipToMainUI(clientEngine);
		});


		quitButton.setOnAction(e -> {
			clientEngine.getClientController().logoutRequest(username);
			this.removePlayer(username);
			System.exit(0);
		});

	}

	public void addPlayer(String username, int x, int y, int sitId) {
		Role player = new Role(username, x, y, sitId);
		board[x][y].setPiece(player);
		if (pieceGroup.getChildren().contains(player)) {
			return;
		}
		pieceGroup.getChildren().add(player);
		if (players.containsKey(username))
			return;
		players.put(username, player);

	}

	public Role getPlayer(String username) {
		return this.players.get(username);
	}

	public void removePlayer(String username) {
		Role player = this.getPlayer(username);
		if (pieceGroup.getChildren().contains(player))
			pieceGroup.getChildren().remove(player);
		if (players.containsKey(player))
			players.remove(player);
	}

	public void eaten(String username) {
		Role p = this.getPlayer(username);
		if (p != null)
			p.setVisible(false);
	}

	public void setNumText(int numOfPlayer) {
		this.numText.setText("Max Player: " + numOfPlayer);
	}
	
	public Role getMonster() {
		return this.monster;
	}
	
	public HashMap<String, Role> getPlayers(){
		return this.players;
	}
}
