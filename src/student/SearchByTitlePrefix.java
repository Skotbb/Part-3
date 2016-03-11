/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.io.FileNotFoundException;
import java.util.Comparator;
import student.RaggedArrayList.L2Array;
import student.Song.CmpTitle;

/**
 *
 * @author Scott Thompson
 */
public class SearchByTitlePrefix {

    private RaggedArrayList<Song> songs;
    public Comparator<Song> cmp;

    public SearchByTitlePrefix(SongCollection sc) {
        cmp = new CmpTitle();
        ((CmpCnt)cmp).resetCmpCnt();
        songs = new RaggedArrayList<>(cmp);

        for (int i = 0; i < sc.getAllSongs().length - 1; i++) {
            songs.add(sc.getAllSongs()[i]);
        }
    }
    
    public Song[] search(String titlePrefix) {
        Song[] result = null;
        // write this method
        // case insensitive
        titlePrefix = titlePrefix.toLowerCase();
        // create a Song key with artistPrefix
        Song key = new Song("", titlePrefix, "");
        System.out.println("Key: " + key);
        //Set the end character of key to the next letter    
        String endKey;
        endKey = titlePrefix.replace(titlePrefix.charAt(titlePrefix.length()-1),
                Character.
                        valueOf((char)(titlePrefix.charAt(titlePrefix.length()-1)+1)));
        System.out.println("end key: " + endKey);
        Song endSong = new Song("", endKey, "");    
        
        // create a comparator object
        cmp = new Song.CmpTitle();
        
        int x,y;
        x = songs.findFront(key).level1Index;
        y = songs.findFront(key).level2Index;
        L2Array l2 = null;
        l2 = (L2Array) songs.l1Array[x-1];
        System.out.println("Song before: " + l2.items[l2.numUsed-1]);
        
        // create an arraylist to store the matches
        RaggedArrayList<Song> list = new RaggedArrayList<>(cmp);
        // artist comparison counter
        int count = ((CmpCnt) cmp).getCmpCnt();
        
        list = songs.subList(key, endSong);
        
        result = new Song[list.size];
        result = list.toArray(result);
        
     return result;   
    }

    public static void main(String[] args) throws FileNotFoundException {
        SongCollection sc = new SongCollection("allSongs.txt");
        
        SearchByTitlePrefix sbtp = new SearchByTitlePrefix(sc);
        
        
            //Search and print results for search
        System.out.println("searching for: search");
        Song[] searchSearch = sbtp.search("search");
        System.out.println("Results: " + searchSearch.length);
        for(int j=0; j < 11 && j < searchSearch.length-1; j++){
            System.out.println(searchSearch[j]);
        }
        System.out.println("Compare count: " + ((CmpCnt) sbtp.cmp).getCmpCnt());
//            //Search and print results for angel
//        System.out.println("searching for: angel");
//        Song[] angelSearch = sbtp.search("angel");
//        System.out.println("Results: " + angelSearch.length);
//        for(int j=0; j < 11 && j < angelSearch.length-1; j++){
//            System.out.println(angelSearch[j]);
//        }
//            //Search and print results for T
//        System.out.println("searching for: T");
//        Song[] tSearch = sbtp.search("T");
//        System.out.println("Results: " + tSearch.length);
//        for(int j=0; j < 11 && j < tSearch.length-1; j++){
//            System.out.println(tSearch[j]);
//        }    
    }

}
