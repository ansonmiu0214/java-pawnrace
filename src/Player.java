import java.util.List;
import java.util.Random;
import java.util.Scanner;
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

  public Set<Square> getPieces() {
    return game.getPieces(colour);
  }

  public List<Move> getValidMoves() {
    return game.getValidMoves(colour);
  }

  private boolean isPassedPawn(Square square) {
    assert(square != null && square.occupiedBy() == colour);

    return game.isPassedPawn(square);
  }

  public void makeMove() {
    Move move = null;
    if (isComputerPlayer) {
      // Advance passed pawn, if any
      for (Move nextMove : getValidMoves()) {
        if (isPassedPawn(nextMove.getFrom())) {
          move = nextMove;
          break;
        }
      }

      // Find optimal move from MoveNode if no passed pawn
      if (move == null) {
        // Get optimal move
        MoveNode tree = new MoveNode(colour);
        List<Move> optimal = tree.getOptimal(game);
        move = optimal.get(new Random().nextInt(optimal.size()));
      }

      System.out.println((colour == Colour.WHITE ? "WHITE " : "BLACK ") + "has played " + move.toString());
      System.out.println();
      System.out.println();

    } else {
      System.out.println();

      // Setup input stream
      Scanner in = new Scanner(System.in);
      String san;
      while (move == null) {
        System.out.println("Enter your move in SAN (e.g. b5b6, b6xc5):");
        san = in.nextLine();
        move = game.parseMove(san);

        if (move == null) {
          System.out.println("Unable to parse move.");
        } else if (!game.getValidMoves(colour).contains(move)) {
          move = null;
          System.out.println("Invalid move detected.");
        }
      }

      System.out.println("You played " + move.toString());
    }

    game.applyMove(move);
  }
}
