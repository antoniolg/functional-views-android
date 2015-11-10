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

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.antonioleiva.functionalviewsandroid.R
import com.antonioleiva.functionalviewsandroid.ui.common.Ui
import com.antonioleiva.functionalviewsandroid.ui.common.inflate
import com.antonioleiva.functionalviewsandroid.ui.common.load
import com.antonioleiva.functionalviewsandroid.ui.common.runUi
import org.jetbrains.anko.findOptional
import org.jetbrains.anko.onClick

class ImageListAdapter(val items: List<Item>, val clickListener: (Int) -> Ui) : RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(parent.inflate(R.layout.image_item))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        runUi(holder.bind(items[position], position, clickListener))
    }

    override fun getItemCount(): Int = items.size

    interface AdapterItemView {
        val parent: View
        val image: ImageView?
        val description: TextView?
        val check: ImageView?
    }

    interface AdapterItemPresentationLogic {
        val adapterItemView: AdapterItemView

        fun bind(item: Item, position: Int, clickListener: (Int) -> Ui) = with(adapterItemView) {
            Ui ({
                parent.onClick { runUi(clickListener(position)) }
                image?.load(item.url)
                description?.text = item.category
                check?.visibility = if (item.selected) View.VISIBLE else View.GONE
            })
        }
    }

    class ImageViewHolder(override val parent: View) : RecyclerView.ViewHolder(parent),
            AdapterItemView, AdapterItemPresentationLogic {

        override val image: ImageView? = parent.findOptional(R.id.image)
        override val description: TextView? = parent.findOptional(R.id.text)
        override val check: ImageView? = parent.findOptional(R.id.check)
        override val adapterItemView = this
    }
}