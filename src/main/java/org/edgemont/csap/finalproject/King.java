package org.edgemont.csap.finalproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * org.edgemont.csap.finalproject.King piece. org.edgemont.csap.finalproject.King remains the same and won't swap roles
 *
 * @author anderson zhang
 * @version 1.0
 */
public class King extends ChessPiece {
    private boolean castleable = true;

    public King(PlayerType color, Position x, String iconFile) throws IOException {
        super(color, "org.edgemont.csap.finalproject.King", x, iconFile);
    }

    public boolean isInCheck(Position p) throws IOException {
        return p != null && p.isOccupied() && ChessBoard.getInstance().isInCheck(p.getPiece().getColor());
    }
    public ArrayList<Position> getLegalMoves() throws IOException {
        return getLegalMoves(true);
    }

    private ArrayList<Position> getLegalMoves(boolean checkInCheck) throws IOException {
        ArrayList<Position> legals = new ArrayList<>();
        ChessPositionCollection posCollection = ChessBoard.getInstance().getPositionCollection();
        Position[] temp = {
                posCollection.getPosition(pos.getX() + 1, pos.getY() + 1),
                posCollection.getPosition(pos.getX(), pos.getY() + 1),
                posCollection.getPosition(pos.getX() - 1, pos.getY() + 1),
                posCollection.getPosition(pos.getX() + 1, pos.getY()),
                posCollection.getPosition(pos.getX() + 1, pos.getY() - 1),
                posCollection.getPosition(pos.getX(), pos.getY() - 1),
                posCollection.getPosition(pos.getX() - 1, pos.getY() - 1),
                posCollection.getPosition(pos.getX() - 1, pos.getY())
        };
        for (Position i : temp) {
            if ((!checkInCheck || !isInCheck(i)) && isLegalPos(i)
                    && !isNextToOppsKing(i)) legals.add(i);
        }
        if (checkInCheck) {
            ArrayList<Position> castles = getCastlePositions();
            legals.addAll(castles);
        }
        return legals;
    }

    private boolean isNextToOppsKing(Position p) throws IOException {
        ChessPositionCollection posCollection = ChessBoard.getInstance().getPositionCollection();
        Position[] temp = {
                posCollection.getPosition(p.getX() + 1, p.getY() + 1),
                posCollection.getPosition(p.getX(), p.getY() + 1),
                posCollection.getPosition(p.getX() - 1, p.getY() + 1),
                posCollection.getPosition(p.getX() + 1, p.getY()),
                posCollection.getPosition(p.getX() + 1, p.getY() - 1),
                posCollection.getPosition(p.getX(), p.getY() - 1),
                posCollection.getPosition(p.getX() - 1, p.getY() - 1),
                posCollection.getPosition(p.getX() - 1, p.getY())
        };
        for (Position i : temp) {
            if (i != null && i.isOccupied() && i.getPiece() instanceof King
                    && i.getPiece().getColor() != getColor()) {
                System.out.println("org.edgemont.csap.finalproject.Position is next to the opponent king" + i.toString() + ": " + i.getPiece().toString());
                return true;
            }
        }
        return false;
    }

    /**
     * Castling is unlikely because pieces are randomly disguised, but exists just in case it is possible
     * CASTLE RULES:
     * Neither the king nor the rook has previously moved.
     * There are no pieces between the king and the rook.
     * The king is not currently in check.
     * The king does not pass through or finish on a square that is attacked by an enemy piece.
     * @return positions that can be castled
     */
    protected ArrayList<Position> getCastlePositions() throws IOException {
        ChessPositionCollection posCollection = ChessBoard.getInstance().getPositionCollection();
        ArrayList<Position> castles = new ArrayList<Position>();
        if(!isInCheck(pos)) {
            Position[] queenSide = {posCollection.getPosition(pos.getX(), pos.getY() -2),
                    posCollection.getPosition(pos.getX(), pos.getY()-1)};
            Position[] kingSide = {posCollection.getPosition(pos.getX(), pos.getY() +2),
                    posCollection.getPosition(pos.getX(), pos.getY()+1)};
            if(castleChecker(queenSide, posCollection.getPosition(pos.getX(), 0),
                    posCollection.getPosition(pos.getX(), 1)))
                castles.add(posCollection.getPosition(pos.getX(), pos.getY() -2));
            if(castleChecker(kingSide, posCollection.getPosition(pos.getX(), 7), null))
                castles.add(posCollection.getPosition(pos.getX(), pos.getY() +2));
        }
        return castles;
    }

    private boolean castleChecker(Position[] walkList, Position rookPos, Position rookExtra) throws IOException {
        if(!castleable) return false;
        for(Position pq : walkList) {
            if(pq.isOccupied() || isInCheck(pq)) return false;
        }
        if(rookExtra != null && rookExtra.isOccupied()) return false;
        return rookPos.isOccupied() &&
                rookPos.getPiece() instanceof Rook &&
                ((Rook) rookPos.getPiece()).castleable;
    }

    public Stack<ChessMoveBreakdown> move(Position d) throws IOException {
        Stack<ChessMoveBreakdown> moves = super.move(d);
        castleable = false;
        return moves;
    }

    public ArrayList<Position> getLegalMovesSkipCheckInCheck() throws IOException {
        return getLegalMoves(false);
    }
}