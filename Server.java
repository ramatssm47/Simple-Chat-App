package Chat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Server extends JFrame{
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	public Server() {
		
		super("Instant Messenger");
		
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener() {
					
					public void actionPerformed(ActionEvent event) {
						
						sendMessage(event.getActionCommand());
						userText.setText("");
					}

				}
			);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(300, 120);
		setVisible(true);
	
	}
		public void startRunning(){
			try{
				server = new ServerSocket(8080,100);
				while(true){
					try{
						waitForConn();
						setupStreams();
						whileChatting();
					}catch(EOFException eofexception){
						showMessage("\n Server ended the connection\n");
					}finally{
						closeConn();
					}
				}
			}catch(IOException ioexception){
				ioexception.printStackTrace();
			}
		}

	private void closeConn() {
			
		showMessage("\n Closing connection....\n");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioexception){
			ioexception.printStackTrace();
		}
			
		}
	private void whileChatting() throws IOException{
			
		String message = "You are now connected!";
		sendMessage(message);
		ableToType(true);
		do{
			try{
				message = (String)input.readObject();
				showMessage("\n "+message);
			}catch(ClassNotFoundException classNotfooundException){
				showMessage("What the user send !");
			}
		}while(!message.equals("CLIENT - END"));
			
		}

	private void setupStreams() throws IOException{
			
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are ready! \n");	
		}
	
	private void waitForConn() throws IOException{
		
		showMessage("\n Waiting for someone to connect....\n");
		connection = server.accept();
		showMessage("\n Now connected to "+connection.getInetAddress().getHostName());
	}
	

	private void sendMessage(String message) {
		
		try{
			output.writeObject("SERVER "+message);
			output.flush();
			showMessage("\nSERVER - "+message);
		}catch(IOException iexception){
			chatWindow.append("\nSorry can't send that message");
		}

	}
	
	private void showMessage(final String text) {
		
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						chatWindow.append(text);
					}
				}
				);
	}
	
	private void ableToType(final boolean toF) {
		
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						userText.setEditable(toF);
					}
				}
				);
	}
}
