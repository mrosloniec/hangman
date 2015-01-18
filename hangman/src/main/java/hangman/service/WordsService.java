package hangman.service;

import hangman.commons.Constants;
import hangman.model.GeneratedWord;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class WordsService extends AbstractFileReader {

	private List<String> wordsList = new ArrayList<String>();

	public WordsService() {
		initWordList();
	}

	private void initWordList() {
		BufferedInputStream bis = new BufferedInputStream(WordsService.class.getClassLoader().getResourceAsStream(Constants.WORDS_LIST_FILE));
		BufferedReader br = new BufferedReader(new InputStreamReader(bis));
		String word;
		try {
			while ((word = br.readLine()) != null) {
				wordsList.add(word);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeResources(br);
			closeResources(bis);
		}
	}

	public GeneratedWord generateWord() {
		Random random = new Random();
		int next = random.nextInt(wordsList.size());
		String word = wordsList.get(next);
		return new GeneratedWord(word, hashWord(word));
	}

	public String hashWord(String word) {
		return word.replaceAll("[a-zA-Z]", "_");
	}

	public List<String> getIndexesOfChars(String letter, String word) {
		List<String> indexesOfChar = new ArrayList<String>();
		int index = 0;
		for (char c : word.toCharArray()) {
			String character = String.valueOf(c);
			if (character.equalsIgnoreCase(letter)) {
				indexesOfChar.add(String.valueOf(index));
			}
			index++;
		}
		return indexesOfChar;
	}



}
