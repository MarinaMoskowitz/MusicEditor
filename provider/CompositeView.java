package cs3500.music.view;

import cs3500.music.controller.MouseHandler;
import cs3500.music.model.Note;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * To represent a composite view.
 */
public class CompositeView extends JFrame implements IView<Note> {

  private JScrollPane scrollPane;
  private JScrollPane scrollPane2;

  private NotesScrollPane notesPane;
  private NoteNamePane namePane;
  private PianoPane pianoPane;

  private JComboBox<String> noteNameBox;
  private JComboBox<String> pitchBox;
  private JTextField noteheadBox;
  private JComboBox<String> sustainBox;
  private JButton addNoteButton;

  private boolean paused;
  private MidiViewImpl midiView;

  /**
   * Configure the view.
   */
  public CompositeView() {
    super();

    this.midiView = new MidiViewImpl();
    this.paused = true;

    // basic config
    this.setTitle("Music Editor");
    this.setSize(GuiConstant.WINDOW_WIDTH,GuiConstant.WINDOW_HEIGHT);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // add content panel
    JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());
    contentPane.setPreferredSize(new Dimension(GuiConstant.WINDOW_WIDTH,
        GuiConstant.WINDOW_HEIGHT));
    this.setContentPane(contentPane);

    // config notes pane
    notesPane = new NotesScrollPane();
    notesPane.setPreferredSize(new Dimension(
        (notesPane.getMeasure() + 2) * GuiConstant.NOTE_BLOCK_SIZE, 800));

    // add to scroll pane
    scrollPane = new JScrollPane();
    scrollPane.setPreferredSize(new Dimension(600, 600));
    this.getContentPane().add(scrollPane, BorderLayout.CENTER);
    scrollPane.setViewportView(notesPane);
    scrollPane.setFocusable(true);

    // config name pane
    namePane = new NoteNamePane();
    namePane.setPreferredSize(new Dimension(GuiConstant.TEXT_WIDTH + 10, 600));
    scrollPane2 = new JScrollPane();

    // add name pane
    this.getContentPane().add(scrollPane2, BorderLayout.WEST);
    scrollPane2.setViewportView(namePane);

    // piano
    pianoPane = new PianoPane();
    pianoPane.setPreferredSize(new Dimension(GuiConstant.PIANO_WIDTH,
        GuiConstant.PIANO_HEIGHT));
    this.getContentPane().add(pianoPane, BorderLayout.SOUTH);

