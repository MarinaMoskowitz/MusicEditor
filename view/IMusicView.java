package cs3500.music.view;

import cs3500.music.model.IMusicModel;
import cs3500.music.model.ISong;

/**
 * An interface for the Music player view.
 */
public interface IMusicView extends IView {

  /**
   * Makes the view visible.
   */
  void play();

  /**
   * Refreshes the visual part of the view.
   */
  void refresh();

  /**
   * ADDED IN HW7!
   * Sets the Views internal model to the given model.
   *
   * @param model The model that the view will display data for.
   */
  void setModel(IMusicModel model);

  /**
   * ADDED IN HW 7!
   * Gets the song from the view.
   */
  ISong getSong();

  /**
   * ADDED IN HW 7!
   * Gets the current beat from the view.
   */
  int getCurrentBeat();
}
