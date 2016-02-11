package student;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**Modified by: Jeremy Spofford
 * Modified by: Scott Thompson
 * 
 * RaggedArrayList.java
 *  
 * Initial starting code by Prof. Boothe Sep 2015
 *
 * The RaggedArrayList is a 2 level data structure that is an array of arrays.
 *  
 * It keeps the items in sorted order according to the comparator.
 * Duplicates are allowed.
 * New items are added after any equivalent items.
 *
 * NOTE: normally fields, internal nested classes and non API methods should 
 *  all be private, however they have been made public so that the tester code 
 *  can set them
 * @author boothe 2015
 * Revised by Anne Applin 2016
 * 
 * @param <E>
 */

public class RaggedArrayList<E> implements Iterable<E> {

    private static final int MINIMUM_SIZE = 4;    // must be even so when split get two equal pieces

    /**
     *
     */
    public int size;

    /**
     * really is an array of L2Array, but compiler won't let me cast to that
     */
    public Object[] l1Array;  

    /**
     *
     */
    public int l1NumUsed;
    private Comparator<E> comp;


    /**
     * create an empty list
     * always have at least 1 second level array even if empty, makes 
     * code easier 
     * (DONE - do not change)
     * @param c a comparator object
     */
    public RaggedArrayList(Comparator<E> c) {
        size = 0;
        l1Array = new Object[MINIMUM_SIZE];                // you can't create an array of a generic type
        l1Array[0] = new L2Array(MINIMUM_SIZE);  // first 2nd level array
        l1NumUsed = 1;
        comp = c;
    }

    /**
     * nested class for 2nd level arrays
     * (DONE - do not change)
     */
    public class L2Array {

        /**
         *  the array of items
         */
        public E[] items;

        /**
         * number of item in this L2Array with values
         */
        public int numUsed;

        /**
         * Constructor for the L2Array
         * @param capacity the initial length of the array
         */
        public L2Array(int capacity) {
             // you can't create an array of a generic type
            items = (E[]) new Object[capacity]; 
            numUsed = 0;
        }
    }// end of nested class L2Array

    /**
     *  total size (number of entries) in the entire data structure
     *  (DONE - do not change)
     * @return total size of the data structure
     */
    public int size() {
            return size;
    }


    /**
     * null out all references so garbage collector can grab them
     * but keep otherwise empty l1Array and 1st L2Array
     * (DONE - Do not change)
     */
    public void clear() {
        size = 0;
        // clear all but first l2 array
        Arrays.fill(l1Array, 1, l1Array.length, null);  
        l1NumUsed = 1;
        L2Array l2Array = (L2Array) l1Array[0];
        // clear out l2array
        Arrays.fill(l2Array.items, 0, l2Array.numUsed, null); 
        l2Array.numUsed = 0;
    }


    /**
     *  nested class for a list position
     *  used only internally
     *  2 parts: level 1 index and level 2 index
     */
    public class ListLoc {

        /**
         * Level 1 index
         */
         public int level1Index;

        /**
         * Level 2 index
         */
         public int level2Index;

        /**
         * Parameterized constructor
         * @param level1Index input value for property
         * @param level2Index input value for property
         */
        public ListLoc(int level1Index, int level2Index) {
           this.level1Index = level1Index;
           this.level2Index = level2Index;
        }


        /**
         * test if two ListLoc's are to the same location 
         * (done -- do not change)
         * @param otherObj 
         * @return
         */
        public boolean equals(Object otherObj) {
            // not really needed since it will be ListLoc
            if (getClass() != otherObj.getClass()) {
                return false;
            }
            ListLoc other = (ListLoc) otherObj;

            return level1Index == other.level1Index && 
                    level2Index == other.level2Index;
        }
        /**
         * move ListLoc to next entry
         *  when it moves past the very last entry it will be 1 index past the 
         *  last value in the used level 2 array can be used internally to 
         *  scan through the array for sublist also can be used to implement 
         *  the iterator
        */
        public void moveToNext() {
            // TO DO
        }
    }


