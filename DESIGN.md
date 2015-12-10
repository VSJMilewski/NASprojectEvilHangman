# Evil Hangman DESIGN document

## GameActivity
![Alt text](/images/GameActivity.png)

In the game activity the game is played. On the screen are 3 controllers the user can use. The first one is the edit text. The user can fill in a letter he wants to guess. The guess can be submitted by pressing the 'guess' button. 

In the menu bar there is the menu button. This will give the user three options. Go to the highscores, go to the settings or start a new game. 

The game itself is managed in the Gameplay class. If in the settings for eviltype is chosen, the EvilGameplay class is used. Otherwise the GoodGameplay class is used. Both extend the Gameplay class. 

| Gameplay                                                                |
|-------------------------------------------------------------------------|
| Random Generator                                                        |
| word                                                                    |
| wordLetters[]                                                           |
| lettersGuessed[]                                                        |
| leftGuesses                                                             |
| score                                                                   |
| lexicon                                                                 |
| setGuesses                                                              |
| setLength                                                               |
|                                                                         |
| selectWord                                                              |
| selectWord(w)                                                           |
| GuessedString: returns string, guessed letters                          |
| GuessesString: returns string, left guesses                             |
| wordString: returns string, the word with what is guessed so far        |
| guessed(letter): returns boolean, true if the letter was guessed before |
| guess(letter, context)                                                  |
| addScore(i)                                                             |

In the gameplay class all the variables are stored for playing the game. The random generator is to randomly select a word. The word letters array is to keep in mind which letters from the word are already guessed, and which letters are not. The set guesses and the setLength are number of guesses and the wordlength as was set in the settings for this game. 

SelectWord will randomly select a word. If you give it a string, it will use that as the word. In guess the given letter is tested if it is in the word. If not, a toast is created, otherwise it is filled in in wordLetters. 

| GoodGameplay (implements Gameplay)                                      |
|-------------------------------------------------------------------------|
| No exta variables or methods                                            |

GoodGameplay has nothing extra. It is only to seperate between EvilGameplay and GoodGameplay.

| EvilGameplay (implements Gameplay) |
|------------------------------------|
| words                              |
| powerset                           |
|                                    |
| guess(override)                    |

In EvilGameplay there are two extra variables. Words is an arraylist with all the words from a given length. Guess is extended, so that after every guess, words is seperated in subsets and the largest is chosen. Words are all the possible answers it can be. 
Powerset are all the possible subsets where words can be divided in. 

At the start of the game, the lexicon is loaded in by creating HangmanLexicon.

| HangmanLexicon                                                             |
|----------------------------------------------------------------------------|
| lexicon                                                                    |
| lines                                                                      |
| longest                                                                    |
|                                                                            |
| getWordCount: returns int, number of words in lexicon                      |
| getLongest: returns int, longest word in lexicon                           |
| getWord(index): returns string, the word in the lexicon at the given index |

HangmanLexicon stores all the words that it gets from the xmlparser. It also stores the number of lines and what the longest word is.

| WordlistXmlParser                                                                           |
|---------------------------------------------------------------------------------------------|
| parse(parser): returns list, all the words read in from the file that was set in the parser |

The WordlistXmlParser reads in all the words. It goes through the xml file by reading every type. If it is a tag, it does nothing. But it is text, it is a word and it is added to a list. The finished list is returned.

## HighscoreActivity
![Alt text](/images/HighscoreActivity.png)

There are two ways you get in the highscore activity. One is by going there from the menu button(as explained above), and the other is by finishing a game. When you finish a game, a new score is passed along with an intent. If it is a new highscore, you get a dialog where you fill in your name. If you got in the activity with the button, nothing happens. 

In this activity there are two controllers. One is again the menu button in the menu bar. There are three options, start a new game, go to settings or go back to the game. The other controller is a switch. With this switch the user can select to see the highscores from Good gametype or from Evil gametype. They will be displayed in a table. 

The Highscores are stored in the HighScores class. the checking for a new highscore is done there. 

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

HighScores has two arraylists: evilScores and goodScores. They contain 10 elements from class Score. They are ordered depending on the set points in the Score elements. 

By using the method clear, both arraylist are made entirely empty. There are two ways for adding scores. Normally newScore(score) is used. This detects if it is an evil score or a good score. It is then tested if it is a new highscore and where it should be added in the list.
The other way to add a score is by using newScoreRead(score). This immediatly places it at the next position in the appropriate list. This is for restoring the scores from a file that is readed. This method is used, so that the scores keep the same order as in the file. 

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

The Score class is where the information is stored for each finished game. It keeps the points won in this game, the name of the user who won it, the length of the words and on which gametype was played. 

## SettingActivity
![Alt text](/images/SettingsActivity.png)
In the settings activity the user can change the game settings for the next game. In this activity are four controllers. The first is again the menu button in the menu bar. In here are three options. Go back to the game, start a new game or go to the highscores. 

The other controllers are for changing the settings. The first one is a switch, this changes the game type between evil an good. The second one is a slider that changes number of guesses a user has. And the last one is again a slider that changes the length the words will have in the game. 
