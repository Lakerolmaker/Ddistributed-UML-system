
public class Menu {

	//: Prints the starts screen annimation
	public void printLogo() {

				Main main = new Main();

				String aOrgin = "Welcome To";
				String bOrgin = "Reusax Corporate";
				String cOrgin = "A World Without Boundaries";
				String jacobIsestjacobIsestjacobIsestjacobIsestjacobIsestjacobIsestjacobIsestjacobIsestjacobIsest = "jacob Is da best.";

				String SpaceA = "";
				String SpaceB = "";
				String SpaceC = "";

				String a = "";
				String b = "";
				String c = "";

				for( int i = 0 ;  i <  cOrgin.length(); i++) {

					main.cls();

					try {
						a += aOrgin.charAt(i);
					}catch(Exception e){}

					try {
						b += bOrgin.charAt(i);
					}catch(Exception e){}

					try {
						c += cOrgin.charAt(i);
					}catch(Exception e){}



					SpaceA = "";
					int aWhiteSpace = 10 - a.length() ;
					for(int z = 0; z < aWhiteSpace; z++) {
							SpaceA += new String(" ");
					}

					SpaceB = "";
					int bWhiteSpace = 16 - b.length() ;
					for(int z = 0; z < bWhiteSpace; z++) {
							SpaceB += new String(" ");
					}

					SpaceC = "";
					int cWhiteSpace = 26 - c.length() ;
					for(int z = 0; z < cWhiteSpace; z++) {
							SpaceC += new String(" ");
					}



        			System.out.println("" +
				" ------------------------------------------------------- "	+ "\n" +
				" |                                                     | " + "\n" +
				" |                                                     | " + "\n" +
				" |                                                     | " + "\n" +
				" |                                                     | " + "\n" +
				" |                                                     | " + "\n" +
				" |                     " + a + SpaceA + "                      | " + "\n" +
				" |                                                     | " + "\n" +
				" |                                                     | " + "\n" +
				" |                                                     | " + "\n" +
				" |                  " + b +  SpaceB + "                   | " + "\n" +
				" |                                                     | " + "\n" +
				" |                                                     | " + "\n" +
				" |              " + c + SpaceC + "             | " + "\n" +
				" |                                                     | " + "\n" +
				" |                                                     | " + "\n" +
				" |                                                     | " + "\n" +
				" ------------------------------------------------------- "	+ "\n" + "\n" );


        			sleep(100);


				}

		 sleep(2000);
		 main.cls();
	}


	public  void sleep(int time){
		try
		{
		    Thread.sleep(time);
		}
		catch(InterruptedException ex)
		{
		    Thread.currentThread().interrupt();
		}
	}

	public  void print(Object o) {
	    System.out.print(o);

	}



}
