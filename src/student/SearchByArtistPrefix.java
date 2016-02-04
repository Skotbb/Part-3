//Modified by Scott Thompson
//Modified by Angela Tucci
package student;

import java.io.*;
import java.util.*;

/**
 * SearchByArtistPrefix.java 
 * starting code Boothe 2015 
 * revised by aapplin
 */
public class SearchByArtistPrefix {

    // keep a direct reference to the song array
    private Song[] songs;

    /**
     * constructor initializes the property.
     *
     * @param sc a SongCollection object
     */
    public SearchByArtistPrefix(SongCollection sc) {
        songs = sc.getAllSongs();
    }

    /**
     * find all songs matching artist prefix uses binary search should operate
     * in time log n + k (# matches)
     *
     * @param artistPrefix all or part of the artist's name
     * @return an array of songs by artists with substrings that match the
     * prefix
     */
    public Song[] search(String artistPrefix) {
        // write this method
        // case insensitive
        artistPrefix = artistPrefix.toLowerCase();
        // create a Song key with artistPrefix
        Song key = new Song(artistPrefix, "", "");
        // create a comparator object
        Comparator<Song> cmp = new Song.CmpArtist();
        int partLength = artistPrefix.length();
        // start the binary search using the array, key, and comparator
        int i = Arrays.binarySearch(songs, key, cmp);
        // artist comparison counter
        int count = ((CmpCnt) cmp).getCmpCnt();
        // create an arraylist to store the matches
        ArrayList<Song> list = new ArrayList<>();

        // if the index is negative, switch it to positive to find matches
        if (i < 0) {
            i = -i - 1;
        }
        // once the index is positive...
        if (i > 0) {
            // while the prefix matches an artist and the partLength is not greater than the artist...
            while (songs[i].getArtist().substring(0, partLength).compareToIgnoreCase(artistPrefix) == 0
                    && songs[i - 1].getArtist().length() >= partLength) {
                i--;
            }
            if (songs[i - 1].getArtist().length() >= partLength) {
                i++;
            }
            // add the matches to the arraylist
            while (songs[i].getArtist().substring(0, partLength).compareToIgnoreCase(artistPrefix) == 0
                    && songs[i + 1].getArtist().length() >= partLength) {
                list.add(songs[i]);

                if (i == songs.length - 2) {    //If near the end, manually add last element
                    list.add(songs[i + 1]);   //Keeps while loop from going out of bounds
                    i = 1;                  //Resets i to kick out of loop.
                } else {
                    i++;
                }
            }
            // artist comparison counter for loop one
            System.out.println("artist comparisons loop one: " + count);
            // reset the counter
            ((CmpCnt) cmp).resetCmpCnt();
            // convert the arraylist to an array
            Song[] result = new Song[list.size()];
            result = list.toArray(result);
            return result;
        } else if (i == 0) {    //Already at beginning of list, so start compiling matches
            while (songs[i].getArtist().substring(0, partLength).compareToIgnoreCase(artistPrefix) == 0) {
                list.add(songs[i]);
                i++;

            }
            // artist comparison counter for loop two
            System.out.println("artist comparisons loop two: " + count);
            // reset the counter
            ((CmpCnt) cmp).resetCmpCnt();
            Song[] result = new Song[list.size()];
            result = list.toArray(result);
            return result;
            // if there are no matches...
        } else {
            return null;
        }
    }

    /**
     * testing method for this unit
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try{
        if (args.length == 0) {
//            System.err.println("usage: prog songfile [search string]");
//            return;
            SongCollection sc = new SongCollection("allSongs.txt");
            SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);

            System.out.println("searching for: santana");
            Song[] byArtistResult = sbap.search("santana");

            if (byArtistResult.length > 0) {
                System.out.println("Total number of songs: " + byArtistResult.length + "\n");
                for (int i = 0; i < 10 && i < byArtistResult.length; i++) {
                    System.out.println(byArtistResult[i].getArtist()
                            + "   " + byArtistResult[i].getTitle());
                }
            } else if (byArtistResult.length == 0) {
                System.out.println("No results found");
            }
        }

        if (args.length > 1) {
            SongCollection sc = new SongCollection(args[0]);
            SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);

            System.out.println("searching for: " + args[1]);
            Song[] byArtistResult = sbap.search(args[1]);

            // to do: show first 10 matches
        }
        } catch (FileNotFoundException ex) {
            
            System.out.println("File not found");
            System.exit(1);
        }

//        System.out.println("searching for: santana");
//        Song[] byArtistResult = sbap.search("santana");
//        System.out.println("searching for: arlo");
//        Song[] byArtistResult = sbap.search("arlo");
//        System.out.println("searching for: a");
//        Song[] byArtistResult = sbap.search("a");
//        System.out.println("searching for: z");
//        Song[] byArtistResult = sbap.search("z");
//        System.out.println("searching for: x");
//        Song[] byArtistResult = sbap.search("x");
    }
}