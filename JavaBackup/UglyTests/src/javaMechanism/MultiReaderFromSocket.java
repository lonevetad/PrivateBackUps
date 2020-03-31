package javaMechanism;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Si aprono due reader, un BufferedReader per leggere String e un
 * ObjecInputStream per leggere oggetti, tutti dallo stesso InputStream, per
 * vedere se possono coesistere due istanze di lettori diversi sullo stesso
 * inputstream.
 */
public class MultiReaderFromSocket {
	static final int PORT = 8082, BACKLOG = 10;
	static final String HOST = "localhost";

	public MultiReaderFromSocket() {
	}

	ServerSocket server;
	Socket socketServer, socketClient;

	void startTest() {
		try {
			server = new ServerSocket(PORT, BACKLOG, InetAddress.getLocalHost());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	//

	//

	static class StringReaderWriter {
		StringReaderWriter(Socket socket, boolean serverSide) {
			this.socket = socket;
			neverOpened = true;
			this.serverSide = serverSide;
			open();
		}

		protected boolean neverOpened, serverSide;
		Socket socket;
		BufferedReader in;
		BufferedWriter out;

		void open() {
			if (neverOpened) {
				if (serverSide) {
					try {
						in = new BufferedReader(//
								new InputStreamReader(socket.getInputStream()));
						out = new BufferedWriter(//
								new PrintWriter(socket.getOutputStream()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					try {
						out = new BufferedWriter(//
								new PrintWriter(socket.getOutputStream()));
						in = new BufferedReader(//
								new InputStreamReader(socket.getInputStream()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				neverOpened = false;
			}
		}

		void close() {
			if (!neverOpened) {
				if (serverSide) {
					try {
						out.close();
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					try {
						in.close();
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				in = null;
				out = null;
				neverOpened = true;
			}
		}

		public void send(String s) {
			if (!neverOpened) {
				try {
					out.write(s);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public String read() {
			if (!neverOpened) {
				try {
					return in.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}

	//

	//

	public static void main(String[] args) {
		MultiReaderFromSocket m;
		System.out.println("START");
		m = new MultiReaderFromSocket();
		m.startTest();
		System.out.println("END");
		m = null;
	}

}