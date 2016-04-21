/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Scott Thompson
 */
public class PhraseRanking {

    private int rank;

    public PhraseRanking() {
        rank = -1;

    }

    static int rankPhrase(String lyrics, String lyricsPhrase) {
        lyrics = lyrics.toLowerCase();
        lyricsPhrase = lyricsPhrase.toLowerCase();

        String[] songLyrics = null, phraseArray = null, subArray = null;

        ArrayList<String> lyricSet = new ArrayList<String>();
        ArrayList<String> subSet = new ArrayList<String>();

        if (!lyricsPhrase.isEmpty()) { //Count phrase for rank and split words
            phraseArray = lyricsPhrase.split("[^a-zA-Z]+");
        }
        if (!lyrics.isEmpty()) {      //Split song lyrics into words array.
            songLyrics = lyrics.split("[^a-zA-Z]+");
            lyricSet.addAll(Arrays.asList(songLyrics));
        }

        if (lyrics.contains(lyricsPhrase)) { //If Lyrics contain the exact phrase
            return lyricsPhrase.length();
        } else if (lyricSet.containsAll(Arrays.asList(phraseArray))) { //If Lyrics contain all words in the phrase
            StringBuffer tempSB = new StringBuffer();
            String subLyrics;
            int firstWord = lyrics.indexOf(phraseArray[0]),
                    lastWord = lyrics.indexOf(phraseArray[phraseArray.length - 1], firstWord);

            //find the last instance of the first word in the phrase
            if (lyrics.lastIndexOf(phraseArray[0], lastWord) != -1) {
                firstWord = lyrics.lastIndexOf(phraseArray[0], lastWord);
            }
            //find the last word in the phrase
            if (lastWord != -1) {
                if (!lyrics.substring(lastWord,
                        lastWord + phraseArray[phraseArray.length - 1].length()).
                        equals(phraseArray[phraseArray.length - 1])
                        || lyrics.indexOf(phraseArray[phraseArray.length - 1],
                                lastWord + 1) != -1) {
                    lastWord = lyrics.indexOf(phraseArray[phraseArray.length - 1],
                            lastWord + 1);
                }
                lastWord += phraseArray[phraseArray.length - 1].length();
            }
            if (firstWord != -1 && lastWord != -1) {
                subLyrics = lyrics.substring(firstWord, lastWord);
                subArray = subLyrics.split("[^a-zA-Z]+");
                subSet.clear();
                subSet.addAll(Arrays.asList(subArray));
            }

            //Change ArrayList into a sentence, to count.
            for (int i = 0; i < subSet.size(); i++) {
                if (i <= subSet.size() - 1) {
                    tempSB.append(subSet.get(i) + " ");
                } else {
                    tempSB.append(subSet.get(i));
                }
            }
            if (tempSB.length() > 0) {
                return tempSB.length();
            } else {
                return -1;
            }
        }

        return -1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Song current;
        int rank;

        SongCollection sc = new SongCollection("allSongs.txt");
        HashSet<Song> songSet = new HashSet<Song>();
        TreeMap<Song, Integer> songRanking = new TreeMap<Song, Integer>();

//        SearchByTitlePrefix sbt = new SearchByTitlePrefix(sc);
//        Song[] testArray = new Song[10];
//        testArray = sbt.search("cecilia");
//        for (int i = 0; i < testArray.length; i++) {
//            rank = PhraseRanking.rankPhrase(testArray[i].getLyrics(), "she loves you");
//        }

        songSet.addAll(Arrays.asList(sc.getAllSongs()));
        Iterator<Song> itr = songSet.iterator();
        
        while(itr.hasNext()){
            current = itr.next();
            System.out.println("Current: " + current.getTitle());
            rank = PhraseRanking.rankPhrase(current.getLyrics(), "she loves you");
            
            if(rank > -1){
                songRanking.put(current, rank);
                //System.out.println("Song: " + current.getTitle() +" rank: "+ rank);
            }else{
                //System.out.println("Song: " + current.getTitle() +" rank: "+ rank);
            }
        }
        System.out.println("Results: " + songRanking.size());
        for(Map.Entry<Song, Integer> entry : songRanking.entrySet()){
            System.out.println("Rank: " + entry.getValue() +" "+ entry.getKey());
        }
    }
}
