package cs3500.music.view;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import cs3500.music.model.IMusicModel;
import cs3500.music.model.ISong;
import cs3500.music.view.IView;
import cs3500.music.model.Note;
import cs3500.music.model.Song;

/**
 * ADDED IN HW 7!!
 * A class for the Text View of a song that implements the IView interface.
 */
public class TextView implements IView {
  private IMusicModel model;
  private ISong song;
  private Appendable out;


  /**
   * Constructor for testing purposes.
   */
  public TextView(Appendable out) {
    this.out = out;
  }

  /**
   * Default constructor.
   */
  public TextView() {
    this.out = System.out;
  }

  /**
   * Shows the music as a single string displayed as a view.
   */
  public void play() {
    //System.out.println(model.getSong().getBeats());
    Song song = model.getSong();
    if (song.getBeats().isEmpty()) {
      throw new IllegalArgumentException("There is no song to print");
    }
    StringBuilder acc = this.makeHead();
    Note minNote = song.minNote();
    Integer lastBeat = model.getLastBeat();
    int beatLength = lastBeat.toString().length();
    int lineLength = this.makeHead().length();
    List<StringBuilder> display = new ArrayList<>();
    for (int i = 0; i <= lastBeat; i++) {
      display.add(new StringBuilder(String.format("%" + (lineLength - 1) + "s" + "\n", "")));
      display.get(i).replace(0, beatLength, String.format("%" + beatLength + "s", i));
    }
    for (Integer i : song.getBeats()) {
      for (Note n : song.getNotes(i)) {
        int pos = 5 * ((12 * (n.getOctave() - 1)) + n.getPitch().ordinal() -
                ((12 * (minNote.getOctave() - 1)) + minNote.getPitch().ordinal())) + beatLength + 2;
        display.get(i).replace(pos, pos + 1, "X");

        for (int q = 1; q < n.getBeats(); q++) {
          display.get(i + q).replace(pos, pos + 1, "|");
        }
      }
    }
    for (StringBuilder str : display) {
      acc.append(str);
    }

    try {
      out.append(acc.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates the first line of the music to be shown in the console.
   *
   * @return the first line of the music being shown
   * @throws new IllegalArgumentException if if doesn't do what it should :(
   */
  private StringBuilder makeHead() {
    Integer endBeat = model.getEndBeat();
    int beatLength = endBeat.toString().length();
    StringBuilder head = new StringBuilder();
    for (int i = 0; i < beatLength; i++) {
      head.append(' ');
    }
    List<String> notes = song.getPitches();
    for (String s : notes) {
      int spaces;
      boolean moreSpaces = true;
      switch (s.length()) {
        case 2:
          spaces = 2;
          break;
        case 3:
          spaces = 1;
          break;
        case 4:
          spaces = 1;
          moreSpaces = false;
          break;
        case 5:
          spaces = 0;
          moreSpaces = false;
          break;
        default:
          throw new IllegalArgumentException();
      }
      for (int i = 0; i < spaces; i++) {
        head.append(' ');
      }
      head.append(s);
      if (moreSpaces) {
        head.append(' ');
      }
    }
    return head.append('\n');
  }

  @Override
  public void setModel(IMusicModel model) {
    this.model = model;
  }

  @Override
  public void setSong(ISong song) {
    this.song = song;
  }

}
