package passoff.chess.game;

import chess.ChessGame;
import chess.chessMove;
import chess.chessPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.chess.TestUtilities;

import java.util.ArrayList;
import java.util.List;

public class ValidMovesTests {
    private static final String TRAPPED_PIECE_MOVES = "ChessGame validMoves returned valid moves for a trapped piece";

    @Test
    @DisplayName("Check Forces Movement")
    public void forcedMove() {

        var game = new ChessGame();
        game.setTeamTurn(ChessGame.TeamColor.BLACK);
        game.setBoard(TestUtilities.loadBoard("""
                    | | | | | | | | |
                    | | | | | | | | |
                    | |B| | | | | | |
                    | | | | | |K| | |
                    | | |n| | | | | |
                    | | | | | | | | |
                    | | | |q| |k| | |
                    | | | | | | | | |
                    """));

        // Knight moves
        chessPosition knightPosition = new chessPosition(4, 3);
        var validMoves = TestUtilities.loadMoves(knightPosition, new int[][]{{3, 5}, {6, 2}});
        assertMoves(game, validMoves, knightPosition);

        // Queen Moves
        chessPosition queenPosition = new chessPosition(2, 4);
        validMoves = TestUtilities.loadMoves(queenPosition, new int[][]{{3, 5}, {4, 4}});
        assertMoves(game, validMoves, queenPosition);
    }


    @Test
    @DisplayName("Piece Partially Trapped")
    public void moveIntoCheck() {

        var game = new ChessGame();
        game.setBoard(TestUtilities.loadBoard("""
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    |k|r| | | |R| |K|
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    """));

        chessPosition rookPosition = new chessPosition(5, 6);
        var validMoves = TestUtilities.loadMoves(rookPosition, new int[][]{
                {5, 7}, {5, 5}, {5, 4}, {5, 3}, {5, 2}
        });

        assertMoves(game, validMoves, rookPosition);
    }

    @Test
    @DisplayName("Piece Completely Trapped")
    public void rookPinnedToKing() {

        var game = new ChessGame();
        game.setBoard(TestUtilities.loadBoard("""
                    |K| | | | | | |Q|
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | |r| | | | |
                    | | | | | | | | |
                    | |k| | | | | | |
                    | | | | | | | | |
                    """));

        chessPosition position = new chessPosition(4, 4);
        Assertions.assertTrue(game.validMoves(position).isEmpty(), TRAPPED_PIECE_MOVES);
    }


    @Test
    @DisplayName("Pieces Cannot Eliminate Check")
    public void kingInDanger() {

        var game = new ChessGame();
        game.setTeamTurn(ChessGame.TeamColor.BLACK);
        game.setBoard(TestUtilities.loadBoard("""
                    |R| | | | | | | |
                    | | | |k| | | |b|
                    | | | | |P| | | |
                    |K| |Q|n| | | | |
                    | | | | | | | | |
                    | | | | | | | |r|
                    | | | | | |p| | |
                    | |q| | | | | | |
                    """));

        //get positions
        chessPosition kingPosition = new chessPosition(7, 4);
        chessPosition pawnPosition = new chessPosition(2, 6);
        chessPosition bishopPosition = new chessPosition(7, 8);
        chessPosition queenPosition = new chessPosition(1, 2);
        chessPosition knightPosition = new chessPosition(5, 4);
        chessPosition rookPosition = new chessPosition(3, 8);


        var validMoves = TestUtilities.loadMoves(kingPosition, new int[][]{{6, 5}});

        assertMoves(game, validMoves, kingPosition);

        //make sure teams other pieces are not allowed to move
        Assertions.assertTrue(game.validMoves(pawnPosition).isEmpty(), TRAPPED_PIECE_MOVES);
        Assertions.assertTrue(game.validMoves(bishopPosition).isEmpty(), TRAPPED_PIECE_MOVES);
        Assertions.assertTrue(game.validMoves(queenPosition).isEmpty(), TRAPPED_PIECE_MOVES);
        Assertions.assertTrue(game.validMoves(knightPosition).isEmpty(), TRAPPED_PIECE_MOVES);
        Assertions.assertTrue(game.validMoves(rookPosition).isEmpty(), TRAPPED_PIECE_MOVES);
    }


    @Test
    @DisplayName("King Cannot Move Into Check")
    public void noPutSelfInDanger() {

        var game = new ChessGame();
        game.setBoard(TestUtilities.loadBoard("""
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | |k| | |
                    | | | | | | | | |
                    | | | | | |K| | |
                    | | | | | | | | |
                    """));

        chessPosition position = new chessPosition(2, 6);
        var validMoves = TestUtilities.loadMoves(position, new int[][]{
                {1, 5}, {1, 6}, {1, 7}, {2, 5}, {2, 7},
        });
        assertMoves(game, validMoves, position);
    }

    @Test
    @DisplayName("Valid Moves Independent of Team Turn")
    public void validMovesOtherTeam() {
        var game = new ChessGame();
        game.setBoard(TestUtilities.defaultBoard());
        game.setTeamTurn(ChessGame.TeamColor.BLACK);

        chessPosition position = new chessPosition(2, 5);
        var validMoves = TestUtilities.loadMoves(position, new int[][]{
                {3, 5}, {4, 5}
        });
        assertMoves(game, validMoves, position);
    }

    private static void assertMoves(ChessGame game, List<chessMove> validMoves, chessPosition position) {
        var generatedMoves = game.validMoves(position);
        var actualMoves = new ArrayList<>(generatedMoves);
        TestUtilities.validateMoves(validMoves, actualMoves);
    }
}
