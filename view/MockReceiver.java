package cs3500.music.view;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

/**
 * Mocks a Receiver for testing purposes
 */
public class MockReceiver implements Receiver {
  private StringBuilder s;

  public MockReceiver(StringBuilder s) {
    this.s = s;
  }

  @Override
  public void send(MidiMessage m, long timeStamp) {
    ShortMessage message = (ShortMessage) m;
    s.append("note ");
    s.append(message.getCommand());
    s.append(" ");
    s.append(message.getChannel());
    s.append(" ");
    s.append(message.getData1());
    s.append(" ");
    s.append(message.getData2());
    s.append("\n");
  }

  @Override
  public void close() {

  }
}
