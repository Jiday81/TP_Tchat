package reseau;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class adresse_ip {
	public static void main(String[] args) {
		InetAddress address;
		try {
			address = InetAddress.getLocalHost();
			System.out.println(address.getHostName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
