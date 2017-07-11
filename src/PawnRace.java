/**
 * Created by Anson on 11/7/2017.
 */
public class PawnRace {

  static final int NUM_OF_ROWS = 8;

  public static void main(String[] args) {
    Board board = new Board('a', 'h');

    board.display();

    Move b_move1 = new Move(board.getSquare(6, 1), board.getSquare(3, 1));
    Move w_move1 = new Move(board.getSquare(1, 2), board.getSquare(3, 2));
    Move b_move2 = new Move(board.getSquare(3, 1), board.getSquare(2, 2), true, true);
    Move w_move2 = new Move(board.getSquare(1, 1), board.getSquare(2, 2), true, false);

    board.applyMove(b_move1);
    board.applyMove(w_move1);

    board.display();

    board.applyMove(b_move2);

    board.display();

    board.applyMove(w_move2);
    board.display();

    board.unapplyMove(w_move2);
    board.display();
  }

}
