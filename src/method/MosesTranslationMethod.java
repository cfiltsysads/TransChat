//sg


package method;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;

import preprocess.Compressor;
import preprocess.Transliterate;

//import spell.JazzySpellChecker;

public class MosesTranslationMethod {

	Socket translationSocket, normalizerSocket;
	HashSet<String> dict;
	final String SCORE_FILE = "/home/sic209/moses_translation_score";
	String TRANSLATOR_HOST = "10.144.22.121";
	int TRANSLATOR_PORT = 1235;
	String NORMALIZER_HOST = "10.144.22.121";
	int NORMALIZER_PORT = 1234;
	//String TLATE_COMMAND, NORM_COMMAND; 
	//JazzySpellChecker jspellChecker;
	public MosesTranslationMethod(HashSet<String> dict) throws UnknownHostException, IOException {
		//jspellChecker = new JazzySpellChecker();
		this.dict = dict;
		//translationSocket = new Socket(TRANSLATOR_HOST, TRANSLATOR_PORT);
		//normalizerSocket = new Socket(NORMALIZER_HOST, NORMALIZER_PORT);
	}
	

	public String[] process(String sentence) {
		try 
		{			
			//System.out.println("Got : " + sentence);
			
			//----------------------------
			//Step 1 : Decompress
			//----------------------------
			
			sentence  = decompress(sentence);
		//	System.out.println("Decompressed : " + sentence);
			//----------------------------
			//Step 2 : Normalize
			//----------------------------
			String normedStr  = normalize(sentence);
		//	System.out.println("Normalized : " + normedStr);
			
			//----------------------------
			//Step 3 : spell check
			//----------------------------
			
			//normedStr = jspellChecker.getCorrectedLine(normedStr);
		//	System.out.println("Spell Corrected : " + normedStr);
			 String[] translated = null;
			//----------------------------
			//Step 4 : Translate
			//----------------------------
			String translatedStrHi = translate(normedStr, "HOST", "PORT");
			String translatedStrGu = translate(normedStr, "HOST", "PORT");
			String translatedStrMr = translate(normedStr, "HOST", "PORT");
			String translatedStrPa = translate(normedStr, "HOST", "PORT");
			String translatedStrMa = translate(normedStr, "HOST", "PORT");
		//	System.out.println("Translated : " + translatedStr);
			
			//----------------------------
			//Step 5 : Transliterate
			//----------------------------
			try {
				translatedStrHi = Transliterate.doTransliteration(translatedStrHi, "hi");
				translatedStrGu = Transliterate.doTransliteration(translatedStrGu, "gu");
				translatedStrMr = Transliterate.doTransliteration(translatedStrMr, "mr");
				translatedStrPa = Transliterate.doTransliteration(translatedStrPa, "pa");
				translatedStrMa = Transliterate.doTransliteration(translatedStrMa, "ma");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			translated = new String[] {translatedStrHi, translatedStrGu, translatedStrMr, translatedStrPa, translatedStrMa};
		//	System.out.println("Transliterated : " + translatedStr);
			
			//Now read the score from the file created in the home directory
			//double score = readScore();
			//System.out.println(Double.toString(score) + "##" + normedStr + "##" + translatedStr.toString());
			return translated;
		
		} catch(IOException ioe) {
			System.out.println(ioe.toString());
			return null;
		}
		
		
	}
	
	private String decompress(String str) {
		Compressor c = new Compressor(dict);
		return c.compressSentence(str);
		
	}
	private String normalize(String str) throws IOException {
		String[] normCmd = {
				"/bin/sh",
				"-c",
				"echo \"" + str + "\"|nc " + NORMALIZER_HOST + " " + NORMALIZER_PORT
				};
		
		String line = null;
		
		StringBuilder normed = new StringBuilder();
		Process normProcess = Runtime.getRuntime().exec(normCmd);
		BufferedReader normInput =
		    new BufferedReader(new InputStreamReader(normProcess.getInputStream()));
		
		while((line = normInput.readLine()) != null){
		   // System.out.println("Command results : " + line);
			normed.append(line);
			normed.append('\n');
		}
		
		String normedStr = replacePipes(normed.toString());
		return normedStr;
		
	}
	
	private String translate(String normedStr, String TRANSLATOR_HOST, String TRANSLATOR_PORT) throws IOException {
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
		return translatedString;
	}
	private double readScore() throws IOException {
		double score = -Double.MAX_VALUE;
		String scoreLine = readFileToString(this.SCORE_FILE);
		//System.out.println("Score Line : " + scoreLine);
		StringBuilder scoreVal = new StringBuilder();
		for(int i = scoreLine.length() - 1; scoreLine.charAt(i) != '-'; i--) {
			scoreVal.append(scoreLine.charAt(i));
		}
		scoreVal = scoreVal.reverse();
		
		score = Double.parseDouble(scoreVal.toString());
		return -score;
	}
	private String replacePipes(String str) {
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
	
	private String readFileToString(String fileName) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
}
	

