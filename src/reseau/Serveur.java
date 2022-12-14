// CROS Jean-David / MARAVAL Yoann

package reseau;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.LinkedList;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JTextField;

public class Serveur extends Fenetre implements WindowListener {

	private static final long serialVersionUID = -3987461078516664743L;

	private LinkedList<SecretKey> keys = new LinkedList<>(); // Liste des clés des clients

	private LinkedList<ObjectOutputStream> dOut = new LinkedList<>(); // Liste des OutputStreams
	private LinkedList<ObjectInputStream> dIn = new LinkedList<>(); // Liste des IuputStreams

	public Serveur() throws Exception {
		//Permet d'initialiser les variable de la classe serveur, 
		super("Serveur");
		JTextField j = this.jtf;
		j.setToolTipText("Entrez votre message.");
		this.jtf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = j.getText();
				if (message.length() > 0) {
					j.setText("");
					try {
						envoyer_message("Serveur : " + message);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	public void ajouter_client(ObjectInputStream in, ObjectOutputStream out) throws Exception {//permet la connexion du client au serveur 
		String cle = in.readObject().toString(); // Clé du client reçue
		byte[] decodedKey = Base64.getDecoder().decode(cle);
		keys.add(new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"));

		this.dIn.add(in);
		this.dOut.add(out);
	}

	public void ajouter_message(String s) throws Exception {// méthode nécéssaire à afficher un message reçu dans la fenêtre ainsi que de tout fermé si le message est "bye"
		String clair = Cryptage.decrypte(s, keys.get(0));
		ajouter_ligne(clair, s);
		if (clair.equals("Client : bye") || clair.equals("Serveur : bye")) {
			this.stop();
		}
	}

	public void envoyer_message(String s) throws Exception {// méthode nécéssaire à envoyer un message ecrit 
		String message = Cryptage.crypte(s, this.keys.get(0));
		this.dOut.get(0).writeObject(message);
		this.dOut.get(0).flush();
		ajouter_message(message);
	}

	public static void main(String[] test) throws Exception {
		//Le main ouvre le serveur aux demande de connexion des clients et permet de connecter les deux en continu pour l'échange de messages
		final ServerSocket serveurSocket = new ServerSocket(1789);
		FenetreAttente att = new FenetreAttente();

		Socket clientSocket = serveurSocket.accept();

		att.stop();

		final Serveur serv = new Serveur();
		serv.addWindowListener(serv);

		ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

		serv.ajouter_client(in, out);

		String message;

		message = in.readObject().toString();
		while (message != null) {
			serv.ajouter_message(message);
			message = in.readObject().toString();
		}
		clientSocket.close();
		serveurSocket.close();
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		try {//Nous permet de fermer les deux fenêtre du moment ou une seule est fermé et donc que la connexion est coupé.
			this.envoyer_message("Serveur : bye");
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