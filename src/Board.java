/**
 * Created by Anson on 11/7/2017.
 */
public class Board {

  private static final int MARGIN = 2;
  static final int WHITE_BASE = 1;
  static final int BLACK_BASE = 6;

  private final Square[][] board;

  public Board(char whiteGap, char blackGap) {
    this.board = new Square[PawnRace.NUM_OF_ROWS][PawnRace.NUM_OF_ROWS];

    int w_gap = whiteGap - 'a';
    int b_gap = blackGap - 'a';

    for (int row = 0; row < PawnRace.NUM_OF_ROWS; ++row) {
      for (int col = 0; col < PawnRace.NUM_OF_ROWS; ++col) {
        switch (row) {
          case WHITE_BASE:
            board[row][col] = (col == w_gap) ? new Square(row, col) : new Square(row, col, Colour.WHITE);
            break;

          case BLACK_BASE:
            board[row][col] = (col == b_gap) ? new Square(row, col) : new Square(row, col, Colour.BLACK);
            break;

          default:
            board[row][col] = new Square(row, col);
            break;
        }
      }
    }

  }

  public Square getSquare(int row, int col) {
    return isInBound(row, col) ? board[row][col] : null;
  }

  public boolean isInBound(int row, int col) {
    return row >= 0 && col >= 0 && row < PawnRace.NUM_OF_ROWS && col < PawnRace.NUM_OF_ROWS;
  }

  public void applyMove(Move move) {
    Square from = move.getFrom();
    Square to = move.getTo();

    to.setOccupier(from.occupiedBy());
    from.setOccupier(Colour.NONE);

    if (move.isEnPassantCapture()) {
      Square pawn = getSquare(from.getRow(), to.getCol());
      pawn.setOccupier(Colour.NONE);
    }
  }

  public void unapplyMove(Move move) {
    Square from = move.getFrom();
    Square to = move.getTo();

    from.setOccupier(to.occupiedBy());
    if (move.isCapture() && !move.isEnPassantCapture()) {
      to.setOccupier(from.occupiedBy().getNext());
    } else {
      to.setOccupier(Colour.NONE);

      if (move.isEnPassantCapture()) {
        Square pawn = getSquare(from.getRow(), to.getCol());
        pawn.setOccupier(from.occupiedBy().getNext());
      }
    }
  }

  public void display() {
    for (int row = PawnRace.NUM_OF_ROWS + MARGIN; row > -MARGIN; --row) {
      for (int col = -MARGIN; col < PawnRace.NUM_OF_ROWS + MARGIN; ++col) {
        if ((col == -MARGIN || col == PawnRace.NUM_OF_ROWS + MARGIN - 1)
                && row > 0 && row <= PawnRace.NUM_OF_ROWS) {
          System.out.print(row);
        } else if ((row == 1 - MARGIN || row == PawnRace.NUM_OF_ROWS + MARGIN)
                && col >= 0 && col < PawnRace.NUM_OF_ROWS) {
          System.out.print(Character.toString((char) (col + (int) 'a')));
        } else {
          Square square = getSquare(row - 1, col);

          if (square != null) {
            System.out.print(square.occupiedBy().toString());
          } else {
            System.out.print(" ");
          }
        }

        System.out.print(" ");
      }
      System.out.println();
    }
    System.out.println();
  }

}
