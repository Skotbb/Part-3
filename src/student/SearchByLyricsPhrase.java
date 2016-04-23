/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

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
        Song[] listToSearch = null;
        int rank;
        
        listToSearch = lyricWords.search(lyricPhrase);
        for(int i = 0; i < listToSearch.length; i++){
            rank = PhraseRanking.rankPhrase(listToSearch[i].getLyrics(), lyricPhrase);
        }
        
        return null;
    }
}
