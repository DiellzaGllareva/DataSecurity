package Kodet;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security. * ;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

public class CreateDelete {
	static final String KEY_ALGORITHM = "RSA";
	static final int KEY_LENGTH = 1024;
	static final String NL = System.getProperty("line.separator");

	public static void saveKeys(String user) throws Exception {
		KeyPair keyPair = createKeyPair(KEY_LENGTH);
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		//Fjalekalimi
		String passwordhere = PasswordField.readPassword("Jepni fjalekalimin: ");
		String confirmhere = PasswordField.readPassword("Perserite fjalekalimin: ");
		List < String > errorList = new ArrayList < String > ();
		//Kontrolla se a eshte passi valid
		while (!isValid(passwordhere, confirmhere, errorList)) {
			for (String error: errorList) {
				System.out.println(error);
			}
			passwordhere = PasswordField.readPassword("Jepni fjalekalimin: ");
			confirmhere = PasswordField.readPassword("Sheno fjalekalimin:");
		}

		String privateKeyAsXml = savePrivateKeyAsXml(privateKey);
		File fajlliPub = new File("./keys/" + user + ".pub.xml");
		File fajlliPriv = new File("./keys/" + user + ".xml");
		if (! (fajlliPub.exists() && fajlliPriv.exists()) && isValid(passwordhere, confirmhere, errorList)) {
			writeFile(privateKeyAsXml, "./keys/" + user + ".xml");
			String publicKeyAsXml = savePublicKeyAsXml(publicKey);
			writeFile(publicKeyAsXml, "./keys/" + user + ".pub.xml");
			//Ne qofte se files nuk ekzistojne paraprakisht dhe passi eshte valid krijo shfrytezuesin
			System.out.println("Eshte krijuar shfrytezuesi '" + user + "'.");
			System.out.println("Eshte krijuar celesi publik: " + user + ".pub.xml");
			System.out.println("Eshte krijuar celesi privat: " + user + ".xml");
			byte[] salti = getSalt();
			byte[] salt = encode64(new String(salti)).getBytes();
			String password = getSecurePassword(passwordhere, salt);
			//Insertimi i shfrytezuesit ne db
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = null;
				conn = DriverManager.getConnection("jdbc:mysql://localhost/projekti", "root", "");
				System.out.println("Inserting records into the table...");
				Statement stmt = null;
				stmt = conn.createStatement();
				stmt.executeUpdate("insert into users(shfrytezuesi,fjalekalimi,salt) values('" + user + "','" + password + "','" + new String(salt) + "')");
				System.out.print("Shfrytezuesi u regjistrua me sukses ne databaze!");
				conn.close();
			} catch(Exception e) {
				System.out.print("Eshte shfaqur nje gabim-Gabimi:" + e);
			}
		}

