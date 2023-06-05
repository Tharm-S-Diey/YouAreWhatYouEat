package org.edgemont.csap.finalproject;

import org.edgemont.csap.finalproject.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Rook extends ChessPiece {
    protected boolean castleable = true; // can the rook and king castle?

    public Rook(PlayerType color, Position x, String iconFile) throws IOException {
        super(color, "org.edgemont.csap.finalproject.Rook", x, iconFile);
    }

    public ArrayList<Position> getLegalMoves() throws IOException {
        ArrayList<Position> legals = new ArrayList<Position>();
        ChessPositionCollection posCollection = ChessBoard.getInstance().getPositionCollection();
        int legalX = pos.getX();
        int legalY = pos.getY() - 1;
        while (legalY >= 0) {
            Position p = posCollection.getPosition(legalX, legalY);
            if (isLegalPos(p)) {
                legals.add(posCollection.getPosition(legalX, legalY));
                if (p.getPiece() != null) break;
            }
            legalY--;
        }
        legalY = pos.getY() + 1;
        while (legalY <= 7) {
            Position p = posCollection.getPosition(legalX, legalY);
            if (isLegalPos(p)) {
                legals.add(posCollection.getPosition(legalX, legalY));
                if (p.getPiece() != null) break;
            }
            legalY++;
        }

        legalY = pos.getY();
        legalX--;
        while (legalX >= 0) {
            Position p = posCollection.getPosition(legalX, legalY);
            if (isLegalPos(p)) {
                legals.add(posCollection.getPosition(legalX, legalY));
                if (p.getPiece() != null) break;
            }
            legalX--;
        }

        legalX = pos.getX() + 1;
        while (legalX <= 7) {
            Position p = posCollection.getPosition(legalX, legalY);
            if (isLegalPos(p)) {
                legals.add(posCollection.getPosition(legalX, legalY));
                if (p.getPiece() != null) break;
            }
            legalX++;
        }
        return legals;
    }

    public Stack<ChessMoveBreakdown> move(Position d) throws IOException {
        Stack<ChessMoveBreakdown> moves = super.move(d);
        castleable = false;
        return moves;
    }

    @Override
    public ChessPiece cloneForOpponent() throws CloneNotSupportedException, IOException {
        Rook p = (Rook)super.cloneForOpponent();
        p.castleable = false;
        return p;
    }
}
