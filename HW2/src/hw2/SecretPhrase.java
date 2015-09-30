package hw2;
/**
 * Class representing a secret phrase that may be partially revealed as letters are identified.
 * Initially, the displayed form shows a special character PLACEHOLDER in place of each character(spaces and punctuation, if any, are shown normally.)
 * Whenever the update() method is called with a letter that actually occurs in the secret phrase, the displayed form is updated to show that character in place of the placeholder character.
 * Internally, all characters are converted to upper-case.
 * 
 * @author Kelvien Hidayat
 *
 */
public class SecretPhrase {
/**
 * Placeholder '*' is to conceal the phrase for the game.
 */
public static final char PLACEHOLDER='*';
/**
 * Phrase that has been converted with the placeholders.
 */
private String HiddenPhrase="";
/**
 * Characters of the Hidden phrase stored as an array.
 */
private char[] HiddenChar;
/**
 * Phrase that is needed to be guessed.
 */
private String phrase;
/**
 * characters of phrase that is needed to be guessed stored in array.
 */
private char[] chars;
/**
 * status of each characters whether it's still in forms of placeholders or revealed.
 */
private boolean[] hidden;
/**
 * set of letters to be checked its availability
 */
private String letters="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
/**
 * set of consonant letters to be checked its availability in guessConsonants().
 */
private String consonants="BCDFGHJKLMNPQRSTVWXYZ";
/**
 * Number of letters left .
 */
private int numOfLetters=0;
/**
 * Number of consonants left.
 */
private int numOfConsonants=0;
/**
 * Number of a chosen letter revealed
 */
private int letterCount=0;
/**
 * checked letters to be converted into upper-case.
 */
private String checkedLetter;
/**
 * Constructs a HiddenPhrase using the given string as the secret phrase.
 * @param phrase
 * the given secret phrase
 */
public SecretPhrase(String phrase){
this.phrase = phrase.toUpperCase();
chars = new char[this.phrase.length()];
hidden = new boolean[this.phrase.length()];
	for(int a = 0;a<this.phrase.length();a++){
		for(int b = 0;b<letters.length();b++){
			if(this.phrase.charAt(a)==letters.charAt(b)){
				HiddenPhrase+="*";
				chars[a]=this.phrase.charAt(a);
				hidden[a]=true;
				numOfLetters++;
				break;
			}
			else if(b==letters.length()-1&&this.phrase.charAt(a)!=letters.charAt(b)){
				HiddenPhrase+=phrase.charAt(a);
				chars[a]=this.phrase.charAt(a);
			}
		}
	}
	HiddenChar = HiddenPhrase.toCharArray();	
}
/**
 * Returns the number of character positions that are not currently being displayed.
 * @return numOfLetters
 * number of characters positions not yet identified
 */
public int countRemaining(){
	for(int a = 0;a<HiddenPhrase.length();a++){
		for(int b = 0;b<letters.length();b++){
			if(HiddenPhrase.charAt(a)==letters.charAt(b)){
				numOfLetters++;
				break;
			}
		}
	}
	return numOfLetters;
}
/**
 * Returns the number of consonants that are not currently being displayed.
 * @return numOfConsonants
 * number of consonants not yet identified
 */
public int countRemainingConsonants(){
	for(int a = 0;a<HiddenPhrase.length();a++){
		for(int b = 0;b<consonants.length();b++){
			if(chars[a]==consonants.charAt(b)&&HiddenPhrase.charAt(a)=='*'){
				numOfConsonants++;
				break;
			}
		}
	}
	return numOfConsonants;
}
/**
 * Returns the displayed form of the secret phrase, in which only the letters that have been identified by the update() method are shown.
 * @return HiddenPhrase
 * displayed form of the secret phrase
 */
public String getDisplayedPhrase(){
	return HiddenPhrase;
}
/**
 * Returns the actual secret phrase stored in this object.
 * @return HiddenPhrase
 * the secret phrase
 */
public String getSecretPhrase(){
	return phrase;
}
/**
 * Returns the number of times the given character occurs in the secret phrase. Does not modify the displayed form of the phrase.
 * @param ch
 * character to be checked its occurance.
 * @return letterCount
 * number of occurrences of the given character in the secret phrase
 */
public int letterCount(char ch){
letterCount = 0;
checkedLetter="";
checkedLetter+=ch;
	for(int a = 0;a<phrase.length();a++){
		if(phrase.charAt(a)==checkedLetter.toUpperCase().charAt(0)){
			letterCount++;
		}
	}	
	return letterCount;
}
/**
 * Updates the displayed phrase to reveal all occurrences of the given character in the secret phrase, if any. Has no effect if the character does not occur in the secret phrase.
 * @param ch
 * character to be checked and updated
 */
public void update(char ch){
HiddenPhrase="";
	for(int a = 0;a<phrase.length();a++){
			if(phrase.charAt(a)==ch){
				hidden[a]=false;
			}
			else{
				hidden[a]=true;
			}
	}
	for(int b =0;b<phrase.length();b++){
		if(hidden[b]==false){
			HiddenChar[b]=chars[b];
			HiddenPhrase+=HiddenChar[b];
		}
		else if(hidden[b]==true){
			HiddenPhrase+=HiddenChar[b];
		}
	}

}
/**
 * Updates the displayed phrase to reveal all hidden letters.
 */
public void updateAllRemaining(){
HiddenPhrase = this.phrase;
}

}

