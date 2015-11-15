package preprocess;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Normalizer {

	HashMap<String, String> map;

	public Normalizer() throws IOException {

		String pathToDictionary = "data/SMS_Abbr.dat";
		intitializeSMSNormalizer(pathToDictionary);

	}

	public void intitializeSMSNormalizer(String pathToDictionary)
			throws IOException {
		Scanner scanner = new Scanner(new FileReader(pathToDictionary));

		map = new HashMap<String, String>();

		while (scanner.hasNextLine()) {
			String[] columns = scanner.nextLine().split("---");
			map.put(columns[0], columns[1]);
		}
		scanner.close();
	}

	public String normalize(String sentence) {
		String[] words = sentence.split(" ");
		StringBuilder normalizedSentence = new StringBuilder("");
		for (int i = 0; i < words.length; i++) {
			String normalizedWords = (String)map.get(words[i]);
			//System.out.println(words[i] + " -> " + normalizedWords);
			if (null == normalizedWords) {
				normalizedWords = words[i];
			}
			normalizedSentence.append(normalizedWords + " ");
		}
		return normalizedSentence.toString();

	}

}
