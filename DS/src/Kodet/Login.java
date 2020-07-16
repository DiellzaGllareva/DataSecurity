package Kodet;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Login {

	public void loginGenerateToken(String username) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekti", "root", "");
			String query = "SELECT * FROM users WHERE shfrytezuesi='" + username + "'";
			Scanner in =new Scanner(System. in );
			System.out.print("Shkruani passwordin: ");
			String password = in.nextLine();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			String salt = null;
			String hashedPassword = null;
			while (rs.next()) {
				salt = rs.getString("salt");
				hashedPassword = rs.getString("fjalekalimi");
			}

			if (salt == null || hashedPassword == null) throw new Exception("Perdoruesi nuk ekziston!");

			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(salt.getBytes());
			byte[] bytes = messageDigest.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			// Get complete hashed password in hex format
			String encodedHash = sb.toString();

			if (encodedHash.equals(hashedPassword)) {
				Token tk = new Token();
				String aString = tk.Generated(username);
				System.out.println(aString);
			} else throw new Exception("Gabim: Shfrytezuesi ose fjalekalimi i gabuar!");
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}

	}
}
