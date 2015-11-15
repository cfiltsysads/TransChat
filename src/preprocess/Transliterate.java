package preprocess;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Global transliteration methods.
 */
public class Transliterate {

  
	  /**takes a word and returns its trasliteration
	   * 
	   * @param args
	 * @throws IOException 
	   * @throws Exception
	   */
	 public static String transliterate(String input) throws IOException {
		 String ip = input.replaceAll(" ", "%20");
			URL url = new URL("http://www.google.com/inputtools/request?ime=transliteration_en_hi&num=5&cp=0&cs=0&ie=utf-8&oe=utf-8&text=" + ip);
			URLConnection connection = url.openConnection();
			connection.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			String res = null;
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
				String s = inputLine.split(",")[2].replaceAll("\\[\"", "");
				res = s.substring(0, s.length() - 1);
			}
			in.close();
			return res;
	 	}
	 public static void main(String[] args) throws Exception{

			//System.out.println(Transliterate.doTransliteration("मेरा नाम is NAman B 223"));
			//System.out.println(tl.transliterate("223"));

		}

		public static String doTransliteration(String toTransliterate, String lang ) throws Exception{

			String splitString[] = toTransliterate.split(" ");
			int lengthString = splitString.length;
			StringBuffer englishWords = new StringBuffer("");

			int[] index = new int[lengthString];
			for (int i = 0; i < lengthString; i++) {
				//Skip the pipes
				if(splitString[i].charAt(0) == '|') {
					continue;
				}
				boolean valid = splitString[i].matches("[a-zA-Z]+");
				if (valid) {
					index[i] = 1;
					englishWords.append(" " + splitString[i]);
				}
			}
		//	System.out.println("eng words" + englishWords);
			if(englishWords.length() == 0) {
				return toTransliterate;
			}
			// Transliterate
			//String[] transliteratedEnglishWords = englishWords.toString()
				//	.split(" ");
			String[] transliteratedEnglishWords = getTransliterationFromGoogle(englishWords.toString().trim(), lang).split(" ");
					//	.split(" ");
			
			
			StringBuffer transliteratedString = new StringBuffer("");
			int j = 0;
			for (int i = 0; i < lengthString; i++) {
				if (index[i] == 1) {
					transliteratedString.append(" "
							+ transliteratedEnglishWords[j++]);
				} else {
					transliteratedString.append(" " + splitString[i]);
				}

			}
			return transliteratedString.toString().trim();

		}

		public static String getTransliterationFromGoogle(String englishString, String lang)
				throws Exception {
			String ip = englishString.replaceAll(" ", "%20");
		//	System.out.println(ip);
			URL url = new URL(
					"http://www.google.com/inputtools/request?ime=transliteration_en_hi&num=5&cp=0&cs=0&ie=utf-8&oe=utf-8&text="
							+ ip);
			URLConnection connection = url.openConnection();
			connection.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String inputLine;
			String res = null;
			while ((inputLine = in.readLine()) != null) {
				String s = inputLine.split(",")[2].replaceAll("\\[\"", "");
				res = s.substring(0, s.length() - 1);
			//	System.out.println(res);
			}
			in.close();
			return res;
		}



}