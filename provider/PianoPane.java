package cs3500.music.view;


import cs3500.music.model.BEATTYPE;
import cs3500.music.model.Note;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

/**
 * This is the piano pane in Gui view.
 */
public class PianoPane extends JPanel {

  private List<Note> noteList;
  private Note leftmost;
  private Note rightmost;
  private int measure;
  private int curMeasure;

  private static Map<Integer, Integer> whiteKeyMap = new HashMap<>();
  private static Map<Integer, Integer> blackKeyMap = new HashMap<>();

  /**
   * Create a default piano pane.
   */
  public PianoPane() {
    super();
    this.noteList = new ArrayList<>();
    this.leftmost = new Note("C", 3, 0, 1, 0, 0);
    this.rightmost = new Note("G", 4, 0, 1, 0, 0);
    this.measure = 38;
    this.curMeasure = 0;

    noteList.add(leftmost);
    noteList.add(rightmost);
  }

  public void setScore(List arr) {
    this.noteList = arr;
  }

  public void setLeftMostNote(Note note) {
    this.leftmost = note;
  }

  public void setRightMostNote(Note note) {
    this.rightmost = note;
  }

  public void setCurMeasure(int curMeasure) {
    this.curMeasure = curMeasure;
    this.repaint();
  }

  public void setMeasure(int measure) {
    this.measure = measure;
  }

  private ArrayList<ArrayList<BEATTYPE>> toArrBeatType() {

    ArrayList<ArrayList<BEATTYPE>> arr = new ArrayList<>();
    ArrayList<BEATTYPE> emptyArr = new ArrayList<>();

    for (int i = 0; i < measure; i++) {
      emptyArr.add(BEATTYPE.REST);
    }

    for (int i = 0; i <= rightmost.getInterval(leftmost); i++) {
      arr.add(new ArrayList<>(emptyArr));
    }

    for (Note note : noteList) {
      arr.get(note.getInterval(leftmost)).set(note.getHead(), BEATTYPE.HEAD);
      for (int i = 0; i < note.getSustain(); i++) {
        if (arr.get(note.getInterval(leftmost)).get(note.getHead() + i + 1).equals(BEATTYPE.REST)) {
          arr.get(note.getInterval(leftmost)).set(note.getHead() + i + 1, BEATTYPE.SUSTAIN);
        }
      }
    }

    return arr;
  }

  private static void setMap() {
    whiteKeyMap.put(0, 0);
    whiteKeyMap.put(2, 1);
    whiteKeyMap.put(4, 2);
    whiteKeyMap.put(5, 3);
    whiteKeyMap.put(7, 4);
    whiteKeyMap.put(9, 5);
    whiteKeyMap.put(11, 6);

    blackKeyMap.put(1, 0);
    blackKeyMap.put(3, 1);
    blackKeyMap.put(6, 3);
    blackKeyMap.put(8, 4);
    blackKeyMap.put(10, 5);
  }

  /**
   * Move the view to right by one measure.
   */
  public void moveRight() {
    if (curMeasure == measure - 1) {
      return;
    } else {
      curMeasure += 1;
    }
    repaint();
  }

  /**
   * Move the view to left by one measure.
   */
  public void moveLeft() {
    if (curMeasure == 0) {
      return;
    } else {
      curMeasure -= 1;
    }
    repaint();
  }

  /**
   * Move the view to the start.
   */
  public void moveHome() {
    this.curMeasure = 0;
    repaint();
  }

  /**
   * Move the view to the End.
   */
  public void moveEnd() {
    this.curMeasure = measure - 1;
    repaint();
  }

