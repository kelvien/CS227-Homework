package hw2;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Text-based user interface for the WheelOfFortuneGame.
 */
public class WheelOfFortuneTextUI
{
  /**
   * Scanner to read from the console.
   */
  private Scanner scanner = new Scanner(System.in);
  
  /**
   * The game.
   */
  private WheelOfFortuneGame game;
  
  /**
   * Overall winnings of player 0, in case there are multiple
   * rounds.
   */
  int player0Total = 0;
  
  /**
   * Overall winnings of player 1, in case there are multiple
   * rounds.
   */  
  int player1Total = 0;

  /**
   * Entry point.
   */
  public static void main(String[] args) throws FileNotFoundException
  {
    new WheelOfFortuneTextUI().go("phrases.txt");
  }
  
  /**
   * Starts the UI.
   * @throws FileNotFoundException
   */
  public void go(String filename) throws FileNotFoundException
  {   
    PhraseGenerator generator = new PhraseGenerator(filename);
    Wheel wheel = new Wheel();
    game = new WheelOfFortuneGame(wheel);  
    int playerToStart = 0;
    boolean over = false;
    
    System.out.println("CS 227 Wheel of Fortune");
    System.out.println("-----------------------");
    printFinalStatus();
    System.out.println();
    
    while (!over)
    {
      String phrase = generator.getRandomPhrase();
      game.startRound(playerToStart, new SecretPhrase(phrase));
      while (!game.roundOver())
      {
        printCurrentStatus();
        if (game.needsSpin())
        {
          spin();
        }
        if (game.getWheelState() != Wheel.BANKRUPT && 
            game.getWheelState() != Wheel.LOSE_A_TURN)
        {
          menu();
          System.out.print("Enter your choice: ");
          String entry = scanner.nextLine().toLowerCase();
          if (entry.startsWith("c"))
          {
            doGuessLetter(false);
          }
          else if (entry.startsWith("b"))
          {
            doGuessLetter(true);
          }
          else
          {
            doGuessPhrase();
          }
        }
      }
      System.out.println("That ends this round: ");
      
      // add to overall totals
      player0Total += game.getBalance(0);
      player1Total += game.getBalance(1);
      printFinalStatus();
      
      System.out.println();
      System.out.print("Play again (y/n)? ");
      String response = scanner.nextLine().toLowerCase();
      over = response.startsWith("n");
      
      // let other player start
      playerToStart = 1 - game.whoseTurn();
    }
  }
  
  /**
   * Displays the current balances for the round, 
   * the current displayed phrase, and whose turn it is.
   */
  private void printCurrentStatus()
  {
    System.out.println("Player 0: " + game.getBalance(0));
    System.out.println("Player 1: " + game.getBalance(1));
    System.out.println();
    System.out.println(game.getDisplay());
    System.out.println();
    System.out.println("It is Player " + game.whoseTurn() + "'s turn.");
  }

  /**
   * Displays the overall totals.
   */
  private void printFinalStatus()
  {
    System.out.println("Player 0 total winnings: " + player0Total);
    System.out.println("Player 1 total winnings: " + player1Total);
  }

  /**
   * Displays the short menu of choices for a player's turn.
   */
  private void menu()
  {
    System.out.println("  a) solve the puzzle");
    if (game.getBalance(game.whoseTurn()) >= WheelOfFortuneGame.VOWEL_COST)
    {
      System.out.println("  b) buy a vowel");  // only display this if player has enough money 
    }
    System.out.println("  c) guess a consonant");
  }
  
  /**
   * Spins the wheel and displays the result.
   */
  private void spin()
  {
    System.out.println("Press ENTER to spin the wheel");
    scanner.nextLine();
    game.spinWheel();
    int result = game.getWheelState();
    String outcome;
    if (result == Wheel.BANKRUPT)
    {
      outcome = "BANKRUPT!";
    }
    else if (result == Wheel.LOSE_A_TURN)
    {
      outcome = "Lose a Turn";
    }
    else
    {
      outcome = "$" + result;
    }
    System.out.println("You spun: " + outcome);
  }
  
  /**
   * Handles the user's choice to guess a consonant or buy
   * a vowel.
   * @param buyingAVowel
   *   true if the user is buying a vowel
   */
  private void doGuessLetter(boolean buyingAVowel)
  {
    char guess;
    int result;
    if (buyingAVowel)
    {
      System.out.print("Enter a vowel: ");
      guess = scanner.nextLine().toUpperCase().charAt(0);
      result = game.buyVowel(guess);
    }
    else
    {
      System.out.print("Enter a consonant: ");
      guess = scanner.nextLine().toUpperCase().charAt(0);
      result = game.guessConsonant(guess);
    }
    if (result == 1)
    {
      System.out.println("There was one " + guess);
    }
    else if (result > 1)
    {
      System.out.println("There were " + result + " " + guess + "'s");        
    }
    else
    {
      System.out.println("Sorry, there were no " + guess + "'s");
    }
    System.out.println();
  }
  
  /**
   * Handles the user's choice to solve the puzzle.
   */
  private void doGuessPhrase()
  {
    System.out.print("Enter your guess: ");
    String guess = scanner.nextLine();
    boolean correct = game.guessPhrase(guess);
    if (correct)
    {
      System.out.println("That's it!");
    }
    else
    {
      System.out.println("Sorry, that wasn't it.");
    }
    System.out.println();
  }
}