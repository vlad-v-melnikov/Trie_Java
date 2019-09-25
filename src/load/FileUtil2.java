package load;

import java.io.*;
import java.util.*;

public class FileUtil2 {
	
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
		System.out.println("Loading file " + fileName);
		
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
		
		System.out.println("Writing to file " + fileName);
		outStr.print(s);
		outStr.close();
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
		s = deleteDouble(s, '\n');
		
		return s;
	}
	
	//delete <p> - for experimentation, need to delete all in-line paragraphs. In actual situation, will replace them back to \n once they are inside the String []
	public static String deleteP (String s) {
		s = s.replaceAll("<p>", "\n");
		return s;
	}
	
	public static String cleanNonABC (String s) {
		//lowercase everything
		s = s.toLowerCase();
		s = s.replaceAll("\\[http.*\\]", ""); //delete all web addresses in text
		s = s.replaceAll("\\(http.*\\)", "");
		s = s.replaceAll("/", " "); //to avoid useabuse later on
		
		//clean everything that is not \n or a letter
		StringBuilder aSB = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if ((c >= 'a' && c <= 'z') || c == '\n' || c == ' ') {
				aSB.append(c);
			}
		}
		s = aSB.toString();
		
		//delete double spaces
		s = deleteDouble (s, ' ');
		
		//replace '\n'+' ' to '\n'
		s = s.replace("\n ", "\n");
		
		return s;
	}
	
	public static String deleteDouble (String oldS, char toDelete) {
		String newS = oldS;
		do {
			oldS = newS;
			newS = oldS.replaceAll("  ", Character.toString(toDelete));
		} while (oldS.length() != newS.length());
		
		return newS.trim();
	}
	
	public static String stopWords (String fileName, String txt) {
		/**
		 * 1) loads the stop words from a file into a String,
		 * 2) identifies stops words there
		 * 3) deletes stop words from the processed String
		 */
		
		//PHASE 2: removes all stop words from received string
		String stopWs = load(fileName).trim();
		stopWs = stopWs.replaceAll(" ", "");
		
		//pad the string with " " in the beginning and the end, to eliminate border cases of word replacement. Trim after processing
		txt = " " + txt + " ";
				
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
			
			txt = txt.replaceAll(sWs, " ");
			txt = txt.replaceAll(sWn, "\n");
			txt = txt.replaceAll(nWs, "\n");
			
			left = right + 1;
			if (left < stopWs.length()-1) {	
				right = left + stopWs.substring(left, stopWs.length()).indexOf(',');
				
				if (right == left-1) {
					right = stopWs.length();
				}
			}
		} while (left < stopWs.length());
		
		return txt.trim();
	}
}

