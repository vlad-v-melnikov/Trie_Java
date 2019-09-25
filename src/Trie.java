import java.util.*;

public class Trie {
	private TrieNode root;
	 
    public Trie() {
        root = new TrieNode();
    }
 
    // adds a word into the trie
    public void insert (String word, int paraNo) {
        TrieNode p = root; // trie node iterator
        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            int index = c - 'a';
            if(p.arr[index] == null) {//no word yet with this letter, so a new trie node is created
                TrieNode temp = new TrieNode();
                p.arr[index] = temp;
                p = temp; //move down one level
            } else {
                p = p.arr[index]; //move down one level
            }
        }
        p.isEnd = true; //we made it to the end of the word
        p.paraList.add(paraNo);
    }
 
    // Returns if the word is in the trie
    public ArrayList<Integer> search (String word) {
        TrieNode p = searchNode(word);
        if(p == null){
            return null;
        } else {
            if(p.isEnd)
                return p.paraList;
        }
        return null;
    }
 
    public TrieNode searchNode(String s) {
        TrieNode p = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int index = c -'a';
            if (p.arr[index] != null){
                p = p.arr[index]; //move down the found letter path
            } else {
                return null; //nowhere to move, so no word is found
            }
        }
        if(p == root)
            return null; //we never moved anywhere

        return p;
    }
    
    //wrapper function for recursive node display
    public void display () {
    	displayNode (root);
    	System.out.print("\n\n");
    }
    
    public void displayNode (TrieNode p) {
    	for (int i = 0; i < p.arr.length; i++) {
    		if (p.arr[i] != null) {
    			char c = (char)i;
    			c += 'a';
    			System.out.print(c);
    			
    			if (!p.isEnd || p == root) {
    				displayNode (p.arr[i]);
    				System.out.print("-");
    			}
    		}
    	}
    }
    
    //function for search - gives key words and provides output of the paragraphs with this word
    public void userSearch (String [] inputArr) {
    	String searchResExport = "";
    	String stopWordFile = "some-noise-words.txt";
		
		//search dialogue loop
		Scanner kb = new Scanner (System.in);
		int choice = 0;
		
		System.out.println("You have the following search options:\n"
				+ "1) keyword1\n"
				+ "2) keyword1 AND keyword2\n"
				+ "3) keyword1 OR keyword2\n"
				+ "4) keyword1 AND (keyword2 OR keyword3)\n"
				+ "5) keyword1 OR (keyword2 AND keyword3)\n"
				+ "0) exit search");
		boolean flag = false;
		do {
			try {
				choice = kb.nextInt();
				kb.nextLine();
				flag = true;
			} catch (InputMismatchException e) {
				System.out.println("Integers only, please.");
				kb.nextLine();
			}
		} while (!flag);
			
		if (choice == 1) {
			String w1;
			System.out.println("Please enter the KEYWORD1 (lower case, a-z only):");
			w1 = kb.nextLine();
			searchResExport = searchW1(w1, inputArr);
			//stop word check
			if (FileUtil.checkStopWord(w1, stopWordFile)) {
				searchResExport += w1 + " is a noise word.\n";
			}
		} 
		else if (choice == 2 || choice == 3) {
			String w1, w2;
			System.out.println("Please enter the KEYWORD1  (lower case, a-z only):");
			w1 = kb.nextLine();
			
			System.out.println("Please enter the KEYWORD2  (lower case, a-z only):");
			w2 = kb.nextLine();
			if (choice == 2) {
				searchResExport = searchW1andW2 (w1, w2, inputArr);
			} else {
				searchResExport = searchW1orW2 (w1, w2, inputArr);
			}
			//stop word check
			if (FileUtil.checkStopWord(w1, stopWordFile)) {
				searchResExport += w1 + " is a noise word.\n";
			}
			if (FileUtil.checkStopWord(w2, stopWordFile)) {
				searchResExport += w2 + " is a noise word.\n";
			}
		} else if (choice == 4 || choice == 5) {
			String w1, w2, w3;
			System.out.println("Please enter the KEYWORD1  (lower case, a-z only):");
			w1 = kb.nextLine();
			
			System.out.println("Please enter the KEYWORD2  (lower case, a-z only):");
			w2 = kb.nextLine();
			
			System.out.println("Please enter the KEYWORD3  (lower case, a-z only):");
			w3 = kb.nextLine();
			
			if (choice == 4 ) {
				searchResExport = searchW1andPW2orW3P (w1, w2, w3, inputArr);
			} else {
				searchResExport = searchW1orPW2andW3P (w1, w2, w3, inputArr);
			}
			
			//stop word check
			if (FileUtil.checkStopWord(w1, stopWordFile)) {
				searchResExport += w1 + " is a noise word.\n";
			}
			if (FileUtil.checkStopWord(w2, stopWordFile)) {
				searchResExport += w2 + " is a noise word.\n";
			}
			if (FileUtil.checkStopWord(w3, stopWordFile)) {
				searchResExport += w3 + " is a noise word.\n";
			}
		}
		
		//displaying search result
		System.out.println(searchResExport);
		
		//saving search result
		FileUtil.save(searchResExport, "SearchResult.txt");
		
		System.out.println("THANK YOU!");
    }
    
    public String searchW1 (String w1, String [] inputArr) {
    	ArrayList <Integer> paraList;
    	String searchResExport = "";
    	
		paraList = this.search(w1);
		if (paraList != null) {
			searchResExport = "\n" + w1 + "" + " found in paragraph(s): \n";
			
			//delete double entries from paralist by copying it into a set and back
			Set <Integer> temp = new HashSet <>();
			temp.addAll(paraList);
			paraList.clear();
			paraList.addAll(temp);
			Collections.sort(paraList);
			
			//display paragraph numbers
			for (int i = 0; i < paraList.size(); i++) {
				searchResExport += paraList.get(i);
				if (i < paraList.size()-1) {
					searchResExport += (", ");
				}
			}
			searchResExport += "\nParagraph(s) that contain " + w1 + ":\n";
			for (Integer i : paraList) {
				searchResExport += i + "\n";
				searchResExport += inputArr[i]+ "\n\n";
			}
		} else {
			searchResExport += w1 + " NOT found\n";
		}
		return searchResExport;
    }
    
    public String searchW1andW2 (String w1, String w2, String [] inputArr) {
    	ArrayList <Integer> paraList1;
    	ArrayList <Integer> paraList2;
    	ArrayList <Integer> matchList = new ArrayList <Integer> ();
    	String searchResExport = "";
    	
		paraList1 = this.search(w1);
		paraList2 = this.search(w2);
		
		//one of the words is not fond
		if (paraList1 == null) {
			searchResExport += w1 + " NOT found\n";
			return searchResExport;
		}
		if (paraList2 == null) {
			searchResExport += w2 + " NOT found\n";
			return searchResExport;
		}
		
		//both words found:
		
		//delete double entries from paralists and sort them:
		cleanParaList(paraList1);
		cleanParaList(paraList2);
		
		//determine if there are matches between two paragraph lists
		for (int i = 0; i < paraList1.size(); i++) {
			for (int j = 0; j < paraList2.size(); j++) {
				if (paraList1.get(i) == paraList2.get(j)) {
					matchList.add(paraList1.get(i));
				}
			}
		}
		//if there are no matches - we quit
		if (matchList.isEmpty()) {
			searchResExport += "There are NO paragraphs that contain both " + w1 + " and " + w2 + "\n";
			return searchResExport;
		}
		//if there are matches - we display the result
		
		searchResExport += "\n" + w1 + "" + " and " + w2 + " found in paragraph(s): \n";
		
		//display paragraph numbers
		for (int i = 0; i < matchList.size(); i++) {
			searchResExport += matchList.get(i);
			if (i < matchList.size()-1) {
				searchResExport += (", ");
			}
		}
		searchResExport += "\nParagraph(s) that contain " + w1 + " and " + w2 + ":\n";
		for (Integer i : matchList) {
			searchResExport += i + "\n";
			searchResExport += inputArr[i]+ "\n\n";
		}
		
		return searchResExport;
    }
    
    public String searchW1orW2 (String w1, String w2, String [] inputArr) {
    	
    	ArrayList <Integer> paraList1;
    	ArrayList <Integer> paraList2;
    	ArrayList <Integer> mergeList = new ArrayList <Integer> ();
    	String searchResExport = "";
    	
		paraList1 = this.search(w1);
		paraList2 = this.search(w2);
		
		//one of the words is not found
		if (paraList1 == null && paraList2 == null) {
			searchResExport += "NONE of the words was found\n";
			return searchResExport;
		}
		if (paraList1 == null) {
			searchResExport += w1 + " NOT found\n";
			searchResExport += "Showing results only for " + w2 + "\n";
			searchResExport += this.searchW1(w2, inputArr);
			return searchResExport;
		}
		if (paraList2 == null) {
			searchResExport += w2 + " NOT found\n";
			searchResExport += "Showing results only for " + w1 + "\n";
			searchResExport += this.searchW1(w1, inputArr);
			return searchResExport;
		}
		
		//both words found:
		//delete double entries from paralists and sort them:
		cleanParaList(paraList1);
		cleanParaList(paraList2);
		
		//merge the two paralists, delete doubles and sort:
		mergeList.addAll(paraList1);
		mergeList.addAll(paraList2);
		cleanParaList(mergeList);
		
		//display the result
		searchResExport += "\n" + w1 + "" + " or " + w2 + " found in paragraph(s): \n";
		
		//display paragraph numbers
		for (int i = 0; i < mergeList.size(); i++) {
			searchResExport += mergeList.get(i);
			if (i < mergeList.size()-1) {
				searchResExport += (", ");
			}
		}
		searchResExport += "\nParagraph(s) that contain " + w1 + " or " + w2 + ":\n";
		for (Integer i : mergeList) {
			searchResExport += i + "\n";
			searchResExport += inputArr[i]+ "\n\n";
		}	
		return searchResExport;
    }
    
    public String searchW1andPW2orW3P (String w1, String w2, String w3, String [] inputArr) {
    	String searchResExport = "";
    	ArrayList <Integer> paraList1 = this.search(w1);
    	ArrayList <Integer> paraList2 = this.search(w2);
    	ArrayList <Integer> paraList3 = this.search(w3);
    	ArrayList <Integer> mergeList = new ArrayList <Integer> ();
    	ArrayList <Integer> matchList = new ArrayList <Integer> ();
    	
    	//none of the 3 words found
    	if (paraList1 == null && paraList2 == null && paraList3 == null) {
			searchResExport += "NONE of the words was found\n";
			return searchResExport;
    	}
    	//w1 not found - query failed 
    	if (paraList1 == null) {
			searchResExport += w1 + " NOT found. \n";
			return searchResExport;
		}
    	//w1 and w2 not found - query failed
    	if (paraList2 == null && paraList3 == null) {
			searchResExport += w2 + " and " + w3 + " NOT found. \n";
			return searchResExport;
		}
    	//w1 is found, but either w2 or w3 is not found
    	if (paraList2 == null || paraList3 == null) {
    		//do query for w1 && w2
    		if (paraList3 == null) {
    			searchResExport += w3 + " NOT found. Searching " + w1 + " and " + w2 + "\n";
    			searchResExport += searchW1andW2 (w1, w2, inputArr);
    			return searchResExport;
    		} else {//query for w1 && w3
    			searchResExport += w2 + " NOT found. Searching " + w1 + " and " + w3 + "\n";
    			searchResExport += searchW1andW2 (w1, w3, inputArr);
    			return searchResExport;
    		}
    	}
    	// all are found
    	
    	//delete double entries from paralists and sort them:
		cleanParaList(paraList1);
		cleanParaList(paraList2);
		cleanParaList(paraList3);
		
		//merge "w2 or w3" results into one list
		mergeList.addAll(paraList2);
		mergeList.addAll(paraList3);
		cleanParaList(mergeList);
		
		//match merge results and w1
		for (int i = 0; i < paraList1.size(); i++) {
			for (int j = 0; j < mergeList.size(); j++) {
				if (paraList1.get(i) == mergeList.get(j)) {
					matchList.add(paraList1.get(i));
				}
			}
		}
		
		//if there are no matches - we quit
		if (matchList.isEmpty()) {
			searchResExport += "\nThere are NO paragraphs that contain " + w1 + " and (" + w2 + " or " + w3 + ")";
			return searchResExport;
		}
		//if there are matches - we display the result
		searchResExport += w1 + " and (" + w2 + " or " + w3 + ") found in paragraph(s): \n";
		
		//display paragraph numbers
		for (int i = 0; i < matchList.size(); i++) {
			searchResExport += matchList.get(i);
			if (i < matchList.size()-1) {
				searchResExport += (", ");
			}
		}
		searchResExport += "\nParagraph(s) that contain " + w1 + " and (" + w2 + " or " + w3 + "):\n";
		for (Integer i : matchList) {
			searchResExport += i + "\n";
			searchResExport += inputArr[i]+ "\n\n";
		}
    	
    	return searchResExport;
    }
    
    public String searchW1orPW2andW3P (String w1, String w2, String w3, String [] inputArr) {
    	String searchResExport = "";
    	ArrayList <Integer> paraList1 = this.search(w1);
    	ArrayList <Integer> paraList2 = this.search(w2);
    	ArrayList <Integer> paraList3 = this.search(w3);
    	ArrayList <Integer> mergeList = new ArrayList <Integer> ();
    	ArrayList <Integer> matchList = new ArrayList <Integer> ();
    	
    	//none of the 3 words found
    	if (paraList1 == null && paraList2 == null && paraList3 == null) {
			searchResExport += "NONE of the words was found\n";
			return searchResExport;
    	}
    	//w1 not found - searching w2 && w3:
    	if (paraList1 == null) {
    		searchResExport += w1 + " NOT found. Searching (" + w2 + " and " + w3 + ").\n";
    		searchResExport +=  searchW1andW2(w2, w3, inputArr);
    		return searchResExport;
    	}
    	//either w2 or w3 not found - searching w1
    	if (paraList2 == null || paraList3 == null) {
    		searchResExport += "Either " + w2 + " or " + w3 + " NOT found. Seaching " + w1 + "\n";
    		searchResExport += this.searchW1(w1, inputArr);
    		return searchResExport;
    	}
    	//all three are found:
    	cleanParaList(paraList1);
		cleanParaList(paraList2);
		cleanParaList(paraList3);
		//match results for w2 && w3
		for (int i = 0; i < paraList2.size(); i++) {
			for (int j = 0; j < paraList3.size(); j++) {
				if (paraList2.get(i) == paraList3.get(j)) {
					matchList.add(paraList2.get(i));
				}
			}
		}
		cleanParaList(matchList);
		//merge results for (w2 && w3) and w1
		mergeList.addAll(paraList1);
		mergeList.addAll(matchList);
		cleanParaList(mergeList);
    	
		//if there are matches - we display the result
		searchResExport += w1 + " or (" + w2 + " and " + w3 + ") found in paragraph(s): \n";
		
		//display paragraph numbers
		for (int i = 0; i < mergeList.size(); i++) {
			searchResExport += mergeList.get(i);
			if (i < mergeList.size()-1) {
				searchResExport += (", ");
			}
		}
		searchResExport += "\nParagraph(s) that contain " + w1 + " and (" + w2 + " or " + w3 + "):\n";
		for (Integer i : mergeList) {
			searchResExport += i + "\n";
			searchResExport += inputArr[i]+ "\n\n";
		}
		
    	return searchResExport;
    }
    
    public void cleanParaList (ArrayList <Integer> paraList) {
    	Set <Integer> temp = new HashSet <>();
		temp.addAll(paraList);
		paraList.clear();
		paraList.addAll(temp);
		Collections.sort(paraList);
    }
}