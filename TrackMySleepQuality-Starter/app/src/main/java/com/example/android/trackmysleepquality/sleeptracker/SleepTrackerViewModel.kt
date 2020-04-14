/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {


    //Use encapsulation to only expose a gettable version of the LiveData to the ViewModel
    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality

    //Add a doneNavigating() function that resets the variable that triggers navigation.
    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }



    //To set up and trigger a snackbar when the data is cleared, you can use the same technique as for triggering navigation.
    private val _showSnackBarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent : LiveData<Boolean>
        get() = _showSnackBarEvent

    fun doneShowingSnackbar(){
        _showSnackBarEvent.value = false
    }



    private val nights = database.getAllNights()
    var nightsString = Transformations.map(nights) { nights -> formatNights(nights, application.resources) }


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var tonight = MutableLiveData<SleepNight?>()

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = database.getTonight()
            if (night?.endTimeMilli != night?.startTimeMilli) {
                night = null
            }
            night
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun onStartTracking() {
        uiScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            // update tonight
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(newNight: SleepNight) {
        withContext(Dispatchers.IO) {
            database.insert(newNight)
        }
    }


    fun onStopTracking() {
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)

            _navigateToSleepQuality.value = oldNight
        }
    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
            tonight.value = null
            _showSnackBarEvent.value = true
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }


    /*use transformation maps to manage button visibility so that users can only make the right choice
     - The Start button should be enabled when tonight is null.
     - The Stop button should be enabled when tonight is not null.
     - The Clear button should only be enabled if nights, and thus the database, contains sleep nights.
    */

    val startButtonVisible = Transformations.map(tonight) {
        it == null
    }

    val stopButtonVisible = Transformations.map(tonight){
        it != null
    }

    val clearButtonVisible = Transformations.map(nights){
        it?.isNotEmpty()
    }



    /*
    * Important: Now you can see a pattern:
    1. Launch a coroutine that runs on the main or UI thread, because the result affects the UI.
    2. Call a suspend function to do the long-running work, so that you don't block the UI thread while waiting for the result.
    3. The long-running work has nothing to do with the UI. Switch to the I/O context, so that the work can run in a thread pool that's optimized and set aside for these kinds of operations.
    4. Then call the database function to do the work.
    The pattern is shown below.
    * fun someWorkNeedsToBeDone {
        uiScope.launch {

            suspendFunction()
        }
      }

        suspend fun suspendFunction() {
           withContext(Dispatchers.IO) {
               longrunningWork()
           }
        }
    *
    *
    * */
}

