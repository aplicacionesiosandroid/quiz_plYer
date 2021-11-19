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

package com.phinnovation.quizplayer.presentation.screens.admin.quizeditor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phinnovation.core.domain.Question
import com.phinnovation.quizplayer.R
import kotlinx.android.synthetic.main.admin_quiz_item.view.*


class QuizEditorQuestionListAdapter(
        private val questions: MutableList<Question> = mutableListOf(),
        private val itemClickListener: (Question) -> Unit
) : RecyclerView.Adapter<QuizEditorQuestionListAdapter.ViewHolder>() {

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val titleTextView: TextView = view.tvTitle
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_quiz_item, parent, false)
    )
  }

  override fun getItemCount() = questions.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.run {
    holder.titleTextView.text = questions[position].id.toString() + " : " + questions[position].title
    holder.itemView.setOnClickListener { itemClickListener.invoke(questions[position]) }
  }

  fun update(newQuestions: List<Question>) {
    questions.clear()
    questions.addAll(newQuestions)

    notifyDataSetChanged()
  }
}