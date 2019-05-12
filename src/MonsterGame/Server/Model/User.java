package MonsterGame.Server.Model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable, Comparable<User>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8944538062288803227L;
	private String username;
	private String password;
	
	private int numGame;
	private int numWin;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.numGame = 0;
		this.numWin = 0;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || o.getClass() != this.getClass()) return false;
		
		User user = (User) o;
		if(user.username.equals(username) && user.password.equals(password)) return true;
		else return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(username, password);
	}
	
	public void win() {
		this.numGame++;
		this.numWin++;
	}
	
	public void lose() {
		this.numGame++;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public boolean verify(String password) {
		if(password.equals(this.password)) {
			return true;
		}else return false;
	}
	
	public double getWinRate() {
		if(this.numGame == 0) return 0.00;
		return (double)this.numWin/this.numGame*100;
	}
	
	public String toString() {
		String rate = String.format("%.2f", this.getWinRate());
		return this.username+"\t"+this.numWin+"\t"+this.numGame+"\t"+rate+"%";
	}

	@Override
	public int compareTo(User u1) {
		if(this.getWinRate() > u1.getWinRate()) return 1;
		else if(this.getWinRate() == u1.getWinRate()) {
			if(this.numWin > u1.numWin) return 1;
			else if(this.numWin == u1.numWin) {
				if(this.numGame > u1.numGame) return 1;
				else if(this.numGame == u1.numGame) return 0;
				else return -1;
			}
			else return 0;
		}
		else return -1;
	}
	
}
