package MonsterGame.Server.Model;


import java.util.ArrayList;
import java.util.List;

public class Cell {

	private int x;
	private int y;
	
	private boolean isObstacle;
	private Cell Up;
	private Cell Down;
	private Cell Right;
	private Cell Left;
	private List<Cell> conn;
	
	private int numOfChoice;
	
	
	public Cell(int x, int y) {
		setObstacle(false);
		this.x = x;
		this.y = y;
		Up = null;
		Down = null;
		Right = null;
		Left = null;
		conn = new ArrayList<>();
	}

	public boolean isObstacle() {
		return isObstacle;
	}
	
	public void setObstacle(boolean isObstacle) {
		this.isObstacle = isObstacle;
	}

	public Cell getUp() {
		return Up;
	}

	public void setUp(Cell up) {
		Up = up;
		if(up != null) this.numOfChoice++;
	}

	public Cell getDown() {
		return Down;
	}

	public void setDown(Cell down) {
		Down = down;
		if(down != null) this.numOfChoice++;
	}

	public Cell getRight() {
		return Right;
	}

	public void setRight(Cell right) {
		Right = right;
		if(right != null) this.numOfChoice++;
	}

	public Cell getLeft() {
		return Left;
	}

	public void setLeft(Cell left) {
		Left = left;
		if(left != null) this.numOfChoice++;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public List<Cell> getConn(){
		return  this.conn;
	}
	
	
	public int getNumOfChoice() {
		return this.numOfChoice;
	}
	
	public void setNumOfChoice(int numOfChoice) {
		this.numOfChoice = numOfChoice;
	}
}
