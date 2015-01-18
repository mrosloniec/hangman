package hangman.model;

import java.util.List;

public class LetterAvailability {

	private Integer status;
	private List<String> indexesOfChar;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<String> getIndexesOfChar() {
		return indexesOfChar;
	}

	public void setIndexesOfChar(List<String> indexesOfChar) {
		this.indexesOfChar = indexesOfChar;
	}
}
