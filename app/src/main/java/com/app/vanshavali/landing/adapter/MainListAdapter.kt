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

package com.app.vanshavali.landing.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vanshavali.R
import com.app.vanshavali.databinding.ItemBinding
import com.app.vanshavali.landing.clickinterface.MainOnItemClickListener
import com.app.vanshavali.landing.entity.MainEntity
import com.app.vanshavali.landing.repo.MainRepository
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class MainListAdapter(
    val onItemClickListener: MainOnItemClickListener,
    var repository: MainRepository
) :
    ListAdapter<MainEntity, MainListAdapter.MainViewHolder>(
        WORDS_COMPARATOR
    ), Filterable {

    var mainList: MutableList<MainEntity> =  arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
         //mainList = currentList
        return MainViewHolder.create(parent)
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        var current = getItem(position)
        var parent: MainEntity? = null
        var childCount = 0
        runBlocking {
            launch {
                parent = repository.getUserById(current.parentId)
                childCount = repository.getChildCount(current.memberId)

            }
        }

        holder.bind(current, parent, childCount)
        holder.itemView.setOnClickListener {
            onItemClickListener.OnItemClick(current)
        }

    }

    class MainViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            getRandomColor(binding.liChild)
        }

        fun bind(mainEntity: MainEntity, parentDetail: MainEntity?, childCount: Int) {
            binding.userName =
                "Name: ".plus(
                    mainEntity.nameEnglish.plus(" (".plus(mainEntity.nameHindi)).plus(")")
                )
            if (parentDetail != null) {
                binding.fName = "Father's Name: ".plus(parentDetail.nameEnglish)
                    .plus(" (".plus(parentDetail.nameHindi)).plus(")").plus("\n(Parent Id: ".plus(parentDetail.memberId).plus(")"))
            } else {
                binding.fName = "Father's Name: NA"
            }
//            binding.noOfChild = mainEntity.noOfChildren.plus(" Children's")
            binding.noOfChild = "".plus(childCount).plus(" Children's")

            binding.noOfmarriage = "marriage ".plus(mainEntity.noOfMarriage)
            binding.address = mainEntity.address
            binding.memberId = "User Id: ".plus(mainEntity.memberId)

//            Glide
//                .with(itemView.context)
//                .load(mainEntity.profileImage)
//                .centerCrop()
//                .placeholder(R.drawable.default_user)
//                .error(R.drawable.default_user)
//                .into(binding?.userImage)

        }

        companion object {
            fun create(parent: ViewGroup): MainViewHolder {

                return MainViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.recyclerview_item, parent,
                        false
                    )
                )
            }
        }
    }

    companion object {
        fun getRandomColor(linearLayout: LinearLayout) {
            val rnd = Random()
            val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            linearLayout.background.setTint(color)
            linearLayout.background.alpha = 100


        }

        private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<MainEntity>() {
            override fun areItemsTheSame(oldItem: MainEntity, newItem: MainEntity): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: MainEntity, newItem: MainEntity): Boolean {
                return oldItem.nameEnglish == newItem.nameEnglish
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            var filterList: MutableList<MainEntity>  = arrayListOf()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterList = mainList
                    submitList(filterList)
                } else {
                     val resultList: MutableList<MainEntity> = arrayListOf()
                    for (row in mainList) {
                        if (row.nameEnglish.startsWith(
                                charSearch.lowercase(Locale.ROOT),
                                ignoreCase = true
                            )
                        ) {
                            resultList.add(row)
                        }
                    }
                    filterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                try {
                    filterList = results?.values as MutableList<MainEntity>
                    submitList(filterList)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }

        }
    }
}
