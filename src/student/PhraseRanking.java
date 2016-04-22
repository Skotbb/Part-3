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
/**Coded by Scott Thompson
 * 
 * @param lyrics String of lyrics from the song being tested
 * @param lyricsPhrase String phrase that you want to check in the song
 * @return int rank (the number of characters in the matching phrase)
 */
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
        } //If Lyrics contain all words in the phrase, separately
        else if (lyricSet.containsAll(Arrays.asList(phraseArray))) { //If Lyrics contain all words in the phrase
            StringBuffer tempSB = new StringBuffer();
            String subLyrics;
            
            int firstWord = -1,
                    lastWord = -1;
            int[] wordInd = new int[phraseArray.length];
            
            //Try to set indexes for all words in phrase
            //System.out.println("Index after she: " + lyrics.indexOf(phraseArray[phraseArray.length-1], lyrics.indexOf(phraseArray[0])));
            if(lyrics.indexOf(phraseArray[phraseArray.length-1], lyrics.indexOf(phraseArray[0])) > -1){
                lastWord = lyrics.lastIndexOf(phraseArray[phraseArray.length - 1]);//lyrics.indexOf(phraseArray[phraseArray.length-1], lyrics.indexOf(phraseArray[0]));
                        //lyrics.lastIndexOf(phraseArray[phraseArray.length - 1]);
                wordInd[wordInd.length-1] = lastWord;
            }
            for(int i= wordInd.length-2; i >= 0; i--){
                wordInd[i] = lyrics.lastIndexOf(phraseArray[i], wordInd[i+1]);
            }
            //Now start from the back to tighten up.
            firstWord = wordInd[0];
            for(int i=1; i< phraseArray.length; i++){
                wordInd[i] = lyrics.indexOf(phraseArray[i], wordInd[i-1]);
            }
            lastWord = wordInd[wordInd.length-1];
            //Make sure the indexes are in order.
            for(int i=1; i < wordInd.length; i++){
                if(wordInd[0] > wordInd[i] || wordInd[wordInd.length-1] < wordInd[i]){
                    return -1;
                }
            }

            //find the last instance of the first word in the phrase
//            if (lyrics.lastIndexOf(phraseArray[0], wordInd[1]) != -1) {
//                firstWord = lyrics.lastIndexOf(phraseArray[0], wordInd[1]);
//            }
            //find the last word in the phrase
            
            if (lastWord != -1) {
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
//        testArray = sbt.search("drop dead gorgeous");
//        for (int i = 0; i < testArray.length; i++) {
//            rank = PhraseRanking.rankPhrase(testArray[i].getLyrics(), "she loves you");
//        }

        songSet.addAll(Arrays.asList(sc.getAllSongs()));
        Iterator<Song> itr = songSet.iterator();
        
        while(itr.hasNext()){
            current = itr.next();
            //System.out.println("Current: " + current.getTitle());
            rank = PhraseRanking.rankPhrase(current.getLyrics(), "time can bring you down");
            
            if(rank > -1){
                songRanking.put(current, rank);
            }
        }
        System.out.println("Results: " + songRanking.size());
        for(Map.Entry<Song, Integer> entry : songRanking.entrySet()){
            System.out.println("Rank: " + entry.getValue() +" "+ entry.getKey());
        }
    }
}
