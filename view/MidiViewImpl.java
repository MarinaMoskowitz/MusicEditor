package cs3500.music.view;

import javax.sound.midi.*;

import cs3500.music.model.IMusicModel;
import cs3500.music.model.ISong;
import cs3500.music.model.MusicModel;
import cs3500.music.model.Note;
import cs3500.music.model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Represents the view of a musical editor through sound of a Song.
 */
public class MidiViewImpl implements IMidiView {
  private int currentBeat;
  private Synthesizer synth;
  private Receiver receiver;
  private IMusicModel model;

  /**
   * Constructor for Midi.
   */
  public MidiViewImpl(Synthesizer synth) {
    this.synth = synth;
    try {
      this.receiver = synth.getReceiver();
      this.synth.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
  }

  /**
   * Default constructor for Midi.
   */
  public MidiViewImpl() {
    try {
      this.synth = MidiSystem.getSynthesizer();
      this.receiver = synth.getReceiver();
      this.synth.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
  }

  /**
   * Constructor that takes a song and sets it to this MIDI view.
   * @param s the song to be used
   */
  public MidiViewImpl(ISong s) {
    try {
      this.synth = MidiSystem.getSynthesizer();
      this.receiver = synth.getReceiver();
      this.synth.open();
      this.model = new MusicModel((Song)s);
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
  }

  /**
   * Constructor for Midi.
   */
  public MidiViewImpl(ISong s, Synthesizer synth) {
    Objects.requireNonNull(s);
    this.synth = synth;
    try {
      this.receiver = synth.getReceiver();
      this.synth.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
  }

  /**
   * Plays the note with the given arguments.
   * @param pitch The pitch to play at
   * @param volume The volume of the note
   * @param length the length of the note
   * @param tempo The tempo of the note
   * @param instrument the instrument of the note
   * @throws InvalidMidiDataException
   */
  public void playNote(int pitch, int volume, int length, int tempo, int instrument)
          throws InvalidMidiDataException {
    // gets channels from receiver and sets the accumulator to the next free channel
    int freeChannel = 0;
    MidiChannel[] channels = synth.getChannels();
    // if it is a percussion instrument
    if (length == 0 || instrument == 10) {
      freeChannel = 9;
    } else {
      for (int i = 0; i < (channels.length - 1); i++) {
        if (channels[i] == null && i != 9) {
          freeChannel = i;
          break;
        }
      }
    }

    // changes instrument for the channel it wants to play the next note at
    this.receiver.send(new ShortMessage(ShortMessage.PROGRAM_CHANGE, (freeChannel + 1),
            instrument, 0), -1);

    // starts playing the note
    MidiMessage start = new ShortMessage(ShortMessage.NOTE_ON, (freeChannel + 1), pitch, volume);

    // stops playing the note
    MidiMessage stop = new ShortMessage(ShortMessage.NOTE_OFF, (freeChannel + 1), pitch, volume);

    // sends the starting message
    this.receiver.send(start, -1);

    // sends the stopping message
    this.receiver.send(stop, (this.synth.getMicrosecondPosition() + (length * tempo)));

    // removed the closing because the player would not play.
    //this.receiver.close();
  }

  /**
   * Creates and plays this Song.
   */
  public void play() {
   Song song = model.getSong();

    Integer[] beats = song.getSong().keySet().toArray(new Integer[0]);
    int endBeat = beats[beats.length - 1];
    int tempo = song.getTempo();
    Note endNote = song.getSong().get(endBeat).get(0);

    for (int i = 0; i <= endBeat; i++) {
      List<Note> currBeat = song.getSong().getOrDefault(i, new ArrayList<Note>());
      if (!currBeat.isEmpty()) {
        for (Note n : currBeat) {
          try {
            this.playNote(n.midiPitch(), n.getVolume(), n.getBeats(), tempo, n.getInstrument());
          } catch (InvalidMidiDataException e) {
            e.printStackTrace();
          }
        }
      }
      try {
        Thread.sleep(tempo / 1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    try {
      Thread.sleep((tempo / 1000) * (endNote.getBeats()- 1));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setModel(IMusicModel model) {
    this.model = model;
  }

  @Override
  public void setSong(ISong song) {
    this.model = new MusicModel((Song)song);
  }


  @Override
  public ISong getSong() {
    return this.model.getSong();
  }

  @Override
  public void setBeat(int beat) {
    this.currentBeat = beat;
  }

  @Override
  public int getBeat() {
    return this.currentBeat;
  }

}