import chess.ChessGame;

public record DisplayingGameData(int displayingGameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
}
