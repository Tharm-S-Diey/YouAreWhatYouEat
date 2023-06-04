import java.io.IOException;
import java.util.ArrayList;
public class ChessBoard {
    // We don't need to define the types, but use polymorphism: {"Pawn", "Rook", "Knight", "Bishop", "King", "Queen"};    
    private final ChessPositionCollection chessPositions;
    private final ArrayList<ChessPiece> blacks;
    private final ArrayList<ChessPiece> whites;

    private King blackKing;
    private King whiteKing;
    public boolean isWhiteTurn = true;
    private ChessPiece selectedPiece;
    private Position selectedPosition;
    //private Position[][] board;
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
        String colorPrefix = player == PlayerType.BLACK ? "src/images/b": "src/images/w";
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

        return pieces;
    }

    public boolean isCheckMate(PlayerType player) throws IOException {
        King king = player == PlayerType.BLACK ? blackKing : whiteKing;
        return (king.getLegalMovesSkipCheckInCheck().size() == 0 && isInCheck(player));
    }

    public boolean move(Position src, Position tgt) throws IOException {
        ChessPiece pz = src.getPiece();
        if(pz != null) {
            if (pz.canMove(tgt)) {
                ChessPiece capturedPz = tgt.getPiece();
                pz.move(tgt);
                // if this causes myself to be checked, we have to undo
                if (isInCheck(pz.getColor())) {
                    pz.move(src, false);
                    tgt.setPiece(capturedPz);
                    if (capturedPz != null) {
                        capturedPz.setPos(tgt);
                    }
                    // break out as no move has taken place
                    throw new IOException("Illegal move; potential check");
                }
                castleMove(src, tgt, pz);
                enPassantMove(src, tgt, pz);
                System.out.println("public void move(src, tgt) works");
                isWhiteTurn = !isWhiteTurn;

                PlayerType opponent = pz.getColor() == PlayerType.BLACK? PlayerType.WHITE: PlayerType.BLACK;

                if(isInCheck(opponent)) {
                    String checkMsg = (pz.getColor() == PlayerType.BLACK ? "BLACK " : "WHITE ") + "is checked";
                    if (isCheckMate(opponent)) checkMsg = "Checkmate. " + opponent + " is defeated";
                    //System.out.println("ERROR! ERROR! ERROR! ERROR! ERROR! ERROR! ERROR! ERROR! \n ERROR! ERROR! ERROR! ERROR! ERROR! ERROR! ERROR! ERROR! \n ERROR! ERROR! ERROR! ERROR! ERROR! ERROR! ERROR! ERROR!");
                    throw new IOException(checkMsg);
                }
            }
            return true;
        }
        else {
            throw new IOException("Illegal Move");
        }
    }

    public void transform(Position pos) throws IOException {
        if (pos.getPiece() != null){
            ChessPiece transformed = pos.getPiece().transform();
            if (transformed != null) {
                pos.setPiece(transformed);
                transformed.setPos(pos);
            }
        }
    }

    public void castleMove(Position src, Position tgt, ChessPiece piece) throws IOException {
        if(piece instanceof King) {
            if(Math.abs(src.getY()-tgt.getY()) == 2) {
                Position rookPos = chessPositions.getPosition(src.getX(), src.getY() > tgt.getY() ? 0 : 7);
                Position newRookPos = chessPositions.getPosition(src.getX(), (src.getY()+ tgt.getY())/2);
                rookPos.getPiece().move(newRookPos, false);
            }
        }
    }

    public void enPassantMove(Position src, Position tgt, ChessPiece piece) throws IOException {
        if(piece instanceof Pawn) {
            if(src.getX() != tgt.getX() && src.getY() != tgt.getY()) {
                Position epPos = chessPositions.getPosition(src.getX(), tgt.getY());
                if (epPos.isOccupied()) epPos.getPiece().setPos(null);  // the piece has no position, it's captured.
                epPos.setPiece(null);
            }
        }
    }

    /**
     * check occurs when a player's king can be captured by the opponent
     *
     */
    public boolean isInCheck(PlayerType player) throws IOException {
        ArrayList<ChessPiece> opponentPieces = player == PlayerType.BLACK ? whites : blacks;
        ArrayList<ChessPiece> playerPieces = player == PlayerType.BLACK ? blacks : whites;
        Position kingPos = null;
        for (ChessPiece p : playerPieces) {
            if(p instanceof  King) {
                kingPos = p.getPos();
                break;
            }
        }
        for(ChessPiece op : opponentPieces) {
            if (op.pos != null) {

                ArrayList<Position> legals = (op instanceof King) ?
                        ((King)op).getLegalMovesSkipCheckInCheck(): op.getLegalMoves();
                if(legals.contains(kingPos)) {
                    //((King)kingPos.getPiece()).setInCheck(true);
                    return true;
                }
            }
        }
        return false;
    }

    public ChessPositionCollection getPositionCollection() {
        return chessPositions;
    }
}