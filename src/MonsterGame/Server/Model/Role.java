package MonsterGame.Server.Model;

/*
 * avatars of players and monster
 */
public class Role {

	private int x;
	private int y;
	private int sitId;
	private boolean host;
	
	private boolean isAlive;
	
	public void setAliveState(boolean isAlive) {
		this.isAlive = isAlive;
	}
	
	public int getSitId() {
		return this.sitId;
	}
	
	public boolean isAlive() {
		return this.isAlive;
	}
	
	public boolean isHost() {
		return this.host;
	}
	
	public Role(int x, int y) {
		this.x = x;
		this.y = y;
		this.isAlive = true;
		host = false;
	}
	public Role(int sitId, Map map) {
		this.sitId = sitId;
		switch(sitId) {
		case 1:
			this.x = map.getStartPos();
			this.y = map.getStartPos();
			this.isAlive = true;
			this.host = true;
			break;
		case 2:
			this.x = map.getEndPos();
			this.y = map.getStartPos();
			this.isAlive = true;
			break;
		case 3:
			this.x = map.getStartPos();
			this.y = map.getEndPos();
			this.isAlive = true;
			break;
		case 4:
			this.x = map.getEndPos();
			this.y = map.getEndPos();
			this.isAlive = true;
			break;
			default:
				break;
		}
	}
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	//it should be added moving check
	public void move(String direction) {
		switch(direction) {
		case "up":
			this.y--;
			break;
		case "down":
			this.y++;
			break;
		case "left":
			this.x--;
			break;
		case "right":
			this.x++;
			break;
			default:
				break;
		}
	}
	
	public void resetPos(Map map) {
		switch(this.sitId) {
		case 1:
			this.x = map.getStartPos();
			this.y = map.getStartPos();
			this.isAlive = true;
			break;
		case 2:
			this.x = map.getEndPos();
			this.y = map.getStartPos();
			this.isAlive = true;
			break;
		case 3:
			this.x = map.getStartPos();
			this.y = map.getEndPos();
			this.isAlive = true;
			break;
		case 4:
			this.x = map.getEndPos();
			this.y = map.getEndPos();
			this.isAlive = true;
			break;
			default:
				break;
			
		}
	}
	
}
