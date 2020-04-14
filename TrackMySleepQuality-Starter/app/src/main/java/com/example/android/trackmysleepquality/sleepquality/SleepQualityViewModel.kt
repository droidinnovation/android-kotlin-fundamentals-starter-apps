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

package com.example.android.trackmysleepquality.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import kotlinx.coroutines.*

class SleepQualityViewModel(private val sleepNightKey: Long = 0L,
                            val databaseDao: SleepDatabaseDao) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigationToSleepTracker = MutableLiveData<Boolean?>()
    val navigationToSleepTracker: LiveData<Boolean?>
        get() = _navigationToSleepTracker

    fun doneNavigation() {
        _navigationToSleepTracker.value = null
    }





    /*Launch a coroutine in the uiScope, and switch to the I/O dispatcher.
    Get tonight using the sleepNightKey.
    Set the sleep quality.
    Update the database.
    Trigger navigation.*/

    fun onSleepQuality(quality: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val tonight = databaseDao.get(sleepNightKey) ?: return@withContext
                tonight.sleepQuality = quality
                databaseDao.update(tonight)
            }
            // Setting this state variable to true will alert the observer and trigger navigation.
            _navigationToSleepTracker.value = true
        }

    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}