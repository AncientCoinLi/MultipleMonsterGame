package MonsterGame.Client.View;

import MonsterGame.Client.Controller.ClientController;
import MonsterGame.Client.Model.ClientEngine;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainUI extends BorderPane{
	
	public MainUI(ClientEngine engine) {
		Text title = new Text();
		title.setText("- Welcome to Multiple Monster Game - ");
		title.setFont(Font.font("Courier", FontWeight.BOLD, 24));
		
		this.setPrefSize(14*50, 9*50);
		this.setTop(title);
		BorderPane.setAlignment(title, Pos.CENTER);
		
		VBox vb = new VBox();
		GridPane grid = new GridPane();
		this.setCenter(grid);
		BorderPane.setAlignment(grid, Pos.CENTER);
			
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(9, 15, 9, 15));
		grid.setHgap(5.5);
		grid.setVgap(5.5);
		grid.add(vb, 0, 0);
		
		vb.setSpacing(20);
		Button login = new Button("Login");
		login.setPrefSize(100, 50);
		vb.getChildren().add(login);
		
		Button register = new Button("Register");
		register.setPrefSize(100, 50);
		vb.getChildren().add(register);

		Button rank = new Button("Top 10");
		rank.setPrefSize(100, 50);
		vb.getChildren().add(rank);
		
		Button quit = new Button("Quit");
		quit.setPrefSize(100, 50);
		vb.getChildren().add(quit);
		

		quit.setOnAction(e->{
			System.exit(0);
		});
		
		login.setOnAction(e->{
			engine.getClientController().skipToLoginUI(engine);
		});
		
		register.setOnAction(e->{
			engine.getClientController().skipToRegisterUI(engine);
		});
		
		rank.setOnAction(e ->{
			engine.getClientController().rankRequest();
		});
	}

}
