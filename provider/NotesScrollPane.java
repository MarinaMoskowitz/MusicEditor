package cs3500.music.view;

import cs3500.music.model.BEATTYPE;
import cs3500.music.model.Note;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * This is the Notes pane in Gui view.
 */
public class NotesScrollPane extends JPanel {

  private List<Note> noteList;
  private Note leftmost;
  private Note rightmost;
  private int  measure;
  private int  barline;

  /**
   * Create a default NotesScrollPane.
   */
  public NotesScrollPane() {
    super();
    this.noteList = new ArrayList<>();
    this.leftmost = new Note("C", 3, 0, 1, 0, 0);
    this.rightmost = new Note("G", 4, 0, 1, 0, 0);
    this.measure = 38;
    this.barline = 0;
  }

  public int getMeasure() {
    return measure;
  }

  // set arr
  public void setScore(List arr) {
    this.noteList = arr;
  }

  // set the leftmost
  public void setLeftMostNote(Note note) {
    leftmost = note;
  }

  // set the rightmost
  public void setRightMostNote(Note note) {
    rightmost = note;
  }

  // set current measure
  public void setCurMeasure(int curMeasure) {
    this.barline = curMeasure;
    this.repaint();
  }

  /**
   * Set the measure to the view.
   *
   * @param measure measure
   */
  public void setMeasure(int measure) {
    this.measure = measure + 1;
    this.setPreferredSize(new Dimension((measure + 2) * GuiConstant.NOTE_BLOCK_SIZE,
        (rightmost.getInterval(leftmost) + 3) * GuiConstant.NOTE_BLOCK_SIZE));
  }

  /*-----------------------------------------------------------------------------------------*/

  /**
   * Move the view to right by one measure.
   */
  public void moveRight() {
    if (barline == measure - 1) {
      return;
    } else {
      barline += 1;
    }
    repaint();
  }

  /**
   * Move the view to left by one measure.
   */
  public void moveLeft() {
    if (barline == 0) {
      return;
    } else {
      barline -= 1;
    }
    repaint();
  }

  /**
   * Move the view to the start.
   */
  public void moveHome() {
    this.barline = 0;
    repaint();
  }

  /**
   * Move the view to the End.
   */
  public void moveEnd() {
    this.barline = measure - 1;
    repaint();
  }

  /**
   * Render the view by the given ticks.
   *
   * @param ticks  midi ticks.
   */
  public void renderTicks(float ticks) {
    this.barline = (int)ticks / 10;
    repaint();
  }

  /**
   * get the pitch of the note at position x.
   *
   * @param x position y
   */
  public int getNoteMeasureAt(int x) {
    x -= 10;
    x /= GuiConstant.NOTE_BLOCK_SIZE;
    return x;
  }

  /**
   * get the pitch of the note at position y.
   *
   * @param y position y
   */
  public String getNotePitchAt(int y) {
    y -= 20;
    int numNotes = rightmost.getInterval(leftmost) + 1;
    int interval = (numNotes * GuiConstant.NOTE_BLOCK_SIZE - y) / GuiConstant.NOTE_BLOCK_SIZE;
    Note temp = leftmost;
    for (int i = 0; i < interval; i++) {
      temp = temp.next();
    }

    return temp.getName().length() == 2 ?
        temp.getName().substring(0, 1) : temp.getName().substring(0, 2);
  }

