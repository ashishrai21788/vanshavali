/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.app.vanshavali.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.vanshavali.home.dao.CatDao
import com.app.vanshavali.home.entity.CatEntity
import com.app.vanshavali.landing.dao.MainDao
import com.app.vanshavali.landing.entity.MainEntity
import com.app.vanshavali.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.room.migration.Migration



/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
@Database(entities = [CatEntity::class, MainEntity::class], version = 3)
abstract class BaseRoomDatabase : RoomDatabase() {

    abstract fun catDao(): CatDao
    abstract fun mainDao(): MainDao


    companion object {
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Since we didn't alter the table, there's nothing else to do here.
            }
        }
        @Volatile
        private var INSTANCE: BaseRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): BaseRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BaseRoomDatabase::class.java,
                    "main_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(RoomDatabaseCallback(scope))
                    .addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class RoomDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                      //  populateDatabase(context =BaseRoomDatabase::class.java,    catDao = database.catDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(context: Context, catDao: CatDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            catDao.deleteAll()
            var jsonFileString = Utils.getJsonDataFromAsset(context, "category.json")
//
//            var word = Word("Hello")
//            wordDao.insert(word)
//            word = Word("World!")
//            wordDao.insert(word)

        }
    }


}
