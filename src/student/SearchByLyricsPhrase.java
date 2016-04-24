/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Scott
 */
public class SearchByLyricsPhrase {
    
    private SongCollection songs;
    private PhraseRanking ranks;
    private SearchByLyricsWords lyricWords;
    
    public SearchByLyricsPhrase(SongCollection sc){
        this.songs = sc;
        lyricWords = new SearchByLyricsWords(sc);
    
    }
       
    
    public Song[] search(String lyricPhrase){
        ArrayList<Song> matchSongArray = new ArrayList<>();
        TreeMap<Song, Integer> phraseMatchSongs = new TreeMap<>();
        Song[] listToSearch = null;
        int rank;
        
        listToSearch = lyricWords.search(lyricPhrase);
        for(int i = 0; i < listToSearch.length; i++){
            rank = PhraseRanking.rankPhrase(listToSearch[i].getLyrics(), lyricPhrase);
            if(rank > 0){
                phraseMatchSongs.put(listToSearch[i], rank);
                matchSongArray.add(listToSearch[i]);
            }
            
        }
        phraseMatchSongs.forEach((k, v)->System.out.println(v +" "+ k.getArtist() +", "+ k.getTitle()));
        
        return matchSongArray.toArray(listToSearch);
    }
    
    public static void main(String[] args){
        Song[] songResults = null;
        try {
            SongCollection sc = new SongCollection("allSongs.txt");
            SearchByLyricsPhrase searchLyricPhrase = new SearchByLyricsPhrase(sc);
            
            songResults = searchLyricPhrase.search("she loves you");
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");;
        }
        
        
    }
}
