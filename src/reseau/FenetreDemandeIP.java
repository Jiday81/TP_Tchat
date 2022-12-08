package reseau;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class FenetreDemandeIP extends JFrame {

	private static final long serialVersionUID = -4361869596212052958L;
	private JTextField j;
	public String ip = "";

	public FenetreDemandeIP() {
		super("Client");
		this.setSize(420, 100);
		this.setLayout(new BorderLayout());

		JLabel l = new JLabel("Veuillez entrer l'addresse ip desirée ou localhost puis pressez entrer",
				SwingConstants.CENTER);
		JPanel p = new JPanel(new GridLayout(2, 1));
		this.j = new JTextField();

		p.add(l);
		p.add(j);
		this.add(p);

		this.j.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = j.getText();
				if (s.length() > 0) {
					setIP(s);
				}
			}
		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setVisible(true);
	}

	private void setIP(String s) {
		this.ip = s;
		this.setVisible(false);
	}

	public static void main(String[] args) {
		new FenetreDemandeIP();
	}

}