  /**
   * Render the view by the given ticks.
   *
   * @param ticks  midi ticks.
   */
  public void renderTicks(float ticks) {
    this.curMeasure = (int)ticks / 10;
    repaint();
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setBackground(Color.gray);

    ArrayList<ArrayList<BEATTYPE>> arrBeatType = this.toArrBeatType();
    setMap();
    int numNote = rightmost.getInterval(leftmost) + 1;
    int leftmostMidiNum = leftmost.getMidiNum();

    // draw the white keys' outline
    for (int i = 0; i < 70; i++) {
      g.setColor(Color.BLACK);
      g.drawRect(GuiConstant.PIANO_MARGIN + i * GuiConstant.WHITE_KEY_WIDTH, 1,
          GuiConstant.WHITE_KEY_WIDTH,
          GuiConstant.WHITE_KEY_HEIGHT);
      g.setColor(Color.white);
      g.fillRect(GuiConstant.PIANO_MARGIN + i * GuiConstant.WHITE_KEY_WIDTH + 1, 2,
          GuiConstant.WHITE_KEY_WIDTH - 1,
          GuiConstant.WHITE_KEY_HEIGHT - 1);
    }

    // fill the white keys
    for (int i = 0; i < numNote; i++) {
      if ((i + leftmostMidiNum) % 12 == 1 ||
          (i + leftmostMidiNum) % 12 == 3 ||
          (i + leftmostMidiNum) % 12 == 6 ||
          (i + leftmostMidiNum) % 12 == 8 ||
          (i + leftmostMidiNum) % 12 == 10) {
        // black keys
      } else if (!arrBeatType.get(i).get(curMeasure).equals(BEATTYPE.REST)) {
        g.setColor(Color.orange);
        g.fillRect(GuiConstant.PIANO_MARGIN +
                (i + leftmostMidiNum) / 12 * 7 * GuiConstant.WHITE_KEY_WIDTH +
                whiteKeyMap.get((i + leftmostMidiNum) % 12) * GuiConstant.WHITE_KEY_WIDTH + 1,
            2,
            GuiConstant.WHITE_KEY_WIDTH - 1,
            GuiConstant.WHITE_KEY_HEIGHT - 1);
      }
    }

    // draw the black keys' outline
    for (int i = 0; i < 10; i++) {
      for (int k = 0; k < 6; k++) {
        if (k != 2) {
          g.setColor(Color.black);
          g.drawRect(GuiConstant.PIANO_MARGIN + i * 7 * GuiConstant.WHITE_KEY_WIDTH +
                  k * GuiConstant.WHITE_KEY_WIDTH +
                  GuiConstant.WHITE_KEY_WIDTH - GuiConstant.BLACK_KEY_WIDTH / 2,
              1, GuiConstant.BLACK_KEY_WIDTH, GuiConstant.BLACK_KEY_HEIGHT);
        }
      }
    }

    // fill the black keys
    for (int i = 0; i < 10; i++) {
      for (int k = 0; k < 6; k++) {
        if (k != 2) {
          g.setColor(Color.black);
          g.fillRect(GuiConstant.PIANO_MARGIN + i * 7 * GuiConstant.WHITE_KEY_WIDTH +
                  k * GuiConstant.WHITE_KEY_WIDTH +
                  GuiConstant.WHITE_KEY_WIDTH - GuiConstant.BLACK_KEY_WIDTH / 2 + 1, 2,
              GuiConstant.BLACK_KEY_WIDTH - 1, GuiConstant.BLACK_KEY_HEIGHT - 1);
        }
      }
    }

    // fill the played black keys
    for (int i = 0; i < numNote; i++) {
      if ((i + leftmostMidiNum) % 12 == 1 ||
          (i + leftmostMidiNum) % 12 == 3 ||
          (i + leftmostMidiNum) % 12 == 6 ||
          (i + leftmostMidiNum) % 12 == 8 ||
          (i + leftmostMidiNum) % 12 == 10 ) {
        if (!arrBeatType.get(i).get(curMeasure).equals(BEATTYPE.REST)) {
          g.setColor(Color.orange);
          g.fillRect(GuiConstant.PIANO_MARGIN +
                  (i + leftmostMidiNum) / 12 * 7 * GuiConstant.WHITE_KEY_WIDTH +
                  (blackKeyMap.get((i + leftmostMidiNum) % 12)) * GuiConstant.WHITE_KEY_WIDTH +
                  GuiConstant.WHITE_KEY_WIDTH - GuiConstant.BLACK_KEY_WIDTH / 2 + 1, 2,
              GuiConstant.BLACK_KEY_WIDTH - 1, GuiConstant.BLACK_KEY_HEIGHT - 1);
        }
      }
    }
  }
}
