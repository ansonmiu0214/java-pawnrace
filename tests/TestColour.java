import org.junit.Test;

import java.util.function.BiFunction;

import static org.junit.Assert.*;

/**
 * Created by Anson on 11/7/2017.
 */
public class TestColour {

  @Test
  public void colourToString() {
    assertEquals(Colour.WHITE.toString(), "W");
    assertEquals(Colour.BLACK.toString(), "B");
    assertEquals(Colour.NONE.toString(), ".");
  }

  @Test
  public void colourGetNext() {
    assertEquals(Colour.WHITE.getNext(), Colour.BLACK);
    assertEquals(Colour.BLACK.getNext(), Colour.WHITE);
    assertNull(Colour.NONE.getNext());
  }

  @Test
  public void colourDirection() {
    BiFunction<Integer, Integer, Integer> w_dir = Colour.WHITE.getDir();
    BiFunction<Integer, Integer, Integer> b_dir = Colour.BLACK.getDir();

    assertTrue(w_dir.apply(2, 1) == 3);
    assertTrue(b_dir.apply(2, 1) == 1);
  }

}
