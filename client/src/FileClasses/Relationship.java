package FileClasses;

public class Relationship {
	private UMLClass source;
	private UMLClass destination;
	private RelaType type;

	public Relationship(UMLClass source, UMLClass destination, RelaType type) {
		super();
		this.source = source;
		this.destination = destination;
		this.type = type;
	}

	public UMLClass getSource() {
		return source;
	}

	public void setSource(UMLClass source) {
		this.source = source;
	}

	public UMLClass getDestination() {
		return destination;
	}

	public void setDestination(UMLClass destination) {
		this.destination = destination;
	}

	public RelaType getType() {
		return type;
	}

	public void setType(RelaType type) {
		this.type = type;
	}

	public enum RelaType {
		EXTENDS, // TheEconomy extends ConcreteSubject => ConcreteSubject <|-- TheEconomy
		IMPLEMENTS, // ConcreteSubject implements Subject => Subject <|.. ConcreteSubject
		COMPOSITON, // ConcreteSubject has collection<Observer> => ConcreteSubject 1 - * Observer
		ASSOCIATION // ..>
	}
}
