package TCP;

import java.util.ArrayList;


/*
 * 
 *  A class for runing a function later with arguments inside
 *  
 * 
 * 
 */

public abstract class RunnableArg<dataType> implements Runnable {

	public ArrayList<dataType> args;

    public RunnableArg() {
		args = new ArrayList<dataType>();
    }
    
    public void start() {
        setArgs(args);
        run();
    }

    public void start(ArrayList<dataType> args) {
        setArgs(args);
        Runnable runner = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				  run();
			}
        	
        };
      
        new Thread(runner).start();
    }

    public void setArgs(ArrayList<dataType> newArgs) {
    		this.args = newArgs;
    }
    
    public void setArg(dataType newArgs) {
		this.args.clear();
		args.add(newArgs);
    }
    
    public void addArgs(dataType arg) {
    		args.add(arg);
    }

    public int getArgCount() {
        return args.size();
    }

    public ArrayList<dataType> getArgs() {
        return args;
    }
    
    public dataType getArg() {
        return args.get(0);
    }
    
    public String geAsOne() {
    	String buffer = "";
    	for (dataType dataType : args) {
    		buffer += dataType;
		}
    	return buffer;
    }
}
