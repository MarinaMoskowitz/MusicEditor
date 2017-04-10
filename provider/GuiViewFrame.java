package cs3500.music.view;

import cs3500.music.model.Note;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

/**
 * A skeleton Frame (i.e., a window) in Swing
 */
public class GuiViewFrame extends javax.swing.JFrame implements IView<Note> {

  private JScrollPane scrollPane;
  private JScrollPane scrollPane2;

  private NotesScrollPane notesPane;
  private NoteNamePane namePane;
  private PianoPane pianoPane;

  private KeyListener keyListener;

  /**
   * Configure the gui.
   */
  public GuiViewFrame() {
    super();

    // basic config
    this.setTitle("Music Editor");
    this.setSize(GuiConstant.WINDOW_WIDTH,GuiConstant.WINDOW_HEIGHT);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //
    JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());
    contentPane.setPreferredSize(new Dimension(GuiConstant.WINDOW_WIDTH,
        GuiConstant.WINDOW_HEIGHT));
    this.setContentPane(contentPane);

    //
    notesPane = new NotesScrollPane();
    notesPane.setPreferredSize(new Dimension(
        (notesPane.getMeasure() + 2) * GuiConstant.NOTE_BLOCK_SIZE, 800));

    // scroll
    scrollPane = new JScrollPane();
    scrollPane.setPreferredSize(new Dimension(600, 600));
    this.getContentPane().add(scrollPane, BorderLayout.CENTER);
    scrollPane.setViewportView(notesPane);

    // name
    namePane = new NoteNamePane();
    // namePane.setPreferredSize(new Dimension(GuiConstant.TEXT_WIDTH + 20, 600));
    scrollPane2 = new JScrollPane();
    scrollPane2.setViewportView(namePane);
    this.getContentPane().add(scrollPane2, BorderLayout.WEST);

    // piano
    pianoPane = new PianoPane();
    pianoPane.setPreferredSize(new Dimension(800, 300));
    this.getContentPane().add(pianoPane, BorderLayout.SOUTH);

    scrollPane.revalidate();
    scrollPane.repaint();
    pianoPane.revalidate();
    pianoPane.repaint();
    this.scrollPane.setFocusable(true);
  }

  /**
   * Add a key listener to the view.
   *
   * @param listener keyListener
   */
  public void addKeyListener(KeyListener listener) {
    namePane.addKeyListener(listener);
    scrollPane.addKeyListener(listener);
    pianoPane.addKeyListener(listener);
    notesPane.addKeyListener(listener);
  }

  @Override
  public void setScore(List<Note> arr) {
    this.pianoPane.setScore(arr);
    this.notesPane.setScore(arr);
    this.repaint();
  }

  @Override
  public void setLeftMostNote(Note note) {
    this.pianoPane.setLeftMostNote(note);
    this.notesPane.setLeftMostNote(note);
    this.namePane.setLeftMostNote(note);
    this.repaint();
  }

  @Override
  public void setRightMostNote(Note note) {
    this.pianoPane.setRightMostNote(note);
    this.notesPane.setRightMostNote(note);
    this.namePane.setRightMostNote(note);
    this.repaint();
  }

  @Override
  public void setCurMeasure(int curMeasure) {
    notesPane.setCurMeasure(curMeasure);
    pianoPane.setCurMeasure(curMeasure);
  }

  @Override
  public void setBeats(int beats) {
    notesPane.setMeasure(beats);
    pianoPane.setMeasure(beats);
  }

  @Override
  public void refresh() {
    this.repaint();
  }

  @Override
  public void display() {
    this.setVisible(true);
  }

  /**
   * Move the view to right by one measure.
   */
  public void moveRight() {
    pianoPane.moveRight();
    notesPane.moveRight();
  }

  /**
   * Move the view to left by one measure.
   */
  public void moveLeft() {
    pianoPane.moveLeft();
    notesPane.moveLeft();
  }

  /**
   * Move the view to the start.
   */
  public void moveHome() {
    scrollPane.getViewport().setViewPosition(new Point(0, 0));
    scrollPane2.getViewport().setViewPosition(new Point(0, 0));
    pianoPane.moveHome();
    notesPane.moveHome();
  }

  /**
   * Move the view to the End.
   */
  public void moveEnd() {
    scrollPane.getViewport().setViewPosition(
        new Point(notesPane.getWidth() - scrollPane.getWidth(), 0));
    scrollPane2.getViewport().setViewPosition(new Point(0, 0));
    pianoPane.moveEnd();
    notesPane.moveEnd();
  }

  /**
   * Render the view by the given ticks.
   *
   * @param ticks  midi ticks.
   */
  public void renderTicks(float ticks) {
    pianoPane.renderTicks(ticks);
    notesPane.renderTicks(ticks);
    scorllOnTicks(ticks);
  }

  // scroll the pane while playing
  private void scorllOnTicks(float ticks) {
    JViewport viewport = scrollPane.getViewport();
    double boundary = ticks * GuiConstant.NOTE_BLOCK_SIZE / MidiViewImpl.TICKS_PER_BEAT;
    if (viewport.getWidth() + viewport.getViewPosition().getX() < boundary) {
      scrollPane.getViewport().setViewPosition(new Point(
          (int) viewport.getViewPosition().getX(), viewport.getY()));
    }

    if (viewport.getWidth() + viewport.getViewPosition().getX() > boundary) {
      scrollPane.getViewport().setViewPosition(new Point(
          (int) viewport.getViewPosition().getX(), viewport.getY()));
    }
  }
}
