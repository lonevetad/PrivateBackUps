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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import tools.gui.swing.HintTextFieldUI;

public class RemoteControl {
	protected static final long MILLIS_BETWEEN_EACH_SCREENCAST = 500; // 250;

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
		protected ViewRC() {}

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

		public void init() {
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
			jpScreencast.setSize(im.getWidth(), im.getHeight());
			jpScreencast.setPreferredSize(jpScreencast.getSize());
			jpScreencast.repaint();
		}

		protected void tryToConnectMasterToSlave(String hostAddress, int port) {
			try {
				this.modelController = new MCMasterRC(this, hostAddress, port);
				// DONE :D
				toTheMasterMasteringUponConnection();
				jlConnectionStatus.setText("Connected to the Slave :D");
			} catch (Exception e) {
				e.printStackTrace();
				jlConnectionStatus.setText("Something went wrong:\n" + e.getMessage());
			}
		}

		protected void onConnectionEstablished() { jlConnectionStatus.setText("Connected :D"); }

		//

		/** But it's the part where it connects to the slave... */
		protected void setupGuiMaster() {
			ConnectionInfoView civ;
			civ = new ConnectionInfoView(modelController, false) {
				private static final long serialVersionUID = 20123016578339L;

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
			jpMasterConnecting.add(civ);
			jpMasterConnecting.add(jlConnectionStatus);
			jbStartMastering = new JButton("Try to connect");
			jbStartMastering.addActionListener(l -> {
				try {
					int port;
					String hostAddress;
					hostAddress = civ.jtHostAddress.getText().trim();
					port = Integer.parseInt(civ.jtPort.getText().trim());

					// I don't want to block the GUI
					ExecutorService executor = Executors.newSingleThreadExecutor();
					executor.submit(() -> {
						jlConnectionStatus.setText("Connecting ...");
						tryToConnectMasterToSlave(hostAddress, port);
						try {
							System.out.println("attempt to shutdown executor");
							executor.shutdown();
							executor.awaitTermination(5, TimeUnit.SECONDS);
						} catch (InterruptedException e) {
							System.err.println("tasks interrupted");
						} finally {
							if (!executor.isTerminated()) { System.err.println("cancel non-finished tasks"); }
							executor.shutdownNow();
							System.out.println("shutdown finished");
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					jlConnectionStatus.setText("Something went wrong:\n" + e.getMessage());
				}
			});
			jpMasterConnecting.add(jbStartMastering);
			jpMasterConnecting.setLayout(new BoxLayout(jpMasterConnecting, BoxLayout.Y_AXIS));
		}

		protected void toMaster() {
			toPanel(PanelName.MasterConnecting);
			setupGuiMaster();
			System.out.println("shown panel master connecting");
		}

		protected void setupGuiMasterMastering() {
			// todo
			jpScreencast = new JPanel() {
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					paintScreencastedImage(g);
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
				private static final long serialVersionUID = 89753134583L;

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
				System.out.println("I'm slaving :D");
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

		// TODO GUI inner class
		abstract static class ConnectionInfoView extends JPanel {
			private static final long serialVersionUID = -12334215L;
			protected JTextField jtHostAddress, jtPort;
			protected AModelControllerRC theController;

			protected ConnectionInfoView(AModelControllerRC modelController, boolean isSlave) {
				super();
				String txt;
				this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				this.theController = modelController;
				txt = "IP address: " + getConnectionAddress(theController);
				System.out.println(txt);
				jtHostAddress = new JTextField();
				jtHostAddress.setToolTipText(txt);
//				jtHostAddress.addFocusListener(new FocusListenerWithPrompt(jtHostAddress, txt));
				jtHostAddress.setUI(new HintTextFieldUI(txt));
				jtHostAddress.setSize(200, 35);
				this.add(jtHostAddress);
				txt = "IP port: " + getConnectionPort(theController);
				System.out.println(txt);
				jtPort = new JTextField();
				jtPort.setToolTipText(txt);
//				jtPort.addFocusListener(new FocusListenerWithPrompt(jtPort, txt));
				jtPort.setUI(new HintTextFieldUI(txt));
				jtPort.setSize(200, 35);
				if (isSlave) {
					jtHostAddress.setEditable(false);
					jtPort.setEditable(false);
				}
				this.add(jtPort);
				this.setSize(450, 100);
				this.setPreferredSize(this.getSize());
			}

			protected abstract String getConnectionAddress(AModelControllerRC theController);

			protected abstract int getConnectionPort(AModelControllerRC theController);

			@Deprecated
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
		protected static final MsgPing PING_MSG = new MsgPing();

		protected MCMasterRC(ViewRC view, String host, int port) throws AWTException, IOException {
			super(view);
			this.socket = new Socket(host, port);
			this.os = this.socket.getOutputStream();
			this.outputChannel = new ObjectOutputStream(this.os);
			System.out.println("master got OUTput stream");
			this.fromSlaveReceiver = new ObjectInputStream(this.socket.getInputStream());
			System.out.println("master got INput stream");
			onSettingUp();
		}

		protected boolean isAlive = true;
		protected final Socket socket;
		protected final OutputStream os;
		protected final ObjectOutputStream outputChannel;
		protected ObjectInputStream fromSlaveReceiver;
		protected MasterRunner masterRunner;
		protected Thread threadMasterRunner;

		@Override
		protected boolean isClient() { return true; }

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

		protected void ping() {
			execute(PING_MSG);
			System.out.println("master pinged");
		}

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
				System.out.println("MASTER RUNNER:\n\t this.masterRCInstance.isAlive: " + this.masterRCInstance.isAlive
						+ "\n\t canRun: " + canRun);
				while (this.masterRCInstance.isAlive && this.canRun) {
					try {
						Thread.sleep(MILLIS_BETWEEN_EACH_SCREENCAST);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (this.masterRCInstance.isAlive) {
						// try to read some incoming message
						System.out.println("master reading");
						try {
							if (this.masterRCInstance.fromSlaveReceiver.available() > 0) {
								try {
									Object objRead = this.masterRCInstance.fromSlaveReceiver.readObject();
									if (objRead instanceof AMessageCommand) {
//										shutDown(false);
										System.out.println(
												"master received " + objRead.getClass().getSimpleName() + " message");
										((AMessageCommand) objRead).execute(this.masterRCInstance);
									}
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								}
							} else
								System.out.println("master nothing to read");
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
			this.server = new ServerSocket(port, 4, InetAddress.getLocalHost());
			threadExecutor = Executors.newFixedThreadPool(2);
			startConnectionAndReading();
		}

		protected ConnectionStatus status = ConnectionStatus.NewBorn;
		protected final ServerSocket server;
		protected Socket master;
		protected ObjectInputStream msgReceiver;
		protected ObjectOutputStream msgSender;
		protected ConnectorAndReaderMsg runnerReader;
		protected ScreencasterRunner runnerScreencaster;
		protected ExecutorService threadExecutor;

		@Override
		protected boolean isClient() { return false; }

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

		protected void sendScreenshoot(BufferedImage screenshot) {
			MsgScreencast msg = new MsgScreencast(screenshot);
			try {
				System.out.println("slave sending screenshot.....");
				msgSender.writeObject(msg);
				System.out.println("slave sent screenshot :D");
				msgSender.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
				if (runnerReader != null)
					this.runnerReader.shutDownReader();
				if (runnerScreencaster != null)
					this.runnerScreencaster.shutDownScreencaster();
				status = ConnectionStatus.ShuttedDown;
				if (master == null)
					return;
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
				this.msgSender = null;
				this.runnerReader = null;
				this.runnerScreencaster = null;
				if (threadExecutor != null) {
					threadExecutor.shutdownNow();
					this.threadExecutor = null;
				}
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + this.status);
			}
		}

		protected void startConnectionAndReading() {
			if (this.runnerReader != null)
				return;
			threadExecutor.submit(this.runnerReader = newReader());
		}

		/** Override designed */
		protected ConnectorAndReaderMsg newReader() { return new ConnectorAndReaderMsg(); }

		protected ScreencasterRunner newScreencasterRunner() { return new ScreencasterRunner(); }

		@Override
		protected void onConnectionEstablished() {
			System.out.println("slave connected");
			threadExecutor.submit(runnerScreencaster = newScreencasterRunner());
			this.view.onConnectionEstablished();
		}

		//
		// TODO SLAVE RUNNERs
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
//					threadReader.interrupt();
					threadExecutor.shutdown();
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
						System.out.println("slave has accepted a socket");
						msgReceiver = new ObjectInputStream(master.getInputStream());
						System.out.println("slave got IN	put stream");
						msgSender = new ObjectOutputStream(master.getOutputStream());
						System.out.println("slave got OUTput stream");
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
				System.out.println("slave start reading a message");
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
								System.out.println("slave sleep : no msg to read");
								Thread.sleep(250);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else {
							try {
								System.out.println("slave something to read");
								if (checkReader())
									return;
								objRead = msgReceiver.readObject();
								if (objRead instanceof AMessageCommand) {
									System.out.println("slave received a message: " + objRead.getClass().getName());
									execute((AMessageCommand) objRead);
								}
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

			protected boolean canScreencast = true;

			protected void shutDownScreencaster() {
				this.canScreencast = false;
				try {
//					threadScreencaster.interrupt();
					threadExecutor.shutdown();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void run() {
				long delta;
				BufferedImage screenshot;
				System.out.println("slave screencasting :D " + canScreencast);
				try {
					while (canScreencast) {
						delta = System.currentTimeMillis();
						try {
							Rectangle areaScreenshot;
							areaScreenshot = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
							screenshot = robotActionSensorActuator.createScreenCapture(areaScreenshot);
							System.out.println(areaScreenshot);
							sendScreenshoot(screenshot);
							System.out.println("sent screenshoot");
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

		ViewRC v;
		v = new ViewRC();
		v.init();
	}
}