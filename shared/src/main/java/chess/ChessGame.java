package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements Cloneable {
    private TeamColor teamColar;
    private ChessBoard board;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamColar = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

        return teamColar;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamColar = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<chessMove> validMoves(chessPosition startPosition) {
        ChessPiece Piece = board.getPiece(startPosition);
        if(Piece == null) {
            return new ArrayList<>();
        }
        Collection<chessMove> moves = Piece.pieceMoves(board, startPosition);
        ArrayList<chessMove> validMoves = new ArrayList<>();
        for (chessMove move : moves) {
            ChessGame copiedGame = this.clone();
            copiedGame.board.addPiece(move.getStartPosition(), null);
            copiedGame.board.addPiece(move.getEndPosition(),Piece);
            if(!copiedGame.isInCheck(Piece.getTeamColor())){
                validMoves.add(move);
            }
        }
        return validMoves;
//        ChessBoard copiedBoard = board.copy;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */

    // make a copy of the board, then check is it in check????
    //I cannot delete piece on the original spot
    public void makeMove(chessMove move) throws InvalidMoveException {
        //make a copy of the ChessGame??
        //then make a copy of the board...
        Collection<chessMove> validMoves = validMoves(move.getStartPosition());

        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move");
        }
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if(piece.getTeamColor() != teamColar) {
            throw new InvalidMoveException("Invalid move:not correct team color");
        }


        board.addPiece(move.getStartPosition(), null);

        if (move.getPromotionPiece() == null) {

            board.addPiece(move.getEndPosition(), piece);
        }
        else{
            ChessPiece promotedPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            board.addPiece(move.getEndPosition(), promotedPiece);
        }

            //switch term
        if(teamColar == TeamColor.BLACK){
            teamColar = TeamColor.WHITE;
        }
        else{
            teamColar = TeamColor.BLACK;
        }

        //should I use try and cathc here??????????????


    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
//        ChessPiece kingPiece = null;
        chessPosition kingPosition = null;
        for(int i =1; i<=8; i++){
            for(int j =1; j<=8; j++){
                ChessPiece targetedPiece = board.getPiece(new chessPosition(i, j));
                if(targetedPiece == null ||  targetedPiece.getTeamColor() != teamColor){
                    continue;
                }
                else if (targetedPiece.getPieceType() == ChessPiece.PieceType.KING && targetedPiece.getTeamColor() == teamColor) {
//                    kingPiece =    targetedPiece;
                    kingPosition = new chessPosition(i, j);
                    break;
                }
            }
        }

        for(int i =1; i<=8; i++){
            for(int j =1; j<=8; j++){
                ChessPiece targetedPiece = board.getPiece(new chessPosition(i, j));
                if (targetedPiece == null || targetedPiece.getTeamColor() == teamColor) {
                    continue;
                }
                chessPosition piecePosition = new chessPosition(i, j);
                Collection<chessMove> moves = targetedPiece.pieceMoves(board, piecePosition);
                for (chessMove move : moves) {
                    chessPosition goingPosition = move.getEndPosition();
                    if ( goingPosition.equals(kingPosition)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    //this one tells if the game is end
    //how t heck should I check this
    public boolean isInCheckmate(TeamColor teamColor) {
        if(this.isInCheck(teamColor)){
            for(int i =1; i<=8; i++){
                for(int j =1; j<=8; j++){
                    chessPosition detectingPosition = new chessPosition(i, j);
                    ChessPiece detectingPiece = board.getPiece(detectingPosition);
                    if(detectingPiece != null && detectingPiece.getTeamColor() == teamColor){
                        Collection<chessMove> validMoves = this.validMoves(detectingPosition);
                        if(!validMoves.isEmpty()){
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    //How t heck should I do this
    //what does this one do
    public boolean isInStalemate(TeamColor teamColor) {
        if(this.isInCheck(teamColor)){
            return false;
        }
        for(int i =1; i<=8; i++){
            for(int j =1; j<=8; j++){
                chessPosition detectingPosition = new chessPosition(i, j);
                ChessPiece detectingPiece = board.getPiece(detectingPosition);
                if (detectingPiece == null || detectingPiece.getTeamColor() != teamColor)
                {
                    continue;
                }
                if (!validMoves(detectingPosition).isEmpty()) {
                    return false;
                }
            }
        }
        return true;

    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }


    @Override
    public ChessGame clone(){
        try{
            ChessGame clone = (ChessGame) super.clone();
            ChessBoard clonedBoard = new ChessBoard();
            for (int i = 1; i<=8; i++){
                for (int j = 1; j<=8; j++){
                    ChessPiece movingPiece = board.getPiece(new chessPosition(i, j));
                    clonedBoard.addPiece(new chessPosition(i, j), movingPiece);
                }
            }
            clone.board = clonedBoard;
            //Do I need to make a deepcopy for teamColor ??????????
            clone.teamColar = teamColar;
            return clone;
        } catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamColar == chessGame.teamColar && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColar, board);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamColar=" + teamColar +
                ", board=" + board +
                '}';
    }
}

