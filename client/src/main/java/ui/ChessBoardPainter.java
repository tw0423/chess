package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class ChessBoardPainter {
    private static EscapeSequences es = new EscapeSequences();

    private static final int BoardSquareLength = 8;
    private static final int SquareCharactersWidth = 1;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    //I don't know what should logic here to be so I will use chessGame first
    private static ChessGame chessGame = new ChessGame();
    private static SquareColor squareColor = SquareColor.WHITE;


    public static void main(String[] args) {

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(es.ERASE_SCREEN);

//        drawHeaders(out);

        drawBoard(out);

//        out.print(SET_BG_COLOR_BLACK);
//        out.print(SET_TEXT_COLOR_WHITE);
    }

    public static void drawBoard(PrintStream out) {
        for (int row = 0; row < BoardSquareLength; row++) {
            drawSquares(out, row);
            //reset the colors
            switchSqaureColors(out);
            out.println();
        }

    }

    public static void drawSquares(PrintStream out, int row) {
        int middleline = SquareCharactersWidth / 2;
        for (int subline = 0; subline < SquareCharactersWidth; ++subline) {
            for (int squareNum = 0; squareNum < BoardSquareLength; ++squareNum) {
                switchSqaureColors(out);
                if (subline == middleline) {
                    int prefixLength = SquareCharactersWidth / 2;
                    int suffixLength = SquareCharactersWidth - prefixLength - 1;
                    out.print(es.EMPTY.repeat(prefixLength));
                    printPlayer(out, row, squareNum);
                    out.print(es.EMPTY.repeat(suffixLength));
                }
                else {
                    out.print(es.EMPTY.repeat(SquareCharactersWidth));
                }

                //If want to draw a vertical line, put it here, but I don't want to

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
        ChessPiece piece = chessGame.getBoard().getPiece(new ChessPosition(row+1, col+1));
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

    private static void setRed(PrintStream out) {
        out.print(es.SET_BG_COLOR_RED);
        out.print(es.SET_TEXT_COLOR_RED);
    }

    private static void setBlack(PrintStream out) {
        out.print(es.SET_BG_COLOR_BLACK);
        out.print(es.SET_TEXT_COLOR_BLACK);
    }

    private static void setGreen(PrintStream out) {
        out.print(es.SET_BG_COLOR_GREEN);
        out.print(es.SET_TEXT_COLOR_GREEN);
    }


}
