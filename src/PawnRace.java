/**
 * Created by Anson on 11/7/2017.
 */
public class PawnRace {

  static final int NUM_OF_ROWS = 8;
  static final char PLAYER = 'p';
  static final char COMPUTER = 'c';
  static final char MIN_GAP = 'a';
  static final char MAX_GAP = 'h';

  public static void main(String[] args) {

    // Program argument validation
    if (args.length != 4) {
      printUsage();
      return;
    }

    // Parse arguments
    char w = args[0].toLowerCase().charAt(0);
    char b = args[1].toLowerCase().charAt(0);
    char white_gap = args[2].toLowerCase().charAt(0);
    char black_gap = args[3].toLowerCase().charAt(0);

    // Parsed input validation
    if ((w != PLAYER && w != COMPUTER) || (b != PLAYER && b != COMPUTER)) {
      printUsage();
      return;
    }

    if (white_gap < MIN_GAP || white_gap > MAX_GAP || black_gap < MIN_GAP || black_gap > MAX_GAP) {
      printUsage();
      return;
    }

    // Instantiation
    Board board = new Board(white_gap, black_gap);
    Game game = new Game(board);

    Player white = new Player(board, game, Colour.WHITE,  w == COMPUTER);
    Player black = new Player(board, game, Colour.BLACK, b == COMPUTER);

    initialisePlayers(white, black);

    game.getBoard().display();

    // Game loop
    while (game.getGameResult() == null) {
      white.makeMove();
      game.getBoard().display();

      if (game.getGameResult() == null) {
        black.makeMove();
        game.getBoard().display();
      }
    }

    switch (game.getGameResult()) {
      case WHITE:
        System.out.println("The winner is <WHITE>.");
        break;
      case BLACK:
        System.out.println("The winner is <BLACK>.");
        break;
      case NONE:
        System.out.println("Stalemate reached.");
        break;
      default:
        System.err.println("Pre-condition violated - game not finished!");
        break;
    }
  }

  private static void printUsage() {
    System.err.println("Usage: ./pawnrace @param_1 @param_2 @param_3 @param_4");
    System.err.println("@param_1:\twhite player or computer [p/c]");
    System.err.println("@param_2:\tblack player or computer [p/c]");
    System.err.println("@param_3:\twhite gap [a..h]");
    System.err.println("@param_4:\tblack gap [a..h]");
  }

  private static void initialisePlayers(Player p1, Player p2) {
    assert(p1 != null && p2 != null);

    p1.setOpponent(p2);
    p2.setOpponent(p1);
  }

}
