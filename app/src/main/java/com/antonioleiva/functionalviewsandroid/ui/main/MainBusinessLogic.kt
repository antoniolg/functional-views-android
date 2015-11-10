/*
 * Copyright 2015 Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.antonioleiva.functionalviewsandroid.ui.main

import org.jetbrains.anko.async

interface MainBusinessLogic {

    companion object {
        val animals = "animals"
        val city = "city"
        val food = "food"
        val people = "people"
        val sports = "sports"
        val technics = "technics"
        val nature = "nature"

        val categories = listOf(animals, city, food, people, sports, technics)
    }

    fun fetchAsyncData(callback: (List<Item>) -> Unit) = async {
        Thread.sleep(1000)
        val res = categories.flatMap { cat ->
            (1..5).map { i ->
                Item(cat, i, false)
            }
        }
        callback(res)
    }


}