/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author Scott
 */
public class SearchByLyricWords {

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

    public SearchByLyricWords(SongCollection songs) {
        this.songs = songs;
        Song[] sc = songs.getAllSongs();

        commonWordSet = new TreeSet();
        songSet = new TreeSet<Song>();  //Set of songs for each word.

        String[] arr = commonWordsString.split(" ");  //Separate all words in string
        commonWordSet.addAll(Arrays.asList(arr));   //Add all words separately to a set.

//        Iterator itr = commonSet.iterator();
//        while(itr.hasNext()){
//            System.out.println(itr.next());
//        }
        for (int i = 0; i < sc.length - 1; i++) {   //For each song in the list
            String[] lyricString = sc[i].getLyrics().split("[^a-zA-Z]+");

            for (int j = 0; j < lyricString.length - 1; j++) {    //For each word in lyrics
                String temp = lyricString[j].toLowerCase();
                //System.out.println("Lyric: " + lyricString[j]);
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

    public void statistics() {
        int references = 0;

        for (Map.Entry<String, TreeSet<Song>> entry : lyricSet.entrySet()) {
            //Add number of songs to references
            references += entry.getValue().size();
        }

        System.out.println("# of keys: " + lyricSet.size());
        System.out.println("References: " + references);
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
            int key = (int)itr.next();
            System.out.println(key + " entries of " + tempMap.get(key));
            i++;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        SongCollection sc = new SongCollection("allSongs.txt");
        SearchByLyricWords search = new SearchByLyricWords(sc);

        search.statistics();
        search.top10Words();
    }
}
