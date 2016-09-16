import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.naming.directory.InvalidAttributesException;

/**
 * @class_name MemoryGame.java
 * @author Charles
 * @version 1.0
 * @date Sep 15, 2016
 * @description In this classic card game, players take turns flipping over pairs of cards. When a player flips a matching set, they score a point. The pair is removed from the pile and the game continues.
 */

public class MemoryGame {

	public static final int AMOUNT_OF_ROWS = 4; // Constant amount of rows of cards
	public static final int AMOUNT_OF_COLS = 4; // Constant amount of columns of cards

	public static File SCORES_FILE = new File("scores.txt");

	/**
	 * The Main Method
	 * @exception Something went wrong!
	 */
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in); // Declare the scanner

		repeatGame: while (true) { // Loop the entire program as long as they want to play again.

			int amountOfCards = AMOUNT_OF_ROWS * AMOUNT_OF_COLS; // This integer shows the amount of cards that will be in the deck.
			if (amountOfCards % 2 != 0) { // Check if the cards cannot be paired properly.
				throw new InvalidAttributesException("Amount of cards must be divisible by 2."); // Throws an error.
			}

			List<Point> cardPositions = new ArrayList<Point>(); // This list will contain all the possible positions that a card can be in (in the array).
			int[][] deck = new int[AMOUNT_OF_ROWS][AMOUNT_OF_COLS]; // This 2d-array will represent the deck of cards.

			for (int x = 0; x < deck.length; x++) { // Loop through every row of cards.
				for (int y = 0; y < deck[x].length; y++) { // Loop through the columns of the row of cards.
					cardPositions.add(new Point(x, y)); // Add the Point to the list of possible points ArrayList.
				}
			}

			int amountOfSets = amountOfCards / 2; // This integer represents the amount of sets that will be used in this game.
			for (int setValue = 1; setValue <= amountOfSets; setValue++) { // Loop through the amount of sets from 1 to nth (inclusive).
				for (int pairCount = 0; pairCount < 2; pairCount++) { // Each set will have a pair (2 cards). This loop counts to 2.
					Point randomPosition = cardPositions.get(new Random().nextInt(cardPositions.size())); // Finds a random position in the list of possible points.
					deck[randomPosition.x][randomPosition.y] = setValue; // Set the random position to the card's set value.
					cardPositions.remove(randomPosition); // Removes the random position we found above from the list as we no longer need it.
				}
			}

			int playerAPoints = 0; // This integer represents Player A's points.
			int playerBPoints = 0; // This integer represents Player B's points.
			boolean playerABoolean = true; // This boolean represents whether it is Player A's turn.

			while (!isEmpty(deck)) { // Loops through everybody's turn as long as the deck is not empty.

				printDeck(deck); // Prints the deck.
				String playerName = playerABoolean ? "Player A" : "Player B"; // This string represents the name of the player who currently has their turn.

				System.out.print("[" + playerName + "] Select Your First Card: "); // Asks the player to select their first card.
				String firstSelection = scanner.nextLine(); // Retrieves their input.
				String[] firstSelectionSplit = firstSelection.replaceAll("[^0-9 ]", "").split(" "); // Selects the numbers and spaces from the input and split the input based on spaces.
				int firstSelectionX = Integer.parseInt(firstSelectionSplit[0]); // This integer represents the row selection of the first selection.
				int firstSelectionY = Integer.parseInt(firstSelectionSplit[1]); // This integer represents the column selection of the first selection.

				printDeck(deck, firstSelectionX, firstSelectionY); // Prints the deck with the first selections as a variable exception
				System.out.print("[" + playerName + "] Select Your Second Card: "); // Asks the player to select their second card.
				String secondSelection = scanner.nextLine(); // Retrieves their input.
				String[] secondSelectionSplit = secondSelection.replaceAll("[^0-9 ]", "").split(" "); // Selects the numbers and spaces from the input and split the input based on spaces.
				int secondSelectionX = Integer.parseInt(secondSelectionSplit[0]); // This integer represents the row selection of the second selection.
				int secondSelectionY = Integer.parseInt(secondSelectionSplit[1]); // This integer represents the column selection of the second selection.

				printDeck(deck, firstSelectionX, firstSelectionY, secondSelectionX, secondSelectionY); // Prints the deck with both the first and second selections as variable exceptions
				int firstNumber = deck[firstSelectionX][firstSelectionY]; // This integer represents the value of the first selection.
				int secondNumber = deck[secondSelectionX][secondSelectionY]; // This integer represents the value of the second selection.

				if (firstNumber == secondNumber && firstNumber > 0 && secondNumber > 0) { // Check if the values of the first and second selections match and are valid (n > 0)
					System.out.println("Match! 1 point awarded to " + playerName + "!"); // Let the player know that they match.
					deck[firstSelectionX][firstSelectionY] = 0; // Remove the selected cards from the deck.
					deck[secondSelectionX][secondSelectionY] = 0;

					if (playerABoolean) { // Checks if it was the first player's turn.
						playerAPoints++; // Award the first player a point.
					} else { // It not the first player's turn.
						playerBPoints++; // Award the second player a point.
					}
				} else { // The cards the player chose did not match.
					System.out.println("Mismatch! No points awarded to " + playerName + "."); // Let the players know that the cards that they chose did not match.
				}

				playerABoolean = !playerABoolean; // Toggles the current player's turn to the opposite value of the current one.
			}

			System.out.println(); // Spacer

			if (playerAPoints > playerBPoints) { // Check if Player A had more points than Player B.
				System.out.println("Player A has won ths game ( " + playerAPoints + " to " + playerBPoints + " )."); // Player A wins the game!
			} else if (playerAPoints < playerBPoints) { // Check if Player B had more points than Player A.
				System.out.println("Player B has won ths game ( " + playerBPoints + " to " + playerAPoints + " )."); // Player B wins the game!
			} else { // Player A and Player B both had the same amount of points.
				System.out.println("The game has ended in a draw ( " + playerAPoints + " to " + playerBPoints + " )."); // The game ends in a tie.
			}

			updateScores(playerAPoints); // Update the scores in the file.
			updateScores(playerBPoints);

			System.out.println(); // Spacer

			System.out.print("Would you like to play again? (Y/N): "); // Asks the user if they would like to play again.
			String playAgainInput = scanner.nextLine(); // Retrieves the input.

			if (playAgainInput.toLowerCase().contains("n")) { // Checks if the input, in lowercases, contains a 'n'.
				break repeatGame; // Break the loop that repeats the game.
			}
		}

		scanner.close(); // Closes the scanner
	}

	/**
	 * Check if a deck of cards is empty.
	 * @param deck A 2d integer array with the values of the cards.
	 * @return Boolean value of whether the deck is empty or not.
	 */
	public static boolean isEmpty(int[][] deck) {
		for (int x = 0; x < deck.length; x++) { // Loops through the rows of the deck.
			for (int y = 0; y < deck[x].length; y++) { // Loops through the columns of the row.
				if (deck[x][y] != 0) { // Checks if the position is valid.
					return false; // Returns false; the deck is not empty.
				}
			}
		}

		return true; // Returns true; the deck is empty.
	}

	/**
	 * Fully prints a hidden deck of cards to System.out
	 * @param deck A 2d integer array with the values of the cards.
	 */
	public static void printDeck(int[][] deck) {
		printDeck(deck, -1, -1, -1, -1); // Method overload with -1 as 'hidden values', which is not possible in an array.
	}

	/**
	 * Prints a hidden deck of cards to System.out with the exception of one X and Y point.
	 * @param deck A 2d integer array with the values of the cards.
	 * @param showX X position on the deck array of the card that will be shown.
	 * @param showY Y position on the deck array of the card that will be shown.
	 */
	public static void printDeck(int[][] deck, int showX, int showY) {
		printDeck(deck, showX, showY, -1, -1); // Method overload with -1 as 'hidden values', which is not possible in an array.
	}

	/**
	 * Prints a hidden deck of cards to System.out with the exception of two X and Y points.
	 * @param deck A 2d integer array with the values of the cards.
	 * @param showXA X position on the deck array of Card A that will be shown.
	 * @param showYA Y position on the deck array of Card A that will be shown.
	 * @param showXB X position on the deck array of Card B that will be shown.
	 * @param showYB Y position on the deck array of Card B that will be shown.
	 */
	public static void printDeck(int[][] deck, int showXA, int showYA, int showXB, int showYB) {
		System.out.println(); // Spacer

		for (int x = 0; x < deck.length; x++) { // Loops through the rows of the deck.
			for (int y = 0; y < deck[x].length; y++) { // Loops through the columns of each row in the deck.
				if (deck[x][y] == 0) { // Check if the value at said row and column is empty.
					System.out.print("-"); // Prints an empty value.
				} else if ((x == showXA && y == showYA) || (x == showXB && y == showYB)) { // Check if the row and column is an exception given in the method parameters.
					System.out.print(deck[x][y]); // Prints the real value of the row and column in the deck.
				} else { // Unknown row and column. Do not show the card.
					System.out.print("*"); // Prints a hidden card value.
				}
			}

			System.out.println(); // Ends the line.
		}

		System.out.println(); // Spacer
	}

	/**
	 * Update the scores file with a new score.
	 * @param newScore An integer with the new score.
	 * @throws IOException Something went wrong with locating the file.
	 */
	public static void updateScores(int newScore) throws IOException {

		if (!SCORES_FILE.exists()) {// Check if the scores file does not exist.
			SCORES_FILE.createNewFile(); // Creates a new file.
		}

		Scanner scanner = new Scanner(SCORES_FILE); // Declares a scanner to scan the file.

		List<Integer> currentScores = new ArrayList<Integer>(); // This is an list to store the scores.
		while (scanner.hasNextLine()) { // Check that the scanner has another line.
			String nextLine = scanner.nextLine(); // Reads the scanner's next line.
			currentScores.add(Integer.parseInt(nextLine)); // Add the score to the list of scores.
		}

		scanner.close(); // Close the scanner.

		currentScores.add(newScore); // Add the new score to the list.
		Collections.sort(currentScores); // Sort the list from least to greatest.

		PrintWriter pw = new PrintWriter(SCORES_FILE); // Declares a print writer to write to the file.

		int newSize = currentScores.size() > 10 ? 10 : currentScores.size(); // This is the maximum amount of lines to write to the file.
		for (int i = 0; i < newSize; i++) { // Loops through the amount of integers to write to the file.
			pw.println(currentScores.get(currentScores.size() - 1 - i)); // Writes the integer at the specified index starting from the end of the list.
		}

		pw.close(); // Closes the print writer.
	}
}
