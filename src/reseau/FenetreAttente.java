package reseau;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class FenetreAttente extends JFrame {

	private static final long serialVersionUID = 13389029292197148L;

	public FenetreAttente() {
		super("Serveur");
		this.setSize(400, 100);
		this.setLayout(new BorderLayout());
		JLabel l = new JLabel("Veuillez connecter un client pour commencer le chat", SwingConstants.CENTER);
		this.add(l);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setVisible(true);
	}

	public void stop() {
		this.setVisible(false);
	}

	public static void main(String[] args) {
		new FenetreAttente();
	}

}
