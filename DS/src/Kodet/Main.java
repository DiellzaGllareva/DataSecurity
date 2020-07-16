package Kodet;

public class Main {@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		Caesar caesar = new Caesar();
		Vigenere vigenere = new Vigenere();
		RailFence railfence = new RailFence();
		CreateDelete user = new CreateDelete();
		Token tk = new Token();
		VerifyToken vk = new VerifyToken();
		Login lg = new Login();
		if (args[0].equals("caesar") && args[1].equals("encrypt")) {
			try {
				System.out.println("Enkriptimi sipas Kodit Caesar: " + caesar.enkriptimi(args[3], Integer.parseInt(args[2])));
			} catch(Exception e) {
				System.out.println("Nuk keni dhene argumente te mjaftueshme per te realizuar ndonje kod. Shkrimi i sakte i argumenteve duhet te jete si ne vijim:");
				System.out.println("Pozita1:Kodi, Pozita2:Metoda, Pozita3:Celesi, Pozita4:Teksti");
			}
		} else if (args[0].equals("caesar") && args[1].equals("decrypt")) {
			try {
				System.out.println("Dekriptimi sipas Kodit Ceasar:" + caesar.dekriptimi(args[3], 26 - Integer.parseInt(args[2])));
			} catch(Exception e) {
				System.out.println("Nuk keni dhene argumente te mjaftueshme per te realizuar ndonje kod. Shkrimi i sakte i argumenteve duhet te jete si ne vijim:");
				System.out.println("Pozita1:Kodi, Pozita2:Metoda, Pozita3:Celesi, Pozita4:Teksti");
			}
		} else if (args[0].equals("caesar") && args[1].equals("bruteforce")) {
			try {
				System.out.println("Dekriptimet me sulmet BruteForce:");
				caesar.bruteforce(args[2]);
			} catch(Exception e) {
				System.out.println("Nuk keni dhene argumente te mjaftueshme per te realizuar ndonje kod. Shkrimi i sakte i argumenteve duhet te jete si ne vijim:");
				System.out.println("Pozita1:Kodi, Pozita2:Metoda, Pozita3:Celesi, Pozita4:Teksti");
			}
		} else if (args[0].equals("vigenere") && args[1].equals("encrypt")) {
			try {
				System.out.println("Enkriptimi sipas Kodit Vigenere: " + vigenere.Encryption(args[3], (args[2])));
			} catch(Exception e) {
				System.out.println("Nuk keni dhene argumente te mjaftueshme per te realizuar ndonje kod. Shkrimi i sakte i argumenteve duhet te jete si ne vijim:");
				System.out.println("Pozita1:Kodi, Pozita2:Metoda, Pozita3:Celesi, Pozita4:Teksti");
			}
		} else if (args[0].equals("vigenere") && args[1].equals("decrypt")) {
			try {
				System.out.println("Dekriptimi sipas Kodit Vigenere:" + vigenere.Decryption(args[3], (args[2])));
			} catch(Exception e) {
				System.out.println("Nuk keni dhene argumente te mjaftueshme per te realizuar ndonje kod. Shkrimi i sakte i argumenteve duhet te jete si ne vijim:");
				System.out.println("Pozita1:Kodi, Pozita2:Metoda, Pozita3:Celesi, Pozita4:Teksti");
			}
		}
		else if (args[0].equals("railfence") && args[1].equals("encrypt")) {
			try {
				System.out.println("Enkriptimi sipas Kodit RailFence: " + railfence.Enkriptimi(args[3], Integer.parseInt(args[2])));
				railfence.show(railfence.Enkriptimi(args[3], Integer.parseInt(args[2])));
			} catch(Exception e) {
				System.out.println("Nuk keni dhene argumente te mjaftueshme per te realizuar ndonje kod. Shkrimi i sakte i argumenteve duhet te jete si ne vijim:");
				System.out.println("Pozita1:Kodi, Pozita2:Metoda, Pozita3:Celesi, Pozita4:Teksti");
			}
		} else if (args[0].equals("railfence") && args[1].equals("decrypt")) {
			try {
				System.out.println("Dekriptimi sipas Kodit RailFence:" + railfence.Dekriptimi(args[3], Integer.parseInt(args[2])));
			} catch(Exception e) {
				System.out.println("Nuk keni dhene argumente te mjaftueshme per te realizuar ndonje kod. Shkrimi i sakte i argumenteve duhet te jete si ne vijim:");
				System.out.println("Pozita1:Kodi, Pozita2:Metoda, Pozita3:Celesi, Pozita4:Teksti");
			}
		} else if (args[0].equals("create-user")) {
			try {
				user.saveKeys(args[1]);
			} catch(Exception e) {
				System.out.println("Nuk keni argumente te majftueshme per te realizuar kete metode. Per te realizuar kete metode duhet te shkruani:" + "Ne pozicionin e pare create-user(komanda) ndersa ne poziten e dyte emri me te cilin deshironi ta ruani celesin");
			}
		} else if (args[0].equals("delete-user")) {
			try {
				user.deleteUser(args[1]);
			} catch(Exception e) {
				System.out.println("Nuk keni argumente te mjaftueshme per te realizuar kete metode. Per te realizuar kete metode duhet te shkruani:" + "Pozicioni i pare delete-user(komanda),ne poziten e dyte emri i celesit qe deshirojme te fshijme.  ");
			}

		} else if (args[0].equals("status")) {
			try {
				vk.Verified(args[1]);
			} catch(Exception e) {
				System.out.println("Nuk keni argumente te mjaftueshme per te realizuar kete metode. Per te realizuar kete metode duhet te shkruani:" + "Pozicioni i pare status(komanda),ne poziten e dyte tokeni.  ");
			}
		} else if (args[0].equals("login")) {
			try {
				lg.loginGenerateToken(args[1]);
			} catch(Exception e) {
				System.out.println("Nuk keni argumente te mjaftueshme per te realizuar kete metode. Per te realizuar kete metode duhet te shkruani:" + "Pozicioni i pare deleteuser(komanda),ne poziten e dyte emri i celesit qe deshirojme te fshijme.  ");
			}
		} else if ("write-message".equals(args[0])) {
			WriteRead wr = new WriteRead(args[1]);
			if (args.length == 4) {
				wr.Write(args[2], null, args[3]);
			} else if (args.length == 3) {
				wr.Write(args[2], null, null);
			} else {
				System.out.println("Nuk jane dhene komanda valide.\n");
				System.out.println("Komanda write-message nese deshironi vetem t'a enkriptoni mesazhin tuaj duhet te jepet sipas kesaj skeme: \n" + "'write-message <name> <message>' \n");
				System.out.println("Komanda write-message nese deshironi vetem t'a ruani mesazhin e enkriptuar ne nje file duhet te jepet sipas kesaj skeme: \n" + "write-message <name> <message> [file]' \n");
				System.out.println("Komanda write-message nese deshironi t'a enkriptoni mesazhin edhe me --sender the token duhet te jepet sipas kesaj skeme: \n" + "write-message <name> <message> --sender<token>' \n");
			}
		} else if ("read-message".equals(args[0])) {
			WriteRead wr = new WriteRead(args[1]);
			if (args.length == 2) {
				wr.Read(args[1]);
			} else {
				System.out.println("Nuk jane dhene komanda valide.\n");
				System.out.println("Komanda read-message duhet te jepet sipas kesaj skeme: \n" + "' read-message <encrypted-message>' \n");
			}

		}

		else {
			System.out.println("Keni nje gabim ne sintakse. Shkrimi adekuat i argumenteve eshte : ");
			System.out.println("Kodet per pjesen e pare: caesar,vigenere,railfence ");
			System.out.println("Kodet per pjesen e dyte: create-user, delete-user, login, status, write-message [file], write-message --sender token, read-message.");
		}
	}
}
