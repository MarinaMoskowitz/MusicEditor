package cs3500.music.controller;

import cs3500.music.model.IMusicModel;
import cs3500.music.view.IView;

/**
 * CHANGED IN HW 7! Now the model communicates with an IView, not a IMusicView.
 * Controller for the Music player that connects the model and the view.
 */
public class MusicPlayerController implements IMusicController {
  private IMusicModel model;
  private IView view;

  /**
   * Controller constructor.
   * @param model the music model to be used
   * @param view the view to be used
   */
  public MusicPlayerController(IMusicModel model, IView view) {
    this.model = model;
    this.view = view;
  }

  /**
   * MODIFIED IN HW7!
   * Runs the controller.
   */
  @Override
  public void go() {
    this.view.setModel(this.model.getReadOnlyModel());
    view.play();
  }
}
