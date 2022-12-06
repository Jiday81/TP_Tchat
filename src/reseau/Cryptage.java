package reseau;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public abstract class Cryptage {

	public static String crypte(String message, SecretKey cle) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, cle);
		byte[] b = message.getBytes();
		b = cipher.doFinal(b);
		return new String(b);
	}

}
