package cs3500.music.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;
import javax.swing.Timer;

import cs3500.music.model.ISong;
import cs3500.music.model.MusicModel;
import cs3500.music.model.Note;
import cs3500.music.model.Pitch;
import cs3500.music.model.Song;

/**
 * The combined final view for the Music Player. Can move to the beginning of the song with HOME,
 * and to the end with END. Can start or stop the music by pressing "P". Can also use arrow keys
 * to scroll through the song. Adding notes can be done by clicking the key on the keyboard to be
 * added at the current beat.
 */
public class FinalView extends MusicView implements MouseListener {
  // timers to fire at intervals to play the music.
  protected Timer movingTimer = new Timer(song.getTempo()/1000, new MovingAction());
  protected Timer beatTimer = new Timer(song.getTempo()/1000, new MusicPlayer());

  /**
   * Constructor that takes in a given view and midi, and creates a Final View with the
   * same song. The key bindings are then added.
   * @param view the view to be used
   * @param music the midi to be used
   */
  public FinalView(MusicView view, IMidiView music) {
    super(view.getSong());
    if (!view.getSong().equals(music.getSong())) {
      throw new IllegalArgumentException("View and music have different songs.");
    }
    this.midiPlayer = music;
    notesPanel.getInputMap().put(KeyStroke.getKeyStroke("P"), "combinedplay");
    notesPanel.getActionMap().put("combinedplay", new CombinedTimer());
    notesPanel.getInputMap().put(KeyStroke.getKeyStroke("HOME"), "movetobeginning");
    notesPanel.getActionMap().put("movetobeginning", new MoveToBeginning());
  }

  /**
   * A constructor that takes in a song and sets this Final View to be a
   * new MusicView with the song. A new MidiViewImpl is created with the song as well, and the
   * key bindings are set.
   * @param song the song to be used
   */
  public FinalView(ISong song) {
    super(song);
    notesPanel.getInputMap().put(KeyStroke.getKeyStroke("P"), "combinedplay");
    notesPanel.getActionMap().put("combinedplay", new CombinedTimer());
    notesPanel.getInputMap().put(KeyStroke.getKeyStroke("HOME"), "movetobeginning");
    notesPanel.getActionMap().put("movetobeginning", new MoveToBeginning());
    notesPanel.getInputMap().put(KeyStroke.getKeyStroke("END"), "movetoend");
    notesPanel.getActionMap().put("movetoend", new MoveToEnd());
    keysPanel.addMouseListener(this);
  }


