package reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class client extends Cryptage {

	public static void main(String[] args) {

		System.out.println("CLIENT :");

		final Socket clientSocket;
		final BufferedReader in;
		final PrintWriter out;
		@SuppressWarnings("resource")
		final Scanner sc = new Scanner(System.in);

		try {
			clientSocket = new Socket("localhost", 555);

			ObjectOutputStream dOut = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream dIn = new ObjectInputStream(clientSocket.getInputStream());

			Thread envoyer = new Thread(new Runnable() {
				String message;

				@Override
				public void run() {
					SecretKey secretKey = null;
					try {
						secretKey = KeyGenerator.getInstance("AES").generateKey();
						String key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
						dOut.writeObject(key);
						dOut.flush();
					} catch (NoSuchAlgorithmException | IOException e) {
						e.printStackTrace();
					}
					while (true) {
						message = sc.nextLine();
						try {
							message = crypte(message, secretKey);
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
			envoyer.start();

			Thread recevoir = new Thread(new Runnable() {
				String message;

				@Override
				public void run() {
					try {
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