		else {
			System.out.println("Gabim: Celesi '" + user + "' ekziston paraprakisht.");
		}

	}

	public static KeyPair createKeyPair(int keyLength) throws NoSuchAlgorithmException {
		KeyPairGenerator keygen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keygen.initialize(keyLength, new SecureRandom());
		KeyPair keyPair = keygen.generateKeyPair();
		return keyPair;
	}

	public static String savePrivateKeyAsXml(PrivateKey privateKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		RSAPrivateCrtKeySpec spec = keyFactory.getKeySpec(privateKey, RSAPrivateCrtKeySpec.class);
		StringBuilder sb = new StringBuilder();

		sb.append("<RSAKeyValue>" + NL);
		sb.append(getElement("Modulus", spec.getModulus()));
		sb.append(getElement("Exponent", spec.getPublicExponent()));
		sb.append(getElement("P", spec.getPrimeP()));
		sb.append(getElement("Q", spec.getPrimeQ()));
		sb.append(getElement("DP", spec.getPrimeExponentP()));
		sb.append(getElement("DQ", spec.getPrimeExponentQ()));
		sb.append(getElement("InverseQ", spec.getCrtCoefficient()));
		sb.append(getElement("D", spec.getPrivateExponent()));
		sb.append("</RSAKeyValue>");

		return sb.toString();
	}

	public static String savePublicKeyAsXml(PublicKey publicKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		RSAPublicKeySpec spec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
		StringBuilder sb = new StringBuilder();

		sb.append("<RSAKeyValue>" + NL);
		sb.append(getElement("Modulus", spec.getModulus()));
		sb.append(getElement("Exponent", spec.getPublicExponent()));
		sb.append("</RSAKeyValue>");

		return sb.toString();
	}

	public static String getElement(String parameter, BigInteger bigInt) throws Exception {

		byte[] bytesFromBigInt = bigInt.toByteArray();
		String elementContent = Base64.getEncoder().encodeToString(bytesFromBigInt);
		return String.format("  <%s>%s</%s>%s", parameter, elementContent, parameter, NL);
	}

	public static void deleteUser(String user) {
		File publik = new File("./keys/" + user + ".pub.xml");
		File privat = new File("./keys/" + user + ".xml");
		if (user.equals(user) && (publik.exists() || privat.exists())) {
			if (publik.delete()) {
				System.out.println("Eshte larguar celesi publik: '" + "keys/" + user + ".pub.xml" + "' ");
			}
			if (privat.delete()) {
				System.out.println("Eshte larguar celesi privat: '" + "keys/" + user + ".xml" + "' \n");
			}
			//Fshirja e shfrytezuesit nga databaza.
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = null;
				conn = DriverManager.getConnection("jdbc:mysql://localhost/projekti", "root", "");
				Statement stmt = null;
				stmt = conn.createStatement();
				stmt.executeUpdate("delete from users where shfrytezuesi='" + user + "'");
				System.out.print("Shfrytezuesi eshte fshire nga databaza!");
				conn.close();
			} catch(Exception e) {
				System.out.print("Eshte shfaqur nje gabim! - Gabimi:" + e);
			}

		} else System.out.println("Celesi '" + user + "' nuk ekziston");

	}

	public static void writeFile(String text, String filename) throws Exception {
		try (PrintWriter writer = new PrintWriter(filename)) {
			writer.write(text);
		}
	}

	public static boolean isValid(String passwordhere, String confirmhere, List < String > errorList) {

		Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
		Pattern lowerCasePatten = Pattern.compile("[a-z ]");
		Pattern digitCasePatten = Pattern.compile("[0-9 ]");
		errorList.clear();

		boolean flag = true;

		if (!passwordhere.equals(confirmhere)) {
			errorList.add("Gabim: Fjalekalimet nuk perputhen.");
			flag = false;
		}
		if (passwordhere.length() < 6) {
			errorList.add("Gabim: Fjalekalimi duhet t'i kete se paku 6 karaktere.");
			flag = false;
		}
		if (! (digitCasePatten.matcher(passwordhere).find() || specailCharPatten.matcher(passwordhere).find())) {
			errorList.add("Gabim: Fjalekalimi duhet te kete se paku nje numer ose simbol.");
			flag = false;
		}
		if (!UpperCasePatten.matcher(passwordhere).find()) {
			errorList.add("Gabim: Fjalekalimi duhet te kete se paku nje shkronje te madhe.");
			flag = false;
		}
		if (!lowerCasePatten.matcher(passwordhere).find()) {
			errorList.add("Gabim: Fjalekalimi duhet te kete se paku nje shkronje te vogel.");
			flag = false;
		}

		return flag;

	}
	//Metoda per ruajtjen e passit ne formen saltedhash
	private static String getSecurePassword(String passwordToHash, byte[] salt) {
		String generatedPassword = null;
		try {

			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(salt);
			byte[] bytes = md.digest(passwordToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	// Salti
	private static byte[] getSalt() throws NoSuchAlgorithmException,
	NoSuchProviderException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}

	private static String encode64(String str) {
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] encoded = encoder.encode(str.getBytes());
		return new String(encoded);
	}

}