  /**
   * What should happen when the mouse is clicked. In this case, a note will be added at the
   * currentBeat according to which key on the keyboard was clicked.
   * @param e the mouse click triggering this method
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    int whitexfromedge = e.getX() - keysPanel.whiteLeftPadding;
    int octavenum = (whitexfromedge / keysPanel.octaveWidth) + 1;
    MusicModel tempmodel = new MusicModel((Song)song);
    ActionEvent a = new ActionEvent(e.getSource(), e.getID(), e.paramString());

    if (e.getY() > keysPanel.blackHeight) {

      int pitchpixelnum = whitexfromedge % keysPanel.octaveWidth;
      // key from 1 - 7
      int whitepitchnum = (pitchpixelnum / keysPanel.whiteWidth) + 1;
      int pitchnum = -1;
      // used to get a number for Pitch.convertNumToPitch
      switch (whitepitchnum) {
        case 1:
          pitchnum = 0;
          break;
        case 2:
          pitchnum = 2;
          break;
        case 3:
          pitchnum = 4;
          break;
        case 4:
          pitchnum = 5;
          break;
        case 5:
          pitchnum = 7;
          break;
        case 6:
          pitchnum = 9;
          break;
        case 7:
          pitchnum = 11;
          break;
      }
      Pitch realpitch = Pitch.convertNumToPitch(pitchnum);
      tempmodel.addNote(new Note(realpitch, octavenum, 1), currentBeat);
      song = tempmodel.getSong();
      new AddAction().actionPerformed(a);
    }
    //black keys
    if (e.getY() <= keysPanel.blackHeight) {
      int pitchcheck = (e.getX() - keysPanel.whiteLeftPadding) % keysPanel.octaveWidth;
      int blackxfromedge = e.getX() - keysPanel.blackLeftPadding;
      // for first 2
      // checking if first two black keys
      if (pitchcheck < keysPanel.whiteWidth * 3) {
        int pitchpixelnum1 = blackxfromedge % keysPanel.octaveWidth;
        // if a valid black key press for first 2
        if (pitchpixelnum1 % keysPanel.whiteWidth < keysPanel.blackWidth) {
          // first black key
          if (pitchpixelnum1 / keysPanel.whiteWidth == 0) {
            Pitch pitch = Pitch.CSharp;
            tempmodel.addNote(new Note(pitch, octavenum, 1), currentBeat);
            song = tempmodel.getSong();
            new AddAction().actionPerformed(a);
          }
          // second black key
          else if (pitchpixelnum1 / keysPanel.whiteWidth == 1) {
            Pitch pitch = Pitch.DSharp;
            tempmodel.addNote(new Note(pitch, octavenum, 1), currentBeat);
            song = tempmodel.getSong();
            new AddAction().actionPerformed(a);
          }
        }
      }
      // for remaining 3
      else if ((pitchcheck < keysPanel.whiteWidth * 7) &&
              (pitchcheck > (keysPanel.whiteWidth * 3) + 14)) {
        int pitchpixelnum = (blackxfromedge % keysPanel.octaveWidth) - 14 -
                (keysPanel.whiteWidth * 2);
        if (pitchpixelnum % keysPanel.whiteWidth < keysPanel.blackWidth) {
          // third black key
          if (pitchpixelnum / keysPanel.whiteWidth == 0) {
            Pitch pitch = Pitch.FSharp;
            tempmodel.addNote(new Note(pitch, octavenum, 1), currentBeat);
            song = tempmodel.getSong();
            new AddAction().actionPerformed(a);
          }
          // fourth black key
          if (pitchpixelnum / keysPanel.whiteWidth == 1) {
            Pitch pitch = Pitch.GSharp;
            tempmodel.addNote(new Note(pitch, octavenum, 1), currentBeat);
            song = tempmodel.getSong();
            new AddAction().actionPerformed(a);
          }
          // fifth black key
          if (pitchpixelnum / keysPanel.whiteWidth == 2) {
            Pitch pitch = Pitch.ASharp;
            tempmodel.addNote(new Note(pitch, octavenum, 1), currentBeat);
            song = tempmodel.getSong();
            new AddAction().actionPerformed(a);
          }
        }
      }
    }
  }

  @Override
  public void setSong(ISong song) {
    this.song = song;
  }

  /**
   * The timer that starts when the "P" key is pressed. This triggers two other timers for the beat
   * moving and the music note being played. When resetting, the current beat is moved to -1,
   * as starting the timer will increase the beat by 1, making the currentBeat 0 as the playing
   * starts.
   */
  protected class CombinedTimer extends AbstractAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      MusicModel tempmodel = new MusicModel((Song)song);
      // prepares for first tick of timers
      // use else-ifs. didnt and triggered twice
      if (currentBeat == 0 && !movingTimer.isRunning() && !beatTimer.isRunning()) {
        currentBeat = -1;
        keysPanel.setBeat(-1);
        notesPanel.setBeat(-1);
        movingTimer.start();
        beatTimer.start();
      }

      // if music is playing and it is to be paused
      else if (currentBeat > 0 && currentBeat < tempmodel.getLastBeat() && movingTimer.isRunning()
              && beatTimer.isRunning()) {
        movingTimer.stop();
        beatTimer.stop();
      }

