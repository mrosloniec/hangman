package hangman.model;

public class GeneratedWord {

	private String word;
	private String hashedWord;

	public GeneratedWord(String word, String hashedWord) {
		this.word = word;
		this.hashedWord = hashedWord;
	}

	public String getWord() {
		return word;
	}

	public String getHashedWord() {
		return hashedWord;
	}

}
