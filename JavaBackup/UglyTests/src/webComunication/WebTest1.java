package webComunication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.stream.IntStream;

/** IT WORKS !! */
public class WebTest1 {

	static final int PORT = 8082;
	static final String HOST = "localhost"//
			, START_HTTP_REPLY = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n" //
			, HEAD = "<html><head><meta charset=\"utf-8\"><title>megaciaone</title></head><body>"//
			, FOOTER = "</body></html>\r\n\r\n"// ,
			, LI_OPEN = "<li>", LI_CLOSE = "</li>"//
	;
	static {
		System.out.println(HEAD);
	}

	public WebTest1() {
		try {
			server = new ServerSocket(PORT);

			System.out.println(server.getInetAddress());

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(500);
		}
	}

	Socket socket;// server side
	ServerSocket server;
	// OutputStream out;
	// InputStream in;
	BufferedWriter out;
	BufferedReader in;

	//

	//

	void start() throws IOException {
		String s;
		// int x;
		socket = server.accept();
		// remember this order: it's server-side
		in = new BufferedReader(//
				new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(//
				new PrintWriter(socket.getOutputStream()));

		// System.out.println("accepted" + in.available());

		// while (in.available() > 0) {
		while (//
		((s = in.readLine()) != null)//
				&& ((s = s.trim()).length() > 0)//
		) {
			// x = in.read();
			System.out.println(s);
		}

		//
		sendSomething();
	}

	void sendSomething() {
		StringBuilder sb;
		String s;
		System.out.println("SENDING");
		sb = new StringBuilder(START_HTTP_REPLY.length() + HEAD.length() + 128 + FOOTER.length());
		//
		sb.append(START_HTTP_REPLY);
		sb.append(HEAD);
		sb.append(getHTMLContentReply());
		sb.append(FOOTER);
		s = sb.toString();
		sb = null;
		System.out.println(s);
		try {
			out.write(s);
			out.flush();
			System.out.println("done :D");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String getHTMLContentReply() {
		IntStream stInt;
		StringBuilder sb;
		sb = new StringBuilder(128);
		sb.append("<div>");
		sb.append("<h1>Ciaone</h1>");
		sb.append("<h3>sono marco :D</h3>");
		sb.append("<ul>");
		stInt = (new Random()).ints(-64, 65).limit(10);
		stInt.forEach(i -> sb.append(LI_OPEN).append(i).append(LI_CLOSE));
		sb.append("</ul>");
		sb.append("</div>");
		return sb.toString();
	}

	void close() {

		// remember this order: it's server-side
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//

	// TODO main

	/** On any browser, simply connect to "http://localhost:8082/" */
	public static void main(String[] args) {
		WebTest1 w;
		w = new WebTest1();
		try {
			w.start();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}