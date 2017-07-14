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

  public Board getBoard() {
    return board;
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
    // Check if all pawns of one colour are captured
    if (getPieces(Colour.WHITE).size() == 0) {
      return Colour.BLACK;
    }

    if (getPieces(Colour.BLACK).size() == 0) {
      return Colour.WHITE;
    }

    // Check for stalemate
    if (getValidMoves(Colour.WHITE).size() == 0 || getValidMoves(Colour.BLACK).size() == 0) {
      return Colour.NONE;
    }


    // Check if there exists a pawn past its opponent colour's base
    for (int col = 0; col < PawnRace.NUM_OF_ROWS; ++col) {
      if (board.getSquare(0, col).occupiedBy() == Colour.BLACK) {
        return Colour.BLACK;
      }

      if (board.getSquare(7, col).occupiedBy() == Colour.WHITE) {
        return Colour.WHITE;
      }
    }

    // Game not finished
    return null;
  }

  public Set<Square> getPieces(Colour colour) {
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

  public List<Move> getValidMoves(Colour colour) {
    assert(colour != null && colour != Colour.NONE);

    List<Move> validMoves = new ArrayList<>();
    BiFunction<Integer, Integer, Integer> dir = colour.getDir();

    for (Square curr : getPieces(colour)) {
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

            if (getLastMove() != null) {
              if (!getLastMove().isCapture()) {
                Square last_frm = getLastMove().getFrom();
                Square last_to = getLastMove().getTo();

                if (Math.abs(last_frm.getRow() - last_to.getRow()) == 2) {
                  Square between = (last_to.getRow() > last_frm.getRow())
                          ? board.getSquare(last_frm.getRow() + 1, last_frm.getCol())
                          : board.getSquare(last_to.getRow() + 1, last_to.getCol());

                  if (between.equals(cap)) {
                    validMoves.add(new Move(curr, cap, true, true));
                  }
                }
              }
            }
          }
        }
      }
    }

    return validMoves;
  }

  public boolean isPassedPawn(Square square) {
    assert(square != null);

    Colour current = square.occupiedBy();
    int curr_row = square.getRow();
    int curr_col = square.getCol();

    switch(current) {
      case WHITE:
        for (int col = curr_col - 1; col <= curr_col + 1; ++col) {
          for (int row = curr_row + 1; row <= Board.BLACK_BASE + 1; ++row) {
            if (board.isInBound(row, col)) {
              if (board.getSquare(row, col).occupiedBy() == current.getNext()) {
                return false;
              }
            }
          }
        }

        break;
      case BLACK:
        for (int col = curr_col - 1; col <= curr_col + 1; ++col) {
          for (int row = curr_row - 1; row >= Board.WHITE_BASE - 1; --row) {
            if (board.isInBound(row, col)) {
              if (board.getSquare(row, col).occupiedBy() == current.getNext()) {
                return false;
              }
            }
          }
        }

        break;
      default:
        return false;
    }

    return true;
  }

  public Move parseMove(String san) {
    san = san.toLowerCase();

    // Check argument length
    if (!(san.length() == 4 || san.length() == 5)) {
      return null;
    }

    int from_row, from_col, to_row, to_col;

    // Check syntax
    switch (san.length()) {
      case 4:
        // Parse rank/file
        from_row = Character.getNumericValue(san.charAt(1)) - 1;
        from_col = san.charAt(0) - 'a';
        to_row = Character.getNumericValue(san.charAt(3)) - 1;
        to_col = san.charAt(2) - 'a';

        // Check inbounds
        if (!(board.isInBound(from_row, from_col) && board.isInBound(to_row, to_col))) {
          return null;
        }

        return new Move(board.getSquare(from_row, from_col), board.getSquare(to_row, to_col));

      case 5:
        // Parse rank/file
        from_row = Character.getNumericValue(san.charAt(1)) - 1;
        from_col = san.charAt(0) - 'a';
        to_row = Character.getNumericValue(san.charAt(4)) - 1;
        to_col = san.charAt(3) - 'a';

        // Check inbounds
        if (!(board.isInBound(from_row, from_col) && board.isInBound(to_row, to_col))) {
          return null;
        }

        return new Move(board.getSquare(from_row, from_col), board.getSquare(to_row, to_col), true,
                board.getSquare(to_row, to_col).occupiedBy() == Colour.NONE);
    }

    return null;
  }


}