    // button panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setPreferredSize(new Dimension(GuiConstant.BUTTON_PANE_WIDTH,
        GuiConstant.BUTTON_PANE_HEIGHT));
    buttonPanel.setLayout(new FlowLayout());
    this.getContentPane().add(buttonPanel, BorderLayout.EAST);

    // add pitch drop down box
    String[] pitch = new String[]
        {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    noteNameBox = new JComboBox<>(pitch);
    noteNameBox.setPreferredSize(new Dimension(80, 20));
    buttonPanel.add(new JLabel("Pitch:"));
    buttonPanel.add(noteNameBox);

    // add octave drop down box
    String[] octaves = new String[]
        {"1", "2", "3", "4", "5", "6", "7", "8"};
    pitchBox = new JComboBox<>(octaves);
    pitchBox.setPreferredSize(new Dimension(80, 20));
    buttonPanel.add(new JLabel("Octave:"));
    buttonPanel.add(pitchBox);

    // add note head text box
    noteheadBox = new JTextField();
    noteheadBox.setPreferredSize(new Dimension(80, 20));
    buttonPanel.add(new JLabel("Note Head:"));
    buttonPanel.add(noteheadBox);

    // add sustain drop down box
    String[] sustains = new String[]
        {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    sustainBox = new JComboBox<>(sustains);
    sustainBox.setPreferredSize(new Dimension(80, 20));
    buttonPanel.add(new JLabel("Sustain:"));
    buttonPanel.add(sustainBox);

    // add button pane
    addNoteButton = new JButton("Add Note");
    buttonPanel.add(addNoteButton);
  }

  /**
   * Add keylistener to the view.
   *
   * @param listener  keyListener
   */
  public void addKeyListener(KeyListener listener) {
    scrollPane.addKeyListener(listener);
    notesPane.addKeyListener(listener);
    addNoteButton.addKeyListener(listener);
  }

  /**
   * Add mouseListener to the view.
   *
   * @param listener  mouseListener
   */
  public void addMouseListener(MouseHandler listener) {
    notesPane.addMouseListener(listener);

  }

  @Override
  public void setScore(List<Note> arr) {
    this.pianoPane.setScore(arr);
    this.notesPane.setScore(arr);
    midiView.setScore(arr);
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
    this.scrollPane2.setPreferredSize(namePane.getPreferredSize());
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

  // set the tempo
  public void setTempo(float tempo) {
    midiView.setTempo(tempo);
  }

  @Override
  public void refresh() {
    this.repaint();
  }

  @Override
  public void display() {
    this.setVisible(true);
    TimerTask timerTask = new TimerTask() {
      public void run() {
        renderTicks(midiView.getTicks());
      }
    };
    Timer t = new Timer();
    t.schedule(timerTask, 0, 10);

    midiView.display();
  }

  /**
   * Move the view to right by one measure.
   */
  public void moveRight() {
    if (paused) {
      pianoPane.moveRight();
      notesPane.moveRight();
      midiView.moveRight();
    }
  }

  /**
   * Move the view to left by one measure.
   */
  public void moveLeft() {
    if (paused) {
      pianoPane.moveLeft();
      notesPane.moveLeft();
      midiView.moveLeft();
    }
  }

  /**
   * Move the view to the start.
   */
  public void moveHome() {
    scrollPane.getViewport().setViewPosition(new Point(0, 0));
    scrollPane2.getViewport().setViewPosition(new Point(0, 0));
    pianoPane.moveHome();
    notesPane.moveHome();
    midiView.moveHome();
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
    midiView.moveEnd();
  }

  /**
   * Pause the view.
   */
  public void pause() {
    midiView.pause();
    paused = (midiView.isPaused());
  }

  /**
   * Render the view by the given ticks.
   *
   * @param ticks  midi ticks.
   */
  public void renderTicks(float ticks) {
    this.pianoPane.renderTicks(ticks);
    this.notesPane.renderTicks(ticks);
  }

  /*-----------------------------------------------------------------------------------------*/

  // get the measure of the note according to given x-position
  public int getNoteMeasureAt(int x) {
    return notesPane.getNoteMeasureAt(
        (int) scrollPane.getViewport().getViewPosition().getX() + x);
  }

  // get the pitch of the note according to given y-position
  public String getNotePitchAt(int y) {
    return notesPane.getNotePitchAt(
        (int) scrollPane.getViewport().getViewPosition().getY() + y);
  }

  // get the octave of the note according to given y-position
  public int getNoteOctaveAt(int y) {
    return notesPane.getNoteOctaveAt(
        (int) scrollPane.getViewport().getViewPosition().getY() + y);
  }

  /*-------------------------Input Box Settings-----------------------------------------*/

  public String getInputNoteName() {
    return (String) noteNameBox.getSelectedItem();
  }

  public int getInputOctave() {
    return Integer.valueOf((String)pitchBox.getSelectedItem());
  }

  public int getInputNoteHead() {
    return Integer.valueOf(noteheadBox.getText());
  }

  public int getInputSustain() {
    return Integer.valueOf((String)sustainBox.getSelectedItem());
  }

  public void setAddNoteButtonListner(ActionListener listner) {
    addNoteButton.addActionListener(listner);
  }

  // display an error message
  public void showErrorMessage(String error) {
    JOptionPane.showMessageDialog(this, error,"Error",
        JOptionPane.ERROR_MESSAGE);
  }

  // add a note to the view.
  public void addNote(Note n) {
    midiView.addNote(n);
  }
}
