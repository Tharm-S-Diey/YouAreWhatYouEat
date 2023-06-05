package org.edgemont.csap.finalproject;

import org.edgemont.csap.finalproject.ChessPiece;
import org.edgemont.csap.finalproject.ChessPositionCollection;

import java.io.IOException;
import java.util.ArrayList;

public class Queen extends ChessPiece
{
    private final boolean move2 = true;
    
    public Queen(PlayerType color, Position x, String iconFile) throws IOException {
        super(color, "org.edgemont.csap.finalproject.Queen", x, iconFile);
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

        legalX = pos.getX()-1;
        legalY = pos.getY()-1;
        while(legalX >= 0 && legalY >= 0) {
            Position p = posCollection.getPosition(legalX, legalY);
            if (p.getPiece() != null) {
                if (p.getPiece().getColor() != this.getColor()) legals.add(p);
                break;
            }
            else {
                legals.add(p);
                legalX--;
                legalY--;
            }
        }
        legalX = pos.getX()+1;
        legalY = pos.getY()+1;
        while(legalX <= 7 && legalY <= 7) {
            if(!(posCollection.getPosition(legalX, legalY).getPiece() == null)) {
                if(posCollection.getPosition(legalX, legalY).getPiece().getColor() != this.getColor()) legals.add(posCollection.getPosition(legalX, legalY));
                break;
            }
            else {
                legals.add(posCollection.getPosition(legalX, legalY));
                legalX++;
                legalY++;
            }
        }
        legalX = pos.getX()-1;
        legalY = pos.getY()+1;
        while(legalX >= 0 && legalY <= 7) {
            if(!(posCollection.getPosition(legalX, legalY).getPiece()== null)) {
                if(posCollection.getPosition(legalX, legalY).getPiece().getColor() != this.getColor()) legals.add(posCollection.getPosition(legalX, legalY));
                break;
            }
            else {
                legals.add(posCollection.getPosition(legalX, legalY));
                legalX--;
                legalY++;
            }
        }
        legalX = pos.getX()+1;
        legalY = pos.getY()-1;
        while(legalX <= 7 && legalY >= 0) {
            if (!(posCollection.getPosition(legalX, legalY).getPiece() == null)) {
                if (posCollection.getPosition(legalX, legalY).getPiece().getColor() != this.getColor())
                    legals.add(posCollection.getPosition(legalX, legalY));
                break;
            } else {
                legals.add(posCollection.getPosition(legalX, legalY));
                legalX++;
                legalY--;
            }
        }
            return legals;
    }
}
