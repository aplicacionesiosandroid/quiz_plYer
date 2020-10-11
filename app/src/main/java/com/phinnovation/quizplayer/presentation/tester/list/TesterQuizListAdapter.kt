/*
 * Copyright (c) 2019 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.phinnovation.quizplayer.presentation.tester.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phinnovation.core.domain.Quiz
import com.phinnovation.core.domain.QuizState
import com.phinnovation.quizplayer.R
import kotlinx.android.synthetic.main.tester_quiz_item.view.*


class TesterQuizListAdapter(
        private val quizzes: MutableList<Quiz> = mutableListOf(),
        private val itemClickListener: (Quiz) -> Unit
) : RecyclerView.Adapter<TesterQuizListAdapter.ViewHolder>() {

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val titleTextView: TextView = view.tvTitle
    val iconImageView: ImageView = view.ivIcon
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.tester_quiz_item, parent, false)
    )
  }

  override fun getItemCount() = quizzes.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.run {
    holder.titleTextView.text = quizzes[position].title

    when (quizzes[position].state) {
      QuizState.NOT_STARTED -> holder.iconImageView.setImageResource(R.drawable.ic_play)
      QuizState.STARTED -> holder.iconImageView.setImageResource(R.drawable.ic_paused)
      else -> holder.iconImageView.setImageResource(R.drawable.ic_done)
    }


    holder.itemView.setOnClickListener { itemClickListener.invoke(quizzes[position]) }
  }

  fun update(newQuizzes: List<Quiz>) {
    quizzes.clear()
    quizzes.addAll(newQuizzes)

    notifyDataSetChanged()
  }
}