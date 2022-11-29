package reseau;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class client {

	public static void main(String[] args) throws IOException {

		System.out.println("CLIENT :");

		try {
			Socket echoSocket = new Socket("localhost", 555);
			DataOutputStream dOut = new DataOutputStream(echoSocket.getOutputStream());
			DataInputStream dIn = new DataInputStream(echoSocket.getInputStream());
			dOut.writeUTF("test");
			dOut.flush();
			dOut.writeUTF("test2");
			dOut.flush();
			dOut.writeUTF("test3");
			dOut.flush();
			dOut.writeUTF("bye");
			dOut.flush();
			dOut.writeUTF("test");
			dOut.flush();

			dOut.close();
			dIn.close();
			echoSocket.close();
		} catch (UnknownHostException e) {
			System.out.println(" Destination localhost inconnue ");
			System.exit(-1);
		} catch (IOException e) {
			System.out.println(" now to investigate this IO issue ");
			System.exit(-1);
		}
	}
}