
/**
 * Author: Mr. Scutero and Period 1
 * Purpose: to learn how to do things with panels
 * This is the panel that will house the listeners and the objects
 */

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.BasicStroke;
import java.awt.Graphics2D;

public class JohnPanel extends JPanel implements ActionListener, MouseListener
{
    private final Timer time = new Timer(5,this);
    private final int changeX;
    private final int changeY;
    private int locY;
    private final int width;
    private final int height;
    private int ballX;
    private int ballY;
    private int changeBX;
    private int changeBY;
    private final ChessBoard myBoard;
    private final int startPosX = 50;
    private final int startPosY = 50;
    private final int cellSize = 100;
    private Position selectedPos = null;
    private Color defaultColor;
    private final Color clickedColor = Color.YELLOW;
    private final Color currentColor = defaultColor;

    public JohnPanel( ChessBoard board) throws java.io.IOException
    {
        time.start();

        locY = 550;
        width = 50;
        height = 50;
        ballX = 300;
        ballY = 50;
        changeBX = 1;
        changeBY = 1;
        changeX = 0; // initial increase/decrease amount for locX
        changeY = 0; // initial increase/decrease amount for locY

        addMouseListener(this); // must add keylistener to the component
        setFocusable(true); // setFocusable needs to be set to true
        setFocusTraversalKeysEnabled(false); // setFocusTraversalKeysEnabled should be set to false to override certain keys
        myBoard = board;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        int box = 0;
        try
        {
            ChessPositionCollection myCollection = myBoard.getChessPositions();
            int rowCount = myCollection.getRowCount(); // 8
            int colCount = myCollection.getColCount(); // 8
            for(int i = 0; i < rowCount; i++)
            {
                int posX = startPosX + i * cellSize; // could null
                for(int j = 0; j < colCount; j++)
                {
                    ChessPiece pz = myCollection.getPosition(i, j).getPiece();
                    int posY = startPosY + j * cellSize;
                    if(box%2 == 1)
                    {
                        g.setColor(new Color(255,255,255));
                        g.fillRect(posY, posX, cellSize, cellSize);
                    }
                    else
                    {
                        g.setColor(new Color(100,100,100));
                        g.fillRect(posY, posX, cellSize, cellSize);
                    }
                    if (pz != null) {
                        //System.out.println("gt is poverty");
                        BufferedImage originalImage = pz.getIcon();
                        g.drawImage(originalImage, posY, posX, cellSize, cellSize, null);
                    }

                    if (selectedPos != null && selectedPos.getX() == i && selectedPos.getY() == j) {
                        Graphics2D g2 = (Graphics2D)(g);
                        g2.setStroke(new BasicStroke(4));
                        g2.setColor(new Color(255, 255, 0));
                        g2.drawRect(posY, posX, cellSize, cellSize);
                    }

                    box++;
                }
                box++;
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public int getX(Position p) {
        return startPosX + p.getX() * cellSize;
    }

    public int getY(Position p) {
        return startPosY + p.getY() * cellSize;
    }

    public void actionPerformed(ActionEvent e)
    {

        if(ballY>600-height)
        {
            changeBY=-1;
        }
        if(ballX>600-width)
        {
            changeBX=-1;
        }
        if(ballX<0)
        {
            changeBX = 1;
        }
        if(ballY<0)
        {
            changeBY = 1;
        }
        ballX+=changeBX;
        ballY+=changeBY;
        locY+=changeY;
        repaint();
    }

    public void mouseMoved(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {
        try
        {
            ChessPositionCollection myCollection = myBoard.getChessPositions();
            System.out.println(e.getX() + ", " + e.getY());
            int rowIndex = (e.getY() - startPosY)/cellSize;
            int colIndex = (e.getX() - startPosX)/cellSize;
            System.out.println("rowIndx=" + rowIndex +  "colIndx=" + colIndex);
            Position pos = myCollection.getPosition(rowIndex, colIndex);
            System.out.println(pos.getPiece() + "==========");
            if (selectedPos != null) {
                boolean moveSuccessful = false;
                if (pos != selectedPos) {
                    moveSuccessful = myBoard.move(selectedPos, pos);
                }
                if (moveSuccessful) {
                    System.out.print("Moving " + selectedPos + " to " + pos);
                    myBoard.transform(pos);
                    selectedPos = null;  // reset the selectedPos
                }
                else {
                    throw new IOException("Illegal move");
                }
                repaint();
            } else if(selectedPos == null && pos.getPiece() != null) {
                if (myBoard.isWhiteTurn && pos.getPiece().getColor() == PlayerType.WHITE || !(myBoard.isWhiteTurn) && pos.getPiece().getColor() == PlayerType.BLACK) {
                    selectedPos = pos;
                    repaint(); // Trigger a repaint to update the color
                }
                else throw new IOException("Not your turn");
            } else {
                System.out.print("No piece is selected. Please click on a piece rather than empty space " + pos + "!");
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            JFrame frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this);
            JOptionPane.showMessageDialog(frame, ioe.getMessage(),"Error in Move", JOptionPane.ERROR_MESSAGE);
            selectedPos = null;  // reset the selectedPos
            repaint();
        }
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {}

}
