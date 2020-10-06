package tools.minorTools;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class RemoteControl {

	//

	// TODO CLASS

	//

	protected static class ViewRC {
		public static final String[] PANELS_NAME;

		public static enum PanelName {
			Main, ClientSlave, MasterConnecting, MasterMastering
		}

		static {
			int i;
			PanelName[] vals = PanelName.values();
			String[] panNames;
			panNames = new String[i = vals.length];
			while (--i >= 0) {
				panNames[i] = vals[i].name();
			}
//			panNames = Arrays.stream(vals).map(pn -> pn.name()).collect(Collectors.toList()).toArray(panNames);
			PANELS_NAME = panNames;
		}

		//
		protected ViewRC() { init(); }

		private boolean alreadyInitialized = false;
		protected AModelControllerRC modelController;
		protected BufferedImage screenCastReceived;
		protected JFrame fin;
		protected JPanel jpFin, jpMainPanel, jpScreencast, jpSlave, jpMasterConnecting, jpMaster;
		protected JScrollPane jspScreencast;
		protected PanelName currentPanel;
		protected CardLayout allPanelsManager;
		protected JButton jbToSlave, jbToMaster, jbStartMastering;
		protected JLabel jlConnectionStatus;

		protected void init() {
			if (alreadyInitialized)
				return;
			alreadyInitialized = true;
			allPanelsManager = new CardLayout();
			currentPanel = PanelName.Main;
			fin = new JFrame("Remote controller - by lonevetad");
			fin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			fin.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					if (modelController != null) {
						modelController.shutDown(true);
						modelController = null;
					}
				}
			});
			jpFin = new JPanel(allPanelsManager);
			fin.getContentPane().add(jpFin);

			// main

			jpMainPanel = new JPanel();
			jpFin.add(jpMainPanel, PanelName.Main.name());
			jbToSlave = new JButton("Be controlled by remote");
			jbToMaster = new JButton("Control the remote PC");
			jpMainPanel.add(jbToSlave);
			jbToSlave.addActionListener(l -> toSlave());
			jpMainPanel.add(jbToMaster);
			jbToMaster.addActionListener(l -> toMaster());

			// slave
			jpSlave = new JPanel();
			jpFin.add(jpSlave, PanelName.ClientSlave.name());

			// master connecting to slave
			jpMasterConnecting = new JPanel();
			jpFin.add(jpMasterConnecting, PanelName.MasterConnecting.name());

			// master
			jpMaster = new JPanel(new BorderLayout());
			jpFin.add(jpMaster, PanelName.MasterMastering.name());

			// TODO todo altre robe
			jlConnectionStatus = new JLabel("NOT connected yet");

			fin.setSize(500, 500);
			fin.setVisible(true);
		}

		protected void toPanel(PanelName pn) { allPanelsManager.show(jpFin, pn.name()); }

		/** Just saves the instance */
		protected void showScreencast(BufferedImage im) {
			this.screenCastReceived = im;
//resize the image
		}

		protected void onConnectionEstablished() { jlConnectionStatus.setText("Connected :D"); }

		//

		/** But it's the part where it connects to the slave... */
		protected void setupGuiMaster() {
			ConnectionInfoView civ;
			civ = new ConnectionInfoView(modelController, false) {

				@Override
				protected String getConnectionAddress(AModelControllerRC theController) {
					return "ask if to the \"slave\" (the remote PC).";
					// ((MCMasterRC) theController).getSlaveConnectionAddress();
				}

				@Override
				protected int getConnectionPort(AModelControllerRC theController) {
					return 0; // ((MCMasterRC) theController).getSlaveConnectionPort();
				}

			};
			jpMaster.add(civ);
			jpMaster.add(jlConnectionStatus);
			jbStartMastering = new JButton("Try to connect");
			jbStartMastering.addActionListener(l -> {
				try {
					int port;
					String hostAddress;
					hostAddress = civ.jtHostAddress.getText().trim();
					port = Integer.parseInt(civ.jtPort.getText().trim());
					this.modelController = new MCMasterRC(this, hostAddress, port);
					// DONE :D
					toTheMasterMasteringUponConnection();
					jlConnectionStatus.setText("Connected to the Slave :D");
				} catch (Exception e) {
					e.printStackTrace();
					jlConnectionStatus.setText("Something went wrong:\n" + e.getMessage());
				}
			});
			jpMaster.add(jbStartMastering);
		}

		protected void toMaster() {
			toPanel(PanelName.MasterConnecting);
			setupGuiMaster();
		}

		protected void setupGuiMasterMastering() {
			// todo
			jpScreencast = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {
					paintScreencastedImage(g);
					super.paintComponent(g);
				}
			};
			jspScreencast = new JScrollPane(jpScreencast);
			jspScreencast.setViewportView(jpScreencast);
			jspScreencast.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			jspScreencast.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jpMaster.add(jspScreencast, BorderLayout.CENTER);

			// LISTENERS
			jpScreencast.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					keyPressed(e);
					keyReleased(e);
				}

				@Override
				public void keyReleased(KeyEvent e) {
					MCMasterRC master;
					master = (MCMasterRC) modelController;
					master.execute(new MsgKeyboard(false, e.getKeyCode()));
				}

				@Override
				public void keyPressed(KeyEvent e) {
					MCMasterRC master;
					master = (MCMasterRC) modelController;
					master.execute(new MsgKeyboard(true, e.getKeyCode()));
				}
			});
			MouseAdapter ma;
			ma = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					MCMasterRC master;
					master = (MCMasterRC) modelController;
					master.execute(new MsgMouse(MouseAction.Click, e.getButton()));
				}

				@Override
				public void mousePressed(MouseEvent e) {
					MCMasterRC master;
					master = (MCMasterRC) modelController;
					master.execute(new MsgMouse(MouseAction.Press, e.getButton()));
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					MCMasterRC master;
					master = (MCMasterRC) modelController;
					master.execute(new MsgMouse(MouseAction.Release, e.getButton()));
				}

				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					MCMasterRC master;
					master = (MCMasterRC) modelController;
					master.execute(new MsgMouse(MouseAction.Wheel, e.getButton()));
				}

				@Override
				public void mouseDragged(MouseEvent e) {
					mousePressed(e);
					mouseMoved(e);
				}

				@Override
				public void mouseMoved(MouseEvent e) {
					MCMasterRC master;
					master = (MCMasterRC) modelController;
					master.execute(new MsgMouse(MouseAction.Move, e.getX(), e.getY()));
				}
			};
			jpScreencast.addMouseListener(ma);
			jpScreencast.addMouseMotionListener(ma);
			jpScreencast.addMouseWheelListener(ma);
		}

		protected void toTheMasterMasteringUponConnection() {
			toPanel(PanelName.MasterMastering);
			setupGuiMasterMastering();
		}

		//

		protected void setupGuiSlave() {
			ConnectionInfoView civ;
			civ = new ConnectionInfoView(modelController, true) {

				@Override
				protected String getConnectionAddress(AModelControllerRC theController) {
					return ((MCSlaveRC) theController).getConnectionAddress();
				}

				@Override
				protected int getConnectionPort(AModelControllerRC theController) {
					return ((MCSlaveRC) theController).getConnectionPort();
				}

			};
			jpSlave.add(civ);
			jpSlave.add(jlConnectionStatus);
		}

		protected void toSlave() {
			try {
				this.modelController = new MCSlaveRC(this);
				setupGuiSlave();
				toPanel(PanelName.ClientSlave);
			} catch (AWTException | IOException e) {
				e.printStackTrace();
			}
		}

		//

		protected void paintScreencastedImage(Graphics g) {
			if (screenCastReceived == null)
				return;
			jpScreencast.setSize(screenCastReceived.getWidth(), screenCastReceived.getHeight());
			g.drawImage(screenCastReceived, 0, 0, null);
		}

		// inner class
		abstract static class ConnectionInfoView extends JPanel {
			protected JTextField jtHostAddress, jtPort;
			protected AModelControllerRC theController;

			protected ConnectionInfoView(AModelControllerRC modelController, boolean isSlave) {
				super();
				String txt;
				this.theController = modelController;
				txt = "IP address: " + getConnectionAddress(theController);
				jtHostAddress = new JTextField(txt);
				jtHostAddress.setToolTipText(txt);
				jtHostAddress.addFocusListener(new FocusListenerWithPrompt(jtHostAddress, txt));
				add(jtHostAddress);
				txt = "IP port: " + getConnectionPort(theController);
				jtPort = new JTextField(txt);
				jtPort.setToolTipText(txt);
				jtPort.addFocusListener(new FocusListenerWithPrompt(jtPort, txt));
				if (isSlave) {
					jtHostAddress.setEditable(false);
					jtPort.setEditable(false);
				}
				add(jtPort);
			}

			protected abstract String getConnectionAddress(AModelControllerRC theController);

			protected abstract int getConnectionPort(AModelControllerRC theController);

			protected class FocusListenerWithPrompt implements FocusListener {
				final String txt;
				final JTextField textField;

				protected FocusListenerWithPrompt(JTextField textField, String txt) {
					super();
					this.textField = textField;
					this.txt = txt;
				}

				@Override
				public void focusLost(FocusEvent e) {
					if (textField.getText().equals(""))
						textField.setText(txt);
				}

				@Override
				public void focusGained(FocusEvent e) {
					if (textField.getText().equals(txt))
						textField.setText("");
				}
			}
		}
	}

	//
	//

	//

	protected static abstract class AModelControllerRC {
		protected final Robot robotActionSensorActuator;
		protected final ViewRC view;

		protected AModelControllerRC(ViewRC view) throws AWTException, IOException {
			super();
			this.view = view;
			this.robotActionSensorActuator = new Robot();
		}

		public Robot getRobotActionSensorActuator() { return robotActionSensorActuator; }

		protected abstract boolean isClient();

		protected abstract boolean execute(AMessageCommand msg);

		protected abstract void onConnectionEstablished();

		protected abstract void shutDown(boolean isUserAction);
	}

	// TODO MASTER

	/**
	 * Is the Client: has sensors to receive input from view (execute method) and
	 * send it to the Slave.
	 */
	protected static class MCMasterRC extends AModelControllerRC {
		protected static final int PING_INTERVAL_MS = 1000;
		protected static final MsgPing PING_MSG = new MsgPing();

		protected MCMasterRC(ViewRC view, String host, int port) throws AWTException, IOException {
			super(view);
			this.socket = new Socket(host, port);
			this.os = this.socket.getOutputStream();
			this.outputChannel = new ObjectOutputStream(this.os);
			this.shutDownReceiver = new ObjectInputStream(this.socket.getInputStream());
			onSettingUp();
		}

		protected boolean isAlive = true;
		protected final Socket socket;
		protected final OutputStream os;
		protected final ObjectOutputStream outputChannel;
		protected ObjectInputStream shutDownReceiver;
		protected MasterRunner masterRunner;
		protected Thread threadMasterRunner;

		@Override
		protected boolean isClient() { return false; }

		protected String getSlaveConnectionAddress() { return socket.getInetAddress().getHostAddress(); }

		protected int getSlaveConnectionPort() { return socket.getLocalPort(); }

		@Override
		protected boolean execute(AMessageCommand msg) {
			// SEND the msg
			try {
				this.outputChannel.writeObject(msg);
				this.outputChannel.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void shutDown(boolean isUserAction) {
			if (!this.isAlive)
				return;
			this.isAlive = false;
			execute(new MsgShutDown());
//			outputChannel.close();
			this.masterRunner.shutDownRunner();
			this.masterRunner = null;
			this.threadMasterRunner = null;
			try {
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		protected void ping() { execute(PING_MSG); }

		protected void manageScreencast(BufferedImage bi) { this.view.showScreencast(bi); }

		//

		protected MasterRunner newMasterRunner() { return new MasterRunner(this); }

		protected void onSettingUp() {
			this.threadMasterRunner = new Thread(this.newMasterRunner());
			this.onConnectionEstablished();
			this.threadMasterRunner.start();
		}

		//

		/**
		 * Does works on background: Ping, check status (does shutdown or not), receive
		 * screencast, ..
		 */
		protected static class MasterRunner implements Runnable {
			protected boolean canRun = true;
			protected final MCMasterRC masterRCInstance;

			protected MasterRunner(MCMasterRC masterRCInstance) {
				super();
				this.masterRCInstance = masterRCInstance;
			}

			protected void shutDownRunner() {
				this.canRun = false;

			}

			@Override
			public void run() {
				while (this.masterRCInstance.isAlive && this.canRun) {
					try {
						Thread.sleep(PING_INTERVAL_MS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (this.masterRCInstance.isAlive) {
						// try to read some incoming message
						try {
							if (this.masterRCInstance.shutDownReceiver.available() > 0) {
								try {
									Object objRead = this.masterRCInstance.shutDownReceiver.readObject();
									if (objRead instanceof AMessageCommand) {
//										shutDown(false);
										((AMessageCommand) objRead).execute(this.masterRCInstance);
									}
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (this.masterRCInstance.isAlive) { this.masterRCInstance.ping(); }
				}
			}
		}

		@Override
		protected void onConnectionEstablished() {
			System.out.println("master connected");
			this.view.toTheMasterMasteringUponConnection();
		}
	}

	// TODO MCSlaveRC

	/** Is the Server: receive orders (inputs) from the Master and execute them. */
	protected static class MCSlaveRC extends AModelControllerRC {
		protected static enum ConnectionStatus {
			NewBorn, Connecting, Connected, ShuttedDown;
		}

		protected MCSlaveRC(ViewRC view) throws AWTException, IOException { this(view, 0); }

		protected MCSlaveRC(ViewRC view, int port) throws AWTException, IOException {
			super(view);
			this.server = new ServerSocket(port);
			startConnectionAndReading();
		}

		protected ConnectionStatus status = ConnectionStatus.NewBorn;
		protected final ServerSocket server;
		protected Socket master;
		protected ObjectInputStream msgReceiver;
		protected ConnectorAndReaderMsg runnerReader;
		protected ScreencasterRunner runnerScreencaster;
		protected Thread threadReader, threadScreencaster;

		@Override
		protected boolean isClient() { return true; }

		protected String getConnectionAddress() { return server.getInetAddress().getHostAddress(); }

		protected int getConnectionPort() { return server.getLocalPort(); }

		@Override
		protected boolean execute(AMessageCommand msg) {
//			final MessageType mt;
			if (msg == null)
				return false;
			/*
			 * mt = msg.msgType; switch (mt) { case Mouse: { executeCommandMouse((MsgMouse)
			 * msg); } default: throw new IllegalArgumentException("Unexpected value: " +
			 * mt); }
			 */
			msg.execute(this);
			return true;
		}

		/*
		 * @Override protected void executeCommandMouse(MsgMouse mm) {
		 * this.robotActionSensorActuator. }
		 */
		@Override
		protected void shutDown(boolean isUserAction) {
			switch (this.status) {
			case NewBorn:
			case ShuttedDown: {
				return;
			}
			case Connecting:
			case Connected: {
				this.runnerReader.shutDownReader();
				this.runnerScreencaster.shutDownScreencaster();
				status = ConnectionStatus.ShuttedDown;
				if (isUserAction) {
					ObjectOutputStream oos;
					try {
						oos = new ObjectOutputStream(this.master.getOutputStream());
						oos.writeObject(new MsgShutDown());
						oos.flush();
//						oos.close(); // removed because of the "master.close()" below
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					this.master.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					this.master.shutdownInput();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					this.master.shutdownOutput();
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.master = null;
				this.msgReceiver = null;
				this.runnerReader = null;
				this.runnerScreencaster = null;
				this.threadReader = null;
				this.threadScreencaster = null;
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + this.status);
			}

		}

		protected void startConnectionAndReading() {
			if (this.runnerReader != null)
				return;
			this.threadReader = new Thread(this.runnerReader = newReader());
			this.threadReader.start();
		}

		/** Override designed */
		protected ConnectorAndReaderMsg newReader() { return new ConnectorAndReaderMsg(); }

		protected ScreencasterRunner newScreencasterRunner() { return new ScreencasterRunner(); }

		@Override
		protected void onConnectionEstablished() {
			System.out.println("slave connected");
			this.threadScreencaster = new Thread(runnerScreencaster = newScreencasterRunner());
			this.threadScreencaster.start();
		}

		//
		//
		protected class ConnectorAndReaderMsg implements Runnable {
			protected boolean canRead = true;

			@Override
			public void run() {
				connectToMaster();
				readMsg();
			}

			protected void shutDownReader() {
				this.canRead = false;
				try {
					threadReader.interrupt();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}

			protected void connectToMaster() {
				while (canRead && master == null && status == ConnectionStatus.NewBorn) {
					try {
						status = ConnectionStatus.Connecting;
						master = server.accept();
						status = ConnectionStatus.Connected;
						msgReceiver = new ObjectInputStream(master.getInputStream());
						onConnectionEstablished();
					} catch (IOException e) {
						status = ConnectionStatus.NewBorn;
						e.printStackTrace();
					}
				}
			}

			protected boolean checkReader() {
				if (msgReceiver == null)
					status = ConnectionStatus.ShuttedDown;
				return msgReceiver == null;
			}

			protected void readMsg() {
				int avail;
				Object objRead;
				while (this.canRead) {
					avail = 0;
					do {
						try {
							if (checkReader())
								return;
							avail = msgReceiver.available();
						} catch (IOException e) {
							e.printStackTrace();
							avail = 0;
						}
						if (avail == 0) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else {
							try {
								if (checkReader())
									return;
								objRead = msgReceiver.readObject();
								if (objRead instanceof AMessageCommand) { execute((AMessageCommand) objRead); }
							} catch (ClassNotFoundException | IOException e) {
								e.printStackTrace();
							}
						}
					} while (this.canRead && avail > 0);
				}
				System.out.println("No more messages to read");
			}
		}

		protected class ScreencasterRunner implements Runnable {
//			robotActionSensorActuator

			protected boolean canScreencast = true;
			protected long MILLIS_BETWEEN_EACH_SCREENCAST = 250;

			protected void shutDownScreencaster() {
				this.canScreencast = false;
				try {
					threadScreencaster.interrupt();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void run() {
				long delta;
				BufferedImage screenshot;
				try {
					while (canScreencast) {
						delta = System.currentTimeMillis();
						try {
							screenshot = robotActionSensorActuator
									.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
							execute(new MsgScreencast(screenshot));
						} catch (Exception e) {
							e.printStackTrace();
						}
						delta = MILLIS_BETWEEN_EACH_SCREENCAST - (System.currentTimeMillis() - delta);
						if (delta > 0) { Thread.sleep(delta); }
					}
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
	}

	//

	// TODO messages

	//

	protected static enum MessageType {
		Ping, ShutDown, Mouse, Keyboard, Image
	}

	protected static abstract class AMessageCommand implements Serializable {
		private static final long serialVersionUID = 54120L;
		protected final MessageType msgType;

		protected AMessageCommand(MessageType msgType) {
			super();
			this.msgType = msgType;
		}

		public MessageType getMsgType() { return msgType; }

		protected abstract void execute(AModelControllerRC context);
	}

	//

	protected static class MsgPing extends AMessageCommand {
		private static final long serialVersionUID = 650788L;

		protected MsgPing() { super(MessageType.Ping); }

		@Override
		protected void execute(AModelControllerRC context) {}
	}

	protected static class MsgShutDown extends AMessageCommand {
		private static final long serialVersionUID = 6507858L;

		protected MsgShutDown() { super(MessageType.ShutDown); }

		@Override
		protected void execute(AModelControllerRC context) { context.shutDown(false); }
	}

	//

	protected static enum MouseAction implements Serializable {
		Move, Press, Release, Wheel, Click
	}

	/** Mouse position are just deltas, not absolute coordinates. */
	protected static class MsgMouse extends AMessageCommand {
		private static final long serialVersionUID = -25879603L;

		protected MsgMouse(MouseAction mouseAction, int fp, int sp) {
			super(MessageType.Mouse);
			this.mouseAction = mouseAction;
			this.firstParameter = fp;
			this.secondParameter = sp;
		}

		protected MsgMouse(MouseAction mouseAction, int fp) { this(mouseAction, fp, 0); }

		protected final int firstParameter, secondParameter;
		protected final MouseAction mouseAction;

		@Override
		protected void execute(AModelControllerRC context) {
			boolean isClick;
			Robot r;
			r = context.robotActionSensorActuator;
			if (this.mouseAction == MouseAction.Move) {
//				Point p = MouseInfo.getPointerInfo().getLocation();
//				r.mouseMove(p.x + firstParameter, p.y + secondParameter);
				r.mouseMove(firstParameter, secondParameter);
				return;
			}
			isClick = this.mouseAction == MouseAction.Click;
			if (isClick || this.mouseAction == MouseAction.Press) { r.mousePress(firstParameter); }
			if (isClick) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (isClick || this.mouseAction == MouseAction.Release) { r.mouseRelease(firstParameter); }
			if (this.mouseAction == MouseAction.Wheel) { r.mouseWheel(firstParameter); }
		}
	}

	//

	protected static class MsgKeyboard extends AMessageCommand {
		private static final long serialVersionUID = 65210048000L;
		protected final boolean isPressed;
		protected final int key;

		protected MsgKeyboard(boolean isPressed, int key) {
			super(MessageType.Keyboard);
			this.isPressed = isPressed;
			this.key = key;
		}

		@Override
		protected void execute(AModelControllerRC context) {
			Robot r;
			r = context.robotActionSensorActuator;
//			Consumer<Integer> act = isPressed ? r::keyPress : r::keyRelease;
//			act.accept(key);
			if (isPressed)
				r.keyPress(key);
			else
				r.keyRelease(key);
		}
	}

	protected static class MsgScreencast extends AMessageCommand {
		private static final long serialVersionUID = -852310131L;
		protected int imgType, width, height;
		protected int[] rawPixels;
		protected transient BufferedImage reconstructed = null;

		protected MsgScreencast(BufferedImage im) {
			super(MessageType.Image);
			this.rawPixels = null;
			this.height = this.width = this.imgType = 0;
			setUpImgageData(im);
		}

		protected void setUpImgageData(BufferedImage im) {
			int w, h, x, y, i;
			w = this.width = im.getWidth();
			h = this.height = im.getHeight();
			this.imgType = im.getType();
			rawPixels = new int[w * h];
			i = 0;
			y = -1;
			while (++y < h) {
				x = -1;
				while (++x < w) {
					rawPixels[i++] = im.getRGB(x, y);
				}
			}
		}

		@Override
		protected void execute(AModelControllerRC context) {
			if (context instanceof MCMasterRC) {
				if (reconstructed == null) {
					int w, h, x, y, i;
					BufferedImage bi;
					reconstructed = bi = new BufferedImage(width, height, imgType);
					i = 0;
					h = height;
					w = width;
					y = -1;
					while (++y < h) {
						x = -1;
						while (++x < w) {
							bi.setRGB(x, y, rawPixels[i++]);
						}
					}
				}
				((MCMasterRC) context).manageScreencast(reconstructed);
			}
		}
	}

	//

	// TODO MAIN

	//

	public static void main(String args[]) {
		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getLocalHost();
			System.out.println("IP Address:- " + inetAddress.getHostAddress());
			System.out.println("Host Name:- " + inetAddress.getHostName());
			System.out.println("loopback address: " + InetAddress.getLoopbackAddress());
			System.out.println("CanonicalHostName: " + inetAddress.getCanonicalHostName());
			System.out.println("address as array: " + Arrays.toString(inetAddress.getAddress()));
			System.out.println("\n\n trying different way: DatagramSocket");

			try (final DatagramSocket socket = new DatagramSocket()) {
				socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
				var ip = socket.getLocalAddress().getHostAddress();
				System.out.println("ip got:");
				System.out.println(ip);
				System.out.println("inet address:" + socket.getInetAddress());
			} catch (SocketException e) {
				e.printStackTrace();
			}
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
	}
}