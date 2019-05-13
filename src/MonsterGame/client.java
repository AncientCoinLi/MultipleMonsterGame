package MonsterGame;

import MonsterGame.Client.Controller.ClientEngine;
import MonsterGame.Client.Model.Comm;
import javafx.application.Application;
import javafx.stage.Stage;

public class client extends Application {
	public static final int WIDTH = 9;
	public ClientEngine clientEngine;

	@Override
	public void start(Stage primaryStage) throws Exception {
		clientEngine = new ClientEngine(primaryStage);
		Comm.clientEngine = clientEngine;
		clientEngine.start();
	}

	public static void main(String args[]) {
		launch(args);
	}

}
