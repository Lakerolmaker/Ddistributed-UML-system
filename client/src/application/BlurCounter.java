package application;

public class BlurCounter {

	private  static int counter = 0;
	
	public static void increase() {
		counter ++;
	}
	
	public static void decrease() {
		counter --;
	}
	
	public static int getCounter() {
		return counter;
	}
	
}
