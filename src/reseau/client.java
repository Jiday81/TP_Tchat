package reseau;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class client extends Cryptage {

	public static void main(String[] args) throws NoSuchAlgorithmException {

		System.out.println("CLIENT :");

		final Socket clientSocket;
		@SuppressWarnings("resource")
		final Scanner sc = new Scanner(System.in);

		try {
			clientSocket = new Socket("localhost", 555);

			SecretKey sk = KeyGenerator.getInstance("AES").generateKey();

			ObjectOutputStream dOut = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream dIn = new ObjectInputStream(clientSocket.getInputStream());

			Thread envoyer = new Thread(new Runnable() {
				String message;

				@Override
				public void run() {
					try {
						String key = Base64.getEncoder().encodeToString(sk.getEncoded());
						dOut.writeObject(key);
						dOut.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					while (true) {
						message = sc.nextLine();
						try {
							String messageC = crypte(message, sk);
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
			envoyer.start();

			Thread recevoir = new Thread(new Runnable() {
				String message;

				@Override
				public void run() {
					try {
						message = dIn.readObject().toString();
						while (message != null) {
							System.out.println("Serveur [crypté] : " + message);
							message = decrypte(message, sk);
							System.out.println("Serveur [non crypté] : " + message);
							if (message.equals("bye")) {
								System.out.println("Fin de la connexion");
								System.exit(-1);
								break;
							}
							message = dIn.readObject().toString();
						}

						dOut.close();
						dIn.close();
						clientSocket.close();
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