package cs3500.music.model;

/**
 * This is an interface to represent a Note.
 */
public interface INote extends Comparable<Note> {
  /**
   * Get the pitch.
   *
   * @return int    the volume level of a note
   */
  Pitch getPitch();

  /**
   * Get the current octave.
   *
   * @return int    the octave of a note
   */
  int getOctave();

  /**
   * ADDED METHOD IN HW6
   * Get the current instrument.
   *
   * @return int    the instrument of a note
   */
  int getInstrument();

  /**
   * ADDED METHOD IN HW6
   * Get the current volume.
   *
   * @return int    the volume of a note
   */
  int getVolume();

  /**
   * ADDED METHOD IN HW6
   * Get the current beats.
   *
   * @return int    the beats of a note
   */
  int getBeats();

  /**
   * ADDED METHOD IN HW6
   * Get the duration of the current beat.
   *
   * @return int    the duratrion of a beat
   */
  int getBeatDuration();

}

