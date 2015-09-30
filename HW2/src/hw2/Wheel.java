package hw2;

import java.util.Random;

/**
 * This Wheel Class models a wheel for a guessing game such as Wheel of Fortune.
 * Each spin of the wheel has 16 possible outcomes, as follows:
 * 15:Bankrupt
 * 14:Lose A Turn
 * 13:$5000
 * 0-12: Add 6 and multiply by $50 to get a value from $300 to $900
 * 
 * @author Kelvien Hidayat
 *
 */
public class Wheel {
/**
 * BANKRUPT is a final value to indicate whether current player gets a BANKRUPT state or not.
 */
public static final int BANKRUPT=-1;
/**
 * LOSE_A_TURN is a final value to indicate whether current player gets a LOSE_A_TURN state or not.
 */
public static final int LOSE_A_TURN=0;
/**
 * Declaring a new Random class
 */
private Random random;
/**
 * Declaring states from 0-15 states.
 */
private int[] state = new int[16];
/**
 * Declaring the state which is currently received by spinning the wheel randomly.
 * Initialize as 15 so that every players are initially BANKRUPT
 */
private int now=15;
/**
 * The number of states
 */
private int index;
/**
 * Constructs a wheel using a default instance of Random. Initially the wheel's state is BANKRUPT.
 */
public Wheel(){
	random = new Random();
	for(int a =0;a<13;a++){
	state[a]=(a+6)*50;
	}
	state[13]=5000;
	state[14]=0;
	state[15]=-1;
}
/**
 * Constructs a Wheel whose random number generator is initialized with the given value as a seed. Initially the wheel's state is BANKRUPT.
 * @param seed
 * seed which is determined by user.
 */
public Wheel(int seed){
	random = new Random(seed);
}
/**
 * A method to get the current state of the wheel.
 * @return
 * Returns the dollar amount for the current wheel position, using the values 0 for "Lose a Turn" and -1 for "Bankrupt".
 */
public int getState(){
	if(state[now]==BANKRUPT){
		return -1;
	}
	else if(state[now]==LOSE_A_TURN){
		return 0;
	}
	else{
		return state[now];
	}
}
/**
 * Spins the wheel and updates the wheel position.
 */
public void spin(){
	index = random.nextInt(16);
	state[now]=state[index];
}

}
