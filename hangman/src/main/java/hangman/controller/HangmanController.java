package hangman.controller;

import hangman.commons.Constants;
import hangman.exceptions.EndGameException;
import hangman.model.GeneratedWord;
import hangman.model.Hangman;
import hangman.model.LetterAvailability;
import hangman.model.State;
import hangman.service.CookieService;
import hangman.service.DatabaseService;
import hangman.service.WordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/")
public class HangmanController {

	@Autowired
	private WordsService wordsService;

	@Autowired
	private CookieService cookieService;

	@Autowired
	private DatabaseService databaseService;

	@RequestMapping(method = RequestMethod.GET)
	public String main(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		Cookie idCookie = cookieService.getCookie(cookies, Constants.HANGMAN_ID);

		if (idCookie != null) {
			cookieService.addCookie(response, Constants.HANGMAN_ID, idCookie.getValue());
		}

		return "index";
	}

	@RequestMapping(value = "/load", method = RequestMethod.GET)
	@ResponseBody
	public State load(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		Cookie idCookie = cookieService.getCookie(cookies, Constants.HANGMAN_ID);
		if (idCookie != null) {
			Hangman hangman = databaseService.getState(idCookie.getValue());
			if (hangman != null) {
				return new State(hangman.getState(), hangman.getDiscovered(), hangman.getLetters());
			}
		}

		return new State();
	}

	@RequestMapping(value = "/start", method = RequestMethod.GET)
	@ResponseBody
	public String start(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		Cookie oldIdCookie = cookieService.getCookie(cookies, Constants.HANGMAN_ID);

		if (oldIdCookie != null) {
			String oldId = oldIdCookie.getValue();
			databaseService.deleteState(oldId);
		}

		GeneratedWord generatedWord = wordsService.generateWord();
		Cookie idCookie = cookieService.createNewCookie(Constants.HANGMAN_ID, String.valueOf(new Random().nextInt()));
		cookieService.addCookie(response, Constants.HANGMAN_ID, idCookie.getValue());
		databaseService.addState(new Hangman(idCookie.getValue(), generatedWord.getWord()));

		return generatedWord.getHashedWord();
	}

	@RequestMapping(value = "/check/{letter}", method = RequestMethod.GET)
	@ResponseBody
	public LetterAvailability isLetterAvailable(@PathVariable String letter,
												HttpServletRequest request) throws EndGameException {
		LetterAvailability letterAvailability = new LetterAvailability();
		if ("".equals(letter)) {
			return letterAvailability;
		}

		Cookie[] cookies = request.getCookies();
		Cookie idCookie = cookieService.getCookie(cookies, Constants.HANGMAN_ID);
		if (idCookie == null) {
			return letterAvailability;
		}

		Hangman hangman = databaseService.getState(idCookie.getValue());
		if (hangman == null) {
			return letterAvailability;
		}

		hangman.setLetters(hangman.getLetters() + Constants.COMMA + letter + Constants.COMMA);

		Integer hangmanStatus = Integer.valueOf(hangman.getState());
		if (hangmanStatus == Constants.END_GAME_STATUS) {
			throw new EndGameException();
		}

		String word = hangman.getWord();
		List<String> indexesOfChar = wordsService.getIndexesOfChars(letter, word);

		if (!indexesOfChar.isEmpty()) {
			letterAvailability.setIndexesOfChar(indexesOfChar);
			return letterAvailability;
		}

		letterAvailability.setStatus(hangmanStatus + 1);
		hangman.setState(String.valueOf(hangmanStatus + 1));
		databaseService.updateState(hangman);
		return letterAvailability;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public String updateWord(HttpServletRequest request) {
		String word = request.getParameter("word");
		Cookie[] cookies = request.getCookies();
		Cookie idCookie = cookieService.getCookie(cookies, Constants.HANGMAN_ID);
		if (idCookie != null) {
		Hangman hangman = databaseService.getState(idCookie.getValue());
		hangman.setDiscovered(word);
		databaseService.updateState(hangman);
		}
		if (!word.contains(Constants.UNDERSCORE)) {
			return Constants.END_GAME_WORD;
		}
		return "";
	}

	@RequestMapping(value = "/gamesList", method = RequestMethod.GET)
	@ResponseBody
	public List<Hangman> gamesList() {
		return databaseService.getGamesList();
	}

}