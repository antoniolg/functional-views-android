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

import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import com.antonioleiva.functionalviewsandroid.R
import com.antonioleiva.functionalviewsandroid.ui.common.Ui
import com.antonioleiva.functionalviewsandroid.ui.common.runUi
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast

interface MainPresentationLogic {
    val recycler: RecyclerView
    val fabActionButton: FloatingActionButton

    fun initUi(items: List<Item>): Ui = initRecycler(items) + initFabButton()

    private fun initRecycler(items: List<Item>): Ui = Transformations.recycler(recycler, items,
            { Transformations.recyclerSwapAdapter(recycler, it) })

    private fun initFabButton(): Ui = Transformations.actionButton(fabActionButton,
            { actionButtonClickListener() })

    private fun actionButtonClickListener() = Transformations.showPopupMenu(fabActionButton,
            { onClickMenuListener(it) })

    private fun onClickMenuListener(menuItem: Int): Ui = when (menuItem) {
        R.id.useCase1 -> Transformations.useCase1(recycler)
        R.id.useCase2 -> Transformations.useCase2(recycler, listOf(MainBusinessLogic.animals,
                MainBusinessLogic.nature, MainBusinessLogic.food))
        R.id.useCase3 -> Transformations.useCase3(recycler)
        R.id.useCase4 -> Transformations.useCase4(recycler)
        else -> throw UnsupportedOperationException()
    }

    private object Transformations {

        fun recycler(recycler: RecyclerView, items: List<Item>, clickListener: (Int) -> Ui) = with (recycler) {
            Ui({
                adapter = ImageListAdapter(items, clickListener)
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(context, 2)
            })
        }

        fun actionButton(fab: FloatingActionButton, clickListener: () -> Ui) = with(fab) {
            Ui({
                setImageResource(R.drawable.ic_add)
                onClick { runUi(clickListener()) }
            })
        }

        fun recyclerSwapAdapter(recycler: RecyclerView, position: Int) = adapterOpUi(recycler) {
            val it = items.mapIndexed { i, item ->
                if (i == position) item.copy(selected = !item.selected) else item
            }
            recycler.swapAdapter(ImageListAdapter(it, clickListener), false)
        }

        fun showPopupMenu(fab: FloatingActionButton, clickListener: (Int) -> Ui) = with(fab) {
            Ui({
                PopupMenu(context, this).apply {
                    inflate(R.menu.use_cases)
                    setOnMenuItemClickListener { item -> runUi(clickListener(item.itemId)) }
                    show()
                }
            })
        }

        fun useCase1(recycler: RecyclerView) = adapterOpUi(recycler) {
            val animalsConvertedToNature = items.map {
                when (it.category) {
                    MainBusinessLogic.animals -> it.copy(category = MainBusinessLogic.nature)
                    else -> it
                }
            }
            val a = ImageListAdapter(animalsConvertedToNature, clickListener)
            recycler.swapAdapter(a, false)
        }

        fun useCase2(recycler: RecyclerView, filterCategories: List<String>) = adapterOpUi(recycler) {
            val animalItems = items.filter { filterCategories.contains(it.category) }
            recycler.swapAdapter(ImageListAdapter(animalItems, clickListener), false)
        }

        fun useCase3(recycler: RecyclerView) = adapterOpUi(recycler) {
            val animalItems = items.sortedBy { it.category }
            recycler.swapAdapter(ImageListAdapter(animalItems, clickListener), false)
        }

        fun useCase4(recycler: RecyclerView) = adapterOpUi(recycler) {
            val count = items.count { it.selected }
            with(recycler.context) {
                toast(getString(R.string.items_selected, count.toString()))
            }
        }

        private fun adapterOpUi(recycler: RecyclerView, f: ImageListAdapter.() -> Unit): Ui = with(recycler.adapter) {
            Ui({
                when (this) {
                    is ImageListAdapter -> f()
                    else -> recycler.context.toast(R.string.error)
                }
            })
        }
    }
}
