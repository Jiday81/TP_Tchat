package reseau;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.LinkedList;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Serveur extends Fenetre {

	private static final long serialVersionUID = -3987461078516664743L;

	final ServerSocket serveurSocket;

	private LinkedList<SecretKey> keys = new LinkedList<>(); // TODO pour le multicast?

	private LinkedList<ObjectOutputStream> dOut = new LinkedList<>(); // TODO changer en dictionnaire pour multicast?
	private LinkedList<ObjectInputStream> dIn = new LinkedList<>(); // TODO changer en dictionnaire pour multicast?

	public Serveur(ServerSocket serveurSocket) throws Exception {
		super("Serveur");

		this.serveurSocket = serveurSocket;
	}

	public void ajouter_client(ObjectInputStream in, ObjectOutputStream out) throws Exception {
		String cle = in.readObject().toString(); // Clé du client reçue
		byte[] decodedKey = Base64.getDecoder().decode(cle);
		keys.add(new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"));

		this.dIn.add(in);
		this.dOut.add(out);
	}

	public void ajouter_message(String s) throws Exception {
		String clair = Cryptage.decrypte(s, keys.get(0));
		ajouter_ligne("Client : " + clair, s);
	}

	public static void main(String[] test) throws Exception {

		ServerSocket serveurSocket = new ServerSocket(555);
		Serveur serv = new Serveur(serveurSocket);

		Socket clientSocket = serveurSocket.accept();
		ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

		serv.ajouter_client(in, out);

		String message;

		message = in.readObject().toString();
		while (message != null) {
			serv.ajouter_message(message);
			message = in.readObject().toString();
		}
		out.close();
		in.close();
		clientSocket.close();
		serveurSocket.close();
		System.exit(-1);
	}
}