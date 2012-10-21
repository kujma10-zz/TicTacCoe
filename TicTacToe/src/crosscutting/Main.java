package crosscutting;

import java.io.PrintStream;
import java.util.Scanner;

import logic.*;
import model.*;
import ui.*;

import ui.ConsolePresenter;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		PrintStream out = new PrintStream(System.out);
		
		int size = getSize(out, scanner);
		int opt = getOpt(out, scanner, size);
		Board board = new Board(size, opt);
		Presenter presenter = new ConsolePresenter(System.out);
		Player xPlayer = new ConsolePlayer(scanner, System.out);
		Player oPlayer = new PlayerAI();
		
		Referee referee = new PrimeReferee();
		
		Game game = new Game(board, xPlayer, oPlayer, referee, presenter);
		game.play();
		
		scanner.close();
	}

	private static int getSize(PrintStream out, Scanner scanner) {
		out.print("Choose size of the board (max 15): ");
		int size = scanner.nextInt();
		while(size < 3 || size > 15){
			out.print("The size of board should between 3 and 15. Choose again: ");
			size = scanner.nextInt();
		}
		out.println();
		return size;
	}

	private static int getOpt(PrintStream out, Scanner scanner, int size) {
		out.print("Choose win option (3-4-5): ");
		int opt = scanner.nextInt();
		while(opt != 3 && opt != 5 && opt != 4){
			out.print("The number should be 3, 4 or 5. Please try again: ");
			opt = scanner.nextInt();
		}
		while(opt > size){
			out.print("Win option should not be greater than size of board. Please try again: ");
			opt = scanner.nextInt();
		}
		return opt;
	}
}
