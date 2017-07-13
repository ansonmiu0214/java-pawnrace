/**
 * Created by Anson on 11/7/2017.
 */
public class Move {

  private static final String CAPTURE = "x";

  private final Square from;
  private final Square to;
  private final boolean isCapture;
  private final boolean isEnPassantCapture;

  public Move(Square from, Square to, boolean isCapture, boolean isEnPassantCapture) {
    assert(from != null && to != null);

    this.from = from;
    this.to = to;
    this.isCapture = isCapture;
    this.isEnPassantCapture = isEnPassantCapture;
  }

  public Move(Square from, Square to) {
    this(from, to, false, false);
  }

  public Square getFrom() {
    return from;
  }

  public Square getTo() {
    return to;
  }

  public boolean isCapture() {
    return isCapture;
  }

  public boolean isEnPassantCapture() {
    return isEnPassantCapture;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Move)) {
      return false;
    }

    Move other = (Move) obj;
    return other.getFrom().equals(from) && other.getTo().equals(to)
            && other.isCapture() == isCapture
            && other.isEnPassantCapture() == isEnPassantCapture;
  }

  @Override
  public String toString() {
    return from.toString() + (isCapture() ? CAPTURE : "") + to.toString();
  }

}
