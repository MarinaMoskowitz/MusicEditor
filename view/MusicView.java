package cs3500.music.view;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;
import cs3500.music.model.IMusicModel;
import cs3500.music.model.ISong;
import cs3500.music.model.MusicModel;
import cs3500.music.model.Song;

/**
 * A View for the Music Player
 */
public class MusicView extends JFrame implements IMusicView {
  protected KeysPanel keysPanel;
  protected NotesPanel notesPanel;
  protected ISong song;
  protected int currentBeat;
  protected int rightBeat = 33;
  protected int leftBeat = 0;
  protected IMidiView midiPlayer;
  // added in HW 7!!
  private IMusicModel model;

  /**
   * Regular constructor for a music view.
   */
  public MusicView() {
    super();
    this.setTitle("Music Visualizer");
    this.setSize(1000,1200);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.setLayout(new BorderLayout());
    notesPanel = new NotesPanel();
    keysPanel = new KeysPanel();

    notesPanel.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "subbeats");
    notesPanel.getActionMap().put("subbeats", new SubAction());
    notesPanel.getInputMap().put(KeyStroke.getKeyStroke("RIGHT") , "addbeats");
    notesPanel.getActionMap().put("addbeats", new AddAction());

    keysPanel.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "subbeats");
    keysPanel.getActionMap().put("subbeats", new SubAction());
    keysPanel.getInputMap().put(KeyStroke.getKeyStroke("RIGHT") , "addbeats");
    keysPanel.getActionMap().put("addbeats", new AddAction());

    notesPanel.getInputMap().put(KeyStroke.getKeyStroke("T") , "play");
    notesPanel.getActionMap().put("play", new PlayAction());

    this.currentBeat = 0;
    notesPanel.setPreferredSize(new Dimension(800,1000));
    keysPanel.setPreferredSize(new Dimension(1000,150));

    this.add(notesPanel,BorderLayout.NORTH);
    this.add(keysPanel,BorderLayout.SOUTH);

    this.pack();
  }

  /**
   * Music View that takes in a song to use.
   * @param s the song to be used
   */
  public MusicView(ISong s) {
    super();
    this.setTitle("Music Visualizer");
    this.setSize(1100,3000);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.song = s;
    this.midiPlayer = new MidiViewImpl(s);
    this.setLayout(new BorderLayout());
    notesPanel = new NotesPanel(s);
    keysPanel = new KeysPanel(s);

    notesPanel.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "subbeats");
    notesPanel.getActionMap().put("subbeats", new SubAction());
    notesPanel.getInputMap().put(KeyStroke.getKeyStroke("RIGHT") , "addbeats");
    notesPanel.getActionMap().put("addbeats", new AddAction());

    keysPanel.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "subbeats");
    keysPanel.getActionMap().put("subbeats", new SubAction());
    keysPanel.getInputMap().put(KeyStroke.getKeyStroke("RIGHT") , "addbeats");
    keysPanel.getActionMap().put("addbeats", new AddAction());

    notesPanel.getInputMap().put(KeyStroke.getKeyStroke("O") , "play");
    notesPanel.getActionMap().put("play", new PlayAction());

    this.currentBeat = 0;
    // cap height at 500 so keys can be seen
    if (50 + (s.getPitches().size()) * 18 > 485){
      notesPanel.setPreferredSize(new Dimension(800, 485));
    }
    else {
      notesPanel.setPreferredSize(new Dimension(800, 50 + (s.getPitches().size())
              * 18));
    }
    keysPanel.setPreferredSize(new Dimension(1100,150));

    this.add(notesPanel,BorderLayout.NORTH);
    this.add(keysPanel,BorderLayout.SOUTH);

    this.pack();
  }

  public int getCurrentBeat() {
    return this.currentBeat;
  }

  /**
   * What should happen when the right key is pressed.
   */
  public class AddAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
      MusicModel tempmodel = new MusicModel((Song)song);
      if (currentBeat < tempmodel.getLastBeat()) {
        if (currentBeat + 1 > rightBeat) {
          notesPanel.scrollRight();
          rightBeat++;
          leftBeat++;
        }
        keysPanel.addToBeat();
        notesPanel.addToBeat();
        currentBeat++;
        midiPlayer.setBeat(midiPlayer.getBeat() + 1);
        refresh();
      }
    }
  }

  /**
   * What should happen when the left key is pressed.
   */
  public class SubAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (currentBeat > 0) {
        //scrolling
        if (currentBeat - 1 < leftBeat) {
          notesPanel.scrollLeft();
          rightBeat--;
          leftBeat--;
        }
        keysPanel.subFromBeat();
        notesPanel.subFromBeat();
        currentBeat--;
        midiPlayer.setBeat(midiPlayer.getBeat() - 1);
        refresh();
      }
    }
  }

  /**
   * What should happen when the P key is pressed.
   */
  public class PlayAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
      midiPlayer.play();
    }
  }

  @Override
  public void play() {
    this.refresh();
    this.setVisible(true);
  }

  @Override
  public void setModel(IMusicModel model) {
    this.model = model;
  }

  @Override
  public void refresh() {
    this.repaint();
  }

  @Override
  public ISong getSong() {
    return this.song;
  }

  @Override
  public void setSong(ISong song) {
    this.song = song;
  }
}
