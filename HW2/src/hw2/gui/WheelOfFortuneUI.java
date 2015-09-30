package hw2.gui;

import hw2.PhraseGenerator;
import hw2.SecretPhrase;
import hw2.Wheel;
import hw2.WheelOfFortuneGame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Graphical user interface for WheelOfFortuneGame.  Edit the main method
 * below to change the phrase file.  Note that the image file for the wheel,
 * defined by the constant WHEEL_FILENAME, needs to be added to the
 * hw2.gui package in Eclipse.  (Eclipse will copy it over to bin\hw2\gui,
 * from where it can be loaded by the WheelCanvas class.)
 */
public class WheelOfFortuneUI extends JPanel
{ 
  /**
   * Entry point.  Edit here to change the phrase file.
 * @throws FileNotFoundException 
   */
  public static void main(String[] args) throws FileNotFoundException
  {
    PhraseGenerator g = new PhraseGenerator("phrases.txt");
    WheelOfFortuneUI.start(g); 
  }
  
  /**
   * Name of image for wheel.
   */
  public static final String WHEEL_FILENAME = "wheel_300x300.png";
  
  /**
   * Width of each panel.
   */
  private static final int PANEL_WIDTH = 900;
    
  /**
   * Height of the canvas for drawing the word.
   */
  private static final int TEXT_HEIGHT = 100;
  
  /**
   * Height of the panel for the letter buttons.
   */
  private static final int LETTERS_HEIGHT = 100;

  /**
   * Height of panel for player info.
   */
  private static final int PLAYER_HEIGHT = 300;
    
  /**
   * Height for the letter board panel.
   */
  private static final int BOARD_HEIGHT = 300;

  /**
   * Height for the letter board panel.
   */
  private static final int WHEEL_SIZE = 300;

  /**
   * Width for the letter board panel.
   */
  private static final int BOARD_WIDTH = 600;
  
  /**
   * Starting character of the alphabet.
   */
  private static final char START_CHAR = 'A';
  
  /**
   * The word generator.
   */
  private final PhraseGenerator generator;
  
  /**
   * Current instance of the game.
   */
  private WheelOfFortuneGame game;
  
  /**
   * Letters that have already been guessed.
   */
  private boolean[] guesses = new boolean[26];
  
  /**
   * Keep track of player totals over multiple rounds.
   */
  private int[] playerTotal = new int[2];
  
  /**
   * Custom panel for drawing the letter board.
   */
  private BoardCanvas boardCanvas;
  
  /**
   * Custom panel for drawing the wheel.
   */
  private WheelCanvas wheelCanvas;

  /**
   * Label for player names.
   */
  private JLabel[] playerNameLabel;
  
  /**
   * Label for player balances.
   */
  private JLabel[] playerBalanceLabel;
  
  /**
   * Button for player to select buying a vowel.
   */
  private JButton[] playerBuyVowelButton;
  
  /**
   * Button for player to select solving the puzzle.
   */
  private JButton[] playerSolveButton;
  
  /**
   * Button for player to select spinning the wheel.
   */  
  private JButton[] playerSpinButton;
  
  /**
   * Panel for arranging player info.
   */
  private JPanel[] playerPanel;

  /**
   * Buttons for the letters of the alphabet.
   */
  private JButton[] buttons;
  
  /**
   * Button for starting another round.
   */
  private JButton playAgainButton;
  
  /**
   * Puts a task on the Swing event queue to instantiate
   * all the components.
   * @param generator
   *   the PhraseGenerator the game will use
   */
  public static void start(final PhraseGenerator generator)
  {
    Runnable r = new Runnable()
    {
      public void run()
      {
        createAndShow(generator);
      }
    };
    SwingUtilities.invokeLater(r);
  }
  
