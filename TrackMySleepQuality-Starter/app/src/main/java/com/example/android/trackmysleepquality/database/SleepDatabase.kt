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

package com.example.android.trackmysleepquality.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 1. Create a public abstract class that extends RoomDatabase. This class is to act as a database holder.
// The class is abstract, because Room creates the implementation for you.
// 2. Annotate the class with @Database. In the arguments, declare the entities for the database and set the version number.
// 3. Inside a companion object, define an abstract method or property that returns a SleepDatabaseDao. Room will generate the body for you.
// 4. You only need one instance of the Room database for the whole app, so make the RoomDatabase a singleton.
// 5. Use Room's database builder to create the database only if the database doesn't exist. Otherwise, return the existing database.
@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {

    abstract val sleepDatabaseDao: SleepDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: SleepDatabase? = null

        fun getInstance(context: Context): SleepDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, SleepDatabase::class.java, "sleep_history_database")
                            .fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }





}
