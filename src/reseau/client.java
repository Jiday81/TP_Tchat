package reseau;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;
import javax.crypto.* ;
import java.security.* ;

public class client {
	

	public static void main(String[] args) {

		System.out.println("CLIENT :");

		final Socket clientSocket;
		final BufferedReader in;
		final PrintWriter out;
		@SuppressWarnings("resource")
		final Scanner sc = new Scanner(System.in);

		try {
			clientSocket = new Socket("localhost", 555);

			out = new PrintWriter(clientSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			

			Thread envoyer = new Thread(new Runnable() {
				String message;

				@Override
				public void run() {
					SecretKey secretKey = null;
					try {
						secretKey = KeyGenerator.getInstance("AES").generateKey();
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String key = Base64.getEncoder().encodeToString(secretKey.getEncoded()) ;
					out.println(key);
					out.flush();
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
						in.close();
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