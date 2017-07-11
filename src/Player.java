import java.util.Set;

/**
 * Created by Anson on 11/7/2017.
 */
public class Player {

  private final Board board;
  private final Game game;
  private final Colour colour;
  private final boolean isComputerPlayer;
  private Player opponent;

  public Player(Board board, Game game, Colour colour, boolean isComputerPlayer) {
    assert(board != null && game != null && colour != null && colour != Colour.NONE);

    this.board = board;
    this.game = game;
    this.colour = colour;
    this.isComputerPlayer = isComputerPlayer;
  }

  public Player getOpponent() {
    return opponent;
  }

  public void setOpponent(Player opponent) {
    assert(opponent != null);

    this.opponent = opponent;
  }

  public Colour getColour() {
    return colour;
  }

  public boolean isComputerPlayer() {
    return isComputerPlayer;
  }

  public Set<Square> getPawns() {
    return game.getPawns(colour);
  }

  public Set<Move> getValidMoves() {
    return game.getValidMoves(colour);
  }

  public void makeMove() {
    // TODO implement Player.makeMove()
  }
}
