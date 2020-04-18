package com.example.android.trackmysleepquality.sleepdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.Job

class SleepDetailViewModel(private val sleepNightKey: Long = 0L, dataSource: SleepDatabaseDao) : ViewModel() {

    /**
     * Hold a reference to SleepDatabase via its SleepDatabaseDao.
     */
    val database = dataSource

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()


    private val night: LiveData<SleepNight>
    fun getNight() = night

    init {
        night = database.getNightWithId(sleepNightKey)
    }

    //the navigateToSleepTracker variable to control navigation back to the SleepTrackerFragment when the Close button is pressed
    private val _navigationToSleepTracker = MutableLiveData<Boolean?>()
    val navigationToSleepTracker: LiveData<Boolean?>
        get() = _navigationToSleepTracker


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun onClose(){
        _navigationToSleepTracker.value = true
    }

    fun doneNavigating(){
        _navigationToSleepTracker.value = null
    }


}