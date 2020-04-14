package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import timber.log.Timber

class GameViewModel : ViewModel() {

    // The current word
    //var word = ""

    // The current score
    //var score = 0

    /*val word = MutableLiveData<String>()

    val score = MutableLiveData<Int>()*/


    //Encapsulate the LiveData
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word


    // Event which triggers the end of the game
    private val _endGameFinish = MutableLiveData<Boolean>()
    val endGameFinish: LiveData<Boolean>
        get() = _endGameFinish


    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>


    //countdown time
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    private val timer: CountDownTimer

    // The String version of the current time
    //The lambda function passed to Transformation.map() is executed on the main thread, so do not include long-running tasks.
    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // The Hint for the current word
    val wordHint = Transformations.map(word) { wordTrans ->
        val randomPosition = (1..wordTrans.length).random()
        "Current word has ${wordTrans.length} letters  \nThe letter at position ${randomPosition} is ${wordTrans.get(randomPosition-1).toUpperCase()}"
    }


    init {
        _word.value = ""
        _score.value = 0

        resetList()
        nextWord()
        //Timber.i("GameViewModel created!")


        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished / ONE_SECOND
            }

            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinish()
            }
        }
        timer.start()

    }


    companion object {
        // Time when the game is over
        private const val DONE = 0L

        // Countdown time interval
        private const val ONE_SECOND = 1000L

        // Total time for the game
        private const val COUNTDOWN_TIME = 120000L
    }


    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        /* if (!wordList.isEmpty()) {
             //Select and remove a word from the list
             _word.value = wordList.removeAt(0)
         }*/


        if (wordList.isEmpty()) {
            //onGameFinish()
            resetList()
        } else {
            //Select and remove a word from the list
            _word.value = wordList.removeAt(0)
        }

    }


    /** Methods for buttons presses **/
    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()

        /*score--
        nextWord()*/
    }

    fun onCorrect() {
        //score++
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    /** Method for the game completed event **/
    fun onGameFinish() {
        _endGameFinish.value = true
    }

    fun onGameFinishComplete() {
        _endGameFinish.value = false
    }


    override fun onCleared() {
        super.onCleared()
        Timber.i("onCleared() GameViewModel destroyed!")

        timer.cancel()
    }

}