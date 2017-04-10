package cs3500.music.view;

import cs3500.music.model.Note;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * This is the Note Name Pane in Gui view.
 */
public class NoteNamePane extends JPanel {

  private Note leftmost;
  private Note rightmost;

  /**
   * Create a default pane.
   */
  public NoteNamePane() {
    this.leftmost = new Note("C", 3, 0, 2, 0, 0);
    this.rightmost = new Note("G", 4, 0, 1, 0, 0);
  }

  /**
   * Set the left most note.
   *
   * @param note left most note
   */
  public void setLeftMostNote(Note note) {
    this.leftmost = note;
  }

  /**
   * Set the right most note.
   *
   * @param note right most note
   */
  public void setRightMostNote(Note note) {
    this.rightmost = note;
    this.setPreferredSize(new Dimension(GuiConstant.TEXT_WIDTH + 10,
        (rightmost.getInterval(leftmost) + 3) * GuiConstant.NOTE_BLOCK_SIZE));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setBackground(Color.white);

    int numNote = rightmost.getInterval(leftmost) + 1;
    Note temp = leftmost;

    for (int i = numNote; i > 0; i--) {
      g.setColor(Color.black);
      g.drawString(temp.getName(), GuiConstant.TEXT_MARGIN,
          GuiConstant.NOTE_BLOCK_SIZE * i + 15);
      temp = temp.next();
    }
  }
}
