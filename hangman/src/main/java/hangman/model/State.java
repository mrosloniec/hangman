package hangman.model;

public class State {

	private String status = "";
	private String word = "";
	private String letters = "";

	public State() {
	}

	public State(String status, String word, String letters) {
		this.status = status;
		this.word = word;
		this.letters = letters;
	}

	public String getStatus() {
		return status;
	}

	public String getWord() {
		return word;
	}

	public String getLetters() {
		return letters;
	}
}
