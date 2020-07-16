package Kodet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.RSAPrivateKeySpec;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.time.LocalDateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Token {

	public String Generated(String user) throws Exception {
		PrivateKey aKey = getPrivateElements(user);
		byte[] y = user.getBytes();
		String aString = Base64.getEncoder().encodeToString(y);
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expired = now.plusMinutes(20);
		DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		String formatDateTime2 = expired.format(format1);
		byte[] q = formatDateTime2.getBytes();
		String a = Base64.getEncoder().encodeToString(q);
		String plainText = aString + "." + a;
		String tokenString = "Token: " + plainText + "." + sign(plainText, aKey);
		return tokenString;
	}

	public static PrivateKey getPrivateElements(String user) throws Exception {
		try {
			File file = new File("./keys/" + user + ".xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("RSAKeyValue");
			Node node = nodeList.item(0);

			Element eElement = (Element) node;
			String modulus = eElement.getElementsByTagName("Modulus").item(0).getTextContent();
			String d = eElement.getElementsByTagName("D").item(0).getTextContent();
			KeyFactory rsaFactory = KeyFactory.getInstance("RSA");

			byte[] modBytes = modulus.getBytes();
			byte[] dBytes = d.getBytes();
			BigInteger modBigInt = new BigInteger(1, Base64.getDecoder().decode(modBytes));
			BigInteger dBigInt = new BigInteger(1, Base64.getDecoder().decode(dBytes));

			RSAPrivateKeySpec rsaKeyspec = new RSAPrivateKeySpec(modBigInt, dBigInt);
			PrivateKey key = rsaFactory.generatePrivate(rsaKeyspec);

			return key;
		} catch (FileNotFoundException e) {
			System.out.println("Gabim: Celesi privat '" + user + "' nuk ekziston.");
			System.exit(1);
		}
		return null;
	}

	public static String sign(String plainText, PrivateKey privateKey) throws Exception {
		Signature privateSignature = Signature.getInstance("SHA256withRSA");
		privateSignature.initSign(privateKey);
		privateSignature.update(plainText.getBytes());
		byte[] signature = privateSignature.sign();
		return Base64.getEncoder().encodeToString(signature);
	}

}
