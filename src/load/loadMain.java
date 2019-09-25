package load;

public class loadMain {
	
	public static void main (String [] args) {
		String inputS = null;
		String procS = null;
		
		
		/*
		//TEST 1 - short string
		procS = "able forbidable\nable doable\n able\n";
		procS = FileUtil.stopWords("some-noise-words.txt", procS);
		
		//the ways for input and processed string are now partying
		
		System.out.print(procS);
		*/
		
		//TEST 2 - file
		
		inputS = FileUtil2.load("The_State_of_Data_Final.txt");
		
		//delete unnecessary paragraph signs
		inputS = FileUtil2.inTextPara(inputS);
		FileUtil2.save(inputS, "01_outP.txt");
		
		//the ways for input and processed string are now partying
		procS = FileUtil2.deleteP(inputS);
		FileUtil2.save(procS, "02_outNoP.txt");
		
		//cleans all non-letters to enable entry into tries
		procS = FileUtil2.cleanNonABC(procS);
		FileUtil2.save(procS, "03_outAllABC.txt");
		
		//clean all stop words
		procS = FileUtil2.stopWords("some-noise-words.txt", procS);
		FileUtil2.save(procS, "04_outNoStopWords.txt");
		
		//TEST 3 - methods
	}
	
	
	

}
