package MonsterGame.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Map is composed by Cells
 * it stores the size and the positions of obstacles
 * and will also implements the number of players and monsters and their initial positions
 * 
 * Map should be initialized by an 2d integer or a Board
 */

public class Map {

	private int mapSize;// as far as for now, map is supposed to be a square
	private Cell[][] board;
	private int numOfPlayers = 4;
	private List<Role> roles;
	private Role monster;
	private HashMap<Role, List<Cell>> monMoveMap;

	public int getMonX() {
		return monster.getX();
	}

	public int getMonY() {
		return monster.getY();
	}

	public int getMidPos() {
		return mapSize / 2;
	}

	public int getStartPos() {
		return 0;
	}

	public int getEndPos() {
		return mapSize - 1;
	}

	public List<Role> getRoles() {
		return this.roles;
	}

	public int getNumOfAlive() {
		return this.roles.size();
	}

	// construct a map with a 2d array
	public Map(int[][] initData) {

	}

	/*
	 * construct a map with its size obstacles are set up as basic mode for instance
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * only edges can be get through
	 */
	public Map(int size) {
		roles = new ArrayList<Role>(5);
		this.monMoveMap = new HashMap<Role, List<Cell>>();
		this.mapSize = size;
		board = new Cell[size][size];
		this.initMap();
	}

	private void initMap(int[][] data) {

	}

