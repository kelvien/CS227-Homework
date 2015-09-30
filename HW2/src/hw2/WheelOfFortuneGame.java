package hw2;
/**
 * This class encapsulates all the logic for a two-player game of "Wheel of Fortune".
 * Use the startRound() method to start a new round of the game using a new secret phrase.
 * An instance of this class can be used for multiple rounds by calling start() to initiate a new round.
 * 
 * @author Kelvien Hidayat
 *
 */
public class WheelOfFortuneGame {
/**
 * Cost to buy a vowel.
 */
public static final int VOWEL_COST=250;	
/**
 * Current turn of the player to play.
 */
private int currentPlayer;
/**
 * Status of player 0 to play or not.
 */
private boolean player0=false;
/**
 * Status of player 1 to play or not.
 */
private boolean player1=false;
/**
 * Balance money of player 0.
 */
private int player0Money=0;
/**
 * Balance money of player 1.
 */
private int player1Money=0;
/**
 * Characters to be turned into string, turned into upper-case and compared.
 */
private String compareChar;
/**
 * Status of the game.
 */
private boolean gameOn=false;
/**
 * Status of the round.
 */
private boolean roundOn=false;
/**
 * word to be guessed and converted into upper-case.
 */
private String guessWord;
/**
 * set of vowels characters.
 */
private String vowel = "AIUEO";
/**
 * Check whether player need to spin the wheel or not.
 */
private boolean needsSpin=false;
/**
 * an instance of wheel to be used in the game.
 */
private Wheel gameWheel = new Wheel();
/**
 * an instance of secretPhrase to be used in the game.
 */
private SecretPhrase secretPhrase;
/**
 * Constructs a new game instance that will use the given Wheel. Note that no operations are valid until startRound() has been called.
 * @param wheel
 * the wheel that will be used in this game
 */
public WheelOfFortuneGame(Wheel wheel){
	gameWheel = wheel;
	gameOn=false;
	roundOn=false;
	needsSpin=true;
}
/**
 * Checks whether the given vowel occurs in the secret phrase, updates the secret phrase accordingly, and reduces the current player's balance by VOWEL_COST. If the vowel does not occur in the secret phrase, it becomes the other player's turn.
 * @param ch
 * a vowel
 * @return
 * the number of times the given vowel appears in the secret phrase
 */
public int buyVowel(char ch){
compareChar="";
compareChar+=ch;
	if(gameOn==true&&roundOn==true){
		if(currentPlayer==0&&player0==true){
			player0Money-=VOWEL_COST;
			for(int a = 0;a<vowel.length();a++){
				if(vowel.charAt(a)==compareChar.toUpperCase().charAt(0)){
					if(secretPhrase.letterCount(compareChar.toUpperCase().charAt(0))>0){
						secretPhrase.update(compareChar.charAt(0));
						needsSpin=true;
						return secretPhrase.letterCount(compareChar.toUpperCase().charAt(0));
					}
					else if(secretPhrase.letterCount(compareChar.toUpperCase().charAt(0))==0){
						currentPlayer=1;
						player1=true;
						player0=false;
						needsSpin=true;
						return secretPhrase.letterCount(compareChar.toUpperCase().charAt(0));
					}
				}
			}
		}
		else if(currentPlayer==1&&player1==true){
			player0Money-=VOWEL_COST;
			for(int a = 0;a<vowel.length();a++){
				if(vowel.charAt(a)==compareChar.toUpperCase().charAt(0)){
					if(secretPhrase.letterCount(compareChar.toUpperCase().charAt(0))>0){
						secretPhrase.update(compareChar.charAt(0));
						needsSpin=true;
						return secretPhrase.letterCount(compareChar.toUpperCase().charAt(0));
					}
					else if(secretPhrase.letterCount(compareChar.toUpperCase().charAt(0))==0){
						currentPlayer=0;
						player0=true;
						player1=false;
						needsSpin=true;
						return secretPhrase.letterCount(compareChar.toUpperCase().charAt(0));
					}
				}
			}
		}
		
	}
	return -1;
}
/**
 * Returns the solved form of the secret phrase with all hidden letters revealed.
 * @return secretPhrase.getSecretPhrase()
 * solved form of the secret phrase
 */
public String getAnswer(){
	if(gameOn==true&&roundOn==true){
		return secretPhrase.getSecretPhrase();
	}
	return null;
}
/**
 * Returns the potential winnings in this round for the given player.
 * @param player
 * index (0 or 1) of the player
 * @return 
 * balance for the indicated player
 */
public int getBalance(int player){
	if(gameOn==true||roundOn==true){
		if(player==0){
			return player0Money;
		}
		else if(player==1){
			return player1Money;
		}
	}
	return -1;
}
/**
 * Returns the displayed form of the secret phrase.
 * @return secretPhrase.getDisplayedPhrase()
 * displayed form of the secret phrase
 */
public String getDisplay(){
	if(gameOn==true||roundOn==true){
		return secretPhrase.getDisplayedPhrase();
	}
	return null;
}
/**
 * Returns the current state of the wheel for this game.
 * @return gameWheel.getState()
 * the current state of the wheel for this game
 */
public int getWheelState(){
	if(gameOn==true&&roundOn==true){
		return gameWheel.getState();
	}
	return -1;
}
/**
 * Checks whether the given consonant occurs in the secret phrase, updates the secret phrase accordingly, and adjusts the current player's balance by the number of occurrences times the wheel state. If the consonant does not occur in the secret phrase, it becomes the other player's turn.
 * @param ch
 * a consonant
 * @return
 * the number of times the given consonant appears in the secret phrase
 */
public int guessConsonant(char ch){
compareChar="";
compareChar+=ch;
	if(gameOn==true&&roundOn==true){
		if(currentPlayer==0&&player0==true){
			if(secretPhrase.letterCount(ch)>0){
				secretPhrase.update(compareChar.toUpperCase().charAt(0));
				player0Money+=(secretPhrase.letterCount(compareChar.toUpperCase().charAt(0))*gameWheel.getState());
				needsSpin=true;
				return secretPhrase.letterCount(compareChar.toUpperCase().charAt(0));
			}
			else if(secretPhrase.letterCount(compareChar.toUpperCase().charAt(0))==0){
				player0=false;
				player1=true;
				currentPlayer=1;
				needsSpin=true;
				return secretPhrase.letterCount(compareChar.toUpperCase().charAt(0));
			}
		}
		else if(currentPlayer==1&&player1==true){
			if(secretPhrase.letterCount(compareChar.toUpperCase().charAt(0))>0){
				secretPhrase.update(compareChar.toUpperCase().charAt(0));
				player1Money+=(secretPhrase.letterCount(ch)*gameWheel.getState());
				needsSpin=true;
				return secretPhrase.letterCount(compareChar.toUpperCase().charAt(0));
			}
			else if(secretPhrase.letterCount(compareChar.toUpperCase().charAt(0))==0){
				player1=false;
				player0=true;
				currentPlayer=0;
				needsSpin=true;
				return secretPhrase.letterCount(compareChar.toUpperCase().charAt(0));
			}
		}
	}
	return -1;
}
/**
 * Checks whether the given string matches the secret phrase without regard to case. If the guess matches, the following takes place:
 * - the current player's balance is increased by the number of hidden consonants times the wheel state
 * - the secret phrase is updated to reveal all characters
 * - the losing player's balance is reduced to zero and the winning player's balance is retained until the next call to startRound().
 * - the round ends
 * Otherwise, it becomes the other player's turn.
 * @param guess
 * the player's guess
 * @return
 * true if the guess matches the secret phrase
 */
public boolean guessPhrase(String guess){
guessWord="";
guessWord = guess.toUpperCase();
	if(gameOn==true&&roundOn==true){
		if(currentPlayer==0&&player0==true){
			if(guessWord.equals(secretPhrase.getSecretPhrase())){
				roundOn=false;
				player1Money=0;
				player0Money+=secretPhrase.countRemainingConsonants()*gameWheel.getState();
				secretPhrase.updateAllRemaining();
				needsSpin=true;
				return true;
			}
			
			else{
				currentPlayer=1;
				player0=false;
				player1=true;
				needsSpin=true;
				return false;
			}
		}
		else if(currentPlayer==1&&player1==true){
			if(guessWord.equals(secretPhrase.getSecretPhrase())){
				roundOn=false;
				player0Money=0;
				player1Money+=secretPhrase.countRemainingConsonants()*gameWheel.getState();
				secretPhrase.updateAllRemaining();
				needsSpin=true;
				return true;
			}
			else{
				currentPlayer=0;
				player0=true;
				player1=false;
				needsSpin=true;
				return false;
			}
		}
	}
	return false;
		
}
/**
 * Determines whether the current player needs to spin the wheel.
 * @return needsSpin
 * true if the current player needs to spin the wheel, false otherwise
 */
public boolean needsSpin(){
	if(gameOn==true||roundOn==true){
		return needsSpin;
	}
	return false;
}
/**
 * Determines whether the current round is over.
 * @return roundOn
 * true if the round is over, false otherwise
 */
public boolean roundOver(){
	if(roundOn==true){
		return false;
	}
	else if(roundOn==false){
		return true;
	}
	return false;
}
/**
 * Spins the wheel and updates the game state accordingly; that is, BANKRUPT or LOSE_A_TURN cause it to become the other player's turn, and additionally BANKRUPT reduces the current player's balance to zero. The outcome of the spin is not returned, but is available by calling getWheelState(). 
 */
public void spinWheel(){
	if(gameOn==true&&roundOn==true){
		if(currentPlayer==0&&player0==true){
			gameWheel.spin();
			if(gameWheel.getState()==-1){
				player0Money=0;
				currentPlayer=1;
				player0=false;
				player1=true;
				needsSpin=true;
			}
			else if(gameWheel.getState()==0){
				currentPlayer=1;
				player0=false;
				player1=true;
				needsSpin=true;
			}
			else{
				needsSpin=false;
			}
		}
		else if(currentPlayer==1&&player1==true){
			gameWheel.spin();
			if(gameWheel.getState()==-1){
				currentPlayer=0;
				player1Money=0;
				player1=false;
				player0=true;
				needsSpin=true;
			}
			else if(gameWheel.getState()==0){
				currentPlayer=0;
				player1=false;	
				player0=true;
				needsSpin=true;
			}
			else{
				needsSpin=false;
			}
		}
	}
}
/**
 * Start a new round of the game using the given SecretPhrase.
 * @param whoseTurn
 * 0 to start with player 0, 1 to start with player 1
 * @param secretPhrase
 * the SecretPhrase to use for this round
 */
public void startRound(int whoseTurn, SecretPhrase secretPhrase){
gameOn=true;
roundOn=true;
currentPlayer = whoseTurn;
needsSpin=true;
player0Money=0;
player1Money=0;
	if(currentPlayer==0){
		player0=true;
		player1=false;
	}
	else if(currentPlayer==1){
		player1=true;
		player0=false;
	}
	this.secretPhrase = secretPhrase;
}
/**
 * Returns the index (0 or 1) of the player whose turn it is.
 * @return currentPlayer
 * index of the current player
 */
public int whoseTurn(){
	if(gameOn==true||roundOn==true){
		if(currentPlayer==0){
			return currentPlayer;
		}
		else if(currentPlayer==1){
			return currentPlayer;
		}
	}
	return -1;
}

}
