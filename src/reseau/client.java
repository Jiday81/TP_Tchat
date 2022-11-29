package reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class client {

	public static void main(String[] args) {

		System.out.println("CLIENT :");

		final Socket clientSocket;
		final BufferedReader in;
		final PrintWriter out;
		final Scanner sc = new Scanner(System.in);

		try {
			clientSocket = new Socket("localhost", 555);

			out = new PrintWriter(clientSocket.getOutputStream());

			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			Thread envoyer = new Thread(new Runnable() {
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
			envoyer.start();

			Thread recevoir = new Thread(new Runnable() {
				String message;

				@Override
				public void run() {
					try {
						message = in.readLine();
						while (message != null) {
							System.out.println("Serveur : " + message);
							if (message.equals("bye")) {
								System.out.println("Fin de la connexion");
								break;
							}
							message = in.readLine();

						}
						out.close();
						clientSocket.close();
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