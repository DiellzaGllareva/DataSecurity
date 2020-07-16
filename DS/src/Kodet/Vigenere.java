package Kodet;

public class Vigenere {
	public static String Encryption(String text, String key) throws Exception {
		String cipherText = "";
		text = text.toUpperCase();
		for (int i = 0, j = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c < 'A' || c > 'Z')
				continue;
			cipherText += (char) ((c + key.charAt(j) - 2 * 'A') % 26 + 'A');
			j = ++j % key.length();
		}
		if (key == key.toLowerCase()) {
			System.out.println("Celesi te shenohet me shkronja te medha.");
			System.exit(1);
		}
		return cipherText;
	}

	public static String Decryption(String text, String key) throws Exception {
		String plainText = "";
		text = text.toUpperCase();
		for (int i = 0, j = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c < 'A' || c > 'Z')
				continue;
			plainText += (char) ((c - key.charAt(j) + 26) % 26 + 'A');
			j = ++j % key.length();
		}
		if (key == key.toLowerCase()) {
			System.out.println("Celesi te shenohet me shkronja te medha.");
			System.exit(1);
		}
		return plainText;
	}

}
