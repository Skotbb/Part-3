/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author Scott
 */
public class SearchByLyricsWords {

    private SongCollection songs;
    private String commonWordsString = "the of and a to in is you that it he for was on"
            + " are as with his they at be this from I have or"
            + " by one had not but what all were when we there"
            + " can an your which their if do will each how them"
            + " then she many some so these would into has more"
            + " her two him see could no make than been its now"
            + " my made did get our me too";
    private TreeSet<String> commonWordSet;
    private TreeSet<Song> songSet;

    private TreeMap<String, TreeSet<Song>> lyricSet
            = new TreeMap<String, TreeSet<Song>>();

    private TreeSet<Song> resultSet = new TreeSet<Song>();

    public SearchByLyricsWords(SongCollection songs) {
        this.songs = songs;
        Song[] sc = songs.getAllSongs();

        commonWordSet = new TreeSet();
        songSet = new TreeSet<Song>();  //Set of songs for each word.

        String[] arr = commonWordsString.split(" ");  //Separate all words in string
        commonWordSet.addAll(Arrays.asList(arr));   //Add all words separately to a set.


        for (int i = 0; i < sc.length; i++) {   //For each song in the list
            String[] lyricString = sc[i].getLyrics().split("[^a-zA-Z]+");

            for (int j = 0; j < lyricString.length; j++) {    //For each word in lyrics
                String temp = lyricString[j].toLowerCase();
                
                //Compare that word against all common words

                if (!commonWordSet.contains(temp) && temp.length() > 1) {
                    if (lyricSet.containsKey(temp)) { //If set contains word
                        //Set to existing songSet for key
                        songSet = lyricSet.get(temp);
                        //Add song to set
                        songSet.add(sc[i]);
                        //System.out.println("Set exists. Adding " + sc[i].getTitle()
                        //        + " to " + temp);
                    } else {   //If word doesn't exist in set
                        //Add song to set
                        songSet = new TreeSet<Song>(); //Create new set.
                        songSet.add(sc[i]);
                        //Create key and songset in lyricSet
                        lyricSet.put(temp, songSet);
                        //System.out.println("Adding " + temp + ", "
                        //        + sc[i].getTitle() + " to lyricSet.");
                    }
                }
            }
        }
    }

    public Song[] search(String lyricsWords) {
        ArrayList<String> wordsToCompare = new ArrayList<String>();
        String[] lyricsSearch = lyricsWords.split("[^a-zA-Z]+"); //Split String into separate words
        Song[] results = null;
        for (int i = 0; i < lyricsSearch.length; i++) { //for each word in string
            String temp = lyricsSearch[i].toLowerCase();
            if (!commonWordSet.contains(temp) && temp.length() > 1) { //Check each word is more than
                                                                    //one letter and not common
                if (lyricSet.containsKey(temp)) {
                    wordsToCompare.add(temp);
                }
//                if (lyricSet.containsKey(temp)) {
//                    if(resultSet.isEmpty()){
//                        resultSet.addAll(lyricSet.get(temp));
//                    }else{
//                    resultSet.retainAll(lyricSet.get(temp));
//                    }
            }
        }
        if (wordsToCompare.size() > 2) { //start list by comparing first and second songSets
            resultSet.clear();
            
            TreeSet<Song> intersect = new TreeSet<Song>(lyricSet.get(wordsToCompare.get(0)));
            intersect.retainAll((lyricSet.get(wordsToCompare.get(1))));
            resultSet.addAll(intersect);
            for(int i = 2; i<wordsToCompare.size(); i++){ //Compare remaining lists against resultList
                resultSet.retainAll(lyricSet.get(wordsToCompare.get(i)));
            }
            
            results = new Song[resultSet.size()];
            return resultSet.toArray(results);
        } else if (wordsToCompare.size() == 2) {
            resultSet.clear();
            
            TreeSet<Song> intersect = new TreeSet<Song>(lyricSet.get(wordsToCompare.get(0)));
            intersect.retainAll((lyricSet.get(wordsToCompare.get(1))));
            resultSet.addAll(intersect);
            
            results = new Song[resultSet.size()];
            return resultSet.toArray(results);
        }else if (wordsToCompare.size() == 1) {
            resultSet.clear();
            resultSet.addAll(lyricSet.get(wordsToCompare.get(0)));
            
            results = new Song[resultSet.size()];
            return resultSet.toArray(results);
        }else if (wordsToCompare.isEmpty()) {
            resultSet.clear();
        }

        results = new Song[resultSet.size()];
        return results;
    
    }

    public void statistics() {
        int references = 0,
                charsFromKeys = 0,
                charsFromEntries = 0,
                keyBytes = 0,
                entryBytes = 0;

        for (Map.Entry<String, TreeSet<Song>> entry : lyricSet.entrySet()) {
            //Add number of songs to references
            references += entry.getValue().size();
        }
        for (Map.Entry<String, TreeSet<Song>> entry : lyricSet.entrySet()) {
            //Determine the number of characters in all the keys.
            charsFromKeys += entry.getKey().length();
        }
        for (Map.Entry<String, TreeSet<Song>> entry : lyricSet.entrySet()) {
            //Determine the number of characters in all the references.
            Iterator itr = entry.getValue().iterator();
            //Iterate through each song and count the characters in its title
            while (itr.hasNext()) {
                Song current = (Song) itr.next();
                charsFromEntries += current.getTitle().length();
            }
        }
        keyBytes = 8 * ((charsFromKeys * 2 + 45) / 8);
        entryBytes = 8 * ((charsFromEntries * 2 + 45) / 8);

        System.out.println("# of keys: " + lyricSet.size());
        System.out.println("References: " + references);
        System.out.println("Bytes used by keys: " + keyBytes);
        System.out.println("Bytes used by titles in references: " + entryBytes);

    }

    public void top10Words() {
//        TreeMap<Integer, TreeMap<String, Integer>> topTen =
//                new TreeMap<Integer, TreeMap<String, Integer>>();
        TreeMap<Integer, String> tempMap = new TreeMap<>();
        int entries;

        for (Map.Entry<String, TreeSet<Song>> current : lyricSet.entrySet()) {
            entries = current.getValue().size();
            //System.out.println("Entries:" + entries + " word: "+ current.getKey());
            tempMap.put(entries, current.getKey());
        }

        NavigableSet keySet = tempMap.descendingKeySet();
        Iterator itr = keySet.iterator();
        int i = 0;
        System.out.println("\nTop ten words:");
        while (itr.hasNext() && i < 10) {
            int key = (int) itr.next();
            System.out.println(key + " entries of " + tempMap.get(key));
            i++;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        SongCollection sc = new SongCollection("allSongs.txt");
        SearchByLyricsWords search = new SearchByLyricsWords(sc);

        Song[] results;
        String[] searches = {"We don't need no education", "she loves my car",
        "blue jeans", "little", "notaword"};

        search.statistics();
        //search.top10Words();
        //results = search.search("she loves my car");
        for(int i=0; i < searches.length; i++){
            
            results = search.search(searches[i]);
            System.out.println();
            System.out.println("Number of results: " + results.length +
                    "\nFor: " + searches[i]);
            if(results.length < 10){
                for(int j=0; j<results.length;j++){
                    System.out.println(results[j].getArtist() +"-> "+ results[j].getTitle());
                }
            }else{
                for(int j=0; j<10;j++){
                    System.out.println(results[j].getArtist() +"-> "+ results[j].getTitle());
                }
            }
        }
    }
}
