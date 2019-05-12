package MonsterGame.Client.Model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Cell extends Rectangle {

    private Role role;

    public boolean hasPiece() {
        return role != null;
    }

    public Role getPiece() {
        return role;
    }

    public void setPiece(Role role) {
        this.role = role;
    }
    
    public static final int TILE_SIZE = 50;
    public static final int WIDTH = 9;
    public static final int HEIGHT = 9;

    public Cell(boolean light, int x, int y) {
    	this.role = null;
        setWidth(TILE_SIZE);
        setHeight(TILE_SIZE);
        relocate(x * TILE_SIZE, y * TILE_SIZE);
        setFill(light ? Color.valueOf("#feb") : Color.valueOf("#582"));  
        setStroke(Color.BLACK);
    }
}
