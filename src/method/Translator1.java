package method;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.concurrent.Callable;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class Translator1 implements Callable<String>{
	String normedStr; String TRANSLATOR_HOST; String TRANSLATOR_PORT; String lang;
	
	public Translator1(String normedStr, String tRANSLATOR_HOST,
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
		
		String finalString="";
  	  String sent=normedStr;
  	  String port="10.144.22.105:13012";
			// Create an instance of XmlRpcClient
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL("http://"+port+"/RPC2"));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);
			// The XML-RPC data type used by mosesserver is <struct>. In Java, this data type can be represented using HashMap.
			HashMap<String,Object> mosesParams = new HashMap<String,Object>();
			String textToTranslate=sent;
			mosesParams.put("text", textToTranslate);
			mosesParams.put("align", "true");
			mosesParams.put("nbest",20);
          mosesParams.put("nbest-distinct","true");
			mosesParams.put("report-all-factors", "true");
			// The XmlRpcClient.execute method doesn't accept Hashmap (pParams). It's either Object[] or List. 
			Object[] params = new Object[] { null };
			params[0] = mosesParams;
			// Invoke the remote method "translate". The result is an Object, convert it to a HashMap.
			HashMap result = (HashMap)client.execute("translate", params);
			// Print the returned results
			String textTranslation = (String)result.get("text");
			System.out.println(textTranslation);
			String translatedString = textTranslation;
			String splitString[] = translatedString.split("[ \t]+");
			for(int i=0; i<splitString.length; i++)
			{
				String word=splitString[i];
				if(word.contains("|UNK"))
				{
					word=word.trim();
					word=word.replace("|UNK|UNK|UNK", "");
					word=getTransliterationFromGoogle(word.toString().trim(), "f");
					splitString[i]=word;
				}
			}
			finalString = String.join(" ", splitString);
			finalString = finalString.replace("|UNK|UNK|UNK", "");
			System.out.println(finalString);
			
		return finalString.toString().trim();

		
	}



}
