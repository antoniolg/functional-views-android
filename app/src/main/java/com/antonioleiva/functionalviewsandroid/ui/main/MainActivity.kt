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

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.antonioleiva.functionalviewsandroid.R
import com.antonioleiva.functionalviewsandroid.ui.common.runUi
import org.jetbrains.anko.find
import org.jetbrains.anko.findOptional

class MainActivity : AppCompatActivity(), MainPresentationLogic, MainBusinessLogic {

    override val recycler by lazy { find<RecyclerView>(R.id.recycler) }
    override val fabActionButton by lazy { find<FloatingActionButton>(R.id.fab_action_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findOptional<Toolbar>(R.id.toolbar)
        toolbar?.let { setSupportActionBar(it) }

        fetchAsyncData { data -> runUi(initUi(data)) }
    }
}