    /**Coded by Scott Thompson
     * 
     * find 1st matching entry
     * @param item  we are searching for a place to put.
     * @return ListLoc of 1st matching item or of 1st item greater than the 
     * item if no match this might be an unused slot at the end of a 
     * level 2 array
     */
    public ListLoc findFront(E item) {
        int x=0, y=0, i=0, j=0;
        L2Array l2Array = (L2Array) l1Array[0];
        
        if(size < 1){       //If Array is empty, return (0,0)
            return new ListLoc(0,0);
        }
        //Loop for X Coords (Lvl 1)
        for(i=0; i < l1Array.length-1 && comp.compare(item, l2Array.items[y]) != 0; i++){
            j = 0;  //Reset J coord
            x = i;  //Set X Coord to i loop value
            l2Array = (L2Array) l1Array[x]; //Make sure lvl1 matches X coord
            
            //Loop for Y Coords (Lvl 2)
            if(comp.compare(item, l2Array.items[0]) <= 0){  //Check beginning of L2
                x = i;
                return new ListLoc(x, 0);  
            }
            else{
                while(j <= l2Array.numUsed-1 && comp.compare(item, l2Array.items[j]) >= 0 &&  //Continue until it approaches
                        comp.compare(item, l2Array.items[y]) != 0){
                    if(comp.compare(item, l2Array.items[j]) == 0){  //If J matches
                        y = j;
                    }
                    else if(comp.compare(item, l2Array.items[j]) == 1 &&    //If at end of L1/L2 array
                            j+1 == l2Array.numUsed && x == l1NumUsed-1){
                        return new ListLoc(x,j+1);
                    }
                    else if(comp.compare(item, l2Array.items[j]) == 1 &&   //If it's one off
                            j+1 <= l2Array.numUsed-1){   //and the next is valid
                        if(j+1 > l2Array.numUsed-1){ 
                            return new ListLoc(x,j+1);  
                        }
                        else if(comp.compare(item, l2Array.items[j+1]) == 0){ //If next item matches
                           return new ListLoc(x,j+1);
                        }
                        else if(comp.compare(item, l2Array.items[j+1]) < 0){ //If next item goes past
                            return new ListLoc(x,j+1);        //Set it one forward.
                        }
                        else j++;
                    }
                    else{
                        j++;
                    }
                }
            }
        }
        return new ListLoc(x, y);
    }


    /**Coded by Jeremy Spofford
     * 
     * find location after the last matching entry or if no match, it finds 
     * the index of the next larger item this is the position to add a new 
     * entry this might be an unused slot at the end of a level 2 array
     * @param item
     * @return the location where this item should go 
     */
    public ListLoc findEnd(E item) {
         // TO DO
        L2Array currentArray = ((L2Array) l1Array[l1NumUsed - 1]);
        // int x, y; 
        ListLoc loc = new ListLoc(l1NumUsed - 1, currentArray.numUsed - 1);
        if(loc.level1Index < 0 || loc.level2Index < 0){
            loc.level2Index = 0;
           //loc.level1Index = 0;
        }
        if (((L2Array) l1Array[loc.level1Index]).numUsed == 0) {
            return loc;
        }
        for (int i = l1NumUsed - 1; i >= 0; i--) {
            currentArray = (L2Array) l1Array[i];
            for (int j = currentArray.numUsed - 1; j >= 0; j--) {
                int c = comp.compare(item, currentArray.items[j]);
                if (c >= 0) {
                    loc.level1Index = i;
                    loc.level2Index = j + 1;

                    return loc;
                }
            }
        }
        loc.level1Index = 0;
        loc.level2Index = 0;
        return loc; // when finished should return: new ListLoc(l1,l2);
    }


