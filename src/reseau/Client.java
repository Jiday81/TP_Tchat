//CROS Jean-David / MARAVAL Yoann

package reseau;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.JTextField;

public class Client extends Fenetre implements WindowListener {

	private static final long serialVersionUID = 4202495388188258814L;
	private final ObjectOutputStream out;
	private final SecretKey sk;

	public Client(ObjectOutputStream out) throws Exception {
		// Nous permet d'initialiser toutes les variables nécéssaire au bon
		// fonctionnement du client
		super("Client");
		this.out = out;
		this.sk = KeyGenerator.getInstance("AES").generateKey();
		String key = Base64.getEncoder().encodeToString(this.sk.getEncoded());
		out.writeObject(key);
		out.flush();

		JTextField j = this.jtf;
		this.jtf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = j.getText();
				if (message.length() > 0) {
					j.setText("");
					try {
						envoyer_message("Client : " + message);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	public void ajouter_message(String s) throws Exception {// méthode nécéssaire à afficher un message reçu dans la
															// fenêtre ainsi que de tout fermé si le message est "bye"
		String clair = Cryptage.decrypte(s, this.sk);
		ajouter_ligne(clair, s);
		if (clair.equals("Client : bye") || clair.equals("Serveur : bye")) {
			this.stop();
		}
	}

	public void envoyer_message(String s) throws Exception {// méthode nécéssaire à envoyer un message ecrit
		String message = Cryptage.crypte(s, this.sk);
		this.out.writeObject(message);
		this.out.flush();
		ajouter_message(message);
	}

	public static void main(String[] args) throws Exception {
		// Le main connecte le client au serveur active de manière constante, l'envoie
		// et la réception de message
		FenetreDemandeIP ipRequest = new FenetreDemandeIP();
		while (ipRequest.ip == "") {
			Thread.sleep(100);
		}

		final Socket clientSocket = new Socket(ipRequest.ip, 1789);
		final ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
		final ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

		final Client cli = new Client(out);
		cli.addWindowListener(cli);

		String message;

		message = in.readObject().toString();
		while (message != null) {
			cli.ajouter_message(message);
			message = in.readObject().toString();
		}
		clientSocket.close();
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		try { // Nous permet de fermer les deux fenêtre du moment ou une seule est fermé et
				// donc que la connexion est coupé.
			this.envoyer_message("Client : bye");
		} catch (Exception e1) {
			System.exit(0);
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}