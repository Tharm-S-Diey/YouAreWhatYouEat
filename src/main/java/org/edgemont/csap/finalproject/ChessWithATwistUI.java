package org.edgemont.csap.finalproject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class ChessWithATwistUI extends JFrame
{
    private static Image menuPanelBgImg;

    private JPanel activePanel = null;
    private JPanel mainMenuPanel, tutorialPanel, creditPanel, buttonsPanel;
    private JohnPanel gamePanel;
    private JButton startButton, menuButton, tutorialButton, creditsButton, newGameButton;
    private static final Color mainMenuBgColor = Color.WHITE;

    public ChessWithATwistUI(String title) throws IOException {
        super(title);
        menuPanelBgImg = ImageIO.read(Objects.requireNonNull(ChessWithATwistUI.class.getClassLoader().getResource("images/logo.png")));
    }

    public void setActivePanel(JPanel panel) {
        if (panel == activePanel) return;
        if (activePanel != null) activePanel.setVisible(false);
//        this.setSize(900, 900);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setVisible(true);
        panel.setVisible(true);
        activePanel = panel;
    }

    public JPanel getMainMenuPanel() throws IOException {
        if (mainMenuPanel == null) {
            mainMenuPanel = new JPanel(new GridBagLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(menuPanelBgImg, 0, 0, null);
                }
            };
            mainMenuPanel.setVisible(false);
            mainMenuPanel.setBackground(mainMenuBgColor);
            mainMenuPanel.setBounds(100, 100, 900, 900);

            JPanel startButtonPanel = createStartButtonPanel();
            // add start button panel
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.CENTER;
//            c.anchor = GridBagConstraints.CENTER; // Center alignment
//            c.insets = new Insets(10, 10, 10, 10); // Add some padding
//            c.ipady = 40;      //make this component tall
            c.weightx = 0.0;
            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 1;
            mainMenuPanel.add(startButtonPanel, c);
            // add
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            mainMenuPanel.add(createButtonPanel(), c);

            add(mainMenuPanel);
        }
        return mainMenuPanel;
    }

    private JPanel createStartButtonPanel() {
        JPanel startButtonPanel = new JPanel();
        startButtonPanel.setBounds(300, 300, 800, 800);
        startButtonPanel.setBackground(mainMenuBgColor);
        startButton = new JButton("Start");
        startButton.setFont(new Font("Cambria", 1, 55));
        startButton.setPreferredSize(new Dimension(400, 100));
        // Create an ActionListener for the button
        startButton.addActionListener(e -> {
            // Method to be executed when the button is clicked
            System.out.println("Button clicked!");
            try {
                startGame();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        });
        startButtonPanel.add(startButton);
        return startButtonPanel;
    }

    public static void main(String[] args) throws IOException {
        ChessWithATwistUI myFrame = new ChessWithATwistUI("Randomized Chess!");
        myFrame.setSize(900, 900);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setVisible(true);
        myFrame.setActivePanel(myFrame.getMainMenuPanel());
    }

    public JohnPanel createGamePanel() throws IOException {
        gamePanel = new JohnPanel(ChessBoard.getInstance().reset());
        add(gamePanel);
        gamePanel.add(createButtonPanel());
        return gamePanel;
    }

    public void startGame() throws IOException {
        JohnPanel gamePanel = createGamePanel();
        setActivePanel(gamePanel);
    }

    private JPanel createFileBasedTextPanel(String resourceFile) {
        JPanel txtPanel = new JPanel(new GridBagLayout());
        txtPanel.setVisible(false);
        List<String> lines = null;
        URL file = ChessWithATwistUI.class.getClassLoader().getResource(resourceFile);
        try {
            lines = Files.readAllLines(Path.of(file.toURI()));
        } catch (Exception e) {
            lines.add("ERROR reading resource file ");
            lines.add(e.getMessage());
        }
        String howToHtmlText = String.join("\n", lines);
        JLabel txtLabel;
        txtLabel = new JLabel(howToHtmlText);
        txtLabel.setPreferredSize(new Dimension(800, 800));
        txtLabel.setVerticalAlignment(1);
        txtLabel.setVerticalTextPosition(0);
        txtLabel.setHorizontalTextPosition(0);
        txtLabel.setHorizontalAlignment(0);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        txtPanel.add(createButtonPanel(), c);
        c.gridy = 1;
        txtPanel.add(txtLabel, c);
        return txtPanel;
    }

    public JPanel getTutorialPage() {
        if (tutorialPanel == null) {
            tutorialPanel = createFileBasedTextPanel("files/tutorial.html");
            add(tutorialPanel);
        }
        return tutorialPanel;
    }

    public JPanel getCreditsPage() {
        if (creditPanel == null) {
            creditPanel = createFileBasedTextPanel("files/credits.html");
            add(creditPanel);
        }
        return creditPanel;
    }

    public JPanel createButtonPanel() {
        menuButton = new JButton("Menu");
        menuButton.setFont(new Font("Cambria", 1, 20));
        menuButton.setPreferredSize(new Dimension(100, 25));
        // Create an ActionListener for the button
        menuButton.addActionListener(e -> {
            // Method to be executed when the button is clicked
            System.out.println("Menu clicked!");
            try {
                setActivePanel(getMainMenuPanel());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        tutorialButton = new JButton("Tutorial");
        tutorialButton.setFont(new Font("Cambria", 1, 20));
        tutorialButton.setPreferredSize(new Dimension(120, 25));
        tutorialButton.addActionListener(e -> {
            // Method to be executed when the button is clicked
            System.out.println("Tutorial clicked!");
            setActivePanel(getTutorialPage());
        });

        creditsButton = new JButton("Credits");
        creditsButton.setFont(new Font("Cambria", 1, 20));
        creditsButton.setPreferredSize(new Dimension(100, 25));
        // Create an ActionListener for the button
        creditsButton.addActionListener(e -> {
            // Method to be executed when the button is clicked
            System.out.println("Credits clicked!");
            setActivePanel(getCreditsPage());
        });

        newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Cambria", 1, 20));
        newGameButton.setPreferredSize(new Dimension(150, 25));
        // Create an ActionListener for the button
        newGameButton.addActionListener(e -> {
            // Method to be executed when the button is clicked
            System.out.println("NewGame clicked!");
            try {
                startGame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints bpc = new GridBagConstraints();
        bpc.fill = GridBagConstraints.HORIZONTAL;
        bpc.gridx = 0;
        bpc.gridy = 0;
        //bpc.gridheight = 100;
        buttonsPanel.setBackground(mainMenuBgColor);
        mainMenuPanel.add(buttonsPanel, bpc);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        buttonsPanel.add(menuButton, c);
        c.gridx = 1;
        buttonsPanel.add(tutorialButton, c);
        c.gridx = 2;
        buttonsPanel.add(creditsButton, c);
        c.gridx = 3;
        buttonsPanel.add(newGameButton, c);
        return buttonsPanel;
    }
}
