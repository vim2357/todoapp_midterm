package com.example.midtermproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midtermproject.adapter.TaskAdapter
import com.example.midtermproject.data.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private val taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = TaskAdapter(taskList,
            onTaskChecked = { task, isChecked -> updateTaskCompletion(task, isChecked) },
            onTaskClicked = { task -> showEditTaskDialog(task) },
            onTaskDeleted = { task -> deleteTask(task) }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAddTask).setOnClickListener {
            showAddTaskDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_show_all -> {
                showAllTasks()
                true
            }
            R.id.action_show_completed -> {
                showCompletedTasks()
                true
            }
            R.id.action_show_incomplete -> {
                showIncompleteTasks()
                true
            }
            R.id.action_sort_by_title -> {
                sortTasksByTitle()
                true
            }
            R.id.action_sort_by_completion -> {
                sortTasksByCompletion()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val editTextTaskTitle = dialogView.findViewById<EditText>(R.id.editTextTaskTitle)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnSave).setOnClickListener {
            val taskTitle = editTextTaskTitle.text.toString().trim()
            if (taskTitle.isNotEmpty()) {
                addTask(taskTitle)
                dialog.dismiss()
            } else {
                editTextTaskTitle.error = "Task title cannot be empty"
            }
        }

        dialog.show()
    }

    private fun addTask(title: String) {
        val newTask = Task(
            id = System.currentTimeMillis(),
            title = title
        )
        taskList.add(newTask)
        adapter.notifyItemInserted(taskList.size - 1)
    }

    private fun showEditTaskDialog(task: Task) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val editTextTaskTitle = dialogView.findViewById<EditText>(R.id.editTextTaskTitle)
        editTextTaskTitle.setText(task.title)  // Populate with current title

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnSave).setOnClickListener {
            val updatedTitle = editTextTaskTitle.text.toString().trim()
            if (updatedTitle.isNotEmpty()) {
                task.title = updatedTitle  // Update title
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            } else {
                editTextTaskTitle.error = "Task title cannot be empty"
            }
        }

        dialog.show()
    }

    private fun updateTaskCompletion(task: Task, isCompleted: Boolean) {
        task.isCompleted = isCompleted
        adapter.notifyDataSetChanged()
    }

    private fun deleteTask(task: Task) {
        val index = taskList.indexOf(task)
        if (index != -1) {
            taskList.removeAt(index)
            adapter.notifyItemRemoved(index)
        }
    }

    private fun showAllTasks() {
        adapter.submitList(taskList)
    }

    private fun showCompletedTasks() {
        val completedTasks = taskList.filter { it.isCompleted }
        adapter.submitList(completedTasks)
    }

    private fun showIncompleteTasks() {
        val incompleteTasks = taskList.filter { !it.isCompleted }
        adapter.submitList(incompleteTasks)
    }

    private fun sortTasksByTitle() {
        taskList.sortBy { it.title }
        adapter.notifyDataSetChanged()
    }

    private fun sortTasksByCompletion() {
        taskList.sortBy { it.isCompleted }
        adapter.notifyDataSetChanged()
    }
}

