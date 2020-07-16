package Kodet;

class Caesar {

	public StringBuffer enkriptimi(String plaintext, int key) throws Exception {
		StringBuffer result = new StringBuffer();

		for (int i = 0; i < plaintext.length(); i++) {
			if (Character.isUpperCase(plaintext.charAt(i))) {
				char ch = (char) (((int) plaintext.charAt(i) + key - 65) % 26 + 65);
				result.append(ch);
			} else {
				char ch = (char) (((int) plaintext.charAt(i) + key - 97) % 26 + 97);
				result.append(ch);
			}
		}
		return result;
	}

	public StringBuffer dekriptimi(String cipher, int key) throws Exception {
		StringBuffer decrypted = new StringBuffer();

		for (int i = 0; i < cipher.length(); i++) {
			if (Character.isUpperCase(cipher.charAt(i))) {
				char ch = (char) (((int) cipher.charAt(i) + key - 65) % 26 + 65);
				decrypted.append(ch);
			} else {
				char ch = (char) (((int) cipher.charAt(i) + key - 97) % 26 + 97);
				decrypted.append(ch);
			}
		}
		return decrypted;
	}

	public void bruteforce(String x) throws Exception {
		for (int i = 0; i < 26; i++) {
			System.out.println(dekriptimi(x, i));

		}

	}
}
