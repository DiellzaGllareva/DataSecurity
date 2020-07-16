package Kodet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Scanner;

public class ImportExport {

	public static void ExportPrivate(String path, String name) throws IOException {
		File file = new File(path + "/" + name + ".pem");
		if (file.exists()) {
			InputStream input = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[8192];
			try {
				for (int length = 0; (length = input.read(buffer)) != -1;) {
					System.out.write(buffer, 0, length);
				}
			} finally {
				input.close();
			}
		} else {
			System.out.println("Celesi privat " + name + " nuk ekziston.");
		}

	}

	private static String GetRequest(String url) throws IOException {

		URL urlObj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
		connection.setRequestMethod("GET");

		Integer responseCode = connection.getResponseCode();
		StringBuffer response = new StringBuffer();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader inputreader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;

			while ((inputLine = inputreader.readLine()) != null) {
				response.append(inputLine);
			}
			inputreader.close();
		}
		return response.toString();
	}

	public static void ExportPublic(String pathh, String namee) throws IOException {
		File filee = new File(pathh + "/" + namee + "pub.pem");
		if (filee.exists()) {
			InputStream input = new BufferedInputStream(new FileInputStream(filee));
			byte[] buffer = new byte[8192];

			try {
				for (int length = 0; (length = input.read(buffer)) != -1;) {
					System.out.write(buffer, 0, length);
				}
			} finally {
				input.close();
			}
		} else {
			System.out.println("Celesi publik " + namee + " nuk ekziston.");
		}

	}

	public static void ExportPrivateFile(String lokacioni, String emri, String filename)
			throws FileNotFoundException, IOException {

		FileInputStream fis = new FileInputStream(lokacioni + "/" + emri + ".pem");

		FileOutputStream fos = new FileOutputStream(filename);

		int b;
		while ((b = fis.read()) != -1)
			fos.write(b);
		fis.close();
		fos.close();
		System.out.println("Celesi privat '" + emri + "'eshte ruajtur ne fajllin :" + filename);
	}

	public static void ExportPublicFile(String lokacionii, String emrii, String filenamee)
			throws FileNotFoundException, IOException {

		FileInputStream fiss = new FileInputStream(lokacionii + "/" + emrii + "pub.pem");
		FileOutputStream foss = new FileOutputStream(filenamee);

		int b;
		while ((b = fiss.read()) != -1)
			foss.write(b);
		fiss.close();
		foss.close();
		System.out.println("Celesi publik '" + emrii + "'eshte ruajtur ne fajllin :" + filenamee);
	}

	public static void ImportPrivate(String pathhh, String nameee, String filenameee)
			throws FileNotFoundException, IOException, GeneralSecurityException {
		try {
			File filee = new File(pathhh + "/" + nameee + ".pem");
			if (!filee.exists()) {
				FileInputStream fisss = new FileInputStream(filenameee);
				FileOutputStream fosss = new FileOutputStream(pathhh + "/" + nameee + ".pem");
				int b;
				while ((b = fisss.read()) != -1)
					fosss.write(b);
				fisss.close();
				fosss.close();
			} else {
				System.out.println("Celesi privat '" + nameee + "' egziston paraprakisht.");
			}
			File fileee = new File(pathhh + "/" + nameee + "pub.pem");
			if (!fileee.exists()) {
				PrintWriter writer = new PrintWriter(fileee, "UTF-8");
				PublicKey publicKey = getPublicKey();
				KeyFactory keyFacPublic = KeyFactory.getInstance("RSA");
				RSAPublicKeySpec pkSpecpublic = keyFacPublic.getKeySpec(publicKey, RSAPublicKeySpec.class);
				writer.println("<RSA Value> : ");
				writer.println("<Modulus> : " + pkSpecpublic.getModulus().toString(16));
				writer.println("<Exponent> : " + pkSpecpublic.getPublicExponent().toString(16));
				writer.println("</RSA Value> : ");
				writer.close();
				System.out.println("Celesi privat eshte ruajtur ne fajllin: " + pathhh + "/" + nameee + ".pem");
				System.out.println("Celesi publik eshte ruajtur ne fajllin: " + pathhh + "/" + nameee + "pub.pem");

			}
		} catch (Exception e) {
			System.out.println("Fajlli i dhene nuk eshte celes valid.");
		}

	}

	public static void ImportPublic(String vendi, String emer, String emrifile)
			throws FileNotFoundException, IOException, InterruptedException {

		File file = new File(vendi + "/" + emer + "pub.pem");
		if (!file.exists()) {
			FileInputStream fissss = new FileInputStream(emrifile);
			FileOutputStream fossss = new FileOutputStream(vendi + "/" + emer + "pub.pem");
			int b;
			while ((b = fissss.read()) != -1)
				fossss.write(b);
			fissss.close();
			fossss.close();
			System.out
					.println("Celesi publik '" + emer + "' eshte ruajtur ne fajllin" + vendi + "/" + emer + "pub.pem");
		} else {
			System.out.println("Celesi publik '" + emer + "' egziston paraprakisht");
		}

	}

	private static PublicKey getPublicKey() throws GeneralSecurityException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(1024);
		return kpg.generateKeyPair().getPublic();
	}

	public static void Import(String vendi, String emer, String emrifile)
			throws IOException, GeneralSecurityException, InterruptedException {
		File file = new File(emrifile);
		StringBuilder fileContents = new StringBuilder((int) file.length());

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine() + System.lineSeparator());
			}
			String a = fileContents.toString();
			if (a.contains("<DQ>")) {
				ImportPrivate(vendi, emer, emrifile);
			} else if (emrifile.contains("http")) {
				GetRequest(emrifile);
			} else {
				ImportPublic(vendi, emer, emrifile);
			}
		} catch (Exception e) {
			System.out.println("");
		}
	}

}
