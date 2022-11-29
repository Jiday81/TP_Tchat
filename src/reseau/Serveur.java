package reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Serveur {

	public static void main(String[] test) {

		System.out.println("SERVEUR :");

		final ServerSocket serveurSocket;
		final Socket clientSocket;
		final BufferedReader in;
		final PrintWriter out;
		final Scanner sc = new Scanner(System.in);

		try {
			serveurSocket = new ServerSocket(555);
			System.out.println("Le serveur est à l'écoute");

			clientSocket = serveurSocket.accept();
			System.out.println("Connexion acceptée sur le port : " + clientSocket.getPort());

			out = new PrintWriter(clientSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			Thread envoi = new Thread(new Runnable() {
				String message;

				@Override
				public void run() {
					while (true) {
						message = sc.nextLine();
						out.println(message);
						out.flush();
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
						message = in.readLine();
						// Avec le client connecté :
						while (message != null) {
							System.out.println("Client : " + message);
							if (message.equals("bye")) {
								System.out.println("Fin de la connexion");
								break;
							}
							message = in.readLine();

						}
						// On ferme la connexion
						out.close();
						clientSocket.close();
						serveurSocket.close();
						System.exit(-1);
					} catch (IOException e) {
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