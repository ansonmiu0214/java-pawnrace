import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Anson on 12/7/2017.
 */
public class MoveNode {

  private static final int MAX_SCORE = 7;
  private static final int DIFFICULTY = 5;

  private final Colour original;
  private int score;
  private Move move;

  public MoveNode(Move move, Colour original) {
    assert(original != null && original != Colour.NONE);

    this.original = original;
    this.score = 0;
    this.move = move;
  }

  public MoveNode(Colour original) {
    this(null, original);
  }

  public Move getMove() {
    return move;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public List<Move> getOptimal(Game game) {

    // Instantiate
    List<Move> validMoves = game.getValidMoves(original);
    Set<MoveNode> options = Sets.newConcurrentHashSet();
    Thread[] threads = new Thread[validMoves.size()];

    for (int i = 0; i < validMoves.size(); ++i) {
      Move move = validMoves.get(i);
      threads[i] = new Thread(() -> {
        try {
          options.add(explore(game.clone(), move, original, DIFFICULTY));
        } catch (CloneNotSupportedException exception) {
          exception.printStackTrace();
        }
      });
    }

    for (int i = 0; i < validMoves.size(); ++i) {
      threads[i].start();
    }

    for (int i = 0; i < validMoves.size(); ++i) {
      try {
        threads[i].join();
      } catch (InterruptedException exception) {
        exception.printStackTrace();
      }
    }

    // Find optimal score form options
    int optimal = options.stream()
            .map(MoveNode::getScore)
            .reduce(Math::max)
            .get();

    // Return optimal moves as list
    return options.stream()
            .filter(x -> x.getScore() == optimal)
            .map(MoveNode::getMove)
            .collect(Collectors.toList());
  }

  private MoveNode explore(Game game, Move move, Colour original, int count) {
    assert(game != null && move != null && original != null && original != Colour.NONE && count >= 0);

    // Instantiate
    MoveNode node = new MoveNode(move, original);

    // Apply move
    game.applyMove(move);

    // Base case - any passed pawns?
    if (game.getCurrentPlayer() != original) {
      // If move gives CPU a passed pawn, don't do it
      for (Square cpu : game.getPieces(game.getCurrentPlayer())) {
        if (game.isPassedPawn(cpu)) {
          node.setScore(-MAX_SCORE);
          return node;
        }
      }

      // If move gives user a passed pawn, do it
      for (Square user : game.getPieces(original)) {
        if (game.isPassedPawn(user)) {
          node.setScore(MAX_SCORE);
          return node;
        }
      }
    }

    if (game.getGameResult() != null || count == 0) {
      // Base case

      if (game.getGameResult() == null) {
        node.setScore(game.getPieces(original).size() - game.getPieces(original.getNext()).size());
      } else if (game.getGameResult() != Colour.NONE) {
        node.setScore(game.getGameResult() == original ? MAX_SCORE : -MAX_SCORE);
      }
    } else {
      // Recursive case

      List<Move> validMoves = game.getValidMoves(game.getCurrentPlayer());
      Set<MoveNode> options = new HashSet<>();

      for (Move nextMove : validMoves) {
        try {
          options.add(explore(game.clone(), nextMove, original, count - 1));
        } catch (CloneNotSupportedException exception) {
          exception.printStackTrace();
        }
      }

      if (game.getCurrentPlayer() == original) {
        // Set score as the sum of the option scores
        node.setScore(options.stream()
                .map(MoveNode::getScore)
                .reduce(0, Math::addExact));
      } else {
        // Set score as minimum -- from CPU's point of view
        node.setScore(options.stream()
                .map(MoveNode::getScore)
                .reduce(Math::min)
                .get());
      }
    }

    return node;
  }
}
