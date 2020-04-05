package appunti.testRoomJoiner;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import appunti.testRoomJoiner.messages.MessageTRJ_Autentication;
import appunti.testRoomJoiner.messages.MessageTRJ_Command;
import appunti.testRoomJoiner.messages.MessageTRJ_ConnectionStatus;
import importedUtilities.abstractCommon.referenceHolderAC.LoggerMessagesHolder;
import importedUtilities.mainTools.LoggerMessages;
import importedUtilities.mainTools.MyLinkedList;
import importedUtilities.mainTools.RedBlackTree;
import importedUtilities.utilities.Comparators;

public class ServerTRJ implements LoggerMessagesHolder {
	private static final long serialVersionUID = 6250455555078206L;

	public static enum ConnectionStatus implements Comparator<ConnectionStatus> {
		UNDEFINED, PENDING, ACCEPTED, REFUSED_NAME_YET_USED, ABORTED, LOST, CLOSING, CLOSED;
		//
		public final byte id;

		ConnectionStatus() {
			id = SF.x++;
		}

		@Override
		public int compare(ConnectionStatus o1, ConnectionStatus o2) {
			if (o1 == o2) return 0;
			if (o2 == null) return 1;
			if (o1 == null) return -1;
			// if (o1.id == o2.id) return 0;
			return o1.id > o2.id ? 1 : -1;
		}

		public static ConnectionStatus idToConnectionStatus(int id) {
			return valuesConnectionStatus[id];
		}

		static final class SF {
			static byte x = 0;
		}
	}

	public static ConnectionStatus[] valuesConnectionStatus = ConnectionStatus.values();
	public static final int DEFAULT_SERVER_PORT = 13545;

	public ServerTRJ() {
		neverInit = alive = true;
		suspendModeWait = false;
		log = LoggerMessages.LOGGER_DEFAULT;
	}

	boolean alive, neverInit, suspendModeWait;
	ServerSocket ss;
	RedBlackTree<String, Player> connectedPlayers;
	MyLinkedList<ConnectionTRJ> pendingToSelfAutenticate;
	ExecutorService threads;
	RunnAccepter runnAccepter;
	RunnAutenticater runnAutenticater;
	RunnServerServicesPerformer runnServerServicesPerformer;
	RunnServerNonautenticatingMessageReader runnMsgReader;
	MyLinkedList<MessageTestRoomJoiner> messagesUnknownType;
	MyLinkedList<MessageTRJ_Command> messagesToBePerformed;
	// BufferLinkedRecyclingNodes<MessageTestRoomJoiner>
	RedBlackTree.DoSomethingWithKeyValue<String, Player> messageReader;
	//
	ServerTRJ_GUI sGUI;
	LoggerMessages log;

	//

	// TODO GETTER

	public boolean isAlive() {
		return alive;
	}

	public boolean isNeverInit() {
		return neverInit;
	}

	@Override
	public LoggerMessages getLog() {
		return log;
	}

	public String getIP() {
		return ss.getInetAddress().getHostAddress();
	}

	public int getPort() {
		return ss.getLocalPort();
	}

	//

	// TODO SETTER

	@Override
	public ServerTRJ setLog(LoggerMessages log) {
		this.log = LoggerMessages.loggerOrDefault(log);
		return this;
	}

	//

	// TODO OTHER

	//

	// TODO STATIC

	//

	// TODO PUBLIC

	public void init() {
		if (neverInit) {
			neverInit = false;
			initGUI();
			initNonGui();
		}
	}

	public void disconnect(String username) {
		Player p;

		log("Disconnecting: " + username);
		if (username != null) {
			p = connectedPlayers.fetch(username);
			if (p != null) {
				this.sendStatusMessageTo(p, ConnectionStatus.CLOSING);
				try {
					p.getConnection().close();
				} catch (IOException e) {
					log(e);
				}
				// sendMessage(new MessageTRJ_ConnectionStatus(p,
				// ConnectionStatus.CLOSING));
				this.connectedPlayers.delete(username);
			}
		}
	}
	//

	// TODO NON-PUBLIC

	void initGUI() {
		sGUI = new ServerTRJ_GUI(this);
		sGUI.init();
		// this.log = sGUI.getLog();
	}

