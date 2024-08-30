import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RockPaperScissorGame extends JFrame {
    private int computerScore = 0;
    private int userScore = 0;
    private int attempts = 0;
    private final int maxAttempts = 10;

    private JLabel computerScoreLabel;
    private JLabel userScoreLabel;
    private JLabel resultLabel;
    private JLabel userChoiceLabel;
    private JLabel computerChoiceLabel;
    private JLabel attemptsLabel;
    private RoundedButton rockButton, paperButton, scissorButton, restartButton;

    private static final String[] CHOICES = {"rock", "paper", "scissor"};
    private static final Map<String, Map<String, String>> CHOICES_MAP = new HashMap<>();

    static {
        CHOICES_MAP.put("rock", Map.of("rock", "draw", "scissor", "win", "paper", "lose"));
        CHOICES_MAP.put("scissor", Map.of("rock", "lose", "scissor", "draw", "paper", "win"));
        CHOICES_MAP.put("paper", Map.of("rock", "win", "scissor", "lose", "paper", "draw"));
    }

    public RockPaperScissorGame() {
        setTitle("Rock Paper Scissor Game");
        setSize(600, 450);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Score Panel
        JPanel scorePanel = new JPanel();
        scorePanel.setBounds(150, 20, 300, 50);
        scorePanel.setLayout(new GridLayout(1, 2, 20, 0));

        computerScoreLabel = new JLabel("Computer: 0", SwingConstants.CENTER);
        computerScoreLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        scorePanel.add(computerScoreLabel);

        userScoreLabel = new JLabel("You: 0", SwingConstants.CENTER);
        userScoreLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        scorePanel.add(userScoreLabel);

        add(scorePanel);

        // Attempts Label
        attemptsLabel = new JLabel("Attempts: 0/10", SwingConstants.CENTER);
        attemptsLabel.setBounds(150, 70, 300, 30);
        attemptsLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        add(attemptsLabel);

        // Weapon Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(100, 110, 400, 100);
        buttonPanel.setLayout(new GridLayout(1, 3, 20, 0));

        rockButton = new RoundedButton("\uD83D\uDC4A", "rock");
        paperButton = new RoundedButton("\uD83D\uDCDC", "paper");
        scissorButton = new RoundedButton("\u2702", "scissor");

        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorButton);

        add(buttonPanel);

        // Details Panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setBounds(150, 230, 300, 100);
        detailsPanel.setLayout(new GridLayout(3, 1));

        userChoiceLabel = new JLabel("", SwingConstants.CENTER);
        userChoiceLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16)); // Adjusted font
        detailsPanel.add(userChoiceLabel);

        computerChoiceLabel = new JLabel("", SwingConstants.CENTER);
        computerChoiceLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16)); // Adjusted font
        detailsPanel.add(computerChoiceLabel);

        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        detailsPanel.add(resultLabel);

        add(detailsPanel);

        // Restart Button
        restartButton = new RoundedButton("Restart", "restart");
        restartButton.setBounds(250, 340, 100, 50); // Adjust size for better appearance
        restartButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        restartButton.setForeground(Color.BLACK);
        restartButton.setFocusPainted(false);
        restartButton.setVisible(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        add(restartButton);
    }

    private class RoundedButton extends JButton {
        private final int radius = 20;
        private final Color borderColor = Color.BLACK;
        private final Color backgroundColor = new Color(255, 213, 27);
        private final String actionCommand;

        public RoundedButton(String text, String actionCommand) {
            super(text);
            this.actionCommand = actionCommand;
            setActionCommand(actionCommand);
            setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new RoundedBorder(radius, borderColor)); // Apply black border
            setBackground(backgroundColor);
            setForeground(Color.BLACK);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setHorizontalAlignment(SwingConstants.CENTER);

            // Add ActionListener
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if ("restart".equals(actionCommand)) {
                        restartGame();
                    } else {
                        checker(actionCommand);
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw background
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            // Draw border
            g2d.setColor(borderColor);
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            // Draw text
            g2d.setColor(getForeground());
            FontMetrics fm = g2d.getFontMetrics();
            String text = getText();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();
            g2d.drawString(text, (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 2);
        }
    }

    private void checker(String userChoice) {
        if (attempts >= maxAttempts) {
            return;
        }

        Random random = new Random();
        String computerChoice = CHOICES[random.nextInt(CHOICES.length)];

        userChoiceLabel.setText("<html>You chose: <b>" + userChoice.toUpperCase() + "</b></html>");
        computerChoiceLabel.setText("<html>Computer chose: <b>" + computerChoice.toUpperCase() + "</b></html>");
        

        String result = CHOICES_MAP.get(userChoice).get(computerChoice);
        switch (result) {
            case "win":
                resultLabel.setText("YOU WIN!");
                resultLabel.setForeground(new Color(56, 142, 60));
                userScore++;
                break;
            case "lose":
                resultLabel.setText("YOU LOSE!");
                resultLabel.setForeground(new Color(211, 47, 47));
                computerScore++;
                break;
            default:
                resultLabel.setText("DRAW!");
                resultLabel.setForeground(new Color(0, 151, 167));
                break;
        }

        computerScoreLabel.setText("Computer: " + computerScore);
        userScoreLabel.setText("You: " + userScore);

        attempts++;
        attemptsLabel.setText("Attempts: " + attempts + "/" + maxAttempts);

        if (attempts >= maxAttempts) {
            announceWinner();
        }
    }

    private void announceWinner() {
        String winner;
        if (userScore > computerScore) {
            winner = "YOU WIN! \uD83D\uDC51";
        } else if (computerScore > userScore) {
            winner = "COMPUTER WINS! \uD83D\uDC51";
        } else {
            winner = "It's a DRAW! \uD83D\uDE1F";
        }

        resultLabel.setText(winner);
        resultLabel.setForeground(new Color(5, 196, 81));
        disableButtons();
        restartButton.setVisible(true);
    }

    private void disableButtons() {
        rockButton.setEnabled(false);
        paperButton.setEnabled(false);
        scissorButton.setEnabled(false);
    }

    private void restartGame() {
        userScore = 0;
        computerScore = 0;
        attempts = 0;
        computerScoreLabel.setText("Computer: 0");
        userScoreLabel.setText("You: 0");
        attemptsLabel.setText("Attempts: 0/10");
        userChoiceLabel.setText("");
        computerChoiceLabel.setText("");
        resultLabel.setText("");
        rockButton.setEnabled(true);
        paperButton.setEnabled(true);
        scissorButton.setEnabled(true);
        restartButton.setVisible(false);
    }

    private static class RoundedBorder implements Border {
        private final int radius;
        private final Color borderColor;

        RoundedBorder(int radius, Color borderColor) {
            this.radius = radius;
            this.borderColor = borderColor;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(borderColor);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RockPaperScissorGame().setVisible(true);
            }
        });
    }
}
