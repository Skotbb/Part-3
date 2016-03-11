/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Iterator;
import student.Song.CmpTitle;

/**
 *
 * @author Scott
 */
public class SearchByTitlePrefix {

    private RaggedArrayList<Song> songs;

    public SearchByTitlePrefix(SongCollection sc) {
        Comparator<Song> cmp = new CmpTitle();
        ((CmpCnt)cmp).resetCmpCnt();
        songs = new RaggedArrayList<>(cmp);

        for (int i = 0; i < sc.getAllSongs().length - 1; i++) {
            songs.add(sc.getAllSongs()[i]);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        SongCollection sc = new SongCollection("shortSongs.txt");
        
        SearchByTitlePrefix sbtp = new SearchByTitlePrefix(sc);

        Iterator itr = sbtp.songs.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }

}
