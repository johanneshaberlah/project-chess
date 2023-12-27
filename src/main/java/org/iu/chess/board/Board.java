package org.iu.chess.board;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.iu.chess.Square;
import org.iu.chess.game.GameEndListener;
import org.iu.chess.move.IllegalMoveException;
import org.iu.chess.move.Move;
import org.iu.chess.piece.Pawn;
import org.iu.chess.piece.Piece;
import org.iu.chess.piece.PieceColor;

import java.util.*;

public class Board {
  private final Collection<GameEndListener> gameEndListeners = Lists.newArrayList();

  private final Map<Square, Optional<Piece>> squares;

  private Board(Map<Square, Optional<Piece>> squares) {
    this.squares = squares;
  }

  public void registerGameEndListener(GameEndListener listener) {
    gameEndListeners.add(listener);
  }

  public Optional<Piece> pieceAt(Square square) {
    return squares.get(square);
  }

  public Iterable<Square> squares() {
    return squares.keySet();
  }

  public void performMove(Move move) throws IllegalMoveException {
    var piece = squares.get(move.from()).orElseThrow(() -> IllegalMoveException.of(move));
    if (!piece.isLegalMove(this, move)) {
      throw IllegalMoveException.of(move);
    }
    // Run Simulation
    Board simulationBoard = Board.of(new HashMap<>(squares));
    commitMove(simulationBoard, move, piece);
    if (isCheck(simulationBoard, piece.color())) {
      throw IllegalMoveException.of(move);
    }
    commitMove(this, move, piece);
    piece.declareMoved();
  }

  private void commitMove(Board board, Move move, Piece piece) {
    if (piece.fenName() == 'K' && move.fileDistance() > 1) {
      // Castling detected
      performCastlingIfNecessary(board, move);
    } else {
      board.squares.put(move.from(), Optional.empty());
      board.squares.put(move.to(), Optional.of(piece));
    }
  }

  private void performCastlingIfNecessary(Board board, Move move) {
    pieceAt(move.from()).ifPresent(from -> pieceAt(move.to()).ifPresent(to -> {
      if (move.from().file() < move.to().file()) {
        squares.put(move.to().withFile(6), Optional.of(from));
        squares.put(move.to().withFile(5), Optional.of(to));
        squares.put(move.to(), Optional.empty());
        squares.put(move.from(), Optional.empty());
      } else {
        squares.put(move.to().withFile(1), Optional.of(from));
        squares.put(move.to().withFile(2), Optional.of(to));
        squares.put(move.from(), Optional.empty());
        squares.put(move.to(), Optional.empty());
      }
    }));
  }

  private boolean isCheck(Board board, PieceColor color) {
    return board.squares.entrySet().stream()
      .filter(entry -> entry.getValue().isPresent() && entry.getValue().get().color() == color)
      .filter(entry -> entry.getValue().isPresent() && entry.getValue().get().fenName() == 'K')
      .anyMatch(entry -> {
        var square = entry.getKey();
        var otherColor = entry.getValue().get().color();
        return piecesWithOtherColor(board.squares, otherColor).stream().anyMatch(otherPiece -> {
          var piece = board.pieceAt(otherPiece);
          return piece.map(value -> value.isLegalMove(board, new Move(otherPiece, square))).orElse(false);
        });
      });
  }

  public boolean isCheckMate(Board board, PieceColor pieceColor) {
    return isCheck(board, pieceColor) && isMate(board, pieceColor);
  }

  public boolean isStaleMate(Board board, PieceColor pieceColor) {
    return !isCheck(board, pieceColor) && isMate(board, pieceColor);
  }

  public boolean isMate(Board board, PieceColor pieceColor) {
    for (Square square : piecesWithColor(pieceColor)) {
      var piece = board.pieceAt(square);
      var legalMoves = piece.map(value -> value.legalMoves(board, square));
      if (!legalMoves.isPresent()) {
        continue;
      }
      for (Move move : legalMoves.get()) {
        // Run Simulation
        Board simulationBoard = Board.of(new HashMap<>(squares));
        commitMove(simulationBoard, move, piece.get());

        if (!isCheck(simulationBoard, piece.get().color())) {
          return false;
        }
      }
    }
    return true;
  }

  public Set<Square> piecesWithColor(PieceColor color) {
    return squares.entrySet().stream()
      .filter(entry -> entry.getValue().isPresent() && entry.getValue().get().color().equals(color))
      .map(Map.Entry::getKey)
      .collect(java.util.stream.Collectors.toSet());
  }

  public Set<Square> piecesWithOtherColor(Map<Square, Optional<Piece>> copy, PieceColor color) {
    return copy.entrySet().stream()
      .filter(entry -> entry.getValue().isPresent() && !entry.getValue().get().color().equals(color))
      .map(Map.Entry::getKey)
      .collect(java.util.stream.Collectors.toSet());
  }

  @Override
  public Board clone() {
    return Board.of(squares);
  }

  public static Board of(Map<Square, Optional<Piece>> position) {
    return new Board(new HashMap<>(position));
  }

  public static Board examplePosition() {
    Map<Square, Optional<Piece>> squares = Maps.newHashMap();
    for (int rank = 0; rank < 8; rank++) {
      for (int file = 0; file < 8; file++) {
        if (rank == 0 || rank == 7) {
          squares.put(new Square(file, rank), Optional.of(Pawn.ofColor(rank == 0 ? PieceColor.WHITE : PieceColor.BLACK)));
        } else {
          squares.put(new Square(file, rank), Optional.empty());
        }
      }
    }
    return new Board(squares);
  }

  public static Board startingPosition() {
    return new Board(Map.of());
  }
}
