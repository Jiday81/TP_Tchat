package reseau;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Serveur extends Cryptage {

	public static void main(String[] test) {

		System.out.println("SERVEUR :");

		final ServerSocket serveurSocket;
		final Socket clientSocket;
		@SuppressWarnings("resource")
		final Scanner sc = new Scanner(System.in);
		LinkedList<SecretKey> keys = new LinkedList<>();

		try {
			serveurSocket = new ServerSocket(555);
			System.out.println("Le serveur est à l'écoute");

			clientSocket = serveurSocket.accept();
			System.out.println("Connexion acceptée sur le port : " + clientSocket.getPort());

			ObjectOutputStream dOut = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream dIn = new ObjectInputStream(clientSocket.getInputStream());

			Thread envoi = new Thread(new Runnable() {
				String message;

				@Override
				public void run() {
					while (true) {
						message = sc.nextLine();
						try {
							String messageC = crypte(message, keys.get(0));
							dOut.writeObject(messageC);
							dOut.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (message.equals("bye")) {
							System.out.println("Fin de la connexion");
							System.exit(-1);
							break;
						}
					}
				}
			});
			envoi.start();

			Thread recevoir = new Thread(new Runnable() {
				String message;

				@Override
				public void run() {
					try {
						message = dIn.readObject().toString();

						System.out.println("Clé : " + message);
						byte[] decodedKey = Base64.getDecoder().decode(message);
						keys.add(new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"));

						message = dIn.readObject().toString();
						while (message != null) {
							System.out.println("Client [crypté] : " + message);
							message = decrypte(message, keys.get(0));
							System.out.println("Client [non crypté] : " + message);
							if (message.equals("bye")) {
								System.out.println("Fin de la connexion");
								System.exit(-1);
								break;
							}
							// message = in.readLine();
							message = dIn.readObject().toString();
						}

						dOut.close();
						dIn.close();
						clientSocket.close();
						serveurSocket.close();
						System.exit(-1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			recevoir.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}