  /**
   * Creates the enclosing frame and starts up the UI machinery.
   * @param generator
   *   the WordGenerator to be used
   */
  private static void createAndShow(PhraseGenerator generator)
  {    
    // create the frame
    JFrame frame = new JFrame("CS227 Wheel of Fortune");

    try
    {   
      // create an instance of our JPanel subclass and 
      // add it to the frame
      frame.getContentPane().add(new WheelOfFortuneUI(generator));

      // use the preferred sizes
      frame.pack();

      // we want to shut down the application if the 
      // "close" button is pressed on the frame
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // make the frame visible and start the UI machinery
      frame.setVisible(true);
    }
    catch (FileNotFoundException e)
    {
      JOptionPane.showMessageDialog(frame, "Unable to open file: " + e.getMessage());
      frame.dispose();
    }
  }

  
  /**
   * Constructor sets up Swing components and initializes the first game.
   * @param generator
   *   the PhraseGenerator to be used 
   * @throws FileNotFoundException
   *   if the file referred to by the PhraseGenerator cannot be opened 
   */
  private WheelOfFortuneUI(PhraseGenerator generator) throws FileNotFoundException
  {
    this.generator = generator;

    // set up the first game
    Wheel wheel = new Wheel();
    game = new WheelOfFortuneGame(wheel); 
    String phrase = generator.getRandomPhrase();
    game.startRound(0, new SecretPhrase(phrase));

    //
    // Rest of constructor creates and lays out the Swing components
    //
    
    // Custom panel for drawing the board
    boardCanvas = new BoardCanvas(game);
    boardCanvas.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    //boardCanvas.setAllText(game.getDisplay());

    // Custom panel for the animated wheel
    wheelCanvas = new WheelCanvas(this, game);
    wheelCanvas.setPreferredSize(new Dimension(WHEEL_SIZE, WHEEL_SIZE));
    
    // button for playing again, initially disabled
    playAgainButton = new JButton();
    playAgainButton.setFont(new Font("Serif", Font.PLAIN, 20));
    playAgainButton.setText("Click here to play again");
    playAgainButton.addActionListener(new PlayAgainHandler());
    playAgainButton.setEnabled(false);
    
    // panel for 26 buttons for the letters
    JPanel letters = new JPanel(new GridLayout(2, 13));
    letters.setPreferredSize(new Dimension(PANEL_WIDTH, LETTERS_HEIGHT));
    buttons = new JButton[26];
    ActionListener listener = new ButtonHandler();
    for (int i = 0; i < 26; ++i)
    {
      buttons[i] = new JButton(Character.toString((char)(START_CHAR + i)));
      buttons[i].setFont(new Font("Serif", Font.PLAIN, 20));
      buttons[i].addActionListener(listener);
      letters.add(buttons[i]);
    }
    
    // two panels with sets of controls for the players
    playerPanel = new JPanel[2];
    playerNameLabel = new JLabel[2];
    playerBalanceLabel = new JLabel[2];
    playerBuyVowelButton = new JButton[2];
    playerSolveButton = new JButton[2];
    playerSpinButton = new JButton[2];
    
    for (int i = 0; i < 2; ++i)
    {
      playerNameLabel[i] = new JLabel("Player " + i);
      playerNameLabel[i].setFont(new Font("Serif", Font.PLAIN, 32));
      playerBalanceLabel[i] = new JLabel("$0");
      playerBalanceLabel[i].setFont(new Font("Serif", Font.PLAIN, 40));
      playerBuyVowelButton[i] = new JButton("Buy a Vowel");
      playerSolveButton[i] = new JButton("Solve");
      playerSpinButton[i] = new JButton("Spin");

      playerPanel[i] = new JPanel();
      playerPanel[i].setLayout(new BoxLayout(playerPanel[i], BoxLayout.PAGE_AXIS));
      playerNameLabel[i].setAlignmentX(Component.CENTER_ALIGNMENT);
      playerPanel[i].add(playerNameLabel[i]);
      playerBalanceLabel[i].setAlignmentX(Component.CENTER_ALIGNMENT);
      playerPanel[i].add(playerBalanceLabel[i]);
      
      // put the three buttons in a horizontal row
      JPanel bottom = new JPanel();
      bottom.setBackground(null);  // inherit parent color
      bottom.setLayout(new GridLayout(1, 3));
      bottom.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
      bottom.setPreferredSize(new Dimension(new Dimension(PANEL_WIDTH / 3, 50)));
      bottom.add(playerBuyVowelButton[i]);
      bottom.add(playerSolveButton[i]);
      bottom.add(playerSpinButton[i]);
      JPanel temp = new JPanel();
      temp.add(bottom);
      temp.setBackground(null);  // inherit parent color
      playerPanel[i].add(temp);
      playerBuyVowelButton[i].addActionListener(new BuyVowelHandler());
      playerSolveButton[i].addActionListener(new SolveHandler());
      playerSpinButton[i].addActionListener(new SpinHandler());
    }
    
    // panel for the two players with the wheel in the middle
    JPanel lowerPanel = new JPanel();
    lowerPanel.setLayout(new GridLayout(1, 3));
    lowerPanel.setPreferredSize(new Dimension(PANEL_WIDTH, PLAYER_HEIGHT));
    lowerPanel.add(playerPanel[0]);
    
    // put the wheel canvas in its own panel, so it can keep the correct size
    JPanel dummy = new JPanel();
    dummy.add(wheelCanvas);
    lowerPanel.add(dummy);
    lowerPanel.add(playerPanel[1]);
    
    // lay out the pieces vertically
    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    
    // put the board canvas in its own panel
    dummy = new JPanel();
    dummy.setPreferredSize(new Dimension(PANEL_WIDTH, BOARD_HEIGHT));
    dummy.add(boardCanvas);
    
    this.add(dummy);
    playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.add(playAgainButton);
    this.add(lowerPanel);
    this.add(letters);
    
    // initial state
    resetAll();
  } 
  
  /**
   * Callback for the wheel to notify the UI when the spinning animation
   * is finished.
   */
  public void wheelReady()
  {
    resetAll();
    repaint();
  }
  
  /**
   * Disables all buttons.
   */
  private void disableAll()
  {
    for (int i = 0; i < 2; ++i)
    {
      playerBuyVowelButton[i].setEnabled(false);
      playerSolveButton[i].setEnabled(false);
      playerSpinButton[i].setEnabled(false);
    }
    disableLetters();
  }
  