      // to play paused music
      else if (currentBeat > 0 && currentBeat < tempmodel.getLastBeat() && !movingTimer.isRunning()
              && !beatTimer.isRunning()) {
        movingTimer.start();
        beatTimer.start();
      }


      // if playing ended
      else if (currentBeat >= tempmodel.getLastBeat() && !movingTimer.isRunning()
              && !beatTimer.isRunning()) {
        currentBeat = -1;
        keysPanel.setBeat(-1);
        notesPanel.setBeat(-1);
        for (int i = 0 ; i <= (tempmodel.getEndBeat() - 26) ; i++) {
          notesPanel.scrollLeft();
        }
        movingTimer.start();
        beatTimer.start();
      }
    }
  }

  /**
   * The moving action of the keysPanel and the notes panel that is triggered using the movingTimer
   * Timer at a rate that matches the tempo.
   */
  protected class MovingAction extends AbstractAction implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent a) {
      MusicModel tempmodel = new MusicModel((Song)song);
      // to scroll
      if (currentBeat + 1 > rightBeat) {
        notesPanel.scrollRight();
        rightBeat++;
        leftBeat++;
      }
      keysPanel.addToBeat();
      notesPanel.addToBeat();
      currentBeat++;
      keysPanel.repaint();
      notesPanel.repaint();


      // if playing has ended
      if (currentBeat >= tempmodel.getLastBeat()) {
        movingTimer.stop();
        beatTimer.stop();
        midiPlayer.setBeat(0);
        leftBeat = 0;
        rightBeat = 33;
      }
    }
  }

  /**
   * The MIDI note player that is triggered using the beatTimer
   * Timer at a rate that matches the tempo.
   */
  protected class MusicPlayer extends AbstractAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent a) {
      Integer[] beats = song.getSong().keySet().toArray(new Integer[0]);
      int endBeat = beats[beats.length - 1];
      int tempo = song.getTempo();
      Note endNote = song.getSong().get(endBeat).get(0);

      //iterates through song playing each note
      //gets notes from the song
      List<Note> currBeat = song.getSong().getOrDefault(midiPlayer.getBeat(),
              new ArrayList<Note>());
      if (!currBeat.isEmpty()) {
        for (Note n : currBeat) {
          try {
            //plays the note
            midiPlayer.playNote(n.midiPitch(), n.getVolume(), n.getBeats(),
                    tempo, n.getInstrument());
          } catch (InvalidMidiDataException e) {
            e.printStackTrace();
          }
        }
      }
      midiPlayer.setBeat(midiPlayer.getBeat() + 1);
    }
  }

  /**
   * Moves the current beat to the starting beat. This is triggered by pressing the HOME
   * button on the keyboard.
   */
  protected class MoveToBeginning extends AbstractAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      while (currentBeat > 0) {
        new SubAction().actionPerformed(e);
      }
      keysPanel.setBeat(0);
      notesPanel.setBeat(0);
      keysPanel.repaint();
      notesPanel.repaint();
    }
  }

  /**
   * Moves the current beat to the last beat in the song. This is triggered by pressing the END
   * button on the keyboard.
   */
  protected class MoveToEnd extends AbstractAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      MusicModel tempmodel = new MusicModel((Song)song);
      for (int i = 0 ; i < tempmodel.getLastBeat() ; i++) {
        new AddAction().actionPerformed(e);
      }
      currentBeat = tempmodel.getLastBeat();
      keysPanel.setBeat(tempmodel.getLastBeat());
      notesPanel.setBeat(tempmodel.getLastBeat());
      keysPanel.repaint();
      notesPanel.repaint();
    }
  }
  
  // nothing should happen for other mouse events.
  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  // returns the action listeners of the Final View for testing purposes.
  protected CombinedTimer getcombinedtimer() {
    return new CombinedTimer();
  }

  protected MoveToBeginning gethometimer() {
    return new MoveToBeginning();
  }

  protected MoveToEnd getendtimer() {
    return new MoveToEnd();
  }
}