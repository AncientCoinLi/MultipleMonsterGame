package MonsterGame.Client.Model;

import java.util.Objects;

import MonsterGame.Client.Controller.ClientController;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;;

public class Role extends StackPane {

	private static final double TILE_SIZE = 50;
	private double oldX, oldY;
	private int x, y;
	private String username;

	public double getOldX() {
		return oldX;
	}

	public double getOldY() {
		return oldY;
	}

	public Role(String username, int x, int y, int sitId) {
		this.username = username;
		this.x = x;
		this.y = y;
		move(x, y);
		String specifyPlayer = "/images/";

		switch(sitId) {
		case 1:
			specifyPlayer = specifyPlayer + "red.png";
			break;
		case 2:
			specifyPlayer = specifyPlayer + "blue.png";
			break;
		case 3:
			specifyPlayer = specifyPlayer + "yellow.png";
			break;
		case 4:
			specifyPlayer = specifyPlayer + "green.png";
			break;
		case 5:
			specifyPlayer = specifyPlayer + "monster.png";
			default:
				break;
		}
		ImageView roleImage = new ImageView(specifyPlayer);
		roleImage.setFitHeight(TILE_SIZE * 0.7);
		roleImage.setFitWidth(TILE_SIZE * 0.7);
		roleImage.setTranslateX(TILE_SIZE * 0.15);
		roleImage.setTranslateY(TILE_SIZE * 0.15);

		getChildren().add(roleImage);
	}

	public void move(int x, int y) {
		this.x = x;
		this.y = y;

		oldX = x * TILE_SIZE;
		oldY = y * TILE_SIZE;
		relocate(oldX, oldY);
	}

	

	public void move(String direction) {
		switch (direction) {
		case "up":
			move(this.x, this.y - 1);
			break;
		case "down":
			move(this.x, this.y + 1);
			break;
		case "left":
			move(this.x - 1, this.y);
			break;
		case "right":
			move(this.x + 1, this.y);
			break;
		default:
			break;
		}
	}

	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;

		Role p = (Role) o;
		return this.username.equals(p.username);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}
}
