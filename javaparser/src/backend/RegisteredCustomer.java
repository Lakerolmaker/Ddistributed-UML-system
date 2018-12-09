package backend;

import java.util.ArrayList;

public class RegisteredCustomer {
	protected ArrayList<Customer> customers;

	public RegisteredCustomer() {
		this.customers = new ArrayList<Customer>();
	}

	public void registerCustomer(String libraryID, String name, String address, int phoneNumber) {
		Customer newCustomer = new Customer(libraryID, name, address, phoneNumber);
		this.customers.add(newCustomer);
	}

	public Customer findCustomer(String libraryID) {
		for (Customer s : this.customers) {
			if (s != null && s.getLibraryID().equals(libraryID))
				return s;
		}
		return null;
	}
}
