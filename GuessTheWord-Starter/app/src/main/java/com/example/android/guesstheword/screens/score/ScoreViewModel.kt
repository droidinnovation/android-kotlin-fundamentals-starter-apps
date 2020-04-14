package com.example.android.guesstheword.screens.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class ScoreViewModel(finalScore: Int): ViewModel() {

    /*val score = finalScore
    init {
        Timber.i("Final score is $finalScore")
    }*/

    //Use LiveData
    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    init {
        _score.value = finalScore
    }

    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain : LiveData<Boolean>
        get() = _eventPlayAgain



    fun onPlayAgain(){
        _eventPlayAgain.value = true
    }

    fun onPlayAgainComplete(){
        _eventPlayAgain.value = false
    }

}