
public class Parser {
	
	/*PARSING*/
	
	//1. receives a String [], gets Strings from it and calls parse method on the extracted String and the Trie 
	public static void parsePara (String [] arrS, Trie aTrie) {
		System.out.println("Creating trie...");
		for (int i = 0; i < arrS.length; i++) {
			parse (arrS[i], aTrie, i);
		}
		System.out.println("Trie created...");
	}
	
	//2. parses words from a string into the trie. Words are divided with " ".
	public static void parse (String s, Trie aTrie, int paraNo) {
		int left, right;
		left = 0;
		right = s.indexOf(" ");
		if (right == -1)
			right = s.length();
				
		do {
			String word = s.substring(left, right);
			aTrie.insert(word, paraNo);
			
			left = right + 1;
			if (right < s.length())
			{
				int temp = s.substring(left, s.length()).indexOf(" ");
				if (temp != -1) {
					right = left + temp;
				} else {
					right = s.length();
				}
			}
		} while (left < s.length());
	}
	
	/*END PARSING*/
	
	//paragraph counter in a string
	public static int countPara (String s) {
		int count = 1; //there is always one paragraph, even if there are no paragraph characters in the string, so we start with 1
		char [] cArr = s.toCharArray();
		for (char c : cArr) {
			if (c == '\n')
				count++;
		}
		return count;
	}
	
	//divides Strings into paragraphs and packs them into String [] txtArr
	public static void packToParaArr (String [] txtArr, String s) {
		int left, right;
		left = 0;
		right = s.indexOf("\n");
		if (right == -1)
			right = s.length();
		
		for (int i = 0; i < txtArr.length; i++) {
			String word = s.substring(left, right);
			txtArr [i] = word;
			
			left = right + 1;
			if (right < s.length())
			{
				int temp = s.substring(left, s.length()).indexOf("\n");
				if (temp != -1) {
					right = left + temp;
				} else {
					right = s.length();
				}
			}
		} while (left < s.length());
	}
}
