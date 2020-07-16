package Kodet;

public class RailFence {

	String Dekriptimi(String mesazhi, int nr_shiritave) throws Exception {
		char[] msg_dekriptuar = new char[mesazhi.length()];
	String teksti = mesazhi.replaceAll("w", "");
		int n = 0;
		for (int k = 0; k < nr_shiritave; k++) {
			int indeksi = k;
			boolean thellesia = true;
			while (indeksi < teksti.length()) {

				msg_dekriptuar[indeksi] = teksti.charAt(n++);

				if (k == 0 || k == nr_shiritave - 1) {
					indeksi = indeksi + 2 * (nr_shiritave - 1);
				} else if (thellesia) {
					indeksi = indeksi + 2 * (nr_shiritave - k - 1);
					thellesia = !thellesia;
				} else {
					indeksi = indeksi + 2 * k;
					thellesia = !thellesia;
				}
			}
		}

		String e = new String(msg_dekriptuar);

		return e;

	}

	String Enkriptimi(String mesazhi, int nr_shiritave) throws Exception {
		char[] msg_enkriptuar = new char[mesazhi.length()];
		int n = 0;

		for (int k = 0; k < nr_shiritave; k++) {
			int indeksi = k;
			boolean thellesia = true;
			while (indeksi < mesazhi.length()) {
				msg_enkriptuar[n++] = mesazhi.charAt(indeksi);

				if (k == 0 || k == nr_shiritave - 1) {
					indeksi = indeksi + 2 * (nr_shiritave - 1);
				} else if (thellesia) {
					indeksi = indeksi + 2 * (nr_shiritave - k - 1);
					thellesia = !thellesia;
				} else {
					indeksi = indeksi + 2 * k;
					thellesia = !thellesia;
				}

			}

		}

		String e = new String(msg_enkriptuar);
		do {
			e = e + "w";
		} 
		while ((int) Math.sqrt(e.length()) != Math.sqrt(e.length()));

		return e;
	}

	static void show(String msg_enkriptuar) {
		int l = msg_enkriptuar.length();
		int k = 0, rreshti, kolona;
		rreshti = (int) Math.floor(Math.sqrt(l));
		kolona = (int) Math.ceil(Math.sqrt(l));

		if (rreshti * kolona < l) {
			rreshti = kolona;
		}

		char s[][] = new char[rreshti][kolona];

		for (int i = 0; i < rreshti; i++) {
			for (int j = 0; j < kolona; j++) {
				if (k < msg_enkriptuar.length())
					s[i][j] = msg_enkriptuar.charAt(k);
				k++;
			}
		}

		for (int i = 0; i < rreshti; i++) {
			for (int j = 0; j < kolona; j++) {
				if (s[i][j] == 0) {
					break;
				}
				System.out.print(s[i][j]);
			}
			System.out.println("");
		}
	}
}
