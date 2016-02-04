package student;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/*
 * SongCollection.java
 * Read the specified data file and build an array of songs.
 * 
 * Starting code by Prof. Boothe 2015
 * Modfied by Anne Applin 2016
 * Coded by Tryphena Vallencourt
 * Comments added by Angela Tucci
 */

/**
 *
 * @author boothe
 */
public class SongCollection {

    private Song[] songs;

    /**
     * Note: in any other language, reading input inside a class is simply not
     * done!! No I/O inside classes because you would normally provide
     * precompiled classes and I/O is OS and Machine dependent and therefore not
     * portable. Java runs on a virtual machine that IS portable. So this is
     * permissable because we are programming in Java.
     *
     * @param filename The path and filename to the datafile that we are using.
     * @throws java.io.FileNotFoundException
     */
    public SongCollection(String filename)throws FileNotFoundException {
	// use a try catch block
        // read in the song file and build the songs array
        // you must use a StringBuilder to read in the lyrics!
        // sort the songs array
        ArrayList<Song> songList = new ArrayList<>();
        Scanner in = null;
        String artist = null, l,title = null, str;
        StringBuilder lyrics = new StringBuilder();
        
        try {
            in = new Scanner(new FileReader(filename));
        }
        catch(FileNotFoundException ex){
            System.out.println("File not found.");
            System.exit(1);
        }
        // while there is something in the file, read it in
        while (in.hasNextLine()){
            str = in.nextLine(); 
            // read in the artist
            if(str.startsWith("ARTIST")){
                artist = str.substring(7);
                artist = artist.replaceAll("\"","");
                //System.out.println(artist);
            }
            // read in the title
            else if(str.startsWith("TITLE")){
                title = str.substring(6);
                title = title.replaceAll("\"", "");
                //System.out.println(title);
            }
            // read in the lyrics
            else if(str.startsWith("LYRICS")){
                l = str.substring(7);
                lyrics.append(l);
                lyrics.append("\n");
            }    
            else if(str.startsWith("\"")){
                l = lyrics.toString();
                //System.out.println(l);
                // create a Song object
                Song s = new Song(artist,title,l);
                // increase the number of songs
               
                // add the song to the arraylist
                songList.add(s);
                lyrics.setLength(0);
            }
            else{
                lyrics.append(str);
                lyrics.append("\n");
            }
    }
        // convert the arraylist into an array of the same size
        songs = songList.toArray(new Song[songList.size()]);
        Arrays.sort(songs);
        
    }
 
    /**
     * this is used as the data source for building other data structures
     * @return the songs array
     */
    public Song[] getAllSongs() {
        
        return songs;
    }
 
    /**
     * unit testing method
     * @param args
     */
    public static void main(String[] args) {
   //if (args.length == 0) {
   //    System.err.println("usage: prog songfile");
   //    return;
   //}
    try{
   SongCollection sc = new SongCollection("allSongs.txt");
   Song[] list = sc.getAllSongs();
   // todo: show song count and first 10 songs (name & title only, 1 per line)
   System.out.printf("Total songs =  %d, first songs: \n",list.length );
   for (int i = 0; i < 10 && i < list.length; i++){
      System.out.printf("%s   \"%s\"\n", list[i].getArtist(), list[i].getTitle());
    } 
    }catch(FileNotFoundException ex){
        System.out.println("file not found");
        System.exit(1);
    }
}
    
}