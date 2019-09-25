
import java.io.*;
import java.util.*;

public class FileUtil {
	
	public static String load (String fileName) {
		Scanner fileRdr = null;

		try {
			fileRdr = new Scanner (new FileInputStream (fileName));
		} 
		catch (FileNotFoundException e) {
			System.out.println(fileName + " could not be opened. Make sure it is there.");
			System.exit(0);
		}
		
		//read the file into a string
		StringBuilder inputSB = new StringBuilder();
		while (fileRdr.hasNextLine()) {
			inputSB.append(fileRdr.nextLine() + "\n");
		}
		return inputSB.toString();
	}
	
	public static void save (String s, String fileName) {
		PrintWriter outStr = null;

		try {
			outStr = new PrintWriter (new FileOutputStream (fileName));
		} catch (FileNotFoundException e) {
			System.out.println("Error opening file to " + fileName);
			System.exit(0);
		}
		
		System.out.println("Writing to file " + fileName + "\n");
		outStr.print(s);
		outStr.close();
	}
	
	public static void save (String [] sArr, String fileName) {
		StringBuilder aSB = new StringBuilder();
		for (String s : sArr) {
			aSB.append(s);
			aSB.append("\n");
		}
		save (aSB.toString(), fileName);
	}
	
	//1) replaces in-text paragraphs to <p> tag; 2) replaces double paragraph signs with single
	public static String inTextPara (String s) {
		
		StringBuilder outSB = new StringBuilder();
		
		//in-text paragraphs -> <p>
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '\n' && i > 0 && i < s.length()-1) {
				char prev = s.charAt(i-1);
				char next = s.charAt(i+1);
				if (prev != '\n' && next != '\n') {
					outSB.append("<p>");
				} else {
					outSB.append(s.charAt(i));
				}
			} else {
				outSB.append(s.charAt(i));
			}
		}
		s = outSB.toString();
		
		//double paragraphs to single
		s = deleteDouble(s, "\n");
		
		return s.trim();
	}
	
	//delete <p> - replace back to \n once they are inside the String []
	public static void deleteP (String [] arrS) {
		for (int i = 0; i < arrS.length; i++) {
			arrS[i] = arrS[i].replaceAll("<p>", "\n");
		}
	}
	
	//wrapper for cleaning non-letters: lowercase everything, delete all web addresses in () or [], replace / for " ".
	public static void cleanNonABC (String [] arrS) {
		for (int i = 0; i < arrS.length; i++) {
			arrS[i] = cleanNonABC (arrS[i]);
		}
	}
	
	public static String cleanNonABC (String s) {
		//special cases
		s = s.replaceAll("U. S. of A.", "USA");
		s = s.replaceAll("U. S.", "US");
		
		//lowercase everything
		s = s.toLowerCase();
		
		//delete all web addresses in () or [], replace / for " ", change <p> to spaces
		s = s.replaceAll("\\[http.*\\]", "");
		s = s.replaceAll("\\(http.*\\)", "");
		s = s.replaceAll("/", " ");
		s = s.replaceAll("<p>", " ");
		
		//clean everything that is not \n or a letter or a space
		StringBuilder aSB = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if ((c >= 'a' && c <= 'z') || c == '\n' || c == ' ') {
				aSB.append(c);
			}
		}
		s = aSB.toString();
		
		//delete double spaces
		s = deleteDouble (s, " ");
		
		return s.trim();
	}
	
	public static String deleteDouble (String oldS, String toDelete) {
		String newS = oldS;
		String toDelete2 = toDelete + toDelete;
		
		do {
			oldS = newS;
			newS = oldS.replaceAll(toDelete2, toDelete);
		} while (oldS.length() != newS.length());
		
		return newS.trim();
	}
	
	public static void stopWords (String fileName, String [] txt) {
		/**
		 * 1) loads the stop words from a file into a String,
		 * 2) identifies stops words there
		 * 3) deletes stop words from the processed String
		 */
		
		String stopWs = load(fileName).trim();
		stopWs = stopWs.replaceAll(" ", ""); //separator is only a ", " instead of " "
		
		for (int i = 0; i < txt.length; i++) {
			//pad the string with " " in the beginning and the end, to eliminate border cases of word replacement. Trim after processing
			txt[i] = " " + txt[i] + " ";
			
			int left, right;
			left = 0;
			right = stopWs.indexOf(',');
			if (right == -1) {
				right = stopWs.length();
			}
			
			do {
				String word = stopWs.substring(left, right);
				
				//change for strings with no \n as in String [] - the only possible case is sWs, considering the padding
				String sWs = " " + word + " ";
				String sWn = " " + word + "\n";
				String nWs = "\n" + word + " ";
				
				txt[i] = txt[i].replaceAll(sWs, " ");
				txt[i] = txt[i].replaceAll(sWn, "\n");
				txt[i] = txt[i].replaceAll(nWs, "\n");
				
				left = right + 1;
				if (left < stopWs.length()-1) {	
					right = left + stopWs.substring(left, stopWs.length()).indexOf(',');
					
					if (right == left-1) {
						right = stopWs.length();
					}
				}
			} while (left < stopWs.length());
			txt[i] = txt[i].trim();
		}
	}
	
	public static boolean checkStopWord (String w, String fileName) {
		String stopWs = load(fileName).trim();
		boolean isStopW = stopWs.contains(w);
		return isStopW;
	}
}

