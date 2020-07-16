package Kodet;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VerifyToken {

	public void Verified(String signature) throws Exception {
		String[] pikat = signature.split("\\.");
		String emri1 = pikat[0];
		String emri = decode64(pikat[0]);
		String sig = pikat[2];
		String time1 = pikat[1];
		String time = decode64(pikat[1]);
		Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(time);
		LocalDateTime ldt = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
		String plainText = emri1 + "." + time1;
		PublicKey bKey = getPublicElements(emri);
		boolean x = verify(plainText, sig, bKey);
		if (x == true) {
			System.out.println("Derguesi: " + emri);
		} else {
			System.out.println("");
		}
		LocalDateTime now = LocalDateTime.now();
		boolean y = ldt.isAfter(now);
		if (y == true) {
			System.out.println("Valid: Po");
			DateTimeFormatter format2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			String formatDateTime1 = ldt.format(format2);
			System.out.println("Skadon: " + formatDateTime1);
		} else {
			System.out.println("Valid: Jo");

		}
	}

	private static String decode64(String str) {
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] dbyte = decoder.decode(str.getBytes());
		return new String(dbyte);
	}

	public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
		Signature publicSignature = Signature.getInstance("SHA256withRSA");
		publicSignature.initVerify(publicKey);
		publicSignature.update(plainText.getBytes());
		byte[] signatureBytes = Base64.getDecoder().decode(signature);
		return publicSignature.verify(signatureBytes);

	}

	public static PublicKey getPublicElements(String name) throws Exception {
		try {
			File file = new File("./keys/" + name + ".pub.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("RSAKeyValue");
			Node node = nodeList.item(0);
			Element eElement = (Element) node;
			String modulus = eElement.getElementsByTagName("Modulus").item(0).getTextContent();
			String exponent = eElement.getElementsByTagName("Exponent").item(0).getTextContent();
			KeyFactory rsaFactory = KeyFactory.getInstance("RSA");

			byte[] modBytes = modulus.getBytes();
			byte[] expBytes = exponent.getBytes();
			BigInteger modBigInt = new BigInteger(1, Base64.getDecoder().decode(modBytes));
			BigInteger expBigInt = new BigInteger(1, Base64.getDecoder().decode(expBytes));

			RSAPublicKeySpec rsaKeyspec;
			rsaKeyspec = new RSAPublicKeySpec(modBigInt, expBigInt);
			PublicKey key = rsaFactory.generatePublic(rsaKeyspec);
			return key;
		} catch (FileNotFoundException e) {
			System.out.println("Gabim: Celesi publik '" + name + "' nuk ekziston.");
			System.exit(1);
		}
		return null;
	}

}
