package backend;

import java.util.ArrayList;

public class Library {
	protected ArrayList<Book> listBooks;
	protected ArrayList<Book> listLentBooks;
	private Book book1, book2, book3;
	private static int days;
	private final static int DAILY_OVERDUE_FEE = 2;

	public Library() {
		this.listBooks = new ArrayList<Book>();
		this.listLentBooks = new ArrayList<Book>();
		book1 = new Book("Java", "978-0133761313", "Pearson", "Education", "", "Y. Daniel Liang");
		book2 = new Book("Math", "978-0073383095", "McGraw-Hill Education", "Education", "", "Kenneth H Rosen");
		book3 = new Book("Interfaces", "978-1449379704", "O'Reilly Media", "Education", "", "Jenifer Tidwell");
		this.listBooks.add(book1);
		this.listBooks.add(book2);
		this.listBooks.add(book3);
	}

	public void addBook(String name, String isbn, String publisher, String genre, String shelf, String author) {
		Book book = new Book(name, isbn, publisher, genre, shelf, author);
		listBooks.add(book);
	}

	public void lendBook(Customer regCustomer, Book book, int duration) {
		this.listLentBooks.add(book);
		this.listBooks.remove(book);
		book.setLentCustomer(regCustomer);
		book.setLendDuration(duration);
	}

	public Book returnBook(Customer regCustomer) {
		for (Book s : this.listLentBooks) {
			if (s != null && s.getLentCustomer().equals(regCustomer))
				return s;
		}
		return null;
	}

	public void retrieveBook(Book book) {
		this.listBooks.add(book);
		this.listLentBooks.remove(book);
		book.setLentCustomer(null);
		book.setLendDuration(0);
	}

	public Book findBook(String ISBN) {
		for (Book s : this.listBooks) {
			if (s != null && s.getISBN().equals(ISBN))
				return s;
		}
		return null;
	}

	public void advanceDays(int days) {
		Library.days = days;
	}

	public int getAdvancedDays() {
		return Library.days;
	}

	public int lateReturnCharge(Book book) {
		return (Library.days - book.getLendDuration()) * DAILY_OVERDUE_FEE;
	}
}
