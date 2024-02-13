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
package com.app.vanshavali.landing.repo

import androidx.annotation.WorkerThread
import com.app.vanshavali.landing.dao.MainDao
import com.app.vanshavali.landing.entity.MainEntity
import kotlinx.coroutines.flow.Flow

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
class MainRepository(private val mainDao: MainDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val getAllCategories: Flow<List<MainEntity>> = mainDao.getAllCategories()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(mainEntity: MainEntity) {
        mainDao.insert(mainEntity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete() {
        mainDao.deleteAll()
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getDetailsById(memberId: String, parentId: String) =
        mainDao.getDetailsByMemberId(memberId, parentId)


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getUserById(parentId: String) =
        mainDao.getDetailsById(parentId)


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getChildCount(memberId: String) =
        mainDao.getChildCount(memberId)


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend  fun hasChild() =
        mainDao.hasItem()
    }



