package experiments;

import java.util.*;

public class expirMain {
	
	public static void main (String [] args) {
		String s = "http://en.wikipedia.org/wiki/Computer " ;
		/*String newS = oldS;
		
		do {
			oldS = newS;
			newS = oldS.replaceAll("  ", " ");
		} while (oldS.length() != newS.length());*/
		
		//put web addresses into ()
		s = s.replaceAll("http:", "(http:");
		int cursor = s.indexOf("http:") + s.substring(s.indexOf("http:")).indexOf(" ");
		s = s.substring(0, cursor) + ")" + s.substring(cursor, s.length());
		
		 
		
		//s = s.replaceAll("\\(http.*\\)", "");
		
		System.out.println(s);
	}

}
