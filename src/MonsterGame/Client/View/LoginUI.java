package MonsterGame.Client.View;

import MonsterGame.Client.Controller.ClientEngine;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LoginUI extends BorderPane {

	public LoginUI(ClientEngine engine) {
		Text title = new Text();
		title.setText("- Log in to Multiple Monster Game - ");
		title.setFont(Font.font("Courier", FontWeight.BOLD, 24));

		this.setPrefSize(14 * 50, 9 * 50);
		this.setTop(title);
		BorderPane.setAlignment(title, Pos.CENTER);

		VBox vb = new VBox();
		HBox hb1 = new HBox();
		HBox hb2 = new HBox();

		GridPane grid = new GridPane();
		this.setCenter(grid);
		BorderPane.setAlignment(grid, Pos.CENTER);

		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(9, 15, 9, 15));
		grid.setHgap(5.5);
		grid.setVgap(5.5);
		grid.add(vb, 0, 0);

		hb1.setAlignment(Pos.BOTTOM_CENTER);
		hb1.getChildren().add(new Label("Username:	"));
		TextField username = new TextField();
		hb1.getChildren().add(username);

		hb2.getChildren().add(new Label("Password:	"));
		TextField password = new PasswordField();
		hb2.getChildren().add(password);

		vb.setSpacing(10);
		vb.setAlignment(Pos.CENTER);
		vb.getChildren().add(hb1);
		vb.getChildren().add(hb2);

		Button login = new Button("Login");
		login.setPrefSize(100, 50);

		vb.getChildren().add(login);

		Button back = new Button("Back");
		back.setPrefSize(100, 50);
		vb.getChildren().add(back);

		Button quit = new Button("Quit");
		quit.setPrefSize(100, 50);
		vb.getChildren().add(quit);

		login.setOnAction(e -> {
			engine.getClientController().loginRequest(username.getText(), password.getText());
		});

		back.setOnAction(e -> {
			engine.getClientController().skipToMainUI(engine);
		});

		quit.setOnMousePressed(e -> {
			System.exit(0);
		});
	}

}
