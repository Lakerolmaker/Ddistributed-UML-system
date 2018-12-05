package application;

import javafx.application.Platform;
import ui.RingProgressIndicator;

public class ProgressThread extends Thread{

	RingProgressIndicator rpi;
	int progress;
	
	public ProgressThread(RingProgressIndicator rpi) {
		this.rpi = rpi;
		progress = 0; 
	}
	
	@Override
	public void run() {
		while(true) {
			// delay 100m ms
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Platform.runLater(()->{
				rpi.setProgress(progress);
			});
			
			progress++;
			if(progress>100) break;
		}
	}
}

