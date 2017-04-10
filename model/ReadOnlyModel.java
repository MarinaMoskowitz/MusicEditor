package cs3500.music.model;

/**
 * ADDED IN HW7!
 * ReadOnly extension of the MusicModel class. Suppresses mutable methods.
 */
public class ReadOnlyModel extends MusicModel {

  ReadOnlyModel(Song s) {
    super(s);
  }

  @Override
  public void removeNote(Note n, int beat) {
    throw new UnsupportedOperationException("Cannot call method on read only model");
  }

  @Override
  public void addNote(Note n, int beat) {
    throw new UnsupportedOperationException("Cannot call method on read only model");
  }

  @Override
  public void playSimultaneously(IMusicModel model) {
    throw new UnsupportedOperationException("Cannot call method on read only model");
  }

  @Override
  public void playConsecutively(IMusicModel model) {
    throw new UnsupportedOperationException("Cannot call method on read only model");
  }
}
