package cs3500.music.view;

import java.util.List;

/**
 * To represent the view interface.
 */
public interface IView<K> {


  void setScore(List<K> arr);

  void setLeftMostNote(K note);

  void setRightMostNote(K note);

  void setCurMeasure(int curMeasure);

  void setBeats(int beats);

  void refresh();

  void display();
}
