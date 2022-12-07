package reseau;

import java.util.HexFormat;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public abstract class Cryptage {

	public static String crypte(String message, SecretKey cle) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, cle);
		byte[] b = message.getBytes("UTF-8");
		b = cipher.doFinal(b);
		return HexFormat.of().formatHex(b);
	}

	public static String decrypte(String message, SecretKey cle) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, cle);
		byte[] b = HexFormat.of().parseHex(message);
		b = cipher.doFinal(b);
		return new String(b, "UTF-8");
	}

}
