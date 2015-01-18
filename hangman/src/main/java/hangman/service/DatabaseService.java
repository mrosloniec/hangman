package hangman.service;

import hangman.commons.Constants;
import hangman.model.Hangman;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope(value = "prototype")
public class DatabaseService extends AbstractFileReader {

	private List<Hangman> gamesList = new ArrayList<Hangman>();

	public DatabaseService() {
		initGamesList();
	}

	public Hangman getState(String id) {
		return getHangman(id);
	}

	public void addState(Hangman hangman) {
		gamesList.add(hangman);
		writeFile();
	}

	public void deleteState(String id) {
		Hangman hangmanObj = getHangman(id);
		if (hangmanObj != null) {
			gamesList.remove(hangmanObj);
		}
		writeFile();
	}

	public void updateState(Hangman hangman) {
		Hangman dbHangman = getHangman(hangman.getId());
		gamesList.remove(dbHangman);
		gamesList.add(hangman);
		writeFile();
	}

	public List<Hangman> getGamesList() {
		return gamesList;
	}

	private void writeFile() {
		String path = WordsService.class.getClassLoader().getResource(Constants.DATABASE_FILE).getPath();
		FileWriter fw;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			for (Hangman hangman : gamesList) {
				bw.write(hangman.toString());
				bw.write(System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeResources(bw);
		}
	}

	private Hangman getHangman(String id) {
		for (Hangman hangman : gamesList) {
			if (hangman.getId().equals(id)) {
				return hangman;
			}
		}
		return null;
	}

	private void initGamesList() {
		BufferedInputStream bis = new BufferedInputStream(WordsService.class.getClassLoader().getResourceAsStream(Constants.DATABASE_FILE));
		BufferedReader br = new BufferedReader(new InputStreamReader(bis));
		String line;
		try {
			while ((line = br.readLine()) != null) {
				String[] split = line.split(Constants.WORD_SEPARATOR, 5);
				Hangman hangman = new Hangman();
				if (split.length >= 1) {
					hangman.setId(split[0]);
				}
				if (split.length >= 2) {
					hangman.setState(split[1]);
				}
				if (split.length >= 3) {
					hangman.setWord(split[2]);
				}
				if (split.length >= 4) {
					hangman.setDiscovered(split[3]);
				}
				if (split.length >= 5) {
					hangman.setLetters(split[4]);
				}
				gamesList.add(hangman);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeResources(br);
			closeResources(bis);
		}
	}

}