	void initNonGui() {
		connectedPlayers = new RedBlackTree<>(
				Comparators.STRING_COMPARATOR/* STRING COMPARER */);
		pendingToSelfAutenticate = new MyLinkedList<>();
		messageReader = this::readPlayerMessage;
		messagesToBePerformed = new MyLinkedList<>();
		messagesUnknownType = new MyLinkedList<>();
		try {
			ss = new ServerSocket(0);
			sGUI.showIPAddress(getIP());
			sGUI.showPort(getPort());
		} catch (IOException e) {
			e.printStackTrace();
			log(e);
			System.exit(-1);
		}
		initThreads();
	}

	void initThreads() {
		runnAccepter = new RunnAccepter(this);
		runnAutenticater = new RunnAutenticater(this);
		runnMsgReader = new RunnServerNonautenticatingMessageReader(this);
		runnServerServicesPerformer = new RunnServerServicesPerformer(this);
		threads = Executors.newFixedThreadPool(4);
		threads.execute(runnAccepter);
		threads.execute(runnAutenticater);
		threads.execute(runnMsgReader);
		threads.execute(runnServerServicesPerformer);
	}

	// key methods

	protected boolean tryAutentication(ConnectionTRJ conn) {
		String username;
		MessageTRJ_Autentication ma;
		MessageTestRoomJoiner mess;

		mess = conn.readMessage();
		if (mess != null && mess instanceof MessageTRJ_Autentication) {
			ma = (MessageTRJ_Autentication) mess;
			username = ma.getUsername();

			// check validity
			if (username != null && (!connectedPlayers.hasKey(username))) {
				acceptConnection(conn, ma);
				return true;
			}
		}
		return false;
	}

	protected void acceptConnection(ConnectionTRJ c, MessageTRJ_Autentication ma) {
		String name;
		MessageTRJ_ConnectionStatus msgStatus;
		connectedPlayers.add(name = ma.getUsername(), new Player(c).setName(name));
		runnServerServicesPerformer.resumeThisRunnable();
		runnMsgReader.resumeThisRunnable();
		log("ACCEPTED CONNECTION OF " + name);
		// acknowledge
		msgStatus = new MessageTRJ_ConnectionStatus(null, ConnectionStatus.ACCEPTED);
		c.sendMessage(msgStatus);
	}

	protected void acceptNewSocket(Socket s) {
		ConnectionTRJ con;
		pendingToSelfAutenticate.add(con = new ConnectionTRJ(s));
		con.setLog(log);
		advertSocketConnectionAccepted(con);
		runnAutenticater.resumeThisRunnable();
		log("New socket accepted");
	}

	// secondary methods

	protected void sendStatusMessageTo(ConnectionTRJ conn, ConnectionStatus status) {
		sendStatusMessageTo(new Player(conn), status);
	}

	protected void sendStatusMessageTo(Player p, ConnectionStatus status) {
		MessageTRJ_ConnectionStatus m;
		String error;
		if (p != null && status != null) {
			try {
				m = new MessageTRJ_ConnectionStatus(null, status);
				m.setConnectionStatus(status);
				m.setGeneratedByServer(true);
				error = p.getConnection().sendMessage(m);
				log(error);
			} catch (Exception e) {
				log(e);
			}
		}
	}

	protected void advertSocketConnectionAccepted(ConnectionTRJ conn) {
		sendStatusMessageTo(conn, ConnectionStatus.ACCEPTED);
	}

	protected void readPlayerMessages() {
		connectedPlayers.forEach(messageReader);
	}

	public void readPlayerMessage(String username, Player p) {
		ConnectionTRJ con;
		MessageTestRoomJoiner msg;
		con = p.getConnection();
		if (con != null) {
			msg = con.readMessage();
			if (msg != null) {
				// gotcha !
				log("MESSAGE READ for " + username);
				msg.setSender(p);

				if (msg instanceof MessageTRJ_Command)
					messagesToBePerformed.add((MessageTRJ_Command) msg);
				else
					messagesUnknownType.add(msg);
				// awake sleeping thread
				runnServerServicesPerformer.resumeThisRunnable();
			}
		}
	}

