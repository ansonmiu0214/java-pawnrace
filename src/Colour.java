import java.util.function.BiFunction;

/**
 * Created by Anson on 11/7/2017.
 */
public enum Colour {

  WHITE("W", (x, y) -> x + y),
  BLACK("B", (x, y) -> x - y),
  NONE(".", null);

  private final String letter;
  private final BiFunction<Integer, Integer, Integer> dir;
  private Colour next;

  static {
    WHITE.next = BLACK;
    BLACK.next = WHITE;
    NONE.next = null;
  }

  Colour(String letter, BiFunction<Integer, Integer, Integer> dir) {
    this.letter = letter;
    this.dir = dir;
  }

  public BiFunction<Integer, Integer, Integer> getDir() {
    return dir;
  }

  public Colour getNext() {
    return next;
  }

  @Override
  public String toString() {
    return letter;
  }

}
