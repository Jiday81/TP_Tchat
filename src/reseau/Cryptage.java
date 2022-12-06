package reseau;

import java.security.Key;

import javax.crypto.Cipher;

public abstract class Cryptage {

	public static String crypte(String message, Key cle) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, cle);
		byte[] b = message.getBytes();
		b = cipher.doFinal(b);
		return new String(b);
	}

}
