import java.util.*;

public class TrieNode {
    TrieNode[] arr;
    boolean isEnd;
    ArrayList <Integer> paraList;
    
    public TrieNode() {
        arr = new TrieNode[26];
        paraList = new ArrayList <Integer> () ;
    }
}