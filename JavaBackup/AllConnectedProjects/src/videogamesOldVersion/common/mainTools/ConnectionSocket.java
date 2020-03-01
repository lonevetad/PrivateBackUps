package common.mainTools;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionSocket /* extends Observable */ {

	public static final int CONNECTION_OPEN = 1, CONNECTION_CLOSED = 0, CONNECTION_CLOSING = 2;

	public ConnectionSocket(Socket sock, boolean serverSide) throws IOException {
		this(sock, serverSide, false);
	}

	public ConnectionSocket(Socket sock, boolean serverSide, boolean blockingRead) throws IOException {
		super();
		this.mySocket = sock;
		this.serverSide = serverSide;
		statusConnection = CONNECTION_CLOSED;
		open();
	}

	protected final boolean serverSide;
	protected boolean blockingRead;
	protected volatile int statusConnection;
	protected Socket mySocket;
	protected ObjectInputStream sockIn;
	protected ObjectOutputStream sockOut;

	//

	// TODO STATIC

	public static ConnectionSocket connectToServer(InetAddress address, int port) throws IOException {
		return connectToServer(address, port, false);
	}

	public static ConnectionSocket connectToServer(InetAddress address, int port, boolean blockingRead)
			throws IOException {
		Socket clientsock;
		if (address == null) return null;
		clientsock = new Socket(address, port);
		return new ConnectionSocket(clientsock, false, blockingRead);
	}

	public static ConnectionSocket connectToServer(String address, int port) throws IOException {
		return connectToServer(address, port, false);
	}

	public static ConnectionSocket connectToServer(String address, int port, boolean blockingRead) throws IOException {
		Socket clientsock;
		if (address == null) return null;
		clientsock = new Socket(address, port);
		return new ConnectionSocket(clientsock, false, blockingRead);
	}

	public static ConnectionSocket acceptConnectionRequest(ServerSocket serverSock) throws IOException {
		return acceptConnectionRequest(serverSock, false);
	}

	public static ConnectionSocket acceptConnectionRequest(ServerSocket serverSock, boolean blockingRead)
			throws IOException {
		Socket clientsock;
		if (serverSock == null) return null;
		clientsock = serverSock.accept();
		return new ConnectionSocket(clientsock, true);
	}

	//

	// TODO PUBLIC

	public boolean isServerSide() {
		return serverSide;
	}

	public boolean isBlockingRead() {
		return blockingRead;
	}

	public int getStatusConnection() {
		return statusConnection;
	}

	//

	public ConnectionSocket setBlockingRead(boolean blockingRead) {
		this.blockingRead = blockingRead;
		return this;
	}

	//

	public synchronized boolean isClosed() {
		return statusConnection == CONNECTION_CLOSED;
	}

	public Object receive() throws ClassNotFoundException, IOException {
		Object o;
		o = null;
		if (statusConnection != CONNECTION_OPEN) return null;
		synchronized (mySocket) {
			if (blockingRead || sockIn.available() > 0) {
				o = sockIn.readObject();
			}
		}
		return o;
	}

	public boolean send(Object o) throws IOException {
		if (o == null || statusConnection != CONNECTION_OPEN) return false;
		synchronized (mySocket) {
			sockOut.writeObject(o);
			sockOut.flush();
		}
		return true;
	}

	//

	// TODO NON-PUBLIC

	protected synchronized void setClosed() {
		statusConnection = CONNECTION_CLOSED;
	}

	protected synchronized void setClosing(boolean b) {
		statusConnection = b ? CONNECTION_CLOSING : CONNECTION_OPEN;
	}

	protected void open() throws IOException {
		if (serverSide) {
			sockIn = new ObjectInputStream(mySocket.getInputStream());
			sockOut = new ObjectOutputStream(mySocket.getOutputStream());
		} else {// clientside
			sockOut = new ObjectOutputStream(mySocket.getOutputStream());
			sockIn = new ObjectInputStream(mySocket.getInputStream());
		}
	}

	protected synchronized void close() {
		int prev;
		prev = statusConnection;
		statusConnection = CONNECTION_CLOSING;
		try {
			if (serverSide) {
				sockOut.close();
				sockIn.close();
			} else {
				sockIn.close();
				sockOut.close();
			}
			statusConnection = CONNECTION_CLOSED;
		} catch (IOException ex) {
			Logger.getLogger(ConnectionSocket.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				mySocket.close();
				mySocket = null;
				sockIn = null;
				sockOut = null;
			} catch (IOException ex) {
				statusConnection = prev;
				Logger.getLogger(ConnectionSocket.class.getName()).log(Level.SEVERE, null, ex);
			}
			// setClosed();
		}
	}
}
