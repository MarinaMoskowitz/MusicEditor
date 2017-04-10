package cs3500.music.model;

import java.util.Objects;

/**
 * A class to represent a Note.
 */
public class Note implements INote {

  // the pitch of the note
  protected final Pitch pitch;

  // the octave of a note
  public final int octave;

  // the beats of the note
  protected final int beats;

  // ADDED IN HW6; the instrument for the MIDI
  protected final int instrument;

  // ADDED IN HW6; the volume of the note
  protected final int volume;

  /**
   * Constructs a note.
   * INVARIANTS:
   * beats cannot be less than 1.
   * octave cannot be less than 1.
   *
   * @param pitch     the pitch of the note.
   * @param octave    the octave of the note.
   * @param beats     the amount of beats in a note.
   * @throws IllegalArgumentException if the given octave, beats, or duration is less than 1
   */
  public Note(Pitch pitch, int octave, int beats) {
    Objects.requireNonNull(pitch);
    if (beats < 1) {
      throw new IllegalArgumentException("Invalid bpm");
    }
    if (octave < 1) {
      throw new IllegalArgumentException("Invalid octave");
    }
    this.pitch = pitch;
    this.octave = octave;
    this.beats = beats;
    this.instrument = 1;
    this.volume = 64;
  }

  /**
   * ADDED IN HW6.
   * Constructs a note with a pitch, octave, beat, and instrument.
   * INVARIANTS:
   * beats cannot be less than 1.
   * octave cannot be less than 1.
   *
   * @param pitch     the pitch of the note.
   * @param octave    the octave of the note.
   * @param beats     the amount of beats in a note.
   * @throws IllegalArgumentException if the given octave, beats, or duration is less than 1
   */
  public Note(Pitch pitch, int octave, int beats, int instrument, int volume) {
    Objects.requireNonNull(pitch);
    if (beats < 1) {
      throw new IllegalArgumentException("Invalid bpm");
    }
    if (octave < 1) {
      throw new IllegalArgumentException("Invalid octave");
    }
    this.pitch = pitch;
    this.octave = octave;
    this.beats = beats;
    this.instrument = instrument;
    this.volume = volume;
  }

  /**
   * New equals method.
   * @param o the object to be compared with
   * @return whether this Note equals that object
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof Note) {
      Note that = (Note) o;
      return (this.pitch == that.pitch && this.octave == that.octave &&
              this.beats == that.beats);
    }
    return false;
  }

  /**
   * Rewritten hashcode to go with the new equals method.
   * @return the hashcode
   */
  @Override
  public int hashCode() {
    return Objects.hash(pitch, octave, beats);
  }

  @Override
  /**
   * Overriding the compareTo method.
   *
   * @return int    if this is larger than that, returns positive else returns negative
   */
  public int compareTo(Note o) {
    if (this.octave == o.octave) {
      return this.pitch.ordinal() - o.pitch.ordinal();
    } else {
      return this.octave - o.octave;
    }
  }

  /**
   * Gets the pitch of this Note
   * @return the pitch
   */
  @Override
  public Pitch getPitch() {
    return this.pitch;
  }

  /**
   * Gets the octave of this Note
   * @return the octave
   */
  @Override
  public int getOctave() {
    return this.octave;
  }

  /**
   * Gets the instrument of this Note
   * @return the instrument
   */
  @Override
  public int getInstrument() {
    return this.instrument;
  }

  /**
   * Gets the volume of this Note
   * @return the volume
   */
  @Override
  public int getVolume() {
    return this.volume;
  }

  /**
   * Get the current beats.
   * @return the beats
   */
  @Override
  public int getBeats() {
    return this.beats;
  }

  /**
   * Get the duration of the current beat.
   * @return the beats
   */
  @Override
  public int getBeatDuration() {
    return this.beats;
  }

  /**
   * Creates a MIDI data value for this Note.
   *
   * @return  the MIDI data
   */
  public int midiPitch() {
    return (this.pitch.ordinal() + this.octave * 12) + 12;
  }
}


