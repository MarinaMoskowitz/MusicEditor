package cs3500.music.view;

import cs3500.music.model.BEATTYPE;
import cs3500.music.model.Note;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * To represent the console view.
 */
public class ConsoleView implements IView<Note> {

  private int beats;
  private Note left;
  private Note right;
  private List<Note> arrNotes;
  private Appendable out;

  /**
   * Constructs a view with an empty model.
   */
  public ConsoleView() {
    this.out = System.out;

    this.beats = 24;
    this.left = new Note("C", 3, 0, 1, 0, 0);
    this.right = new Note("C", 4, 0, 1, 0, 0);
    this.arrNotes = new ArrayList<>();
  }

  public ConsoleView(Appendable output) {
    this.out = output;
  }

  @Override
  public void setScore(List<Note> arr) {
    this.arrNotes = arr;

  }

  @Override
  public void setLeftMostNote(Note note) {
    this.left = note;
  }

  @Override
  public void setRightMostNote(Note note) {
    this.right = note;
  }

  @Override
  public void setCurMeasure(int curMeasure) {
    //
  }

  @Override
  public void setBeats(int beats) {
    this.beats = beats;
  }

  @Override
  public void refresh() {
    // do nothing
  }

  @Override
  public void display() {
    try {
      this.out.append(printScore());
    } catch (IOException e) {
      e.getMessage();
    }
  }

  private String printScore() {
    String str = printHeadline();

    ArrayList<ArrayList<BEATTYPE>> arrOfType = converToArrBeatType();

    for (int i = 0; i < beats; i++) {
      str += lineNumber(i);
      for (ArrayList<BEATTYPE> arr : arrOfType) {
        if (arr.get(i).equals(BEATTYPE.REST)) {
          str += "     ";
        } else if (arr.get(i).equals(BEATTYPE.HEAD)) {
          str += "  X  ";
        } else if (arr.get(i).equals(BEATTYPE.SUSTAIN)) {
          str += "  |  ";
        }
      }
      str += "\n";
    }

    return str;
  }

  // format the linenumber coordinate with the digits of number of beats
  private String lineNumber(int number) {

    if (beats < 11) {
      return Integer.toString(number);
    } else if (beats < 101) {
      if (number < 10) {
        return " " + Integer.toString(number);
      } else {
        return Integer.toString(number);
      }
    } else if (beats < 1001) {
      if (number < 10) {
        return "  " + Integer.toString(number);
      } else if (number < 100) {
        return " " + Integer.toString(number);
      } else {
        return Integer.toString(number);
      }
    } else {
      if (number < 10) {
        return "   " + Integer.toString(number);
      } else if (number < 100) {
        return "  " + Integer.toString(number);
      } else if (number < 1000) {
        return " " + Integer.toString(number);
      } else {
        return Integer.toString(number);
      }
    }
  }

  // print the headline of the score
  private String printHeadline() {
    String str = "";
    if (Integer.toString(beats).length() == 1) {
      str += " ";
    } else if (Integer.toString(beats).length() == 2) {
      str += "  ";
    } else if (Integer.toString(beats).length() == 3) {
      str += "   ";
    } else if (Integer.toString(beats).length() == 4) {
      str += "     ";
    }
    Note temp = left;
    while (right.getInterval(left) >= 0) {
      if (left.getName().length() == 2) {
        str += "  " + left.getName() + " ";
      } else {
        str += " " + left.getName() + " ";
      }

      left = left.next();
    }

    left = temp;
    return str + "\n";
  }

  // convert a list of notes to an arraylist of arraylist of BEATTYPE
  private ArrayList<ArrayList<BEATTYPE>> converToArrBeatType() {
    setLeftmostNote();
    setRightmostNote();

    ArrayList<ArrayList<BEATTYPE>> arr = new ArrayList<>();
    ArrayList<BEATTYPE> emptyArr = new ArrayList<>(beats);

    for (int i = 0; i < beats; i++) {
      emptyArr.add(BEATTYPE.REST);
    }

    for (int i = 0; i <= right.getInterval(left); i++) {
      arr.add(new ArrayList<>(emptyArr));
    }

    for (Note note : arrNotes) {
      arr.get(note.getInterval(left)).set(note.getHead(), BEATTYPE.HEAD);
      for (int i = 0; i < note.getSustain(); i++) {
        if (arr.get(note.getInterval(left)).get(note.getHead() + i + 1).equals(BEATTYPE.REST)) {
          arr.get(note.getInterval(left)).set(note.getHead() + i + 1, BEATTYPE.SUSTAIN);
        }
      }
    }

    return arr;
  }

  // set the leftmost note to the lowest pitch note in the model
  private void setLeftmostNote() {
    if (arrNotes.size() == 0) {
      // return
    } else {
      left = arrNotes.get(0);
      for (Note note : arrNotes) {
        if (note.getInterval(left) < 0) {
          left = note;
        }
      }
    }
  }

  // set the rightmost note to the highest pitch note in the model
  private void setRightmostNote() {
    if (arrNotes.size() == 0) {
      // return
    } else {
      right = arrNotes.get(0);
      for (Note note : arrNotes) {
        if (note.getInterval(right) > 0) {
          right = note;
        }
      }

      if (right.getInterval(left) < 7) {
        Note temp = left;
        for (int i = 0; i < 7; i++) {
          right = temp.next();
        }
      }
    }
  }
}
