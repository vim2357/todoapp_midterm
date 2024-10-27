package com.example.midtermproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.midtermproject.R
import com.example.midtermproject.data.Task
import com.google.android.material.button.MaterialButton

class TaskAdapter(
    private var tasks: MutableList<Task>,
    private val onTaskChecked: (Task, Boolean) -> Unit,
    private val onTaskClicked: (Task) -> Unit,
    private val onTaskDeleted: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
        val title: TextView = view.findViewById(R.id.textViewTitle)
        val btnDelete: MaterialButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.checkBox.isChecked = task.isCompleted

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onTaskChecked(task, isChecked)
        }

        holder.title.setOnClickListener {
            onTaskClicked(task)
        }

        holder.btnDelete.setOnClickListener {
            onTaskDeleted(task)
        }
    }

    override fun getItemCount(): Int = tasks.size

    // Method to update the task list for filtering and sorting
    fun submitList(newList: List<Task>) {
        tasks = newList.toMutableList()
        notifyDataSetChanged()
    }
}
