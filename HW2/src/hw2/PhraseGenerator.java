package hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * Generator for secret phrases for guessing games.
 * A PhraseGenerator chooses a phrase randomly from a file specified in the constructor.
 * Each line of the file represents a distinct phrase.
 * 
 * @author Kelvien Hidayat
 *
 */
public class PhraseGenerator {
/**
 * The phrase which is going to be in the game
 * 
 */
private String phrase;
/**
 * Number of lines in the file.
 */
private int line;
/**
 * random instance is to pick any random phrases inside a file.
 */
private Random random = new Random();
/**
 * scan instance is to scan strings inside a file.
 */
private Scanner scan;
/**
 * file instance is to initialize the file we are using.
 */
private File file;
/**
 * Constructs a PhraseGenerator that will select phrases from the given file.
 * @param givenFileName
 * the name of the file used.
 * @throws
 * if file can not be found.
 */
public PhraseGenerator(String givenFileName) throws FileNotFoundException{
	file = new File(givenFileName);
}
/**
 * Returns a phrase selected at random from this PhraseGenerator file.
 * @return
 * a phrase occurring in the file
 * @throws FileNotFoundException 
 * if the file cannot be opened
 */
public String getRandomPhrase() throws FileNotFoundException{
scan = new Scanner(file);
while(scan.hasNextLine()){
	line++;
	scan.nextLine();
}
scan.close();
scan = new Scanner(file);
int index = random.nextInt(line);
for(int a = 0;a < index+1; a++){
	phrase = scan.nextLine();
}
scan.close();
line=0;
return phrase;	
}


}
