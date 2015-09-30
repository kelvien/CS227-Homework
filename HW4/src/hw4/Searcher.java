package hw4;

import java.util.ArrayList;

/**
 * Utility class for searching a maze described by a collection of MazeCell
 * objects.
 */
/**
 * @author Kelvien Hidayat
 */
public class Searcher {
	/**
	 * Recursively searches a given MazeCell and all of its unexplored
	 * neighbors. Returns true if the current cell is the goal or if the goal is
	 * found in a search initiated from this cell by searching a neighbor.
	 * Returns false if the goal is not found. If the goal is found, then when
	 * this method returns, the given ArrayList will contain a path from the
	 * current cell to the goal.
	 * 
	 * @param current
	 *            the current cell at which a search is being initiated
	 * @param solution
	 *            an empty array list
	 * @return true if the goal was found through this search, false otherwise
	 */
	public static boolean check = false; // variable to check whether the goal
											// has been found or not.

	public static boolean search(MazeCell current, ArrayList<MazeCell> solution) {
		if (current.getStatus() == Status.GOAL) { // The base; Setting a
													// variable 'check' to
													// become true so that
													// recursive method would
													// print a green cell from
													// that onwards.
			check = true; // setting a global variable to become true,
						// indicating that the goal cell has been found.
			solution.add(0, current); // adding the goal cell into the arraylist
										// of solution.
			return true;
		} else {
			if (!check) { // if the goal has not yet been found.
				for (MazeCell neighbours : current.getNeighors()) { // For each
																	// cell
																	// that has
					current.setStatus(Status.EXPLORING); // Printing the
														// cell to
														// become
														// explored
														// (Orange).
														// neighbours.
					if (neighbours.getStatus() == Status.UNEXPLORED
							|| neighbours.getStatus() == Status.GOAL) { // Only
																		// for
																		// cell
																		// that
																		// has
																		// not
																		// been
																		// explored
																		// or a
																		// goal
																		// cell,
																		// it
																		// will
																		// do a
																		// recursive
																		// method.
						search(neighbours, solution); // calling search again to
						// each cells that is
						// unexplored.
					}
				}
				if (check) { // These are cells that has been explored and the
								// check status is true.
					current.setStatus(Status.SUCCEEDED); // start printing green
															// cells.
					solution.add(0, current); // inputting the current solution
												// cell into an arraylist of
												// solution path.
					return true;
				} else { // cells that has been explored and goal has not yet
							// been found.
					current.setStatus(Status.FAILED); // printing them to become
														// red or failed.
					return false;
				}
			}
			return true;
		}
	}
}
