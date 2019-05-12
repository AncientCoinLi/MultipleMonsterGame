package MonsterGame.Client.View;

import MonsterGame.Client.Model.ClientEngine;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class RankUI extends BorderPane {

	String rankInfo;
	
	public RankUI(ClientEngine engine, String rankInfo){
		Text title = new Text();
		this.rankInfo = rankInfo;
		
		title.setText("- Top Ten Players - ");
		title.setFont(Font.font("Courier", FontWeight.BOLD, 24));
		
		this.setPrefSize(14*50, 9*50);
		this.setTop(title);
		BorderPane.setAlignment(title, Pos.CENTER);
		
		Text rank = new Text();
		rank.setText(rankInfo);
		rank.setTextAlignment(TextAlignment.JUSTIFY);
		rank.setStyle("-fx-font-family: monospace");
		rank.setFont(Font.font(20));
		
		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER);
		hb.setPadding(new Insets(50,50,50,50));
		hb.setSpacing(50);
		this.setCenter(rank);
		this.setBottom(hb);
		
		
		Button login = new Button("Login");
		login.setPrefSize(100, 50);
		
		hb.getChildren().add(login);
		
		
		Button back = new Button("Back");
		back.setPrefSize(100, 50);
		hb.getChildren().add(back);

		
		Button quit = new Button("Quit");
		quit.setPrefSize(100, 50);
		hb.getChildren().add(quit);

		login.setOnAction(e->{
			engine.getClientController().skipToLoginUI(engine);
		});
				
		back.setOnAction(e->{
			engine.getClientController().skipToMainUI(engine);
		});
		
		quit.setOnMousePressed(e ->{
			System.exit(0);
		});
	}
}
