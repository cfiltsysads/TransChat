package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;

import method.MosesTranslationMethod;

public class Test {
	static public HashSet<String> dict;
	public Test() throws IOException
	{
		
		//String filePath =Thread.currentThread().getContextClassLoader().getResource("data/words.txt").getFile();
		File file = new File("/home/diptesh/workspace/WebMobileGroupChatServer/data/words.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String temp;
		dict = new HashSet<String>();
		while((temp = br.readLine()) != null) {
			//System.out.println(temp);
			dict.add(temp.toLowerCase());
		}
	}
	public static void main(String[] args) throws UnknownHostException, IOException {
		Test t = new Test();
		MosesTranslationMethod tmMethod = new MosesTranslationMethod(dict);
		System.out.println(tmMethod.process("I am a cool kid"));
	}
}
