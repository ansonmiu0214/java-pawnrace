import java.util.*;
import java.util.function.BiFunction;

/**
 * Created by Anson on 11/7/2017.
 */
public class Game {

  private final Board board;
  private final Stack<Move> history;
  private Colour currentPlayer;

  public Game(Board board) {
    assert(board != null);

    this.board = board;
    this.history = new Stack<>();
    this.currentPlayer = Colour.WHITE;
  }

  public Colour getCurrentPlayer() {
    return currentPlayer;
  }

  public Move getLastMove() {
    return history.isEmpty() ? null : history.peek();
  }

  public void applyMove(Move move) {
    assert(move != null);

    // Apply move on board
    board.applyMove(move);

    // Add to move history
    history.push(move);

    // Next player
    currentPlayer = currentPlayer.getNext();
  }

  public void unapplyMove() {
    Move move = getLastMove();

    // Check if there exists a move to un-apply
    if (move != null) {
      // Un-apply move on board
      board.unapplyMove(move);

      // Remove from history
      history.pop();

      // Next player
      currentPlayer = currentPlayer.getNext();
    }
  }

  public Colour getGameResult() {
    if (getValidMoves(Colour.WHITE).size() == 0 || getValidMoves(Colour.BLACK).size() == 0) {
      return Colour.NONE;
    }

    if (getPawns(Colour.WHITE).size() == 0) {
      return Colour.BLACK;
    }

    if (getPawns(Colour.BLACK).size() == 0) {
      return Colour.WHITE;
    }

    for (int col = 0; col < PawnRace.NUM_OF_ROWS; ++col) {
      if (board.getSquare(0, col).occupiedBy() == Colour.BLACK) {
        return Colour.BLACK;
      }

      if (board.getSquare(7, col).occupiedBy() == Colour.WHITE) {
        return Colour.WHITE;
      }
    }

    return null;
  }

  public Set<Square> getPawns(Colour colour) {
    assert(colour != null && colour != Colour.NONE);

    Set<Square> pawns = new HashSet<>();

    for (int row = 0; row < PawnRace.NUM_OF_ROWS; ++row) {
      for (int col = 0; col < PawnRace.NUM_OF_ROWS; ++col) {
        Square square = board.getSquare(row, col);
        if (square.occupiedBy() == colour) {
          pawns.add(square);
        }
      }
    }

    return pawns;
  }

  public Set<Move> getValidMoves(Colour colour) {
    assert(colour != null && colour != Colour.NONE);

    Set<Move> validMoves = new HashSet<>();
    BiFunction<Integer, Integer, Integer> dir = colour.getDir();

    for (Square curr : getPawns(colour)) {
      int curr_r = curr.getRow();
      int curr_c = curr.getCol();


      Square adv_1 = board.getSquare(dir.apply(curr_r, 1), curr_c);
      Square adv_2 = board.getSquare(dir.apply(curr_r, 2), curr_c);

      List<Square> capt = new ArrayList<>();
      capt.add(board.getSquare(dir.apply(curr_r, 1), curr_c - 1));
      capt.add(board.getSquare(dir.apply(curr_r, 1), curr_c + 1));

      // Check 1-step advance
      if (adv_1 != null) {
        if (adv_1.occupiedBy() == Colour.NONE) {
          validMoves.add(new Move(curr, adv_1));
        }

        // Check 2-step advance
        if ((colour == Colour.WHITE && curr_r == Board.WHITE_BASE) ||
                (colour == Colour.BLACK && curr_r == Board.BLACK_BASE)) {
          if (adv_2 != null) {
            if (adv_2.occupiedBy() == Colour.NONE) {
              validMoves.add(new Move(curr, adv_2));
            }
          }
        }

        // Check capture
        for (Square cap : capt) {
          if (cap != null) {

            if (cap.occupiedBy() == colour.getNext()) {
              validMoves.add(new Move(curr, cap, true, false));
            }

            Square last_frm = getLastMove().getFrom();
            Square last_to = getLastMove().getTo();

            // TODO implement Game.getValidMoves - en passant capture

          }
        }
      }
    }

    return validMoves;
  }

  public Move parseMove(String san) {
    // TODO implement Game.parseMove
    return null;
  }


}
