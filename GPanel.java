
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.Timer;

public class GPanel extends JPanel implements ActionListener, KeyListener
{
    private final Timer time = new Timer(5,this);
    private int changeX;
    private int changeY;
    private int locX;
    private int locY;
    private final int width;
    private final int height;
    private int ballX;
    private int ballY;
    private int changeBX;
    private int changeBY;

    public GPanel()
    {
        time.start();

        locX = 300;
        locY = 550;
        width = 50;
        height = 50;
        ballX = 300;
        ballY = 50;
        changeBX = 1;
        changeBY = 1;
        changeX = 0; // initial increase/decrease amount for locX
        changeY = 0; // initial increase/decrease amount for locY

        addKeyListener(this); // must add keylistener to the component
        setFocusable(true); // setFocusable needs to be set to true
        setFocusTraversalKeysEnabled(false); // setFocusTraversalKeysEnabled should be set to false to override certain keys
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        int box = 0;
        for(int i = 100; i <= 800; i+=100)
        {
            for(int j = 100; j <= 800; j+=100)
            {
                if(box%2 == 0)
                {
                    g.setColor(new Color(255,255,255));
                    g.fillRect(j,i,100,100);
                }
                else
                {
                    g.setColor(new Color(0,0,0));
                    g.fillRect(j,i,100,100);
                }
                box++;
            }
            box++;
        }
        /*g.setColor(new Color(38,45,187));
        g.fillRect(25,30,50,50);
        g.setColor(new Color(100,150,100));
        g.fillOval(ballX,ballY,25,25);
        g.setColor(new Color(200,100,100));
        g.fillRect(locX,locY,width,height);*/
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
        locX+=changeX;
        locY+=changeY;
        repaint();
    }

    public void keyPressed(KeyEvent e)
    {
        int c = e.getKeyCode();
        if(c == KeyEvent.VK_LEFT)
        {
            changeX=-5;
        }
        if(c == KeyEvent.VK_RIGHT)
        {
            changeX = 5;
        }
        if(c == KeyEvent.VK_UP)
        {
            changeY = -5;
        }
        if(c == KeyEvent.VK_DOWN)
        {
            changeY = 5;
        }
    }

    public void keyReleased(KeyEvent e)
    {

    }

    public void keyTyped(KeyEvent e)
    {
        int c = e.getKeyCode();
        if(c == KeyEvent.VK_LEFT)
        {
            changeX=-10;
        }
        if(c == KeyEvent.VK_RIGHT)
        {
            changeX = 10;
        }
    }
}
