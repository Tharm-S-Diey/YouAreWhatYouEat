import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.io.IOException;

public class RandomChessGUI extends JFrame
{
    private static JFrame myFrame;
    private static JPanel myPanel, buttonPanel;
    private static JButton startButton;
    private static JLabel myLabel, yourLabel;
    private static final ChessBoard chessBoard = null;

    public static void main(String[] args) {
        myPanel = new JPanel(new GridBagLayout());
        myPanel.setBackground(new Color(175, 175, 175));
        myPanel.setBounds(100, 100, 900, 900);
        
        buttonPanel = new JPanel();
        buttonPanel.setBounds(300, 300, 300, 600);
        buttonPanel.setBackground(new Color(175, 175, 175));
        
        myLabel = new JLabel(">>>>randomized");
        yourLabel = new JLabel("chess.<<<");
        yourLabel.setFont(new Font("Verdana", 1, 55));
        myLabel.setFont(new Font("Verdana", 1, 35));
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;  // 100% width
        constraints.weighty = 1.0;  // 100% height
        constraints.anchor = GridBagConstraints.CENTER; // Center alignment
        constraints.insets = new Insets(10, 10, 10, 10); // Add some padding
        
        GridBagConstraints constraints2 = new GridBagConstraints();
        constraints2.gridx = 0;
        constraints2.gridy = 30;
        constraints2.gridwidth = 3;
        constraints2.weightx = 1.0;  // 100% width
        constraints2.weighty = 1.0;  // 100% height
        constraints2.anchor = GridBagConstraints.PAGE_START; // Center alignment
        constraints2.insets = new Insets(10, 0, 10, 10); // Add some padding

        startButton = new JButton("Start");
        startButton.setFont(new Font("Cambria", 1, 55));
        startButton.setPreferredSize(new Dimension(400, 100));
        // Create an ActionListener for the button
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Method to be executed when the button is clicked
                System.out.println("Button clicked!");
                try {
                    startGame();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        };
        
        // Add the ActionListener to the button
        startButton.addActionListener(actionListener);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        
        buttonPanel.add(startButton);
        myPanel.add(myLabel, constraints);
        myPanel.add(buttonPanel, c);
        myPanel.add(yourLabel, constraints2);
        
        myFrame = new JFrame("Randomized Chess!");
        myFrame.setSize(900, 900);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setVisible(true);
        myFrame.add(myPanel);
    }

    public static void startGame() throws IOException {
//        myFrame = new JFrame("Randomized Chess! (in session, please wait...)");
        myFrame.setSize(900, 900);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setVisible(true);
        JohnPanel boardPanel = new JohnPanel(ChessBoard.getInstance());
        myFrame.add(boardPanel);
        myPanel.setVisible(false);
        boardPanel.setVisible(true);
    }
}
