const sequenceOfDeath = ['gallows-bot', 'gallows-top', 'rope', 'hangman-head', 'hangman-left-hand', 'hangman-right-hand', 'hangman-body', 'hangman-left-leg', 'hangman-right-leg'];
const alphabet = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
const endGameStatus = 9;

var Hangman = {
	loadState: function () {
		$.ajax({
			url: config.path + "load",
			success: function(ev) {
				var status = ev.status;
				var word = ev.word;
				var letters = ev.letters;

				if (word != null && word.length > 0) {
					Hangman.generateAlphabet();
					Hangman.generateInputsForResult(word);
					Hangman.hideLetters(letters);
					Hangman.initSequenceOfDeath(status);
					$("#alphabet").show();
					if (word.indexOf("_") == -1) {
						var $yupi = $('#yupi');
						$yupi.show();
						$yupi.addClass("on");
						Hangman.disableAlphabetClick();
					}
				}
			}
		});
	},
	hideLetters: function(letters) {
		var lettersArray = letters.split(',');

		for (var i = 0; i < lettersArray.length; i++) {
			if (lettersArray[i] == "") {
				continue;
			}
			var letter = $('#alphabet').find($('.letter:contains('+lettersArray[i]+')'));
			if (letter.length > 0) {
				letter.html("");
				letter.removeClass("letter");
			}
		}
	},
	generateAlphabet: function() {
		for (var i = alphabet.length -1 ; i >= 0; i--) {
			$('#alphabet').prepend($('<div class="letter">' + alphabet[i] + "</div>"))
		}
	},
	generateInputsForResult: function(value) {
		var wordsSeparated = [];

		for (var i = 0; i < value.length; i++) {
			wordsSeparated.push(value[i]);
		}

		for (var j = 0; j < wordsSeparated.length; j++) {
			var word = wordsSeparated[j];
			var input = $("<input>").attr({
				type: 'text',
				readonly: 'readonly',
				disabled: 'disabled',
				id: 'letter-' + j,
				value: word
			});
			$("#word").append(input).css("width", wordsSeparated.length * 20 + 30);
		}
	},
	setWordLetter: function (indexesOfChar, letter) {
		if (indexesOfChar == null) {
			return;
		}
		for (var i = 0; i < indexesOfChar.length; i++) {
			$('#letter-' + indexesOfChar[i]).val(letter);
		}
	},
	initSequenceOfDeath: function(status) {
		if (status == null) {
			return;
		}
		for (var i = 0; i < status; i++) {
			$('#' + sequenceOfDeath[i]).show();
		}
		if (status == endGameStatus) {
			this.endGame(true);
		}
	},
	endGame: function(hang) {
		if (hang) {
			$('#hangman').addClass("on");
		}
		Hangman.disableAlphabetClick();
		$('#yupi').addClass("on");
	},
	startGame: function() {
		this.alphabet();

		$('#start-game').on('click', function(ev) {
			ev.preventDefault();
			var $word = $("#word");
			var $yupi = $('#yupi');
			var $hangman = $("#hangman");
			var $alphabet = $("#alphabet");
			$yupi.hide();
			$yupi.removeClass("on");
			$word.html("");
			$alphabet.html("");
			$alphabet.show();
			Hangman.generateAlphabet();
			Hangman.enableAlphabetClick();
			$hangman.removeClass("on");
			hideSequenceOfDeath();
			startGameAjax();
		});

		function startGameAjax() {
			$.ajax({
				url: config.path + "start",
				success: function (ev) {
					Hangman.generateInputsForResult(ev);
				}
			});
		}

		function hideSequenceOfDeath() {
			for (var i = 0; i < sequenceOfDeath.length; i++) {
				$('#' + sequenceOfDeath[i]).hide();
			}
		}
	},
	alphabet: function() {
		$('#alphabet').on('click', '.letter', function() {
			var letter = $(this).html();
			$(this).html("");
			$(this).removeClass('letter');

			checkLetterExists(letter);
		});

		function checkLetterExists(letter) {
			$.ajax({
				url: config.path + "check/" + letter,
				success: function (ev) {
					var status = ev.status;
					var indexesOfChar = ev.indexesOfChar;

					Hangman.initSequenceOfDeath(status);
					Hangman.setWordLetter(indexesOfChar, letter);

					var word = getWord();
					updateWord(word);
				}
			});
		}

		function getWord() {
			var word = "";
			$("#word").find('input[type=text]').each(function (k, v) {
				word = word + $(v).val();
			});
			return word;
		}

		function updateWord(word) {
			$.ajax({
				url: config.path + "update",
				data: {word: word},
				type: "post",
				success: function (ev) {
					if (ev == "yupi") {
						$('#yupi').show();
						Hangman.endGame(false);
					}
				}
			});
		}
	},
	enableAlphabetClick: function() {
		$('#alphabet').find('div').each(function(k, v) {
			$(v).addClass('letter')
		});
	},
	disableAlphabetClick: function() {
		$('#alphabet').find('div').each(function(k, v) {
			$(v).removeClass('letter')
		});
	},
	handleManagementPage: function() {
		this.handleManagementPageClose();

		$('#open-management').on('click', function(ev) {
			ev.preventDefault();

			$.ajax({
				url: config.path + "gamesList",
				success: function (ev) {
					initManagementList(ev);
				}
			});

			function initManagementList(ev) {
				var $managementTable = $('#management-table');
				var $management = $('#management');
				$management.show();
				$managementTable.html("");
				var table = $("<table>");
				var theader = $("<thead>");
				var tbody = $("<tbody>");
				var idHeader = $('<th>').text('ID');
				var stateHeader = $('<th>').text('State');
				var wordHeader = $('<th>').text('Word to find');
				var discoveredHeaders = $('<th>').text('Word found');
				var lettersHeaders = $('<th>').text('Letters used');

				theader.append(idHeader);
				theader.append(stateHeader);
				theader.append(wordHeader);
				theader.append(discoveredHeaders);
				theader.append(lettersHeaders);
				table.append(theader);

				for (var i = 0; i < ev.length; i++) {
					var id = ev[i].id;
					var state = ev[i].state;
					var word = ev[i].word;
					var letters = ev[i].letters;
					var discovered = ev[i].discovered;

					var row = $('<tr>');
					row.append($("<td>").append(id));
					row.append($("<td>").append(state));
					row.append($("<td>").append(word));
					row.append($("<td>").append(discovered));
					row.append($("<td>").append(letters));

					tbody.append(row)
				}
				table.append(tbody);
				$managementTable.append(table)
			}
		})
	},
	handleManagementPageClose: function() {
		$('.close').on('click', function() {
			$('#management').hide();
		})
	}
};



