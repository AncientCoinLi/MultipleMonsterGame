package MonsterGame.Server.Controller;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import MonsterGame.Server.Model.Map;
import MonsterGame.Server.Model.Role;
import MonsterGame.Server.Model.Send;
import MonsterGame.Server.Model.User;

/*
 * in charge of response to socket
 */
public class ServerEngine {

	// users represents all registered players
	private List<User> users;

	// currUsers --> who are playing the game
	private HashMap<User, Role> currUsers;

	private Map currMap;
	private List<Map> allMaps;
	
	private boolean isRunning;

	private int requirePlayers;
	private ServerController serverController;
	
	public ServerEngine() {
		serverController = new ServerController();
		users = readUsers();
		currUsers = new HashMap<User, Role>();
		allMaps = new ArrayList<Map>();
		currMap = new Map(9);
		isRunning = false;
		requirePlayers = 4;
		readUsers();
	}

	public Map getMap() {
		return currMap;
	}

	public HashMap<User, Role> getCurrUsers() {
		return currUsers;
	}

	public void process(String str, Socket socket) throws Exception {
		String[] order = str.split(":");
		StringBuffer result = new StringBuffer();
		switch (order[0]) {
		case "register":
			result.append("register:");
			User rookie = new User(order[1], order[2]);
			if (register(rookie)) {
				result.append("succeed");
			} else
				result.append("fail");
			
			serverController.reply(result.toString(), socket);
			break;
		case "login":
			result.append("login:");
			User login = getUserByName(order[1]);
			Role role = login(login, order[2]);

			if (role != null) {
				result.append("succeed:");
				result.append(login.getUsername());
				if (role.isHost())
					result.append(":true");
				else
					result.append(":false");

			} else
				result.append("fail");
			serverController.reply(result.toString(), socket);

			if (role != null) {
				refresh();
			}
			break;
		case "move":
			String username = order[1];
			String direction = order[2];
			move(username, direction);
			break;
		case "logout":
			if (order[1].equals("null")) {
				Send.allSocket.remove(order[2]);
			}
			User u = getByUsername(order[1]);
			isRunning = false;
			if (u != null) {
				currMap.removeRole(currUsers.get(u));
				currUsers.remove(u);
			}
			Send.allSocket.remove(order[2]);
			if (currUsers.size() > 0)
				refresh();
			break;
		case "start":
			if (currUsers.size() < requirePlayers) {
				result.append("lack");
				serverController.reply(result.toString(), socket);
				break;
			}
			if (isRunning == false) {
				// reset all avatars positions
				resetPos();
			}
			// modify the running state to true
			isRunning = true;

			new Thread(new Runnable() {
				@Override
				public void run() {
					// make monster start to hunt
					startHunt();
				}
			}).start();
			break;

		case "reset":
			isRunning = false;
			resetPos();
			break;

		case "eat":
			User user = getByUsername(order[1]);
			if (user != null) {
				currMap.removeRole(currUsers.get(user));
				currUsers.get(user).setAliveState(false);
			}
			break;

		case "setnum":
			requirePlayers = Integer.parseInt(order[1]);
			serverController.reply("setnum:" + requirePlayers);
			break;

		case "rank":
			result.append("rank");
			for (int i = 0; i < users.size() && i < 10; i++) {
				result.append(":" + users.get(i).toString());
			}
			serverController.reply(result.toString(), socket);
			break;
		default:
			break;
		}
	}

