package FileClasses;

public class Progress {

	public int procentage;
	public String stage;

	public Progress(int procentage, String stage) {
		super();
		this.procentage = procentage;
		this.stage = stage;
	}

	public int getProcentage() {
		return procentage;
	}

	public void setProcentage(int procentage) {
		this.procentage = procentage;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

}
