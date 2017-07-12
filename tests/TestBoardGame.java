import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Anson on 12/7/2017.
 */
public class TestBoardGame {

  @Test
  public void instantiateGameAndBoard() {
    Board board = new Board('a', 'h');
    Game game = new Game(board);

    assertNull(game.getLastMove());
    assertEquals(game.getCurrentPlayer(), Colour.WHITE);
    assertTrue(game.getValidMoves(Colour.WHITE).size() == 14);
    assertTrue(game.getValidMoves(Colour.BLACK).size() == 14);
  }

  @Test
  public void gameApplyUnApplyMoves() {
    Board board = new Board('a', 'h');
    Game game = new Game(board);

    Move w_move = new Move(board.getSquare(1, 1), board.getSquare(3, 1));
    Move b_move = new Move(board.getSquare(6, 0), board.getSquare(5, 0));

    game.applyMove(w_move);
    assertEquals(board.getSquare(1, 1).occupiedBy(), Colour.NONE);
    assertEquals(board.getSquare(3, 1).occupiedBy(), Colour.WHITE);
    assertEquals(game.getCurrentPlayer(), Colour.BLACK);

    game.unapplyMove();
    assertNotEquals(board.getSquare(1, 1).occupiedBy(), Colour.NONE);
    assertNotEquals(board.getSquare(3, 1).occupiedBy(), Colour.WHITE);
  }

  @Test
  public void winLoseDraw() {
    Game game = new Game(new Board('a', 'h'));
    assertNull(game.getGameResult());

    // White winning move
    game.applyMove(new Move(game.getBoard().getSquare(1, 1), game.getBoard().getSquare(7, 1)));
    assertEquals(game.getGameResult(), Colour.WHITE);

    // Black winning move
    game.unapplyMove();
    for (Square white : game.getPawns(Colour.WHITE)) {
      white.setOccupier(Colour.BLACK);
    }
    assertEquals(game.getGameResult(), Colour.BLACK);

    // Stalemate
    // TODO implement TestBoardGame.winLoseDraw - stalemate
  }

  @Test
  public void differentTypesOfMoves() {

    // Instantiation
    Board board = new Board('a', 'h');
    Game game = new Game(board);

    // Testing different types of move
    Move setup = new Move(board.getSquare(1, 7), board.getSquare(2, 7));
    Move b_move1 = new Move(board.getSquare(6, 1), board.getSquare(3, 1));
    Move w_move1 = new Move(board.getSquare(1, 2), board.getSquare(3, 2));
    Move b_move2 = new Move(board.getSquare(3, 1), board.getSquare(2, 2), true, true);
    Move w_move2 = new Move(board.getSquare(1, 1), board.getSquare(2, 2), true, false);

    // Non-capture move (B)
    game.applyMove(setup);
    game.applyMove(b_move1);
    assertEquals(board.getSquare(6, 1).occupiedBy(), Colour.NONE);
    assertNotEquals(board.getSquare(3, 1).occupiedBy(), Colour.NONE);
    assertEquals(game.getCurrentPlayer(), Colour.WHITE);

    // Non-capture move (W)
    game.applyMove(w_move1);
    assertEquals(board.getSquare(1, 2).occupiedBy(), Colour.NONE);
    assertEquals(board.getSquare(3, 2).occupiedBy(), Colour.WHITE);
    assertEquals(game.getCurrentPlayer(), Colour.BLACK);

    // Capture move (B)
    game.applyMove(b_move2);
    assertNotEquals(board.getSquare(3, 1).occupiedBy(), Colour.BLACK);
    assertEquals(board.getSquare(2, 2).occupiedBy(), Colour.BLACK);
    assertEquals(game.getCurrentPlayer(), Colour.WHITE);

    // Unapply capture move
    game.unapplyMove();
    assertEquals(board.getSquare(3, 1).occupiedBy(), Colour.BLACK);
    assertNotEquals(board.getSquare(2, 2).occupiedBy(), Colour.BLACK);
    game.applyMove(b_move2);

    // En passant capture move (W)
    game.applyMove(w_move2);
    assertNotEquals(board.getSquare(1, 1).occupiedBy(), Colour.WHITE);
    assertEquals(board.getSquare(2, 2).occupiedBy(), Colour.WHITE);
  }

}
