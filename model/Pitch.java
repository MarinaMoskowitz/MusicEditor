package cs3500.music.model;

/**
 * Enum to represent the twelve distinct values a pitch could be.
 */
public enum Pitch {
  C, CSharp, D, DSharp, E, F, FSharp, G, GSharp, A, ASharp, B;

  /**
   * Formats a note in a Pitch as a String.
   *
   * @return note   a note as a String
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(convertPitchToString(this));
    return sb.toString();
  }

  /**
   * Converts a Pitch to a String.
   *
   * @return String    that the given Pitch as a String
   * @throws IllegalArgumentException if there is no Pitch corresponding to the given enum value
   */
  public static String convertPitchToString(Pitch pitch) throws IllegalArgumentException {
    switch (pitch.ordinal()) {
      case 0:
        return "C";
      case 1:
        return "C#";
      case 2:
        return "D";
      case 3:
        return "D#";
      case 4:
        return "E";
      case 5:
        return "F";
      case 6:
        return "F#";
      case 7:
        return "G";
      case 8:
        return "G#";
      case 9:
        return "A";
      case 10:
        return "A#";
      case 11:
        return "B";
      default:
        throw new IllegalArgumentException("Invalid Pitch");
    }
  }

  /**
   * Converts the ordinal to the proper pitch.
   * @param i the number to be converted
   * @return the corresponding pitch
   * @throws IllegalArgumentException when the pitch number is invalid
   */
  public static Pitch convertNumToPitch(int i) throws IllegalArgumentException {
    switch (i) {
      case 0:
        return C;
      case 1:
        return CSharp;
      case 2:
        return D;
      case 3:
        return DSharp;
      case 4:
        return E;
      case 5:
        return F;
      case 6:
        return FSharp;
      case 7:
        return G;
      case 8:
        return GSharp;
      case 9:
        return A;
      case 10:
        return ASharp;
      case 11:
        return B;
      default:
        throw new IllegalArgumentException("Invalid Pitch");
    }
  }
}
