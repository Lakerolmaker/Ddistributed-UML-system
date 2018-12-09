package backend;

import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class LibraryMenu {
	public static final int CUSTOMER_REGISTRATION = 1;
	public static final int PRINT_CUSTOMER = 2;
	public static final int LEND_BOOK = 3;
	public static final int RETURN_BOOK = 4;
	public static final int ADD_BOOK = 5;
	public static final int ADVANCE_TIME = 6;
	public static final int SORT_BOOK = 7;
	public static final int QUIT = 8;
	private Scanner sc;
	private Library library;
	private RegisteredCustomer regCustomer;

	public LibraryMenu() {
		this.library = new Library();
		this.regCustomer = new RegisteredCustomer();
		sc = new Scanner(System.in);
	}

	public void run() {
		int option = 0;
		do {
			try {
				printMenuOptions();
				System.out.print(" Type the option number: ");

				option = sc.nextInt();
				sc.nextLine();
				switch (option) {
				case CUSTOMER_REGISTRATION:
					registerCustomer();
					break;
				case PRINT_CUSTOMER:
					printCustomer();
					break;
				case LEND_BOOK:
					lendBook();
					break;
				case RETURN_BOOK:
					returnBook();
					break;
				case ADD_BOOK:
					addBook();
					break;
				case ADVANCE_TIME:
					advanceTime();
					break;
				case SORT_BOOK:
					Collections.sort(this.library.listBooks, new BookComparator());
					for (Book s : this.library.listBooks) {
						System.out.println(s.getAuthor());
					}
					break;
				case QUIT:
					System.out.println();
					break;

				default:
					System.out.println("Option " + option + " is not valid.");
					System.out.println();
					break;
				}
			} catch (InputMismatchException e) {
				System.out.println("Error: please input an interger not a String");
				sc.next();
			}
		} while (option != QUIT);

	}

	private void printMenuOptions() {
		System.out.println(" ");
		System.out.println(" Choose an option below: ");
		System.out.println(" ");
		System.out.println(" 1. Register a customer. ");
		System.out.println(" 2. Print a customer's information. ");
		System.out.println(" 3. Borrow book. ");
		System.out.println(" 4. Return book. ");
		System.out.println(" 5. Add book. ");
		System.out.println(" 6. Advance time. ");
		System.out.println(" 7. Sort books by author's name. ");
		System.out.println(" 8. Quit the program.");

	}

	private void registerCustomer() {
		System.out.print("Please enter customer's name: ");
		String name = sc.nextLine();
		System.out.print("Please enter customer's address: ");
		String address = sc.nextLine();
		System.out.print("Please enter customer's phone number: ");
		int phoneNumber = sc.nextInt();
		sc.nextLine();
		String libraryID;
		do {
			libraryID = generateRandomChars("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", 10);
		} while (this.regCustomer.findCustomer(libraryID) != null);
		this.regCustomer.registerCustomer(libraryID, name, address, phoneNumber);
		System.out.println("Here's your library card!");
		Customer foundCustomer = regCustomer.findCustomer(libraryID);
		System.out.print(foundCustomer);
	}

	private Customer printCustomer() {
		System.out.print("Please show your library card: ");
		String libraryID = sc.nextLine();
		Customer foundCustomer = regCustomer.findCustomer(libraryID);
		if (foundCustomer != null) {
			System.out.print("");
			System.out.print(foundCustomer);
			return foundCustomer;
		} else
			System.out.println("You are not registered in the system");
		return null;
	}

	private Book printBook() {
		System.out.print("Please choose the book that you want to borrow and enter the book's ISBN-13: ");
		String ISBN = sc.nextLine();
		Book foundBook = this.library.findBook(ISBN);
		if (foundBook != null) {
			System.out.print("");
			System.out.print(foundBook);
			return foundBook;
		} else
			System.out.println("No book found.");
		return null;
	}

	private void lendBook() {
		Customer foundCustomer = printCustomer();
		System.out.println("");
		if (foundCustomer != null) {
			searchBook();
			Book foundBook = printBook();
			System.out.println("");
			System.out.print("Enter number of days the book will be lent: ");
			int duration = sc.nextInt();
			this.library.lendBook(foundCustomer, foundBook, duration);
			System.out.println("Please return the book within " + duration + " days");
		}
	}

	private void returnBook() {
		Customer foundCustomer = printCustomer();
		System.out.println("");
		if (foundCustomer != null) {
			Book book = this.library.returnBook(foundCustomer);
			int exceededDays = this.library.getAdvancedDays() - book.getLendDuration();
			if (exceededDays > 0)
				System.out.println("You have exceeded borrowing duration by " + exceededDays + " days.");
			System.out.println("You have been charged " + this.library.lateReturnCharge(book) + " SEK");
			this.library.retrieveBook(book);
		}
	}

	private void searchBook() {
		System.out.print("Please enter author's name: ");
		String authorName = sc.nextLine();
		for (Book s : this.library.listBooks) {
			if (s != null && s.getAuthor().contains(authorName)) {
				System.out.println("===============================");
				System.out.println(s);
				System.out.println("===============================");
			}
		}
	}

	private void addBook() {
		System.out.print("Please enter book's title: ");
		String name = sc.nextLine();
		System.out.print("Please enter book's author: ");
		String author = sc.nextLine();
		System.out.print("Please enter book's genre: ");
		String genre = sc.nextLine();
		System.out.print("Please enter book's publisher: ");
		String publisher = sc.nextLine();
		System.out.print("Please enter book's ISBN: ");
		String isbn = sc.nextLine();
		this.library.addBook(name, isbn, publisher, genre, "", author);
	}

	private void advanceTime() {
		System.out.print("Enter number of days: ");
		int days = sc.nextInt();
		System.out.println("Advanced time by " + days + " days.");
		this.library.advanceDays(days);
	}

	public static String generateRandomChars(String candidateChars, int length) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
		}
		return sb.toString();
	}

}