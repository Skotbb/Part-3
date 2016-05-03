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
        
        //If Lyrics contain the exact phrase
        if (lyrics.contains(lyricsPhrase)) { 
            return lyricsPhrase.length();
        } 

        //If Lyrics contain all words in the phrase, separately
        else if (lyricSet.containsAll(Arrays.asList(phraseArray))) { //If Lyrics contain all words in the phrase
            StringBuffer tempSB = new StringBuffer();
            String subLyrics;
            
            // added
            // Coded by Angela Tucci
            int count = 0;
            //iterate search words
            for(int k = 0; k < phraseArray.length; k++){
                // iterate song lyrics
                for(int j = 0; j < songLyrics.length; j++){
                    // if the lyric we're looking at equals the search word we're looking at...
                    if(songLyrics[j].equals(phraseArray[k])){
                        // increment a count(keeps track of when we find a search word)
                        count++;
                        
                        // if there are more search terms to look for...
                        if(k < phraseArray.length - 1){
                            // force it to start searching for the next search word
                            // (helps find the search words in order)
                            k++;
                        }
                        else{
                            // otherwise force quit the loop since we've searched for all search words already
                            j = songLyrics.length - 1;
                        }
                    }
                    // if we've found no search words and we're at the end of the lyrics...
                    else if(count == 0 && j == songLyrics.length - 1){
                     //   k = phraseArray.length - 1;
                        // the song is not a valid match
                        return -1;
                    }
                }
                // if we have found all of the search words in order...
                if(count == phraseArray.length){
                    
                    // done with addition
        //        }
        //    }
            // by Scott Thompson
            int firstWord = -1,
                    lastWord = -1;
            int[] wordInd = new int[phraseArray.length];
            
            //Try to set indexes for all words in phrase
            if(lyrics.indexOf(phraseArray[phraseArray.length-1], lyrics.indexOf(phraseArray[0])) > -1){
                lastWord = lyrics.lastIndexOf(phraseArray[phraseArray.length - 1]);
                wordInd[wordInd.length-1] = lastWord;
            }
            
            //Start at the end of song and last word in phrase
            for(int i= wordInd.length-2; i >= 0; i--){
                wordInd[i] = lyrics.lastIndexOf(phraseArray[i], wordInd[i+1]);
                if(phraseArray[i].equals(phraseArray[i+1])){
                    int letterCount = phraseArray[i].length();
                    wordInd[i] = lyrics.lastIndexOf(phraseArray[i], wordInd[i+1] - letterCount);
                }
            }
            firstWord = wordInd[0];
            
            //Start from the first word and make sure matches are as close as possible
            for(int i=1; i< phraseArray.length; i++){
                wordInd[i] = lyrics.indexOf(phraseArray[i], wordInd[i-1]);
                if(phraseArray[i].equals(phraseArray[i-1])){
                    int letterCount = phraseArray[i].length();
                    wordInd[i] = lyrics.indexOf(phraseArray[i], wordInd[i-1] + letterCount);
                }
            }
            lastWord = wordInd[wordInd.length-1];
            
            //Make sure the indexes are in order.
            for(int i=1; i < wordInd.length; i++){
                if(wordInd[0] > wordInd[i] || wordInd[wordInd.length-1] < wordInd[i]){
                    return -1;
                }
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
        } // added to end if statement
            } // added to end for loop  
        }

        return -1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Song current;
        int rank;

        SongCollection sc = new SongCollection("allSongs.txt");
        HashSet<Song> songSet = new HashSet<Song>();
        TreeMap<Song, Integer> songRanking = new TreeMap<Song, Integer>();

        //Single song debug
//        SearchByTitlePrefix sbt = new SearchByTitlePrefix(sc);
//        Song[] testArray = new Song[10];
//        testArray = sbt.search("life");
//        for (int i = 0; i < testArray.length; i++) {
//            rank = PhraseRanking.rankPhrase(testArray[i].getLyrics(), "love love love");
//        }

        songSet.addAll(Arrays.asList(sc.getAllSongs()));
        Iterator<Song> itr = songSet.iterator();
        
        while(itr.hasNext()){
            current = itr.next();
            //System.out.println("Current: " + current.getTitle());
            rank = PhraseRanking.rankPhrase(current.getLyrics(), "she loves you");
            
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