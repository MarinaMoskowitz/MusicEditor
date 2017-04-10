package cs3500.music.model;

import java.util.List;
import java.util.TreeMap;

/**
 * This is an interface to represent a Song.
 */
public interface ISong {
  /**
   * Gets all the beats in this song.
   *
   * @return  a list of all the beats in this song
   */
  List<Integer> getBeats();

  /**
   * Returns the highest Note in this song.
   *
   * @return highest Note in this Song
   */
  Note maxNote();

  /**
   * Gets all the notes in this song.
   *
   * @param beat the notes at this beat
   * @return  a list of all the notes in this song
   */
  List<Note> getNotes(int beat);

  /**
   * Gets this song.
   *
   * @return this song
   */
  TreeMap<Integer, List<Note>> getSong();

  /**
   * Checks if a given beat is in a song.
   *
   * @param beat the beat being checked
   * @return true if the beat is in this song, false if not
   */
  boolean hasBeat(int beat);

  /**
   * Puts a note in a song at a given beat.
   *
   * @param beat the beat for the note being placed in this song
   * @param n the note to be placed in this song
   */
  void put(int beat, List<Note> n);

  /**
   * Returns the lowest Note in this song.
   *
   * @return lowest Note in the this Song
   */
  Note minNote();

  /**
   * Get all the pitches in this song.
   *
   * @return a list of all the pitches
   */
  List<String> getPitches();


  /**
   * ADDED IN HW6
   * Get the tempo of this song.
   *
   * @return the tempo of this song
   */
  int getTempo();

  /**
   * Added in hw6
   * Sets the tempo of this song
   * @param tempo the new tempo
   */
  void setTempo(int tempo);
}
