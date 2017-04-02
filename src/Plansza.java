import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Plansza extends JFrame implements ActionListener
{
	private JLabel lNapis;
	public Plansza()
	{
		setTitle("Balony");
		setSize(500,500);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLayout(new BorderLayout());
		
		lNapis = new JLabel("Elo kurwa tu bêdzie gra", SwingConstants.CENTER);
		lNapis.setBounds(20,20,400,200);
		lNapis.setFont(new Font("SanSerif",Font.BOLD,30));
		add(lNapis);
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
	}

	public static void main(String[] args)
	{
		
	}
}
