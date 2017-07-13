/**
 * Created by Anson on 11/7/2017.
 */
public class Square {

  private final int row;
  private final int col;
  private Colour colour;

  public Square(int row, int col, Colour colour) {
    assert(colour != null && row >= 0 && col >= 0
            && row < PawnRace.NUM_OF_ROWS && col < PawnRace.NUM_OF_ROWS);

    this.row = row;
    this.col = col;
    this.colour = colour;
  }

  public Square(int row, int col) {
    this(row, col, Colour.NONE);
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public Colour occupiedBy() {
    return colour;
  }

  public void setOccupier(Colour colour) {
    assert(colour != null);
    this.colour = colour;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Square)) {
      return false;
    }

    Square otherSquare = (Square) obj;
    return otherSquare.getRow() == row && otherSquare.getCol() == col
            && otherSquare.occupiedBy() == colour;
  }

  @Override
  public String toString() {
    return Character.toString((char) (col + (int) 'a')) + (row + 1);
  }

}
