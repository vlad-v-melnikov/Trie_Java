public class TrieMain {

	public static void main(String[] args) {	
		
		String inputS = FileUtil.load("The_State_of_Data_Final.txt");
		
		//delete in-line paragraphs and double paragraphs
		inputS = FileUtil.inTextPara(inputS);
		
		//count paragraphs and transform the result into a String []
		int paraNo = Parser.countPara(inputS);
		System.out.println("No of paragraphs: " + paraNo + "\n");
		
		String [] inputArr = new String [paraNo];
		Parser.packToParaArr (inputArr, inputS);
		
		//make an array for processing, to keep the input clean for demonstration later
		String [] procArr = new String [paraNo];
		for (int i = 0; i < procArr.length; i++) {
			procArr[i] = inputArr[i];
		}
		
		//revert input String[] to show \n instead of <p>
		FileUtil.deleteP(inputArr);
		
		//clean everything that is not a letter, a ' ' or a '\n'
		FileUtil.cleanNonABC(procArr);
		//FileUtil.save(procArr, "02_ABCOnlyProcArr.txt");
		
		//delete stop words
		FileUtil.stopWords("some-noise-words.txt", procArr);
		//FileUtil.save(procArr, "03_NoStopWordsProcArr.txt");
		
		//parse the text into tries
		Trie aTrie = new Trie();
		
		//read the string and print it. Words divided by one space
		Parser.parsePara(procArr, aTrie);
		aTrie.display();
		
		//search
		aTrie.userSearch(inputArr);
	}
}