	private void saveUsers() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("users.dat"));
			sortUsers();
			out.writeObject(users);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private List<User> readUsers() {
		List<User> user = new ArrayList<>();
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("users.dat"));
			user = (List<User>) in.readObject();
			in.close();

			for (User us : user) {
				System.out.println(us.toString());
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (EOFException e) {
			System.out.println("no user stored");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;
	}

	private boolean register(User user) throws Exception {
		for (User us : users) {
			if (user.getUsername().equals(us.getUsername()))
				return false;
		}
		users.add(user);
		saveUsers();
		return true;
	}

	private User getUserByName(String username) {
		for (User u : users) {
			if (u.getUsername().equals(username)) {
				return u;
			}
		}
		return null;
	}

	private Role login(User user, String password) {
		if (currUsers.containsKey(user)) {
			return null;
		}
		if (user == null)
			return null;
		if (user.verify(password)) {
			Role role = null;
			int num = currMap.getRoles().size();
			switch (num) {
			case 0:
				role = new Role(1, currMap);
				break;
			case 1:
				role = new Role(2, currMap);
				break;
			case 2:
				role = new Role(3, currMap);
				break;
			case 3:
				role = new Role(4, currMap);
				break;
			default:
				break;
			}
			// generate an avatar of this user
			currUsers.put(user, role);
			currMap.addRole(role);
			return role;
		} else
			return null;
	}

	// it is to look for user from currUsers
	private User getByUsername(String username) {
		for (User u : currUsers.keySet()) {
			if (u.getUsername().equals(username))
				return u;
		}
		return null;
	}

	private void startHunt() {
		String direction = "";
		try {
			do {
				TimeUnit.MILLISECONDS.sleep(600);
				StringBuffer result = new StringBuffer();
				result.append("monster:");
				direction = currMap.getMonsterNextMove();
				if (currMap.getRoles().size() == 1 && isRunning == true) {
					isRunning = false;
					win();
					break;
				}
				if (isRunning == false)
					break;

				result.append(direction);
				currMap.monsterMove(direction);
//				synchronized (Unprocessed.send) {
//					Unprocessed.send.notifyAll();
//					Unprocessed.addSend(result.toString());
//					Unprocessed.send.wait();
//				}
				serverController.reply(result.toString());


				direction = currMap.getMonsterNextMove();
				if (direction == null) {
					result.setLength(0);
					result.append("eat:");
					// find the eaten role
					int x, y;
					x = currMap.getMonX();
					y = currMap.getMonY();
					Role r;
					User toBeEaten = null;
					for (User u : currUsers.keySet()) {
						r = currUsers.get(u);
						if (r.getX() == x && r.getY() == y) {
							toBeEaten = u;
							result.append(u.getUsername());
							break;
						}
					}
					currMap.removeRole(currUsers.get(toBeEaten));
					currUsers.get(toBeEaten).setAliveState(false);
					;
//					synchronized (Unprocessed.send) {
//						Unprocessed.send.notifyAll();
//						Unprocessed.addSend(result.toString());
//					}
					serverController.reply(result.toString());

					// time for digest
					TimeUnit.MILLISECONDS.sleep(2000);
				}

			} while (true);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void move(String username, String direction) {
		// find the user
		User u = getByUsername(username);
		Role r = currUsers.get(u);

		if (currMap.roleMove(r, direction)) {
			StringBuffer result = new StringBuffer();
			result.append("move:");
			result.append(username + ":");
			result.append(direction);
			serverController.reply(result.toString());
		}

	}

	private void resetPos() {
		for (User u : currUsers.keySet()) {
			currUsers.get(u).resetPos(currMap);
		}
		currMap.resetMap(this.currUsers);
		serverController.reply("reset");

		for (User u : currUsers.keySet()) {
			currUsers.get(u).setAliveState(true);
			StringBuffer re;
			re = new StringBuffer();
			Role r = currUsers.get(u);
			re.append("role:");
			re.append(u.getUsername() + ":");
			re.append(r.getX() + ":");
			re.append(r.getY() + ":");
			re.append(r.getSitId());
			serverController.reply(re.toString());
		}
	}

	private void refresh() {
		currMap.resetMap(this.currUsers);
		serverController.reply("reset");
		StringBuffer re = new StringBuffer();
		for (User u : currUsers.keySet()) {
			currUsers.get(u).setAliveState(true);
			Role r = currUsers.get(u);
			re.append("role:");
			re.append(u.getUsername() + ":");
			re.append(r.getX() + ":");
			re.append(r.getY() + ":");
			re.append(r.getSitId());
			serverController.reply(re.toString());
			re.setLength(0);
		}

	}

	private void win() {
		StringBuffer info = new StringBuffer();
		for (User u : currUsers.keySet()) {
			if (currUsers.get(u).isAlive()) {
				u.win();
				info = new StringBuffer();
				info.append("win:");
				info.append(u.getUsername());
			} else
				u.lose();
		}
		saveUsers();
		serverController.reply(info.toString());

	}

	private void sortUsers() {
		int currIndex = 0;
		for (int i = 0; i < users.size() - 1; i++) {
			currIndex = i;
			for (int j = i + 1; j < users.size(); j++) {
				if (users.get(j).compareTo(users.get(currIndex)) > 0)
					currIndex = j;
			}
			User tmp = users.get(i);
			users.set(i, users.get(currIndex));
			users.set(currIndex, tmp);
		}
	}

}
