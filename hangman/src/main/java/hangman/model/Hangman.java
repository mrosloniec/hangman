package hangman.model;

public class Hangman {

	private String id;
	private String state = "0";
	private String word = "";
	private String discovered = "";
	private String letters = "";

	public Hangman() {
	}

	public Hangman(String id) {
		this.id = id;
	}

	public Hangman(String id, String word) {
		this.id = id;
		this.word = word;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getDiscovered() {
		return discovered;
	}

	public void setDiscovered(String discovered) {
		this.discovered = discovered;
	}

	public String getLetters() {
		return letters;
	}

	public void setLetters(String letters) {
		this.letters = letters;
	}

	@Override
	public String toString() {
		return id + ';' + state + ';' + word + ';' + discovered + ';' + letters + ';';
	}
}
