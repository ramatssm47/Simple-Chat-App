package Chat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Test extends JFrame{
	
	private JTextArea area;
	private JTextField text;
	
	public Test() {
	super("Test");
	area = new JTextArea();
	add(new JScrollPane(area));
	text = new JTextField();
	add(text,BorderLayout.NORTH);
	text.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					area.append("\n"+e.getActionCommand());
					text.setText("");
					
				}
			});
			
		}
	});
	
	setVisible(true);
	}

	public static void main(String[] args) {
		
		Test t = new Test();

	}

}
