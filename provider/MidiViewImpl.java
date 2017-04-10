package cs3500.music.view;

import cs3500.music.model.Note;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

/**
 * A skeleton for MIDI playback.
 */
public class MidiViewImpl implements IView<Note> {

  private final Sequencer sequencer;
  private float tempo;
  private List<Note> arrNote;
  private boolean paused;

  static final int TICKS_PER_BEAT = 10;

  /**
   * Constructs a default MidiView.
   */
  public MidiViewImpl() {
    Sequencer seq = null;

    try {
      Synthesizer syn = MidiSystem.getSynthesizer();
      syn.loadAllInstruments(syn.getDefaultSoundbank());
      seq = MidiSystem.getSequencer();
      seq.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }

    this.sequencer = seq;
    this.paused = true;
    this.tempo = 200000;
    this.arrNote = new ArrayList<>();
  }

  public Sequencer getSequencer() {
    return this.sequencer;
  }

  @Override
  public void setScore(List<Note> arr) {
    this.arrNote = arr;
  }

  public void setTempo(float tempo) {
    this.tempo = tempo;
  }

  @Override
  public void refresh() {
    float tick = sequencer.getTickPosition();

    sequencer.setTempoInMPQ(tempo);

    try {
      Sequence seq = new Sequence(Sequence.PPQ, TICKS_PER_BEAT);
      Track track = seq.createTrack();

      for (Note n : arrNote) {
        MidiMessage progChange = new ShortMessage(ShortMessage.PROGRAM_CHANGE, 0,
            n.getInstrument(), 0);
        MidiEvent progChangeEvent = new MidiEvent(progChange, n.getHead() * TICKS_PER_BEAT);

        MidiMessage start = new ShortMessage(ShortMessage.NOTE_ON, 0,
            n.getMidiNum(), n.getVelocity());
        MidiMessage stop = new ShortMessage(ShortMessage.NOTE_OFF, 0,
            n.getMidiNum(), n.getVelocity());

        MidiEvent startEvent = new MidiEvent(start, n.getHead()  * TICKS_PER_BEAT);
        MidiEvent stopEvent = new MidiEvent(stop,
            (n.getSustain() + n.getHead()) * TICKS_PER_BEAT);

        track.add(progChangeEvent);
        track.add(startEvent);
        track.add(stopEvent);
      }

      sequencer.setSequence(seq);
    } catch (InvalidMidiDataException e) {
      e.getMessage();
    }
    sequencer.setTickPosition((long)tick);
    paused = true;
  }

  public float getTicks() {
    return sequencer.getTickPosition();
  }

  public boolean isPaused() {
    return paused;
  }

  /**
   * Pause the view.
   */
  public void pause() {
    if (paused) {
      sequencer.start();
    } else {
      sequencer.stop();
    }
    sequencer.setTempoInMPQ(tempo);
    paused = !paused;
  }

  /**
   * Move the view to right by one measure.
   */
  public void moveRight() {
    float t = sequencer.getTickPosition() + TICKS_PER_BEAT;
    if (t >= sequencer.getTickLength()) {
      return;
    }
    sequencer.setTickPosition((long)t);
  }

  /**
   * Move the view to left by one measure.
   */
  public void moveLeft() {
    float t = sequencer.getTickPosition() - TICKS_PER_BEAT;
    if (t < 0) {
      return;
    }
    sequencer.setTickPosition((long)t);
  }

  /**
   * Move the view to the start.
   */
  public void moveHome() {
    sequencer.stop();
    sequencer.setTickPosition(0);
    sequencer.setTempoInMPQ(tempo);
  }

  /**
   * Move the view to the End.
   */
  public void moveEnd() {
    sequencer.setTickPosition(sequencer.getTickLength());
    sequencer.stop();
    paused = true;
  }

  /**
   * Add a note to the view.
   */
  public void addNote(Note n) {
    arrNote.add(n);
    refresh();
    sequencer.setTempoInMPQ(tempo);
  }

  @Override
  public void display() {
    refresh();
  }

  /**
   * Play the song, used in Midi view.
   */
  public void play() {
    refresh();
    sequencer.start();
  }

  @Override
  public void setLeftMostNote(Note note) {
    // does not support
  }

  @Override
  public void setRightMostNote(Note note) {
    // does not support
  }

  @Override
  public void setCurMeasure(int curMeasure) {
    // does not support
  }

  @Override
  public void setBeats(int beats) {
    // does not support
  }
}
