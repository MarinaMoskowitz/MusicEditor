package cs3500.music.view;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JPanel;

import cs3500.music.model.ISong;
import cs3500.music.model.MusicModel;
import cs3500.music.model.Note;
import cs3500.music.model.Song;

/**
 * Display the Notes of the model.Song. The starting beat of a note
 * is displayed as a Black Box; the beats for which the note is extended through are displayed as
 * green rectangles.
 */
public class NotesPanel extends JPanel {
  private ISong song;
  private int currentBeat;

  /**
   * A constructor for a notes panel.
   */
  public NotesPanel() {
    super();
    this.song = new Song();
    this.currentBeat = 0;
  }

  /**
   * A constructor for a notes panel that takes in a song.
   * @param s the song to be used
   */
  public NotesPanel(ISong s) {
    super();
    this.song = s;
    this.currentBeat = 0;
  }

  /**
   * Adds to the current beats of this panel.
   */
  protected void addToBeat() {
    this.currentBeat++;
  }

  /**
   * Subtracts from the current beats of this panel.
   */
  protected void subFromBeat() {
    this.currentBeat--;
  }

  protected void setBeat(int i) {
    this.currentBeat = i;
  }

  // some values to be used in creating the panel
  int leftnotemargin = 90;
  int rightnotemargin = 0;
  int rightpitchmargin = 90;
  int beatwidth = 30;
  int beatHeight = 18;

  /**
   * Creates the visuals for the notes panel.
   * @param g the graphics
   */
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.WHITE);
    MusicModel tempmodel = new MusicModel((Song)this.song);
    g2d.fillRect(0,0, 1100, 1000);

    g2d.setColor(Color.BLACK);
    Font notefont = new Font("Serif", Font.PLAIN, 20);
    g2d.setFont(notefont);
    if (!this.song.getSong().isEmpty()) {
      //flips the arraylist of pitches
      ArrayList<String> orig = new ArrayList<>(this.song.getPitches());
      ArrayList<String> pitchrange = new ArrayList<>();
      for (int i = orig.size() - 1 ; i >= 0 ; i--) {
        pitchrange.add(orig.get(i));
      }
      this.drawNotes(g2d, pitchrange, tempmodel);
      this.drawLines(g2d, pitchrange, tempmodel);
      this.drawPitch(g2d, pitchrange);
      this.paintLine(g2d, pitchrange);
    }
  }

  /**
   * ADDED IN HW 7!
   * Draws the notes.
   * @param g2d the Graphics
   */
  public void drawNotes(Graphics g2d, ArrayList<String> pitchrange, MusicModel tempmodel) {
    for (int i = 0 ; i <= tempmodel.getLastBeat() ; i++) {

      // for starting beats (black blocks)
      if (this.song.getSong().containsKey(i)) {
        for (Note n : this.song.getSong().get(i)) {
          g2d.setColor(Color.BLACK);
          g2d.fillRect(i * beatwidth + leftnotemargin, 34 + noteHeight(pitchrange, n),
                  beatwidth, beatHeight);
        }
      }

      // for trailing beats (green blocks)
      if (this.song.getSong().containsKey(i)) {
        for (Note n : this.song.getSong().get(i)) {
          int startingbeat = i + 1;
          int endbeat = i + n.getBeats();
          while (endbeat - startingbeat > 0) {
            g2d.setColor(Color.GREEN);
            g2d.fillRect((startingbeat) * beatwidth + leftnotemargin, 35 +
                            noteHeight(pitchrange, n),
                    beatwidth, beatHeight - 1);
            startingbeat++;
          }
        }
      }
    }
  }

  /**
   * ADDED IN HW 7!
   * Draw lines and beat in 4 seconds.
   * @param g2d the Graphics
   */
  public void drawLines(Graphics g2d, ArrayList<String> pitchrange, MusicModel tempmodel) {
    // draw line vertical
    g2d.setColor(Color.BLACK);
    for (int i = 0 ; i <= tempmodel.getLastBeat() + 3 ; i = i + 4) {
      g2d.drawLine(leftnotemargin + i * beatwidth, 34, leftnotemargin + i * beatwidth, 34 +
              pitchrange.size() * 18);
      g2d.drawString(Integer.toString(i), leftnotemargin + i * beatwidth, 32);
      if (leftnotemargin + i * beatwidth > rightnotemargin) {
        rightnotemargin = leftnotemargin + i * beatwidth;
      }
    }
    // draw line horizontal
    for (int i = 0; i < pitchrange.size(); i++) {
      g2d.drawLine(leftnotemargin, 34 + i * 18, rightnotemargin, 34 + i * 18);
    }
    g2d.drawLine(leftnotemargin, 34 + pitchrange.size() * 18, rightnotemargin, 34 +
            pitchrange.size() * 18);

    // draw white rect on left side to cover scrolling lines
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, rightpitchmargin, 1000);
  }


  /**
   * ADDED IN HW 7!
   * Draws the pitches.
   * @param g2d the Graphics
   */
  public void drawPitch(Graphics g2d, ArrayList<String> pitchrange) {
    g2d.setColor(Color.BLACK);
    for (int i = 0; i < pitchrange.size(); i++) {
      g2d.drawString(pitchrange.get(i), 35, 50 + (i * 18));
    }
  }

  /**
   * ADDED IN HW 7!
   * Paints the red line at the current beat.
   * @param g2d the Graphics
   */
  public void paintLine(Graphics g2d, ArrayList<String> pitchrange) {
    g2d.setColor(Color.RED);
    g2d.drawLine(leftnotemargin + currentBeat * beatwidth, 34, leftnotemargin +
            currentBeat * beatwidth, 34 + pitchrange.size() * 18);
  }

  /**
   * Calculates the height of the placement of the note.
   * @param pitchrange the range of pitches to be used
   * @param n the note for the height to be calculated for
   * @return the vertical placement of the note representation in the visual.
   */
  private static int noteHeight(ArrayList<String> pitchrange, Note n) {
    int beatHeight = 18;
    int totalHeight = 0;
    for (int i = 0 ; i < pitchrange.size() ; i++) {
      if (pitchrange.get(i).equals(n.getPitch().toString() + n.getOctave())) {
        totalHeight = i * beatHeight;
      }
    }
    return totalHeight;
  }

  /**
   * Scrolls the notes and the beats one beat width to the left.
   */
  protected void scrollLeft() {
    this.leftnotemargin = this.leftnotemargin + beatwidth;
  }

  /**
   * Scrolls the notes and beats one beat width to the right.
   */
  protected void scrollRight() {
    this.leftnotemargin = this.leftnotemargin - beatwidth;
  }
}