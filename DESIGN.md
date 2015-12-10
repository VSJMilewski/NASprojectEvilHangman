| Gameplay                                                                |
|-------------------------------------------------------------------------|
| Random Generator                                                        |
| word                                                                    |
| wordLetters[]                                                           |
| lettersGuessed[]                                                        |
| leftGuesses                                                             |
| score                                                                   |
| lexicon                                                                 |
|                                                                         |
| setGuesses                                                              |
| setLength                                                               |
| selectWord                                                              |
| selectWord(w)                                                           |
| GuessedString: returns string, guessed letters                          |
| GuessesString: returns string, left guesses                             |
| wordString: returns string, the word with what is guessed so far        |
| guessed(letter): returns boolean, true if the letter was guessed before |
| guess(letter, context)                                                  |
| addScore(i)                                                             |

| GoodGameplay (implements Gameplay)                                      |
|-------------------------------------------------------------------------|
| No exta variables or methods                                            |

| EvilGameplay (implements Gameplay) |
|------------------------------------|
| words                              |
| powerset                           |
|                                    |
| guess(override)                    |

| HangmanLexicon                                                             |
|----------------------------------------------------------------------------|
| lexicon                                                                    |
| lines                                                                      |
| longest                                                                    |
|                                                                            |
| getWordCount: returns int, number of words in lexicon                      |
| getLongest: returns int, longest word in lexicon                           |
| getWord(index): returns string, the word in the lexicon at the given index |

| HighScores                                                             |
|------------------------------------------------------------------------|
| evilScores                                                             |
| goodScores                                                             |
|                                                                        |
| clear                                                                  |
| getScores: returns arraylist, all the evil and good scores in one list |
| getEvilScores: returns arraylist, all the evil scores                  |
| getGoodScores: returns arraylist, all the good scores                  |
| newScore(score)                                                        |
| newScoreRead(score)                                                    |

| Score                                                                              |
|------------------------------------------------------------------------------------|
| points                                                                             |
| name                                                                               |
| wordLength                                                                         |
| gameType                                                                           |
|                                                                                    |
| getPoints: returns int, number of point                                            |
| getWordLength: returns int, word length used for the game where this score was won |
| getName: returns string, the name of who got this score                            |
| getGameType: returns string, the game type it was won on, evil or good             |

| WordlistXmlParser                                                                           |
|---------------------------------------------------------------------------------------------|
| parse(parser): returns list, all the words read in from the file that was set in the parser |