  /**
   * Updates all button states and labels.
   */
  private void resetAll()
  {
    disableAll();
    if (wheelCanvas.isAnimating())
    {
      // keep everything disabled and don't update while wheel is spinning
      return;
    }

    int player = game.whoseTurn();
    playerPanel[player].setBackground(Color.RED);
    playerPanel[1 - player].setBackground(Color.LIGHT_GRAY);      

    playerBalanceLabel[0].setText("$" + game.getBalance(0));
    playerBalanceLabel[1].setText("$" + game.getBalance(1));
    playerNameLabel[0].setText("Player 0 ($" + playerTotal[0] + ")");
    playerNameLabel[1].setText("Player 1 ($" + playerTotal[1] + ")");
    
    if (game.roundOver())
    {
      playAgainButton.setEnabled(true);
    }
    else
    {
      enableLetters(false);
      
      if (game.needsSpin())
      {        
        playerSpinButton[player].setEnabled(true);
      }
      else
      {      
        playerSolveButton[player].setEnabled(true);
        if (game.getBalance(player) >= WheelOfFortuneGame.VOWEL_COST &&
            vowelsLeftToGuess())
        {
          playerBuyVowelButton[player].setEnabled(true);
        }
      }
    }
  }
  
  /**
   * Disables all the letter buttons.
   */
  private void disableLetters()
  {
    for (int i = 0; i < 26; ++i)
    {
      buttons[i].setEnabled(false);
    }
  }
  
  /**
   * Enable the letter keys for letters that have not yet
   * been guessed, depending on whether the player is buying
   * a vowel or not.
   * @param buyingAVowel
   *   true if the player is buying a vowel, false otherwise
   */
  private void enableLetters(boolean buyingAVowel)
  {
    for (int i = 0; i < 26; ++i)
    {
      if (guesses[i])
      {
        buttons[i].setEnabled(false);
      }
      else
      {
        char ch = (char) (i + START_CHAR);
        if (buyingAVowel)
        {
          buttons[i].setEnabled(isVowel(ch));
        }
        else
        {
          buttons[i].setEnabled(!isVowel(ch));
        }
      }
    }
  }
  
  /**
   * Determines whether the given character is a vowel.
   * @param ch
   *   the character to check
   * @return
   *   true if the character is a vowel, false otherwise
   */
  private boolean isVowel(char ch)
  {
    return "aeiouAEIOU".indexOf(ch) >= 0;
  }

  /**
   * Determines whether there are any vowels left to guess.
   * @return
   *   true if there are any vowels that haven't been guessed
   */
  private boolean vowelsLeftToGuess()
  {
    final char start = 'a';
    return !(guesses['a' - start] && 
    guesses['e' - start] && 
    guesses['i' - start] && 
    guesses['o' - start] && 
    guesses['u' - start]);
  }

  /**
   * Handler for the letter buttons. 
   */
  private class ButtonHandler implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      // do nothing if game is over or if a spin is needed
      if (!game.roundOver() && !game.needsSpin())
      {
        char ch = e.getActionCommand().charAt(0);
        int index = ch - START_CHAR;
        buttons[index].setEnabled(false);
        
        int count;
        if (isVowel(ch))
        {
          count = game.buyVowel(ch);
        }
        else
        {
          count = game.guessConsonant(ch);
        }
        guesses[ch - START_CHAR] = true;
       
        resetAll();
        repaint();
      }
    }   
  }

  /**
   * Handler for the "buy vowel" button.  This just enables
   * the vowels on the letter panel.
   */
  private class BuyVowelHandler implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
      enableLetters(true);
      repaint();
    }    
  }

  /**
   * Handler for the "spin" button.
   */
  private class SpinHandler implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
      game.spinWheel();
      wheelCanvas.animateSpin();
      resetAll();
      repaint();
    }    
  }

  /**
   * Handler for the "solve" button.
   */
  private class SolveHandler implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
      String guess = (String) JOptionPane.showInputDialog(WheelOfFortuneUI.this, "Enter your guess");
      boolean correct = game.guessPhrase(guess);
      if (correct)
      {
        playerTotal[0] += game.getBalance(0);
        playerTotal[1] += game.getBalance(1);
      }
      resetAll();
      repaint();
    }    
  }
  
  /**
   * Handler for the "play again" button.
   */
  private class PlayAgainHandler implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      try
      {
        String phrase = generator.getRandomPhrase();
        game.startRound(1 - game.whoseTurn(), new SecretPhrase(phrase));
        resetAll();
        for (int i = 0; i < 26; ++i)
        {
          guesses[i] = false;   
        }
        playAgainButton.setEnabled(false);
        repaint();
      }
      catch (FileNotFoundException fnfe)
      {
        // This should really never happen...
        JOptionPane.showMessageDialog(WheelOfFortuneUI.this.getParent(), "Unable to open file: " + fnfe.getMessage());
      }
    }   
  }

}


