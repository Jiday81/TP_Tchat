package reseau;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class client {

	public static void main(String[] args) throws IOException {

		System.out.println("CLIENT :");

		try (Socket echoSocket = new Socket("localhost", 555)) {
			DataOutputStream dOut = new DataOutputStream(echoSocket.getOutputStream());
			dOut.writeUTF("test");
			dOut.flush();

			dOut.close();
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