	protected void performServices() {
		MessageTestRoomJoiner msg;
		MessageTRJ_Command command;
		while (!messagesUnknownType.isEmpty()) {
			msg = messagesUnknownType.removeFirst();
			if (msg != null) {
				log("Undefined message received:");
				log(msg.toString());
				log("");
			}
		}
		while (!messagesToBePerformed.isEmpty()) {
			command = messagesToBePerformed.removeFirst();
			if (command != null) {
				command.setEnvironment(this);
				command.run();
			}
		}
	}

	// little utilities

	public void readPlayerMessage(Player p) {
		readPlayerMessage(p.getName(), p);
	}

	protected void log(Exception e) {
		log(e.toString());
		e.printStackTrace();
	}

	protected void log(String e) {
		if (e != null) log.log("++SERVER++: " + e);
	}

	protected void suspendRunnableTask(RunnableTask rt) {
		if (suspendModeWait)
			rt.suspendMyExecution();
		else
			try {
				Thread.sleep(125);
			} catch (InterruptedException e) {
				log(e);
			}
	}

	//

	// TODO CLASS

	public static abstract class RunnableServer extends RunnableTask {
		private static final long serialVersionUID = 895409865L;
		ServerTRJ server;

		RunnableServer(ServerTRJ s) {
			super(s);
			this.server = s;
		}

		@Override
		public boolean canContinueCycle() {
			return server.alive;
		}
	}

	public static class RunnAccepter extends RunnableServer {
		private static final long serialVersionUID = 8940600660L;

		RunnAccepter(ServerTRJ s) {
			super(s);
		}

		@Override
		public void doEachCycle() {
			Socket s;
			try {
				s = server.ss.accept();
				server.acceptNewSocket(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static class RunnAutenticater extends RunnableServer {
		private static final long serialVersionUID = -96204777528203L;

		RunnAutenticater(ServerTRJ s) {
			super(s);
			// playerAutenticated = new LinkedList<>();
			notAutenticated = new MyLinkedList<>();
			// autenticator = this::tryAutentication;
		}

		// List<Player> playerAutenticated;
		MyLinkedList<ConnectionTRJ> notAutenticated;
		// Consumer<ConnectionTRJ> autenticator;

		@Override
		public void doEachCycle() {
			MyLinkedList<ConnectionTRJ> ptsa;
			MyLinkedList.NodeList<ConnectionTRJ> head;
			ptsa = server.pendingToSelfAutenticate;

			// do not waste time
			if (ptsa.isEmpty() && notAutenticated.isEmpty()) server.suspendRunnableTask(this);

			notAutenticated.clear();
			// .forEach(autenticator);
			while (!ptsa.isEmpty()) {
				head = ptsa.getHead();
				// try to autenticate the connection and, if not, re-put to the
				// pending
				if (!server.tryAutentication(ptsa.removeFirst())) {
					notAutenticated.addNodeAtLast(head);
				}
			}
			// put again the non-autenticated connections to the pending
			while (!notAutenticated.isEmpty()) {
				head = notAutenticated.getHead();
				notAutenticated.removeFirst();
				ptsa.addNodeAtLast(head);
			}

		}
	}

	public static class RunnServerNonautenticatingMessageReader extends RunnableServer {
		private static final long serialVersionUID = 4980030218779L;

		RunnServerNonautenticatingMessageReader(ServerTRJ s) {
			super(s);
		}

		@Override
		public void doEachCycle() {
			// System.out.println("connected player: " +
			// server.connectedPlayers.size());
			if (server.connectedPlayers.isEmpty()) server.suspendRunnableTask(this);
			// server.log(".connected player: " +
			// server.connectedPlayers.size());
			server.readPlayerMessages();

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				server.log(e);
			}
		}
	}

	public static class RunnServerServicesPerformer extends RunnableServer {
		private static final long serialVersionUID = -30320801871144L;

		RunnServerServicesPerformer(ServerTRJ s) {
			super(s);
		}

		@Override
		public void doEachCycle() {
			if (server.messagesToBePerformed.isEmpty() && server.messagesUnknownType.isEmpty())
				server.suspendRunnableTask(this);
			server.performServices();
		}
	}

	//

	// TODO MAIN

	public static void main(String[] args) {
		ServerTRJ s;

		System.out.println("START");
		s = new ServerTRJ();
		s.init();
		System.out.println("END");
	}

}