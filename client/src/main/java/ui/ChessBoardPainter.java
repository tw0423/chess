package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class ChessBoardPainter {
    private static EscapeSequences es = new EscapeSequences();

    private static final int boardSquareLength = 8;
    private static final int squareCharactersWidth = 1;

    //I don't know what should logic here to be so I will use chessGame first
    private static ChessGame chessGame;
    private static SquareColor squareColor = SquareColor.WHITE;
    private static ChessGame.TeamColor color;
    public ChessBoardPainter(ChessGame chessGame, String playerColor) {
        this.chessGame = chessGame;
        if (playerColor.equals("white")) {
            color = ChessGame.TeamColor.WHITE;
        }else{
            color = ChessGame.TeamColor.BLACK;
        }

    }

    public static void main(String[] args) {

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(es.ERASE_SCREEN);

//        drawHeaders(out);

        drawBoard(out);

//        out.print(SET_BG_COLOR_BLACK);
//        out.print(SET_TEXT_COLOR_WHITE);
    }

    public static void drawBoard(PrintStream out) {
        drawColumnLabels(out);
        if(color.equals(ChessGame.TeamColor.BLACK)){
            squareColor = SquareColor.GREY;
        }else{
            squareColor = SquareColor.WHITE;
        }
        for (int row = 0; row < boardSquareLength; row++) {
            labelNumer(row, out);
            drawSquares(out, row);
//            labelNumer(row, out);
            //reset the colors
            switchSqaureColors(out);
            setBlack(out);
            out.println();
            out.print(es.RESET_TEXT_COLOR);
            out.print(es.RESET_BG_COLOR);
        }



    }

    public static void drawSquares(PrintStream out, int row) {
        int middleline = squareCharactersWidth / 2;
        for (int subline = 0; subline < squareCharactersWidth; ++subline) {
            for (int squareNum = 0; squareNum < boardSquareLength; ++squareNum) {
                switchSqaureColors(out);
                if (subline == middleline) {
                    int prefixLength = squareCharactersWidth / 2;
                    int suffixLength = squareCharactersWidth - prefixLength - 1;
                    out.print(es.EMPTY.repeat(prefixLength));
                    printPlayer(out, row, squareNum);
                    out.print(es.EMPTY.repeat(suffixLength));
                }
                else {
                    out.print(es.EMPTY.repeat(squareCharactersWidth));
                }


            }
//            out.println();


        }
    }

    private static void switchSqaureColors(PrintStream out) {
        if(squareColor==SquareColor.GREY){
            setGrey(out);
            squareColor = SquareColor.WHITE;

        }else{
            setWhite(out);
            squareColor = SquareColor.GREY;
        }
    }

    private static void printPlayer(PrintStream out, int row, int col) {
        ChessPiece piece;
        if(color.equals(ChessGame.TeamColor.BLACK)){
             piece = chessGame.getBoard().getPiece(new ChessPosition(row+1, col+1));
        }else{
             piece = chessGame.getBoard().getPiece(new ChessPosition(9-(row+1), 9-(col+1)));
        }
        if (piece == null) {
            out.print(es.EMPTY);
            return;
        }
        ChessPiece.PieceType type = piece.getPieceType();
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        out.print(es.SET_TEXT_COLOR_BLACK);
        if(type != null && teamColor != null){
            if (teamColor == ChessGame.TeamColor.BLACK) {
                switch (type){
                    case KING:
                        out.print(es.BLACK_KING);
                        break;
                    case QUEEN:
                        out.print(es.BLACK_QUEEN);
                        break;
                    case BISHOP:
                        out.print(es.BLACK_BISHOP);
                        break;
                    case KNIGHT:
                        out.print(es.BLACK_KNIGHT);
                        break;
                    case ROOK:
                        out.print(es.BLACK_ROOK);
                        break;
                    case PAWN:
                        out.print(es.BLACK_PAWN);
                        break;
                }
            }else{
                switch (type){
                    case KING:
                        out.print(es.WHITE_KING);
                        break;
                    case QUEEN:
                        out.print(es.WHITE_QUEEN);
                        break;
                    case BISHOP:
                        out.print(es.WHITE_BISHOP);
                        break;
                    case KNIGHT:
                        out.print(es.WHITE_KNIGHT);
                        break;
                    case ROOK:
                        out.print(es.WHITE_ROOK);
                        break;
                    case PAWN:
                        out.print(es.WHITE_PAWN);
                        break;
                }

            }
            out.print(es.RESET_TEXT_COLOR);
        }

    }


    private static void setWhite(PrintStream out) {
        out.print(es.SET_BG_COLOR_WHITE);
        out.print(es.SET_TEXT_COLOR_WHITE);
    }

    private static void setGrey(PrintStream out) {
        out.print(es.SET_BG_COLOR_LIGHT_GREY);
        out.print(es.SET_TEXT_COLOR_LIGHT_GREY);
    }



    private static void setBlack(PrintStream out) {
        out.print(es.SET_BG_COLOR_BLACK);
        out.print(es.SET_TEXT_COLOR_BLACK);
    }

    private static void drawColumnLabels(PrintStream out) {
        setBlack(out);
        out.print(es.SET_TEXT_COLOR_WHITE);
        out.print("   "); // Align with left row numbers
        for (int i = 0; i < boardSquareLength; i++) {
            char colLabel = (char) ('a' + (color == ChessGame.TeamColor.WHITE ? i : boardSquareLength - 1 - i));
            out.print("  " + colLabel + "");
        }
        out.println();
        out.print(es.RESET_TEXT_COLOR);
        out.print(es.RESET_BG_COLOR);
    }

    private static void labelNumer(int i, PrintStream out){
        out.print(es.SET_TEXT_COLOR_WHITE);
        out.print(String.valueOf(i+1));
        out.print(es.EMPTY);
        out.print(es.RESET_TEXT_COLOR);
    }


}
