package cs3500.music;

import cs3500.music.model.ISong;
import cs3500.music.model.Song;
import cs3500.music.util.CompositionBuilder;
import cs3500.music.util.MusicReader;
import cs3500.music.view.IView;
import cs3500.music.view.ViewFactory;
import cs3500.music.view.IMusicView;
import cs3500.music.model.IMusicModel;
import cs3500.music.model.MusicModel;
import cs3500.music.view.FinalView;
import cs3500.music.controller.IMusicController;
import cs3500.music.controller.MusicPlayerController;



import java.io.FileReader;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;

/**
 * CHANGED IN HW7!
 * can now specify which song you want to play instead of it being hard coded in.
 * Factory object for constructing various types of views for the given model.
 */
public class MusicEditorFactoryMain {
  public static void main(String[] args) throws IOException, InvalidMidiDataException {
    CompositionBuilder<ISong> b = new Song.Builder();
    ISong song = MusicReader.parseFile(new FileReader(args[0]), b);
    IView view = ViewFactory.factory(args[1], song);
    IMusicModel model = new MusicModel((Song)song);
    IMusicController controller = new MusicPlayerController(model, view);
    controller.go();
  }
}