  /**
   * get the octave of the note at position y.
   *
   * @param y position y
   */
  public int getNoteOctaveAt(int y) {
    y -= 20;
    int numNotes = rightmost.getInterval(leftmost) + 1;
    int interval = (numNotes * GuiConstant.NOTE_BLOCK_SIZE - y) / GuiConstant.NOTE_BLOCK_SIZE;
    Note temp = leftmost;
    for (int i = 0; i < interval; i++) {
      temp = temp.next();
    }

    return temp.getName().length() == 2 ?
        Integer.parseInt(temp.getName().substring(1)) :
        Integer.parseInt(temp.getName().substring(2));
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

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setBackground(Color.white);

    ArrayList<ArrayList<BEATTYPE>> arrBeatType = this.toArrBeatType();
    int numNote = rightmost.getInterval(leftmost) + 1;

    // draw the vertical boundary
    g.drawLine(10 , 20,
        10, numNote * GuiConstant.NOTE_BLOCK_SIZE + 20);
    // draw the horizontal boundary
    g.drawLine(10, 20,
        GuiConstant.NOTE_BLOCK_SIZE * (measure - 1) + 10, 20);

    // draw the last divider
    g.drawLine((measure - 1) * GuiConstant.NOTE_BLOCK_SIZE + 10, 20,
        (measure - 1) * GuiConstant.NOTE_BLOCK_SIZE + 10,
        numNote * GuiConstant.NOTE_BLOCK_SIZE + 20);

    // draw the measure number
    for (int i = 1; i < measure; i++) {
      if ((i - 1) % 4 == 0) {
        g.setColor(Color.BLACK);
        g.drawString(Integer.toString(i),
            (i - 1) * GuiConstant.NOTE_BLOCK_SIZE + GuiConstant.NOTE_BLOCK_SIZE / 2,
            GuiConstant.NOTE_BLOCK_SIZE - 5);
      }
    }


    // draw the notes
    for (int i = 0; i < numNote; i++) {
      for (int j = 0; j < measure; j++) {
        if (arrBeatType.get(i).get(j).equals(BEATTYPE.HEAD)) {
          g.setColor(Color.BLACK);
          g.fillRect(10 + j * GuiConstant.NOTE_BLOCK_SIZE + GuiConstant.LINE_PIXEL,
              20 + (numNote - i - 1) * GuiConstant.NOTE_BLOCK_SIZE + GuiConstant.LINE_PIXEL,
              GuiConstant.NOTE_BLOCK_SIZE,
              GuiConstant.NOTE_BLOCK_SIZE - 1);
        }
        if (arrBeatType.get(i).get(j).equals(BEATTYPE.SUSTAIN)
            && j + 1 == measure) {
          g.setColor(Color.GREEN);
          g.fillRect(10 + j * GuiConstant.NOTE_BLOCK_SIZE + GuiConstant.LINE_PIXEL,
              20 + (numNote - i - 1) * GuiConstant.NOTE_BLOCK_SIZE + GuiConstant.LINE_PIXEL,
              GuiConstant.NOTE_BLOCK_SIZE,
              GuiConstant.NOTE_BLOCK_SIZE - 1);
        }

        if (arrBeatType.get(i).get(j).equals(BEATTYPE.SUSTAIN)
            && arrBeatType.get(i).get(j + 1).equals(BEATTYPE.SUSTAIN)) {
          g.setColor(Color.GREEN);
          g.fillRect(10 + j * GuiConstant.NOTE_BLOCK_SIZE + GuiConstant.LINE_PIXEL,
              20 + (numNote - i - 1) * GuiConstant.NOTE_BLOCK_SIZE + GuiConstant.LINE_PIXEL,
              GuiConstant.NOTE_BLOCK_SIZE,
              GuiConstant.NOTE_BLOCK_SIZE - 1);
        }
        if (arrBeatType.get(i).get(j).equals(BEATTYPE.SUSTAIN)) {
          g.setColor(Color.GREEN);
          g.fillRect(10 + j * GuiConstant.NOTE_BLOCK_SIZE + GuiConstant.LINE_PIXEL,
              20 + (numNote - i - 1) * GuiConstant.NOTE_BLOCK_SIZE + GuiConstant.LINE_PIXEL,
              GuiConstant.NOTE_BLOCK_SIZE - 1,
              GuiConstant.NOTE_BLOCK_SIZE - 1);
        }
      }
    }

    // draw the horizontal divider
    for (int i = 1; i < numNote + 1; i++) {
      g.setColor(Color.BLACK);
      if ((rightmost.getMidiNum() - i) % 12 == 11) {
        g.drawLine(10, i * GuiConstant.NOTE_BLOCK_SIZE + 19,
            GuiConstant.NOTE_BLOCK_SIZE * (measure - 1) + 10,
            i * GuiConstant.NOTE_BLOCK_SIZE + 19);
        g.drawLine(10, i * GuiConstant.NOTE_BLOCK_SIZE + 20,
            GuiConstant.NOTE_BLOCK_SIZE * (measure - 1) + 10,
            i * GuiConstant.NOTE_BLOCK_SIZE + 20);
      } else {
        g.drawLine(10, i * GuiConstant.NOTE_BLOCK_SIZE + 20,
            GuiConstant.NOTE_BLOCK_SIZE * (measure - 1) + 10,
            i * GuiConstant.NOTE_BLOCK_SIZE + 20);
      }
    }

    // draw the vertical divider
    for (int i = 1; i < measure; i++) {
      if (i % 4 == 0) {
        g.setColor(Color.BLACK);
        g.drawLine(i * GuiConstant.NOTE_BLOCK_SIZE + 10, 20,
            i * GuiConstant.NOTE_BLOCK_SIZE + 10,
            numNote * GuiConstant.NOTE_BLOCK_SIZE + 20);
      }
    }

    // draw the barline
    g.setColor(Color.red);
    g.fillRect(10 + barline * GuiConstant.NOTE_BLOCK_SIZE, 20,
        GuiConstant.BAR_LINE_WIDTH,
        numNote * GuiConstant.NOTE_BLOCK_SIZE + 1);
  }
}
