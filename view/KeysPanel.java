package cs3500.music.view;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

import cs3500.music.model.ISong;
import cs3500.music.model.Note;
import cs3500.music.model.Pitch;
import cs3500.music.model.Song;

/**
 * The panel that represents the keys that are currently being played.
 */
public class KeysPanel extends JPanel {
  private ISong song;
  private int currentBeat;
  protected int whiteWidth = 14;
  protected int blackWidth = 7;
  protected int whiteHeight = 100;
  protected int blackHeight = 55;
  protected int octaveWidth = whiteWidth * 7;
  protected int whiteLeftPadding = 59;
  protected int blackLeftPadding = 70;
  /**
   * A constructor for the key panel.
   */
  public KeysPanel() {
    super();
    this.song = new Song();
    this.currentBeat = 0;
  }

  /**
   * A constructor that sets the song for the key panel.
   * @param s the song to use
   */
  public KeysPanel(ISong s) {
    super();
    this.song = s;
    this.currentBeat = 0;
  }

  /**
   * Adds to the current beats of the panel.
   */
  protected void addToBeat() {
    this.currentBeat++;
  }

  /**
   * Sets the current beat for this view.
   * @param i  the current beat
   */
  protected void setBeat(int i) {
    this.currentBeat = i;
  }

  /**
   * Subtracts from the current beats of the panel.
   */
  protected void subFromBeat() {
    this.currentBeat--;
  }

  /**
   * Creates the visuals for the panel.
   * @param g the graphics
   */
  protected void paintComponent(Graphics g) {
    int whiteWidth = 14;
    int blackWidth = 7;
    int whiteHeight = 100;
    int blackHeight = 55;
    int octaveWidth = whiteWidth * 7;

    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.BLACK);

    ArrayList<Note> notesstarting = new ArrayList<>();
    if (this.song.hasBeat(currentBeat)) {
      for (Note n : this.song.getNotes(currentBeat)) {
        notesstarting.add(n);
      }
    }

