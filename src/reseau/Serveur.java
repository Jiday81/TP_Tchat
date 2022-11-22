package reseau;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {

	public static void main(String[] args) throws IOException {
		System.out.println("SERVEUR :");

		try (ServerSocket serverSocket = new ServerSocket(555)) {
			System.out.println("Le serveur est � l'�coute");

			Socket clientSocket = serverSocket.accept();
			System.out.println("Connexion accept�e sur le port : " + clientSocket.getPort());

			DataInputStream dIn = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream dOut = new DataOutputStream(clientSocket.getOutputStream());

			Boolean done = false;
			while (!done) {
				System.out.println("Message : " + dIn.readUTF());
			}

			dIn.close();
			dOut.close();
		} catch (IOException e) {
			System.out.println("Connexion impossible sur le port 555");
			System.exit(-1);
		}

	}

}
