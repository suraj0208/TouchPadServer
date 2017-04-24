import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.Font;
import java.net.SocketException;
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ShowMyIp extends JFrame {
	JTextField jTextField;

	public ShowMyIp() {
		super("Remote Touchpad");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(300, 200);
		this.setVisible(true);
		jTextField = new JTextField(20);

		jTextField.setFont(new Font("Courier", Font.BOLD, 25));
		add(jTextField);

	}

	public void Settext(String s) {
		this.jTextField.setText("");
		this.jTextField.setText(s);
		this.revalidate();
	}

	public String Gettext() {
		return this.jTextField.getText();
	}
}

class BackTask extends Thread {
	private Socket clientSocket;
	private InputStreamReader inputStreamReader;
	private BufferedWriter bufferedWriter;
	private BufferedReader bufferedReader;
	private int ix = 0, iy = 0;
	private String message;

	public BackTask(Socket clientSocket) {
		this.clientSocket = clientSocket;
		System.out.println(clientSocket);
	}

	@Override
	public void run() {

		try {
			inputStreamReader = new InputStreamReader(
					clientSocket.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					clientSocket.getOutputStream()));


			while (true) {

				message = bufferedReader.readLine();

				System.out.println(message);
				if (message == null) {
					bufferedReader.close();
					inputStreamReader.close();
					clientSocket.close();
					System.out.println("Socket Closed");
					break;
				}

				String capmessage = message.toUpperCase();
				Robot robot = new Robot();

				if (message.length() == 0)
					continue;

				else if (message.length() == 1) {
					System.out.println((int) message.charAt(0));
					if (message.charAt(0) > 122 || message.charAt(0) < 65) {
						System.out.println("single");
						robot.keyPress(message.charAt(0));
						robot.keyRelease((message.charAt(0)));
						continue;
					}

					boolean upperCase = Character
							.isUpperCase(message.charAt(0));

					if (Toolkit.getDefaultToolkit().getLockingKeyState(
							KeyEvent.VK_CAPS_LOCK))
						upperCase = !upperCase;

					String variableName = "VK_" + capmessage;

					Class clazz = KeyEvent.class;
					Field field = clazz.getField(variableName);
					int keyCode = field.getInt(null);

					if (upperCase)
						robot.keyPress(KeyEvent.VK_SHIFT);

					robot.keyPress(keyCode);
					robot.keyRelease(keyCode);

					if (upperCase)
						robot.keyRelease(KeyEvent.VK_SHIFT);

					continue;
				} else if (message.length() == 2) {

					String variableName = "VK_" + capmessage;

					Class clazz = KeyEvent.class;
					Field field = clazz.getField(variableName);
					int keyCode = field.getInt(null);

					robot.keyPress(keyCode);
					robot.keyRelease(keyCode);

					continue;
				}

				switch (message) {

				case "first":
					ix = MouseInfo.getPointerInfo().getLocation().x;
					iy = MouseInfo.getPointerInfo().getLocation().y;
					break;

				case "lclickp":
					robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
					break;

				case "lclickr":
					robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
					break;

				case "lclick":
					robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
					break;

				case "rclick":
					robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
					robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
					break;

				case "scrollup":
					robot.mouseWheel(-1);
					break;

				case "scrolldown":
					robot.mouseWheel(1);
					break;

				case "left":
					robot.keyPress(KeyEvent.VK_5);
					break;

				case "right":
					robot.mouseWheel(1);
					break;

				case "super":
					robot.keyPress(KeyEvent.VK_WINDOWS);
					robot.keyRelease(KeyEvent.VK_WINDOWS);
					break;

				case "ctrlp":
					robot.keyPress(KeyEvent.VK_CONTROL);
					break;

				case "ctrlr":
					robot.keyRelease(KeyEvent.VK_CONTROL);
					break;

				case "altp":
					robot.keyPress(KeyEvent.VK_ALT);
					break;

				case "altr":
					robot.keyRelease(KeyEvent.VK_ALT);
					break;

				case "backspace":
					robot.keyPress(KeyEvent.VK_BACK_SPACE);
					robot.keyRelease(KeyEvent.VK_BACK_SPACE);
					break;

				case "shutdown":
					try {
						Runtime.getRuntime().exec("init 0");
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;

				case "reboot":
					try {
						Runtime.getRuntime().exec("init 6");
					} catch (IOException e) {
						e.printStackTrace();
					}

					break;

				case "lock":
					try{
						Runtime.getRuntime().exec("gnome-screensaver-command -l");					
					}catch(IOException ex){ 
						ex.printStackTrace();
					}
					break;

				case "close":
					robot.keyPress(KeyEvent.VK_ALT);
					robot.keyPress(KeyEvent.VK_F4);
					robot.keyRelease(KeyEvent.VK_ALT);
					robot.keyRelease(KeyEvent.VK_F4);
					break;

				case "delete":
					robot.keyPress(KeyEvent.VK_DELETE);
					robot.keyRelease(KeyEvent.VK_DELETE);
					break;

				case "uparrow":
					robot.keyPress(KeyEvent.VK_UP);
					robot.keyRelease(KeyEvent.VK_UP);
					break;

				case "enter":
					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
					break;

				case "leftarrow":
					robot.keyPress(KeyEvent.VK_LEFT);
					robot.keyRelease(KeyEvent.VK_LEFT);
					break;

				case "downarrow":
					robot.keyPress(KeyEvent.VK_DOWN);
					robot.keyRelease(KeyEvent.VK_DOWN);
					break;

				case "rightarrow":
					robot.keyPress(KeyEvent.VK_RIGHT);
					robot.keyRelease(KeyEvent.VK_RIGHT);
					break;

				case "getpower":
					Process process;
					process = Runtime
							.getRuntime()
							.exec("grep -w POWER_SUPPLY_CAPACITY /sys/class/power_supply/BAT0/uevent");
					
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(process.getInputStream()));

					String percentageString = bufferedReader.readLine();
					percentageString = percentageString.split("=")[1];
					percentageString = percentageString + "\n";

					bufferedWriter.write(percentageString);
					System.out.println("writter battery percentage");
					bufferedWriter.flush();
					break;
				default:

					String[] test = message.split("&");
					robot.mouseMove(Math.round(ix + Float.parseFloat(test[0])),
							Math.round(iy + Float.parseFloat(test[1])));

					break;
				}
			}

		} catch (NumberFormatException ex) {

		} catch (IOException | AWTException e) {
			e.printStackTrace();

			try {
				bufferedReader.close();
				inputStreamReader.close();
				clientSocket.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

class PowerQueryAdapter extends Thread {
	private Socket socket;
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private final int POWER_THRESH=2;

	public PowerQueryAdapter(Socket socket) {
		this.socket = socket;
		System.out.println(socket);
	}

	public void run() {
		try {

			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			int percentage;

			while (true) {
				
				/*Process process;
				process = Runtime
						.getRuntime()
						.exec("grep -w POWER_SUPPLY_CAPACITY /sys/class/power_supply/BAT0/uevent");
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(process.getInputStream()));

				//String percentageString = bufferedReader.readLine();
				String percentageString = "as=32";
				percentageString = percentageString.split("=")[1];
				
				percentage = Integer.parseInt(percentageString);
				
				if(percentage<POWER_THRESH){
					percentageString = percentageString + "\n";
					bufferedWriter.write(percentageString);
					bufferedWriter.flush();
					//socket.close();					
					break;			
				}
				
				
				while(percentage<POWER_THRESH){
					process = Runtime
						.getRuntime()
						.exec("grep -w POWER_SUPPLY_CAPACITY /sys/class/power_supply/BAT0/uevent");
					bufferedReader = new BufferedReader(
							new InputStreamReader(process.getInputStream()));

					percentageString = bufferedReader.readLine();
					percentageString = percentageString.split("=")[1];
				
					percentage = Integer.parseInt(percentageString);
				
				}*/
				//Thread.sleep(1000*60*3);			

			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}

}

public class SimpleTextServer {

	private static ServerSocket serverSocket;
	private static ServerSocket powerServerSocket;
	private static String message;

	public static void main(String[] args) {
		ShowMyIp showMyIp = new ShowMyIp();
		try {
			//Thread.sleep(5);
			serverSocket = new ServerSocket(4444); // Server socket
			powerServerSocket = new ServerSocket(4445);

			Enumeration<NetworkInterface> n = NetworkInterface
					.getNetworkInterfaces();
			for (; n.hasMoreElements();) {

				NetworkInterface e = n.nextElement();

				if (!(e.getName().equals("wlan0")))
					continue;

				Enumeration<InetAddress> a = e.getInetAddresses();
				for (; a.hasMoreElements();) {
					InetAddress addr = a.nextElement();
					Matcher matcher = Pattern.compile(
							"[0-9]+[.][0-9]+[.][0-9]+[.][0-9]+").matcher(
							addr.getHostAddress());
					if (matcher.matches()) {
						showMyIp.Settext("IP: " + matcher.group(0));

					}
					System.out.println("  " + addr.getHostAddress());
				}
			}

		} catch (SocketException ex) {
			ex.printStackTrace();
			System.out.println("Error while getting IP");
			System.exit(1);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("Error while creating socket IP");
			System.exit(2);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("Server started. Listening to the port 4444");

		try {

			while (true) {
				new BackTask(serverSocket.accept()).start();
				//new PowerQueryAdapter(soc).start();

			}

		} catch (IOException | NullPointerException ex) {
			ex.printStackTrace();
			System.out.println("Problem in message reading");
		}
	}

}
