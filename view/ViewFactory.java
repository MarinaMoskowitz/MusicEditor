package cs3500.music.view;

import cs3500.music.controller.IMusicController;
import cs3500.music.controller.MusicPlayerController;
import cs3500.music.model.IMusicModel;
import cs3500.music.model.ISong;
import cs3500.music.model.MusicModel;
import cs3500.music.model.Song;

/**
 * ADDED IN HW7!
 * A factory class to build a view, given a String representing it's type.
 */
public final class ViewFactory {
  /**
   * A factory method used to generate different views based on the view type it is passed.
   *
   * @param type the type of view to be created
   * @return IView the type viewType
   * @throws IllegalArgumentException for invalid view type
   */
  public static IView factory(String type, ISong song) {
    switch (type) {
      case "console":
        return new TextView();
      case "visual":
        return new MusicView(song);
      case "midi":
        return new MidiViewImpl();
      case "final":
        return new FinalView(song);
      default:
        throw new IllegalArgumentException("please choose a valid view type!");
    }
  }
}
