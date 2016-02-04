//Modified by Angela Tucci
package student;

import java.util.*;

/*
 * Song class to hold strings for a song's artist, title, and lyrics
 * Do not add any methods, just implement the ones that are here.
 * Starting code by Prof. Boothe 2015
 * Modfied by Anne Applin 2016
 */
/**
 *
 * @author boothe
 */
public class Song implements Comparable<Song> {
    // fields
    private String artist, title, lyrics;
    

    /**
     * Parameterized constructor
     * @param artist the author of the song
     * @param title the title of the song
     * @param lyrics the lyrics as a string with linefeeds embedded
     */
    public Song(String artist, String title, String lyrics) {
        this.artist = artist;
        this.title = title;
        this.lyrics = lyrics;
    }

    /**
     *
     * @return
     */
    public String getArtist() {
        
        return artist;
    }

    /**
     *
     * @return
     */
    public String getLyrics() {
        
        return lyrics;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        
        return title;
    }

    /**
     *
     * @return
     */
    public String toString() {
        // create a stringbuilder to display artist and title
        StringBuilder sb = new StringBuilder();
        sb.append(artist);
        sb.append("\n");
        sb.append(title);
        sb.append("\n");
        
        return sb.toString();
        
    }

    /**
     * the default comparison of songs
     * primary key: artist, secondary key: title
     * used for sorting and searching the song array
     * if two songs have the same artist and title they are considered the same
     * @param song2
     * @return -1, 0 or 1 depending on whether this song should be  before, 
     *    after or is the same.  Used for a "natural" sorting order.  In this 
     *    case first by author then by title so that the all of an artist's 
     *    songs are together, but in alpha order.  
     **/
    public int compareTo(Song song2) {
        int BEFORE = -1;
        int EQUAL = 0;
        int AFTER = 1;
        
        // if the object is null
        if(this == null){
            return AFTER;
        }
        // variables to compare artists and titles
        int compareArtist = this.getArtist().compareToIgnoreCase(song2.getArtist());
        int compareTitle = this.getTitle().compareToIgnoreCase(song2.getTitle());
        // compare the artists
        if(compareArtist != EQUAL){
            return compareArtist;
        }
        // compare the titles
        if(compareTitle != EQUAL){
            return compareTitle;
        }
        // if they have the same artist and title...
        return EQUAL;
    }
    
    public static class CmpArtist extends CmpCnt implements Comparator<Song>{
        @Override
        public int compare(Song s1, Song s2){
           cmpCnt++;
           
           return s1.getArtist().compareToIgnoreCase(s2.getArtist());
        }  
        }

 
    /**
     * testing method to unit test this class
     * @param args
     */
    public static void main(String[] args) {
        Song s1 = new Song("Professor B",
                "Small Steps",
                "Write your programs in small steps\n"
                + "small steps, small steps\n"
                + "Write your programs in small steps\n"
                + "Test and debug every step of the way.\n");

        Song s2 = new Song("Brian Dill",
                "Ode to Bobby B",
                "Professor Bobby B., can't you see,\n"
                + "sometimes your data structures mystify me,\n"
                + "the biggest algorithm pro since Donald Knuth,\n"
                + "here he is, he's Robert Boothe!\n");

        Song s3 = new Song("Professor B",
                "Debugger Love",
                "I didn't used to like her\n"
                + "I stuck with what I knew\n"
                + "She was waiting there to help me,\n"
                + "but I always thought print would do\n\n"
                + "Debugger love .........\n"
                + "Now I'm so in love with you\n");

        System.out.println("testing getArtist: " + s1.getArtist());
        System.out.println("testing getTitle: " + s1.getTitle());
        System.out.println("testing getLyrics:\n" + s1.getLyrics());

        System.out.println("testing toString:\n");
        System.out.println("Song 1: " + s1);
        System.out.println("Song 2: " + s2);
        System.out.println("Song 3: " + s3);

        System.out.println("testing compareTo:");
        System.out.println("Song1 vs Song2 = " + s1.compareTo(s2));
        System.out.println("Song2 vs Song1 = " + s2.compareTo(s1));
        System.out.println("Song1 vs Song3 = " + s1.compareTo(s3));
        System.out.println("Song3 vs Song1 = " + s3.compareTo(s1));
        System.out.println("Song1 vs Song1 = " + s1.compareTo(s1));
        
        Comparator<Song> cmp = new Song.CmpArtist();
        System.out.println("\ntesting compare artists:");
        System.out.println("Song 1 vs Song 2 = " + cmp.compare(s1,s2));
        System.out.println("Song 2 vs Song 1 = " + cmp.compare(s2,s1));
        System.out.println("Song 1 vs Song 3 = " + cmp.compare(s1,s3));
        System.out.println("Song 3 vs Song 1 = " + cmp.compare(s3,s1));
        System.out.println("Song 1 vs Song 1 = " + cmp.compare(s1,s1));
    }
    
}