    /**
     * add object after any other matching values findEnd will give the
     * insertion position
     * @param item
     * @return 
     */
    public boolean add(E item) {
        ListLoc resultLoc = findEnd(item);  //Get insertion point
        int xIndex = resultLoc.level1Index, //X index for insert
            yIndex = resultLoc.level2Index; //Y index for insert
        L2Array l2Array = (L2Array) l1Array[xIndex];    //Looking at target l2
        
        //TODO Add, if there's room
        if(l2Array.numUsed < l2Array.items.length-1){
            for(int i = l2Array.numUsed; i > yIndex; i--){
                l2Array.items[i] = l2Array.items[i-1];
            }
            l2Array.items[yIndex] = item;
            l2Array.numUsed ++;
        }

        //TODO if l2 < l1 size, double array and add
        else if(l2Array.items.length < l1Array.length){
            l2Array.items = Arrays.copyOf(l2Array.items, l2Array.items.length * 2);
            for(int i = l2Array.numUsed; i > yIndex; i--){
                l2Array.items[i] = l2Array.items[i-1];
            }
            l2Array.items[yIndex] = item;
            l2Array.numUsed ++;
        }else{
            //TODO if l2 > l1 size, split in half and add.
            L2Array l2Next;
            l2Array = (L2Array)l1Array[xIndex];
            
            for(int i = l2Array.numUsed; i > yIndex; i--){  //Shift l2 array
                l2Array.items[i] = l2Array.items[i-1];
            }
            l2Array.items[yIndex] = item;               //Add item, first
            l2Array.numUsed ++;                     //Increment number used.
            
            for(int i = l1NumUsed-1; i > xIndex; i--){    //Now Split and shift array
                l2Array = (L2Array)l1Array[i];  //Set end array
                l2Next = (L2Array)l1Array[i-1]; //Set proceeding array    
                
                l2Array.items = Arrays.copyOf(l2Next.items, l2Next.items.length); //Shift L1 arrays down a position
                l2Array.numUsed = l2Next.numUsed;
            }
            l1NumUsed++;        //Increment L1 number used.
            l2Array = (L2Array)l1Array[xIndex];         //Set target L2 array
            l2Next = (L2Array)l1Array[xIndex+1];        //Set split array
            System.arraycopy(l2Array.items, (int)l2Array.items.length/2,
                    l2Next.items, 0, (int)l2Array.items.length/2);  //Copies code to next line
            Arrays.fill(l2Array.items,(int)l2Array.items.length/2,
                    l2Array.items.length, null);              //Clears last half
            Arrays.fill(l2Next.items,(int)l2Next.items.length/2,
                    l2Next.items.length, null);              //Clears last half
            l2Array.numUsed = (int)l2Array.numUsed/2;       //Adjust number used.
            l2Next.numUsed = (int)l2Next.numUsed/2;
        
        
        }
        

        

        return true;
    }

    /**
     * check if list contains a match
     * @param item
     * @return 
     */
    public boolean contains(E item) {
        // TO DO

        return false;
    }

    /**
     * copy the contents of the RaggedArrayList into the given array
     *
     * @param a - an array of the actual type and of the correct size
     * @return the filled in array
     */
    public E[] toArray(E[] a) {
        // TO DO

        return a;
    }

    /**
     * returns a new independent RaggedArrayList whose elements range from
     * fromElemnt, inclusive, to toElement, exclusive the original list is
     * unaffected findStart and findEnd will be useful
     *
     * @param fromElement
     * @param toElement
     * @return the sublist
     */
    public RaggedArrayList<E> subList(E fromElement, E toElement) {
        // TO DO

        RaggedArrayList<E> result = new RaggedArrayList<E>(comp);
        return result;
    }

    /**
     * returns an iterator for this list this method just creates an instance of
     * the inner Itr() class (DONE)
     * @return 
     */
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * Iterator is just a list loc it starts at (0,0) and finishes with index2 1
     * past the last item in the last block
     */
    private class Itr implements Iterator<E> {

        private ListLoc loc;

        /*
         * create iterator at start of list
         * (DONE)
         */
        Itr() {
            loc = new ListLoc(0, 0);
        }

        /**
         * check if more items
         */
        public boolean hasNext() {
            // TO DO

            return false;
        }

        /**
         * return item and move to next throws NoSuchElementException if off end
         * of list
         */
        public E next() {
            // TO DO

            throw new IndexOutOfBoundsException();
        }

        /**
         * Remove is not implemented. Just use this code. 
         * (DONE)
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
