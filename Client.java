package Chat;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Client extends JFrame{

	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;
	
	public Client(String host) {
		
		super("Client Messenger");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				
				sendData(event.getActionCommand());
				userText.setText("");
			}
		});
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300, 150);
		setVisible(true);
	}
	
	public void startRunning(){
		try{
			connectToServer();
			setupStreams();
			whileChat();
		}catch(EOFException eofException){
			showMessage("\n Client connetion terminated!");
		}catch (IOException ioException) {
			ioException.printStackTrace();
		}finally{
			closeConn();
		}
	}

	private void showMessage(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				chatWindow.append(message);
				
			}
		});
		
	}

	private void whileChat() throws IOException{
		ableToType(true);
		do{
			try{
				message = (String)input.readObject();
				showMessage(message);
			}catch(ClassNotFoundException classnotfoundexception){
				showMessage("\n Object type not readable");
			}
		}while(!message.equals("SERVER - END"));
	}

	private void ableToType(final boolean b) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				userText.setEditable(b);
				
			}
		});
		
	}

	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are good to go...");
	}

	private void connectToServer() throws IOException{
		showMessage("\n Trying to connect to server...");
		connection = new Socket(InetAddress.getByName(serverIP), 8080);
		showMessage("\n connected to server "+connection.getInetAddress().getHostName());
	}

	private void sendData(String message) {
		try{
			output.writeObject("CLIENT - "+message);
			output.flush();
			showMessage("\n CLIENT - "+message);
		}catch(IOException ioexception){
			showMessage("\n Some problem!!!");
		}
		
	}
	
	private void closeConn() {
		showMessage("\n Closing connection...");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioexception){
			ioexception.printStackTrace();
		}
		
	}
}