    // octaves
    for (int i = 1; i <= 10; i++) {
      //white keys
      for (int j = 1; j <= 7; j++) {
        boolean iswhiteplaying = false;
        boolean iswhitetrailplaying = false;

        int leftpadding = 59;
        if (this.song.hasBeat(currentBeat)) {
          for (Note n : notesstarting) {
            // if the note is being started, put a yellow rectangle instead of white
            if (i == n.getOctave() && j == whitePitchToKeyNum(n.getPitch())) {
              g2d.setColor(Color.YELLOW);
              g2d.fillRect(leftpadding + (octaveWidth * (i - 1)) + ((j - 1) * whiteWidth), 0,
                      whiteWidth, whiteHeight);
              g2d.setColor(Color.BLACK);
              g2d.drawRect(leftpadding + (octaveWidth * (i - 1)) + ((j - 1) * whiteWidth), 0,
                      whiteWidth, whiteHeight);
              iswhiteplaying = true;
            }
          }
        }
        // if the note is being trailed, also put a yellow rectangle
        for (int x = 0 ; x < this.currentBeat ; x++) {
          if (this.song.getSong().containsKey(x)) {
            for (Note n : this.song.getSong().get(x)) {
              if (x + n.getBeats() > this.currentBeat && i == n.getOctave() &&
                      j == whitePitchToKeyNum(n.getPitch())) {
                g2d.setColor(Color.YELLOW);
                g2d.fillRect(leftpadding + (octaveWidth * (i - 1)) + ((j - 1) * whiteWidth), 0,
                        whiteWidth, whiteHeight);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(leftpadding + (octaveWidth * (i - 1)) + ((j - 1) * whiteWidth), 0,
                        whiteWidth, whiteHeight);
                iswhitetrailplaying = true;
              }
            }
          }
        }

        if (!iswhiteplaying && !iswhitetrailplaying) {
          g2d.setColor(Color.WHITE);
          g2d.fillRect(leftpadding + (octaveWidth * (i - 1)) + ((j - 1) * whiteWidth), 0,
                  whiteWidth, whiteHeight);
          g2d.setColor(Color.BLACK);
          g2d.drawRect(leftpadding + (octaveWidth * (i - 1)) + ((j - 1) * whiteWidth), 0,
                  whiteWidth, whiteHeight);
        }
      }
      //black keys
      for (int k = 1; k <= 5; k++) {
        // left 2 black keys
        if (k < 3) {
          boolean isblackplaying = false;
          boolean isblacktrailplaying = false;
          int leftpadding = 70;
          if (this.song.hasBeat(currentBeat)) {
            for (Note n : notesstarting) {

              // if the note is being played, put a yellow rectangle instead of white
              if (i == n.getOctave() && k == blackPitchToKeyNum(n.getPitch())) {
                g2d.setColor(Color.YELLOW);
                g2d.fillRect(leftpadding + (octaveWidth * (i - 1)) + ((k - 1) * whiteWidth), 0,
                        blackWidth, blackHeight);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(leftpadding + (octaveWidth * (i - 1)) + ((k - 1) * whiteWidth), 0,
                        blackWidth, blackHeight);
                isblackplaying = true;
              }
            }
          }

          for (int x = 0 ; x < this.currentBeat ; x++) {
            if (this.song.getSong().containsKey(x)) {
              for (Note n : this.song.getSong().get(x)) {
                if (x + n.getBeats() > this.currentBeat && i == n.getOctave() &&
                        k == blackPitchToKeyNum(n.getPitch())) {
                  g2d.setColor(Color.YELLOW);
                  g2d.fillRect(leftpadding + (octaveWidth * (i - 1)) + ((k - 1) * whiteWidth), 0,
                          blackWidth, blackHeight);
                  g2d.setColor(Color.BLACK);
                  g2d.drawRect(leftpadding + (octaveWidth * (i - 1)) + ((k - 1) * whiteWidth), 0,
                          blackWidth, blackHeight);
                  isblacktrailplaying = true;
                }
              }
            }
          }

          if (!isblackplaying && !isblacktrailplaying) {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(leftpadding + (octaveWidth * (i - 1)) + ((k - 1) * whiteWidth), 0,
                    blackWidth, blackHeight);
          }
        }
        // right 3 black keys
        else {
          boolean isblackplaying = false;
          boolean isblacktrailplaying = false;
          int leftpadding = 84;
          if (this.song.hasBeat(currentBeat)) {
            for (Note n : notesstarting) {

              // if the note is being played, put a yellow rectangle instead of white
              if (i == n.getOctave() && k == blackPitchToKeyNum(n.getPitch())) {
                g2d.setColor(Color.YELLOW);
                g2d.fillRect(leftpadding + (octaveWidth * (i - 1)) + ((k - 1) * whiteWidth), 0,
                        blackWidth, blackHeight);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(leftpadding + (octaveWidth * (i - 1)) + ((k - 1) * whiteWidth), 0,
                        blackWidth, blackHeight);
                isblackplaying = true;
              }
            }
          }

          for (int x = 0 ; x < this.currentBeat ; x++) {
            if (this.song.getSong().containsKey(x)) {
              for (Note n : this.song.getSong().get(x)) {
                if (x + n.getBeats() > this.currentBeat && i == n.getOctave() &&
                        k == blackPitchToKeyNum(n.getPitch())) {
                  g2d.setColor(Color.YELLOW);
                  g2d.fillRect(leftpadding + (octaveWidth * (i - 1)) + ((k - 1) * whiteWidth), 0,
                          blackWidth, blackHeight);
                  g2d.setColor(Color.BLACK);
                  g2d.drawRect(leftpadding + (octaveWidth * (i - 1)) + ((k - 1) * whiteWidth), 0,
                          blackWidth, blackHeight);
                  isblacktrailplaying = true;
                }
              }
            }
          }

          if (!isblackplaying && !isblacktrailplaying) {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(leftpadding + (octaveWidth * (i - 1)) + ((k - 1) * whiteWidth), 0,
                    blackWidth, blackHeight);
          }
        }
      }
    }
  }

  /**
   * Changes the pitch's ordinal to the proper placement of the white key in the view
   * representation.
   * @param p the pitch to use
   * @return the order number of the key
   */
  private static int whitePitchToKeyNum(Pitch p) {
    switch (p.ordinal()) {
      case 0:
        return 1;
      case 1:
        return 0;
      case 2:
        return 2;
      case 3:
        return 0;
      case 4:
        return 3;
      case 5:
        return 4;
      case 6:
        return 0;
      case 7:
        return 5;
      case 8:
        return 0;
      case 9:
        return 6;
      case 10:
        return 0;
      case 11:
        return 7;
      default:
        throw new IllegalArgumentException("Invalid Pitch");
    }
  }

  /**
   * Changes the pitch's ordinal to the proper placement of the black key in the view
   * representation.
   * @param p the pitch to use
   * @return the order number of the key
   */
  private int blackPitchToKeyNum(Pitch p) {
    switch (p.ordinal()) {
      case 0:
        return 0;
      case 1:
        return 1;
      case 2:
        return 0;
      case 3:
        return 2;
      case 4:
        return 0;
      case 5:
        return 0;
      case 6:
        return 3;
      case 7:
        return 0;
      case 8:
        return 4;
      case 9:
        return 0;
      case 10:
        return 5;
      case 11:
        return 0;
      default:
        throw new IllegalArgumentException("Invalid Pitch");
    }
  }
}