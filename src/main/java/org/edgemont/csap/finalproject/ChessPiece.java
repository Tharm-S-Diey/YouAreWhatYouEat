package org.edgemont.csap.finalproject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

/**
 * Only here to make a foundation for any of the chess pieces that will be implemented later
 *
 * @author anderson zhang
 * @version 1.0
 */
public abstract class ChessPiece implements Cloneable{
    private String fakeType;
    private PlayerType color; 
    public Position pos;
    private BufferedImage icon;
    private String iconFile;
    public ChessPiece(PlayerType c, String ft, Position p, String iconFile) throws IOException {
        color = c;
        fakeType = ft;
        pos = p;
//        System.out.println(iconFile);
        icon = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource(iconFile)));
        this.iconFile = iconFile;
    }

    /**
     * All the following methods are accessor and mutator methods.
     * @return
     */
    public PlayerType getColor() {return color;}
    public void setColor(PlayerType c) throws IOException {
        color = c;
        String currPrefix, newPrefix;
        if (c == PlayerType.BLACK){  // new color
            currPrefix = "/w";
            newPrefix = "/b";
        } else {
            currPrefix = "/b";
            newPrefix = "/w";
        }
        this.iconFile = this.iconFile.replace(currPrefix, newPrefix);
        icon = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource(iconFile)));
    }
    public BufferedImage getIcon() {return icon;}
    public Position getPos() {return pos;}
    public void setPos(Position p) { pos = p;}
    public void setImage(BufferedImage p) {icon = p;}


    public void disp(Graphics g) {
        int x = pos.getX();
        int y = pos.getY();

        g.drawImage(icon, x, y, null);
    }

    public abstract ArrayList<Position> getLegalMoves() throws IOException;

    public boolean canMove(Position d) throws IOException {
        return getLegalMoves().contains(d);
    }

    public Stack<ChessMoveBreakdown> move(Position d) throws IOException {
        return this.move(d, true);
    }

    /**
     * checkLegal is for the small chance of castling being legal. it is involved in forcing the rook to move to a certain position during castling
     * @param d
     * @param checkLegal
     * @throws IOException
     */
    public Stack<ChessMoveBreakdown> move(Position d, boolean checkLegal) throws IOException {
        // If pawn, the caller should've changed the piece object
        // Assume that the move is legal, and canMove is already checked
        if (checkLegal) {
            ArrayList<Position> legals = getLegalMoves();
            if(legals.size() < 1 || !legals.contains(d)) {
                throw new IOException("Not a legal move");
            }
        }
        Stack<ChessMoveBreakdown> moves = new Stack<>();
        if (d.getPiece() != null) {
            moves.push(ChessMoveBreakdown.captureChess(d));
        }
        moves.push(ChessMoveBreakdown.moveChess(this, this.getPos(), d));
        /*
        pos.setPiece(null);  // clear out current position
        pos = d;  // set the destination position
        d.setPiece(this);
        */
        return moves;
    }

    public ChessPiece promote() throws IOException {
        return null;
    }

    protected boolean isLegalPos(Position p) {
        return (p != null &&
                (p.getPiece() == null
                || p.getPiece() != null && p.getPiece().getColor() != this.color)
        );
    }

    public ChessPiece cloneForOpponent() throws CloneNotSupportedException, IOException {
        ChessPiece pz = (ChessPiece) this.clone();
        assert pz != this;
        pz.setColor(this.color == PlayerType.BLACK? PlayerType.WHITE: PlayerType.BLACK);
        return pz;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(Color:" + color + ")";
    }
}
