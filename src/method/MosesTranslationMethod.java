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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
			ExecutorService pool = Executors.newFixedThreadPool(10);
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
			System.out.println("#####"+normedStr+"######");

			//normedStr = jspellChecker.getCorrectedLine(normedStr);
		//	System.out.println("Spell Corrected : " + normedStr);
			 String[] translated = null;
			//----------------------------
			//Step 4 : Translate
			//----------------------------
			//Translator hi = new Translator(normedStr, "10.144.22.121", "1235");
			 
			Translator hi = new Translator(normedStr, "10.144.22.121", "1235");

			Translator1 gu = new Translator1(normedStr, "10.144.22.105", "13011");
			Translator1 mr = new Translator1(normedStr, "10.144.22.105", "13015");
			Translator1 pa = new Translator1(normedStr, "10.144.22.105", "13016");
			Translator1 ma = new Translator1(normedStr, "10.144.22.105", "13014");
			Future <String> translatedStrHi = pool.submit(hi);
			Future <String> translatedStrGu = pool.submit(gu);
			Future <String> translatedStrMr = pool.submit(mr);
			Future <String> translatedStrPa = pool.submit(pa);
			Future <String> translatedStrMa = pool.submit(ma);
			
		
			
			
		//	System.out.println("Translated : " + translatedStr);
			System.out.println("#####"+translatedStrHi+"######");
			//----------------------------
			//Step 5 : Transliterate
			//----------------------------
			
				
				//translatedStrHi = Transliterate.doTransliteration(translatedStrHi.get(), "hi");
				/*translatedStrGu = Transliterate.doTransliteration(translatedStrGu, "gu");
				translatedStrMr = Transliterate.doTransliteration(translatedStrMr, "mr");
				translatedStrPa = Transliterate.doTransliteration(translatedStrPa, "pa");
				translatedStrMa = Transliterate.doTransliteration(translatedStrMa, "ma");*/
				
			pool.shutdown();
			pool.awaitTermination(Long.MAX_VALUE , TimeUnit.NANOSECONDS);
			String shi = translatedStrHi.get();
			String sgu = translatedStrGu.get();
			String smr = translatedStrMr.get();
			String spa = translatedStrPa.get();
			String sma = translatedStrMa.get();
			System.out.println(shi);
			System.out.println(sgu);
			System.out.println(smr);
			System.out.println(spa);
			System.out.println(sma);
			
			
			
			translated = new String[] {shi, sgu, smr, spa, sma};
		//	System.out.println("Transliterated : " + translatedStr);
			
			//Now read the score from the file created in the home directory
			//double score = readScore();
			//System.out.println(Double.toString(score) + "##" + normedStr + "##" + translatedStr.toString());
			return translated;
		
		} catch(Exception ioe) {
			ioe.printStackTrace();
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
	

