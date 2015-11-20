package method;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

public class Translator implements Callable<String>{
	String normedStr; String TRANSLATOR_HOST; String TRANSLATOR_PORT; String lang;
	
	public Translator(String normedStr, String tRANSLATOR_HOST,
			String tRANSLATOR_PORT) {
		super();
		this.normedStr = normedStr;
		TRANSLATOR_HOST = tRANSLATOR_HOST;
		TRANSLATOR_PORT = tRANSLATOR_PORT;
		
	}
	private static String replacePipes(String str) {
		StringBuilder pipeLess = new StringBuilder();
		for(int i = 0; i < str.length(); i++) {
			if(str.charAt(i) == '|') {
				i++;
				while(str.charAt(i) != '|') {
					i++;
				}
			} else {
				pipeLess.append(str.charAt(i));
			}
		}
		return pipeLess.toString().replaceAll("\\s+", " ");
	}
	public  String getTransliterationFromGoogle(String englishString, String lang)
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

	@Override
	public String call( ) throws Exception {
		
		String line = null;
		String[] tLateCmd = {
				"/bin/sh",
				"-c",
				"echo \"" + normedStr + "\"|nc " + TRANSLATOR_HOST + " " + TRANSLATOR_PORT
				};
		
		StringBuilder tlated = new StringBuilder();
		Process tlateProcess = Runtime.getRuntime().exec(tLateCmd);
		BufferedReader transInput =
		    new BufferedReader(new InputStreamReader(tlateProcess.getInputStream()));
		
		while((line = transInput.readLine()) != null){
		//    System.out.println("Command results : " + line);
			tlated.append(line);
		    tlated.append('\n');
		}
		//normedStr = normedStr.trim().replace("\n", "");
		String translatedString = replacePipes(tlated.toString());
		String splitString[] = translatedString.split(" ");
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
			return translatedString;
		}
		// Transliterate
		//String[] transliteratedEnglishWords = englishWords.toString()
			//	.split(" ");
		String[] transliteratedEnglishWords = this.getTransliterationFromGoogle(englishWords.toString().trim(), lang).split(" ");
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



}