	private void initMap() {
		this.roles.clear();
		this.monMoveMap.clear();
		// instance Cell in board
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				board[i][j] = new Cell(i, j);
			}
		}

		// set edge and their adj
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize - 1; j++) {
				board[i][j].setDown(board[i][j + 1]);
				board[j][i].setRight(board[j + 1][i]);
				board[i][mapSize - 1 - j].setUp(board[i][mapSize - 2 - j]);
				board[mapSize - 1 - j][i].setLeft(board[mapSize - 2 - j][i]);
			}
		}

		int edge1 = 0;
		int midPos = mapSize / 2;
		int edge2 = mapSize - 1;

		// define obstacles in the map
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				if (i == edge1 || i == midPos || i == edge2 || j == edge1 || j == midPos || j == edge2)
					continue;
				board[i][j].setObstacle(true);
			}
		}

		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				if (board[i][j].getUp() != null && !board[i][j].getUp().isObstacle())
					board[i][j].getConn().add(board[i][j].getUp());
				if (board[i][j].getDown() != null && !board[i][j].getDown().isObstacle())
					board[i][j].getConn().add(board[i][j].getDown());
				if (board[i][j].getLeft() != null && !board[i][j].getLeft().isObstacle())
					board[i][j].getConn().add(board[i][j].getLeft());
				if (board[i][j].getRight() != null && !board[i][j].getRight().isObstacle())
					board[i][j].getConn().add(board[i][j].getRight());
			}
		}

		// add a monster
		this.monster = new Role(midPos, midPos);
		this.getByRole(monster).setObstacle(true);
		// this.addRole(monster);
	}

	/*
	 * if x, y are valid then return relevant Cell in map else return null
	 */
	public Cell getByPos(int x, int y) {
		if (x < this.mapSize && x >= 0 && y < this.mapSize && y >= 0)
			return this.board[x][y];
		return null;
	}

	public Cell getByRole(Role r) {
		// if(!roles.contains(r)) return null;
		int x, y;
		x = r.getX();
		y = r.getY();
		return this.getByPos(x, y);
	}

	public void addRole(Role role) {
		if (role == null)
			return;
		this.roles.add(role);
		this.getByPos(role.getX(), role.getY()).setObstacle(true);
	}

	public void removeRole(Role role) {
		this.roles.remove(role);
		this.monMoveMap.remove(role);
	}

	public void resetMap(HashMap<User, Role> users) {
		this.initMap();
		for (User u : users.keySet()) {
			this.addRole(users.get(u));
		}
	}

	public boolean roleMove(Role role, String direction) {
		if (!canMove(role, direction))
			return false;

		this.getByPos(role.getX(), role.getY()).setObstacle(false);
		role.move(direction);
		this.getByPos(role.getX(), role.getY()).setObstacle(true);

		return true;
	}

	private boolean canMove(Role role, String direction) {
		if (role == null)
			return false;
		Cell c = getByPos(role.getX(), role.getY());
		switch (direction) {
		case "up":
			if (c.getUp() == null)
				return false;
			if (c.getUp().isObstacle())
				return false;
			break;
		case "down":
			if (c.getDown() == null)
				return false;
			if (c.getDown().isObstacle())
				return false;
			break;
		case "left":
			if (c.getLeft() == null)
				return false;
			if (c.getLeft().isObstacle())
				return false;
			break;
		case "right":
			if (c.getRight() == null)
				return false;
			if (c.getRight().isObstacle())
				return false;
			break;
		default:
			break;
		}
		return true;
	}

	public void monsterMove(String direction) {
		this.getByPos(monster.getX(), monster.getY()).setObstacle(false);
		monster.move(direction);
		this.getByPos(monster.getX(), monster.getY()).setObstacle(true);
	}

	public List<Cell> getShortestPath(Cell start, Cell target) {
		HashMap<Cell, ArrayList<Cell>> djst = new HashMap<>();
		djst = Dijkstra(start);
		ArrayList<Cell> track = djst.get(target);
		return track;
	}

	public HashMap<Cell, ArrayList<Cell>> Dijkstra(Cell start) {
		// initialize djst
		HashMap<Cell, ArrayList<Cell>> djst = new HashMap<>();
		for (int i = 0; i < this.mapSize; i++) {
			for (int j = 0; j < this.mapSize; j++) {
				if (!this.board[i][j].isObstacle())
					djst.put(this.board[i][j], new ArrayList<>());
			}
		}

		HashMap<Cell, ArrayList<Cell>> result = new HashMap<>();
		while (!djst.isEmpty()) {
			for (Cell cl : start.getConn()) {
				if (djst.containsKey(cl)) {
					if (djst.get(cl).size() == 0) {
						djst.get(cl).addAll(djst.get(start));
						djst.get(cl).add(start);
					}
				}
			}
			djst.get(start).add(start);
			result.put(start, djst.get(start));
			djst.remove(start);
			start = shortest(djst);
		}
		return result;
	}

	private Cell shortest(HashMap<Cell, ArrayList<Cell>> djst) {
		Cell curr = null;
		int min = Integer.MAX_VALUE;
		for (Cell cl : djst.keySet()) {
			if (djst.get(cl).size() != 0 && djst.get(cl).size() < min) {
				min = djst.get(cl).size();
				curr = cl;
			}
		}
		if (curr == null) {
			if (djst.keySet().iterator().hasNext())
				curr = djst.keySet().iterator().next();
		}
		return curr;
	}

	public String getMonsterNextMove() {
		Cell start = this.getByPos(monster.getX(), monster.getY());
		Cell target = null;

		start.setObstacle(false);
		for (Role r : roles) {
			target = this.getByPos(r.getX(), r.getY());
			target.setObstacle(false);
		}

		HashMap<Cell, ArrayList<Cell>> dj = this.Dijkstra(start);

		// update the shortest path info
		for (Role r : roles) {
			target = this.getByPos(r.getX(), r.getY());
			target.setObstacle(true);

			this.monMoveMap.put(r, dj.get(target));
		}
		start.setObstacle(true);

		// get the shortest role
		int length = Integer.MAX_VALUE;
		Role tar = null;
		int steps;
		for (Role r : this.monMoveMap.keySet()) {
			steps = this.monMoveMap.get(r).size();
			if (steps < length) {
				length = steps;
				tar = r;
			}
		}

		// get the path of target
		List<Cell> shortest = this.monMoveMap.get(tar);
		if (this.monMoveMap.isEmpty())
			return null;
		if (shortest.size() < 2)
			return null;
		return this.getDirection(shortest.get(0), shortest.get(1));
	}

	// just a support method
	private String getDirection(Cell start, Cell target) {

		switch (target.getX() - start.getX()) {
		case 1:
			return "right";
		case 0:
			switch (target.getY() - start.getY()) {
			case 1:
				return "down";
			case -1:
				return "up";
			default:
				break;
			}
		case -1:
			return "left";
		default:
			break;
		}
		return null;
	}

}
