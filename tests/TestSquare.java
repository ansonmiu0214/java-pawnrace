import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Anson on 11/7/2017.
 */
public class TestSquare {

  @Test
  public void squareOccupants() {
    Square sq1 = new Square(0, 0, Colour.BLACK);
    Square sq2 = new Square(0, 1, Colour.WHITE);
    Square sq3 = new Square(7, 0);

    assertNotEquals(sq1.occupiedBy(), sq2.occupiedBy());
    assertNotEquals(sq1.occupiedBy(), sq3.occupiedBy());
    assertNotEquals(sq2.occupiedBy(), sq3.occupiedBy());

    sq1.setOccupier(sq2.occupiedBy());
    assertEquals(sq1.occupiedBy(), sq2.occupiedBy());
  }

  @Test
  public void squareToString() {
    Square sq1 = new Square(0, 0);
    Square sq2 = new Square(4, 3);
    Square sq3 = new Square(7, 0);
    Square sq4 = new Square(0, 7);
    Square sq5 = new Square(7, 7);

    assertEquals(sq1.toString(), "a1");
    assertEquals(sq2.toString(), "d5");
    assertEquals(sq3.toString(), "a8");
    assertEquals(sq4.toString(), "h1");
    assertEquals(sq5.toString(), "h8");
  }

  @Test
  public void cloneSquare() {
    Square sq1 = new Square(0, 1, Colour.BLACK);

    try {
      Square sq2 = sq1.clone();
      assertEquals(sq1.getRow(), 0);
      assertEquals(sq1.getCol(), 1);
      assertEquals(sq1.occupiedBy(), Colour.BLACK);
      assertEquals(sq2.getRow(), 0);
      assertEquals(sq2.getCol(), 1);
      assertEquals(sq2.occupiedBy(), Colour.BLACK);

      sq2.setOccupier(Colour.WHITE);
      assertNotEquals(sq1.occupiedBy(), Colour.WHITE);
      assertEquals(sq2.occupiedBy(), Colour.WHITE);
    } catch (CloneNotSupportedException exception) {
      exception.printStackTrace();
    }
  }

}
