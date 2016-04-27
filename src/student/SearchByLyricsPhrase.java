/*
 * By: Scott Thompson and Angela Tucci
 * Class: SearchByLyricsPhrase
 * This class uses the SongCollection and a phase of lyrics.
 * It uses the phrase to search Song lyrics and create a rank based on
 * how closely it matches.
 */
package student;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author Scott
 */
public class SearchByLyricsPhrase {

    private SongCollection songs;
//    private PhraseRanking ranks;
    private SearchByLyricsWords lyricWords;

    public SearchByLyricsPhrase(SongCollection sc) {
        this.songs = sc;
        lyricWords = new SearchByLyricsWords(sc);
    }
    
    /**Written by Angela and Scott
     * 
     * @param lyricPhrase
     * @return 
     */
    public Song[] search(String lyricPhrase) {
        TreeMap<Integer, ArrayList<Song>> matchList = new TreeMap<>(); //test
        
        TreeMap<Integer, Set<Song>> phraseMatchSongs = new TreeMap<>();
        Set matchingSongs = null;
        Song[] listToSearch = null;
        int rank;

        listToSearch = lyricWords.search(lyricPhrase);
        for (int i = 0; i < listToSearch.length; i++) {
            rank = PhraseRanking.rankPhrase(listToSearch[i].getLyrics(), lyricPhrase);
            if (rank > 0) {
                //Playing with a different way to make a list
                if(matchList.containsKey(rank)){
                    matchList.get(rank).add(listToSearch[i]);
                }else{
                    matchList.put(rank, new ArrayList<Song>());
                    matchList.get(rank).add(listToSearch[i]);
                }
//                matchingSongs = phraseMatchSongs.get(rank);
//                phraseMatchSongs.put(rank, matchingSongs);
//                if(matchingSongs == null){
//                    matchingSongs = new TreeSet<>();
//                    phraseMatchSongs.put(rank, matchingSongs);     
//                }
//                    matchingSongs.add(listToSearch[i]);
            }
        }
        // printing for testing
        //System.out.println("Map results: \n" + phraseMatchSongs.entrySet() + "\n\n\n");
        printRank(matchList);
        
        ArrayList<Song> setOfAllSongs = new ArrayList<>();
        for (Map.Entry<Integer, Set<Song>> map : phraseMatchSongs.entrySet()){
            Set<Song> matches = map.getValue();
            setOfAllSongs.addAll(matches);
        }
        Song[] results = new Song[setOfAllSongs.size()];
        setOfAllSongs.toArray(results);        
        return results;
    }
        //A semi-clean way to print the ranked list.
    public void printRank(TreeMap<Integer, ArrayList<Song>> list){
        for (Map.Entry<Integer, ArrayList<Song>> map : list.entrySet()){
            int key = map.getKey();
            ArrayList songList;
            songList = list.get(key);
            for(int i = 0; i < songList.size(); i++){
                Song current = (Song) songList.get(i);
                System.out.println(key +" "+ current.getArtist() +", "+ current.getTitle());
            }
        }
    }
    

    public static void main(String[] args) {
        Song[] songResults = null;
        try {
            SongCollection sc = new SongCollection("allSongs.txt");
            SearchByLyricsPhrase searchLyricPhrase = new SearchByLyricsPhrase(sc);

            songResults = searchLyricPhrase.search("she loves you");
            System.out.println("Searching for: she loves you");
            System.out.println("\n\n\nMain results: \n");
            // doesn't print rankings yet
            for(int i = 0; i < songResults.length; i++){
                System.out.println(songResults[i].getArtist() + "  " +
                        songResults[i].getTitle());
            }
            System.out.println("Total songs: " + songResults.length);  
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");;
        }

    }
}