package application;

import java.util.*;

import TCP.RunnableArg;

public class event {
    public static void main(String[] args) {
        Initiater initiater = new Initiater();
        Responder responder = new Responder(new RunnableArg<String>() {

			@Override
			public void run() {
				System.out.println("handeled");
				
			}
		});

        initiater.addListener(responder);

        initiater.trigger();  // Prints "Hello!!!" and "Hello there..."
    }
}


// An interface to be implemented by everyone interested in "Hello" events
interface HelloListener {
    void someoneSaidHello();
}

// Someone who says "Hello"
class Initiater {
    private List<HelloListener> listeners = new ArrayList<HelloListener>();

    public void addListener(HelloListener toAdd) {
        listeners.add(toAdd);
    }

    public void trigger() {
        // Notify everybody that may be interested.
        for (HelloListener hl : listeners)
            hl.someoneSaidHello();
    }
}

// Someone interested in "Hello" events
class Responder implements HelloListener {
	
	RunnableArg arg;
	
	public Responder(RunnableArg arg) {
		this.arg = arg;
	}
	
    @Override
    public void someoneSaidHello() {
    	arg.run();
    }
}

