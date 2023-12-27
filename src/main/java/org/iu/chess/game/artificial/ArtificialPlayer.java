package org.iu.chess.game.artificial;

import org.iu.chess.board.Board;
import org.iu.chess.game.Game;
import org.iu.chess.game.InvalidGameActionException;
import org.iu.chess.game.player.Player;
import org.iu.chess.game.player.PlayerClock;
import org.iu.chess.game.player.PlayerMove;
import org.iu.chess.move.IllegalMoveException;
import org.iu.chess.move.Move;
import org.iu.chess.move.RelativeMoveWithRequirement;
import org.iu.chess.piece.Piece;
import org.iu.chess.piece.PieceColor;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ArtificialPlayer extends Player {
  private ArtificialPlayer(Optional<PlayerClock> clock) {
    super("Computer", new HashSet<>(), clock);
  }

  public void makeMove(Game game, PieceColor pieceColor) {
    List<Move> legalMoves = game.position().piecesWithColor(pieceColor).stream()
      .filter(square -> game.position().pieceAt(square).isPresent())
      .flatMap(square -> {
        var piece = game.position().pieceAt(square).get();
        return piece.reachableMoves().stream().map(RelativeMoveWithRequirement::move).map(move -> move.asMove(square));
      }).filter((Move move) -> {
        Board simulation = game.position().clone();
        try {
          simulation.performMove(move);
          return true;
        } catch (IllegalMoveException e) {
          return false;
        }
      }).toList();
    var move = evaluateBestMove(legalMoves, game.position(), pieceColor);
    try {
      game.performMove(new PlayerMove(game.playerWithColor(pieceColor), move));
    } catch (IllegalMoveException | InvalidGameActionException e) {
      // Retry
      makeMove(game, pieceColor);
    }
  }

  private Move evaluateBestMove(List<Move> moves, Board position, PieceColor color) {
    return moves.get(ThreadLocalRandom.current().nextInt(0, moves.size()));
  }

  private int simulateMoveAndEvaluatePieceScore(Move move, PieceColor pieceColor, Board board) {
    Board simulationBoard = board.clone();
    try {
      simulationBoard.performMove(move);
    } catch (IllegalMoveException e) {
      return -1;
    }
    return evaluatePositionPieceScore(simulationBoard, pieceColor);
  }

  private int evaluatePositionPieceScore(Board board, PieceColor pieceColor) {
    return board.piecesWithColor(pieceColor).stream().map(board::pieceAt).filter(Optional::isPresent).map(Optional::get).mapToInt(Piece::value).sum();
  }

  public static ArtificialPlayer create(Optional<PlayerClock> clock) {
    return new ArtificialPlayer(clock);
  }
}
