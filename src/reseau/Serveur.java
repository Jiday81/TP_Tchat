package reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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
		final BufferedReader in;
		final PrintWriter out;
		@SuppressWarnings("resource")
		final Scanner sc = new Scanner(System.in);
		LinkedList<SecretKey> keys = new LinkedList<>();

		try {
			serveurSocket = new ServerSocket(555);
			System.out.println("Le serveur est � l'�coute");

			clientSocket = serveurSocket.accept();
			System.out.println("Connexion accept�e sur le port : " + clientSocket.getPort());

			ObjectOutputStream dOut = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream dIn = new ObjectInputStream(clientSocket.getInputStream());

			Thread envoi = new Thread(new Runnable() {
				String message;

				@Override
				public void run() {
					while (true) {
						message = sc.nextLine();
						try {
							message = crypte(message, keys.get(0));
							dOut.writeObject(message);
							dOut.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (message.equals("bye")) {
							System.out.println("Fin de la connexion");
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

						System.out.println("Cl� : " + message);
						byte[] decodedKey = Base64.getDecoder().decode(message);
						keys.add(new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"));

						message = dIn.readObject().toString();
						while (message != null) {
							System.out.println("Client : " + message);
							if (message.equals("bye")) {
								System.out.println("Fin de la connexion");
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
					} catch (IOException | ClassNotFoundException e) {
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