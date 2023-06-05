package org.edgemont.csap.finalproject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ChessBoard {
    // We don't need to define the types, but use polymorphism: {"org.edgemont.csap.finalproject.Pawn", "org.edgemont.csap.finalproject.Rook", "org.edgemont.csap.finalproject.Knight", "Bishop", "org.edgemont.csap.finalproject.King", "org.edgemont.csap.finalproject.Queen"};
    private final ChessPositionCollection chessPositions;
    private final ArrayList<ChessPiece> blacks;
    private final ArrayList<ChessPiece> whites;

    private King blackKing;
    private King whiteKing;
    private Stack<Stack<ChessMoveBreakdown>> blackMoveHistory = new Stack<>();
    private Stack<Stack<ChessMoveBreakdown>> whiteMoveHistory = new Stack<>();
    public boolean isWhiteTurn = true;
    private ChessPiece selectedPiece;
    private Position selectedPosition;
    //private org.edgemont.csap.finalproject.Position[][] board;
    private static ChessBoard instance = null;
    private ChessBoard() throws IOException {
        chessPositions = new ChessPositionCollection();
        blacks = initPlayerPieces(PlayerType.BLACK);
        whites = initPlayerPieces(PlayerType.WHITE);
    }

    public ArrayList<ChessPiece> getPlayerPieces(PlayerType p) {
        ArrayList<ChessPiece> returnedPieces = p == PlayerType.BLACK ? blacks : whites;
        return returnedPieces;
    }

    public static ChessBoard getInstance() throws IOException {
        if (instance == null) instance = new ChessBoard();
        return instance;
    }
    public ChessPositionCollection getChessPositions() throws IOException {
        return chessPositions;
    }

    private ArrayList<ChessPiece> initPlayerPieces(PlayerType player) throws IOException {
        // initialize all pieces and place them in the right position
        ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>(16);
        String colorPrefix = player == PlayerType.BLACK ? "images/b": "images/w";
        int pieceRow = player == PlayerType.BLACK ? 0: 7;
        // pawns:
        for (int i=0; i< 8; i++) {
            int pawnRow = player == PlayerType.BLACK ? 1: 6;
            // row 2 is black pawn row
            Position pos = chessPositions.getPosition(pawnRow, i);
            Pawn pawn = new Pawn(player, pos, colorPrefix + "pawn.png");
            pieces.add(pawn);
            pos.setPiece(pawn);
        }
        
        // rooks        
        for (Position pos: new Position[] {chessPositions.getPosition(pieceRow, 0),
            chessPositions.getPosition(pieceRow, 7)}) {
            ChessPiece pc = new Rook(player, pos, colorPrefix + "rook.png");
            pieces.add(pc);
            pos.setPiece(pc);
        }

        // knights
        for (Position pos: new Position[] {chessPositions.getPosition(pieceRow, 1),
            chessPositions.getPosition(pieceRow, 6)}) {
            ChessPiece pc = new Knight(player, pos, colorPrefix + "knight.png");
            pieces.add(pc);
            pos.setPiece(pc);
        }

        // bishops        
        for (Position pos: new Position[] {chessPositions.getPosition(pieceRow, 2),
            chessPositions.getPosition(pieceRow, 5)}) {
            ChessPiece pc = new Bishop(player, pos, colorPrefix + "bishop.png");
            pieces.add(pc);
            pos.setPiece(pc);
        }
        // queen
        Position pos = chessPositions.getPosition(pieceRow, 3);
        ChessPiece pc = new Queen(player, pos, colorPrefix + "queen.png");
        pieces.add(pc);
        pos.setPiece(pc);
        
        // king
        pos = chessPositions.getPosition(pieceRow, 4);
        pc = new King(player, pos, colorPrefix + "king.png");
        pieces.add(pc);
        pos.setPiece(pc);
        if (player == PlayerType.BLACK) blackKing = (King)pc;
        else whiteKing = (King)pc;

        // randomScatter()
        randomScatter(pieces, player);
        return pieces;
    }

    private void randomScatter(ArrayList<ChessPiece> pieces, PlayerType player) {
        int[] yVals = player == PlayerType.BLACK ? new int[]{0, 1, 2, 3} : new int[]{4, 5, 6, 7};
        for(ChessPiece pc : pieces){
            if(!(pc instanceof King)) pc.getPos().setPiece(null);
        }
        for(ChessPiece pc : pieces) {
            if(pc instanceof King) continue;
            Position pos;
            while(true) {
                int xVal = (int)(Math.random() * 8);
                int yVal = yVals[(int)(Math.random() * 4)];
                pos = chessPositions.getPosition(yVal, xVal);
                if(!(pos.isOccupied())){
                    pos.setPiece(pc);
                    pc.setPos(pos);
                    break;
                }
            }
        }
    }

    public boolean isCheckMate(PlayerType player) throws IOException {
        King king = player == PlayerType.BLACK ? blackKing : whiteKing;
        return (king.getLegalMovesSkipCheckInCheck().size() == 0 && isInCheck(player));
    }

    public boolean move(Position src, Position tgt) throws IOException, CloneNotSupportedException, MoveAlertException, InvalidMoveException {
        ChessPiece pz = src.getPiece();
        if(pz != null) {
            Stack<ChessMoveBreakdown> thisMoveStack = new Stack<>();
            if (!pz.canMove(tgt)) {
                System.out.println("Cannot move " + src + " to " + tgt);
                throw new InvalidMoveException("Illegal move from " + src + " to " + tgt);
            }
            else {
                ChessPiece capturedPz = tgt.getPiece();
                thisMoveStack = pz.move(tgt);   // could be capture + move or move

                Stack<ChessMoveBreakdown> moves = castleMove(src, tgt, pz);
                if (moves != null && moves.size() > 0) {
                    for (ChessMoveBreakdown m : moves)
                        thisMoveStack.push(m);
                }
                moves = enPassantMove(src, tgt, pz);
                if (moves != null && moves.size() > 0) {
                    for (ChessMoveBreakdown m : moves)
                        thisMoveStack.push(m);
                    if (capturedPz == null) capturedPz = moves.peek().getPiece();
                }

                System.out.println("public void move(src, tgt) works");
                if (capturedPz != null && !(pz instanceof King)) {
                    moves = universalTransform(pz.getPos(), capturedPz);
                    if (moves != null && moves.size() > 0) {
                        for (ChessMoveBreakdown m : moves)
                            thisMoveStack.push(m);
                    }
                }
                if (pz instanceof Pawn) {
                    moves = tryPromote(pz.getPos());
                    if (moves != null && moves.size() > 0) {
                        for (ChessMoveBreakdown m : moves)
                            thisMoveStack.push(m);
                    }
                }

                // if this causes myself to be checked, we have to undo
                if (isInCheck(pz.getColor())) {  // undo the move stack
                    while (!thisMoveStack.isEmpty()) {
                        ChessMoveBreakdown m = thisMoveStack.pop();
                        m.undo();
                    }
                    throw new InvalidMoveException("Illegal move; potential check");
                }

                isWhiteTurn = !isWhiteTurn;

                PlayerType opponent = pz.getColor() == PlayerType.BLACK? PlayerType.WHITE: PlayerType.BLACK;

                String checkMsg = null;
                if(isInCheck(opponent)) checkMsg= opponent + " is checked";
                if (isCheckMate(opponent)) checkMsg = "Checkmate. " + opponent + " is defeated";
                if (checkMsg != null) throw new MoveAlertException(checkMsg);
            }

            if (thisMoveStack.size() > 0) blackMoveHistory.add(thisMoveStack);
            else {
                System.out.println("No real move");
            }
            return true;
        }
        else {
            throw new InvalidMoveException("No piece to move " + src + " to " + tgt);
        }
    }

    public Stack<ChessMoveBreakdown> universalTransform(Position pos, ChessPiece tgtPz) throws IOException, CloneNotSupportedException {
        ChessPiece origPz = pos.getPiece();
        assert origPz != null;
        Stack<ChessMoveBreakdown> moves = new Stack<>();
        ChessPiece newPz = tgtPz.cloneForOpponent();
        moves.push(ChessMoveBreakdown.promoteChess(pos, newPz));
        return moves;
    }
    public Stack<ChessMoveBreakdown> tryPromote(Position pos) throws IOException {
        Stack<ChessMoveBreakdown> moves = null;
        if (pos.getPiece() != null){
            ChessPiece transformed = pos.getPiece().promote();
            if (transformed != null) {
                moves = new Stack<>();
                moves.push(ChessMoveBreakdown.promoteChess(pos, transformed));
//                pos.setPiece(transformed);
//                transformed.setPos(pos);
            }
        }
        return moves;
    }

    public Stack<ChessMoveBreakdown> castleMove(Position src, Position tgt, ChessPiece piece) throws IOException {
        if(piece instanceof King) {
            if(Math.abs(src.getY()-tgt.getY()) == 2) {
                Position rookPos = chessPositions.getPosition(src.getX(), src.getY() > tgt.getY() ? 0 : 7);
                Position newRookPos = chessPositions.getPosition(src.getX(), (src.getY()+ tgt.getY())/2);
                return rookPos.getPiece().move(newRookPos, false);
            }
        }
        return null;
    }

    /**
     * Return the captured piece if applicable
     */
    public Stack<ChessMoveBreakdown> enPassantMove(Position src, Position tgt, ChessPiece piece) throws IOException {
//        org.edgemont.csap.finalproject.ChessPiece captured = null;
        Stack<ChessMoveBreakdown> moves = null;
        if(piece instanceof Pawn) {
            if(src.getX() != tgt.getX() && src.getY() != tgt.getY()) {
                Position epPos = chessPositions.getPosition(src.getX(), tgt.getY());
                if (epPos.isOccupied()) {
                    moves = new Stack<>();
                    moves.push(ChessMoveBreakdown.captureChess(epPos));
//                    captured = epPos.getPiece();
//                    epPos.getPiece().setPos(null);  // the piece has no position, it's captured.
//                    epPos.setPiece(null);
                }
            }
        }
        return moves;
//        return captured;
    }

    /**
     * check occurs when a player's king can be captured by the opponent
     *
     */
    public boolean isInCheck(PlayerType player) throws IOException {
        ArrayList<ChessPiece> opponentPieces = player == PlayerType.BLACK ? whites : blacks;
        Position kingPos = player == PlayerType.BLACK ? blackKing.getPos() : whiteKing.getPos();
        for(ChessPiece op : opponentPieces) {
            if (op.pos != null) {

                ArrayList<Position> legals = (op instanceof King) ?
                        ((King)op).getLegalMovesSkipCheckInCheck(): op.getLegalMoves();
                if(legals.contains(kingPos)) {
                    //((org.edgemont.csap.finalproject.King)kingPos.getPiece()).setInCheck(true);
                    return true;
                }
            }
        }
        return false;
    }

    public ChessPositionCollection getPositionCollection() {
        return chessPositions;
    }

    public ChessBoard reset() throws IOException {
        instance = new ChessBoard();
        return instance;
    }

    public void replace(ChessPiece origPz, ChessPiece newPiece) {
        List<ChessPiece> playerPieces = (origPz.getColor() == PlayerType.BLACK)? blacks : whites;
        playerPieces.remove(origPz);
        playerPieces = (newPiece.getColor() == PlayerType.BLACK)? blacks : whites;
        playerPieces.add(newPiece);
    }
}