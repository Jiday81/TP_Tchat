package reseau;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.JTextField;

public class Client extends Fenetre {

	private static final long serialVersionUID = 4202495388188258814L;
	private final ObjectOutputStream out;
	private final SecretKey sk;

	public Client(ObjectOutputStream out) throws Exception {
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
				j.setText("");
				try {
					envoyer_message("Client : " + message);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	public void ajouter_message(String s) throws Exception {
		String clair = Cryptage.decrypte(s, this.sk);
		ajouter_ligne(clair, s);
	}

	public void envoyer_message(String s) throws Exception {
		String message = Cryptage.crypte(s, this.sk);
		ajouter_message(message);
		this.out.writeObject(message);
	}

	public static void main(String[] args) throws Exception {

		final Socket clientSocket = new Socket("localhost", 555);
		final ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
		final ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

		final Client cli = new Client(out);

		String message;

		message = in.readObject().toString();
		while (message != null) {
			cli.ajouter_message(message);
			message = in.readObject().toString();
		}
		out.close();
		in.close();
		clientSocket.close();
		System.exit(-1);
	}
}