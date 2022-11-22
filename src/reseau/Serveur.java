package reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {
	
	
	
	public static void main(String[] args) throws IOException {
		PrintWriter out = null ;
		BufferedReader in= null;
		ServerSocket serverSocket = null ;
		Socket clientSocket;
		try {
			serverSocket = new ServerSocket (555) ;
			System.out.println("Le serveur est à l'écoute");
			clientSocket = serverSocket.accept();
			System.out.println("connexion acceptée sur le port : "+ clientSocket.getPort()) ;
		}
		catch (IOException e) {
			System.out.println("Connexion impossible sur le port 555");
			System.exit(-1);
		}
		BufferedReader stdIn = new BufferedReader ( new InputStreamReader(System.in)) ;
		 
		 String userInput ;
		 
		 while ((userInput = stdIn.readLine())!= null) {
			 out.println(userInput);
			 System.out.println("echo:" + in.readLine()) ;
		 }
		 
		 out.close();
		 in.close();
		 stdIn.close();
	}

}
