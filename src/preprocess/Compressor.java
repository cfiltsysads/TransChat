//sg
package preprocess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Compressor {
	// Deletes occurrences which happen more than twice
	ArrayList<Integer> repeatPosition; // positions where the character is
										// occurring twice

	ArrayList<Integer> vowRepeatPosition; // positions where the character is
	ArrayList<Integer> consRepeatPosition; // positions where the character is

	HashSet<String> dict;
	String currentString;

	public Compressor(HashSet<String> dict) {
		repeatPosition = new ArrayList<Integer>();
		vowRepeatPosition = new ArrayList<Integer>();
		consRepeatPosition = new ArrayList<Integer>();
		this.dict = dict;
	}

	private void refresh() {
		//System.out.println("Here");
		repeatPosition.clear();
		vowRepeatPosition.clear();
		consRepeatPosition.clear();
	}

	String compress(String w) {
		this.currentString = w;
		refresh();
		deleteMoreThanTwo();
		String res = findFirstMatch(currentString, 0, 0);
		if (res == null) {
			this.currentString = w;
			deleteMoreThanTwo();
			return this.currentString;
		} else {
			return res;
		}
	}

	public String compressSentence(String sentence) {
		StringBuilder sbr = new StringBuilder();
		for (String s : sentence.split(" ")) {
			String temp = compress(s);
			if (temp == null) {
				sbr.append(s);
			} else {
				sbr.append(temp);
			}
			sbr.append(" ");
		}
		return sbr.toString();
	}

	boolean isVowel(char c) {

		return (c == 'a' || c == 'e' || c == 'o' || c == 'i' || c == 'u');
	}

	void deleteMoreThanTwo() {
		String s = currentString;
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			res.append(s.charAt(i));
			if (i < (s.length() - 1) && s.charAt(i) == s.charAt(i + 1)) {

				res.append(s.charAt(i + 1));
				int j = i + 2;
				while (j < s.length() && s.charAt(j) == s.charAt(i)) {
					j++;
				}
				i = j - 1;
			}
		}
		currentString = res.toString();
		for (int i = 0; i < currentString.length() - 1; i++) {
			if (currentString.charAt(i) == currentString.charAt(i + 1)) {
				if (isVowel(currentString.charAt(i))) {
					vowRepeatPosition.add(i);
				} else {
					consRepeatPosition.add(i);
				}

				i++;
			}

		}

		repeatPosition.addAll(vowRepeatPosition);
		repeatPosition.addAll(consRepeatPosition);

	//	System.out.println(repeatPosition);
	}

	String findFirstMatch(String s, int currPos, int adj) {
	//	System.out.println(s + ", " + currPos + ", " + adj);
		if (dict.contains(s)) {
			return s;
		}
		if (currPos == repeatPosition.size()) {
			return null;
		}
		String res = findFirstMatch(s, currPos + 1, adj);
		if (res != null) {
			return res;
		}
		int repeatsAt = 0;
		int originalRepPos = repeatPosition.get(currPos);
		if(originalRepPos > adj) {
			repeatsAt = originalRepPos - adj;
		} else {
			repeatsAt = originalRepPos;
		}
		String temp;
		if (repeatsAt == s.length() - 2) { // second last char
			temp = s.substring(0, s.length() - 1);
		} else {
			temp = s.substring(0, repeatsAt + 1) + s.substring(repeatsAt + 2, s.length());
		}
		res = findFirstMatch(temp, currPos + 1, adj + 1);
		return res;
	}

	public static void main(String args[]) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("data/words.txt"));
		String temp;
		HashSet<String> dict = new HashSet<String>();
		while ((temp = br.readLine()) != null) {
			// System.out.println(temp);
			dict.add(temp.toLowerCase());
		}
		Compressor c = new Compressor(dict);
		System.out.println(c.compressSentence("help meeeee plllllllleeeeaaaassseee"));
	}
}