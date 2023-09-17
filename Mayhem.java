//Game of Mayhem, where 2 players take turns using 11 randomly generated letters to find English words
package MayhemFinal;

//importing required packages
import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Mayhem {
	//Scanners that will be used throughout program
	private static Scanner scanner;
	private static Scanner fileScanner;
	
	//Gets the player's name. Parameters are player's number; returns String of their name
	public static String getName(int playerNum) {
		System.out.print("Player " + playerNum + ": Please enter your name:");
		
		String name = scanner.nextLine();
		return name;
	}

	//Generates random letter. No parameters; returns char of a letter a-z
	public static char getLetter() {
		Random r = new Random();
		char newChar = (char)(r.nextInt(26) + 'a');
		return newChar;
	}
	
	//adds letters to the round's char array. Parameter is the char array; no returns
	public static void addChars(char[] charArray) {
		for(int i = 0; i <= 10; i++) {
			charArray[i] = getLetter();
		}
	}
	
	//Checking to see if 2 valid English words can be made - Checking for 2 or more vowels
	//Parameter is the char array; no returns
	public static void checkArray(char[] charArray) {
		while (true) {
			int vowelCount = 0;
			for(int i = 0; i <= 10; i++) {
				if((charArray[i] == 'a')
						|| (charArray[i] == 'e')
						|| (charArray[i] == 'i')
						|| (charArray[i] == 'o')
						|| (charArray[i] == 'u')
						|| (charArray[i] == 'y')) {
					vowelCount++;
				}
			}
			if(vowelCount >= 2) {
				return;
			} else {
				addChars(charArray);
			}
		}
	}
	
	//Gets user input of their guess. No parameters; returns String of their guess
	public static String getGuess() {
		while(true) {
			System.out.print("Please enter a word: ");	
			String guess = scanner.nextLine();
			if (guess.equals("")) {
				continue;
			}
			return guess.toLowerCase();	
		}
	}
	
	//Checks if letters used in the guess are the letters given to them in the round
	//Parameters are the player's guess, and the array of characters for the round
	public static boolean letterCheck(String guess, char[] lettersArray) {
		int guessLength = guess.length();
		for(int i = 0; i <= (guessLength - 1); i++) {
			int counter = 0;
			for(int k = 0; k <= 10; k++) {
				if (guess.charAt(i) == lettersArray[k]) {
					//counts if there are matches
					counter++;
					break;
				}
			}
				if(counter == 0) {
					return false; 
			 }
		}
		return true;
	}
	
	//Checks if the guess matches english word from a text file
	//Parameters are the player's guess and the text file
	//Returns True if the guess is found in the file, False otherwise
	public static boolean checkEnglish(String guess, File fileName)
			throws FileNotFoundException{

			fileScanner = new Scanner(fileName);

			while (fileScanner.hasNextLine()) {
				if (guess.equals(fileScanner.nextLine())) {
					return true;
				}
			}
			return false;
	}
	
	//Uses the get-guess and checking methods to get a valid guess from the user
	//Parameters are the round's char array, the comparison file name, and an ArrayList<String>
	//Returns the user's guess
	public static String checkGuess(char[] lettersArray, File fileName, ArrayList<String> guessList) throws FileNotFoundException {
		//Continues to ask user for a valid guess until valid guess is entered
		while(true) {
			String guess = getGuess();
			
			if (guess.equals("done") || guess.equals("quit")){
				return guess;
			}
			
			boolean letCheck = letterCheck(guess, lettersArray);
			if (!letCheck) {
				System.out.println("The word that you entered includes letters that you were not assigned");
				System.out.println();
				continue;
			}
			
			boolean engCheck = checkEnglish(guess, fileName);
			if (!engCheck) {
				System.out.println("Please enter an English word");
				System.out.println();
				continue;
			}
			
			boolean guessedCheck = alreadyGuessedCheck(guess, guessList);
			if (guessedCheck) {
				System.out.println("This word has already been found!");
				System.out.println();
				continue;
			}
			
			if ((letCheck) && (engCheck)) {
				return guess;
			}
		}
	}
	
	//Gets points of user's guess based on scoring system
	//Parameters are the user's guess; returns int of the points
	public static int getPoints(String guess) {
		int guessLength = guess.length();
				
		switch (guessLength) {
		case 1:
			return 1;
		case 2:
			return 2;
		case 3:
			return 3;
		case 4:
			return 3;
		case 5:
			return 4;
		case 6:
			return 5;
		case 7:
			return 6;
		default:
			return 10;
		}
	}
	
	//Checks if the user is guessing an already guessed input
	//Parameter is their guess, and an ArrayList<String>
	//Returns true if it has already been guessed, false otherwise
	public static boolean alreadyGuessedCheck(String guess, ArrayList<String> guessList) {
		int length = guessList.size();
		for(int i = 0; i <= (length-1); i++) {
			if (guess.equals(guessList.get(i))){
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		String currentPlayer;
		int roundNum = 0;
		String guess;
		File fileName = new File("ListofEnglishWords.txt");
		ArrayList<String> guessList = new ArrayList<String>(1);
		int player1Points = 0;
		int player2Points = 0;
		String winner;
		int winnerScore;
		scanner = new Scanner(System.in);
		
		//Welcome message
		System.out.println("Welcome to Mayhem! Use the given 11 letters to find English words!");
		System.out.println("Enter DONE to end your turn, and QUIT to end the game.");
		System.out.println();
		
		String player1 = getName(1);
		String player2 = getName(2);
		System.out.println();
		
		//Continues to run unless player enters QUIT
		while (true){
			if(roundNum % 2 == 0) {
				currentPlayer = player1;
			} else {
				currentPlayer = player2;
			}
			
			char[] lettersArray = new char[11];
			addChars(lettersArray);
			checkArray(lettersArray);

			System.out.println(currentPlayer + " it is your turn! Here is your set of letters:" + Arrays.toString(lettersArray));
			System.out.println();
			
			
			//Continuously runs for each round, unless DONE is entered
			while (true) {
				guess = checkGuess(lettersArray, fileName, guessList);
				
				if (guess.equals("done") || (guess.equals("quit"))) {
					roundNum++;
					System.out.println();
					break;
				}
				
				if(roundNum % 2 == 0) {
					player1Points += getPoints(guess);
					System.out.println("Your score is: " + player1Points);
					System.out.println();
				} else {
					player2Points += getPoints(guess);
					System.out.println("Your score is: " + player2Points);
					System.out.println();
				}
				
				guessList.add(guess);
				
			}
			
			if (guess.equals("quit")) {
				break;
			}
		}
		
		//Calculates winner's score
		if(player1Points > player2Points) {
			winner = player1;
			winnerScore = player1Points;
		} else if (player2Points > player1Points) {
			winner = player2;
			winnerScore = player2Points;
		} else {
			System.out.print("Tie game!");
			scanner.close();
			fileScanner.close();
			return;
		}
		System.out.print(winner + " you win the game with a score of " + winnerScore + " points. Congratulations!");
		scanner.close();
		fileScanner.close();
	}
}
