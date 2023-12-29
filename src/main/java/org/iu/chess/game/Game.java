package org.iu.chess.game;

import com.google.common.collect.Lists;
import org.iu.chess.board.Board;
import org.iu.chess.game.artificial.ArtificialPlayer;
import org.iu.chess.game.player.Player;
import org.iu.chess.game.player.PlayerClock;
import org.iu.chess.game.player.PlayerMove;
import org.iu.chess.game.player.PlayerTuple;
import org.iu.chess.game.termination.TerminalGameStateComponent;
import org.iu.chess.move.IllegalMoveException;
import org.iu.chess.piece.PieceColor;

import java.util.Collection;
import java.util.Optional;
import java.util.Stack;

public class Game {
  private final TerminalGameStateComponent terminalStates = TerminalGameStateComponent.of(this);
  private final Collection<GameEndListener> gameEndingHandlers = Lists.newArrayList();

  private final Optional<GameTimingStrategy> timingStrategy; // Empty if the game mode is "correspondence" (without timing)
  private final PlayerTuple players;
  private final Stack<PlayerMove> moves;
  private final Board position;

  public Game(
    Optional<GameTimingStrategy> timingStrategy,
    PlayerTuple players,
    Stack<PlayerMove> moves,
    Board position
  ) {
    this.timingStrategy = timingStrategy;
    this.players = players;
    this.moves = moves;
    this.position = position;
  }

  public void registerGameEndHandler(GameEndListener gameEnding) {
    gameEndingHandlers.add(gameEnding);
  }

  public void start() {
    playerWithColor(PieceColor.WHITE).clock().ifPresent(PlayerClock::beginMove);
  }

  public void performMove(PlayerMove move) throws IllegalMoveException, InvalidGameActionException {
    // Checks if the same player tries to move twice
    if (!nextMove().equals(players.playerColor(move.player()))) {
      throw InvalidGameActionException.create(move.move(), "It is not the turn of " + move.player());
    }
    // Finish the current move (stop clock, perform the move on the board, push it to the history)
    var targetPiece = position.pieceAt(move.move().to());
    position.performMove(move.move());
    moves.push(move);
    terminalStates.evaluateTerminalGameState().ifPresent(state -> gameEndingHandlers.forEach(handler -> handler.onGameEnd(state)));
    targetPiece.ifPresent(piece -> playerWithColor(piece.color()).loose(piece));

    // Prepare move for next player (Start Clock and if it is an artificial player, make the move)
    var otherPlayer = players.otherPlayer(move.player());
    move.player().clock().ifPresent(PlayerClock::finishMove);
    otherPlayer.clock().ifPresent(PlayerClock::beginMove);
    if (otherPlayer instanceof ArtificialPlayer artificialPlayer) {
      artificialPlayer.makeMove(this, players.playerColor(artificialPlayer));
    }
  }

  public void pause() {
    playerWithColor(PieceColor.WHITE).clock().ifPresent(PlayerClock::pause);
    playerWithColor(PieceColor.BLACK).clock().ifPresent(PlayerClock::pause);
  }

  public void restartClock() {
    playerWithColor(nextMove()).clock().ifPresent(PlayerClock::beginMove);
  }

  public void checkForTerminalGameState() {
    terminalStates.evaluateTerminalGameState().ifPresent(state -> gameEndingHandlers.forEach(handler -> handler.onGameEnd(state)));
  }

  public PieceColor nextMove() {
    if (moves.isEmpty()) {
      return PieceColor.WHITE;
    }
    return players.playerColor(players.otherPlayer(moves.peek().player()));
  }

  public Stack<PlayerMove> moves() {
    // Since stacks are mutable we don't want to expose them publicly to avoid #clear calls
    var movesCopy = new Stack<PlayerMove>();
    moves.forEach(movesCopy::push);
    return movesCopy;
  }

  public Board position() {
    return position;
  }

  public Player playerWithColor(PieceColor color) {
    return color.equals(PieceColor.WHITE) ? players.white() : players.black();
  }

  public PlayerTuple players() {
    return players;
  }

  public Optional<GameTimingStrategy> timingStrategy() {
    return timingStrategy;
  }

}
