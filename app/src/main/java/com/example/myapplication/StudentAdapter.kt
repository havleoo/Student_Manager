package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class StudentAdapter(private val students: MutableList<StudentModel>) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
        val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
        val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
        val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_student_item, parent, false)
        return StudentViewHolder(itemView)
    }

    override fun getItemCount(): Int = students.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.textStudentName.text = student.studentName
        holder.textStudentId.text = student.studentId

        holder.imageEdit.setOnClickListener {
            showEditStudentDialog(holder, position)
        }

        holder.imageRemove.setOnClickListener {
            showDeleteStudentDialog(holder, position)
        }
    }

    private fun showEditStudentDialog(holder: StudentViewHolder, position: Int) {
        val dialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.dialog_add_student, null)
        val dialog = AlertDialog.Builder(holder.itemView.context)
            .setTitle("Edit Student")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val name = dialogView.findViewById<EditText>(R.id.edit_student_name).text.toString()
                val id = dialogView.findViewById<EditText>(R.id.edit_student_id).text.toString()
                if (name.isNotEmpty() && id.isNotEmpty()) {
                    students[position] = StudentModel(name, id)
                    notifyItemChanged(position)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialogView.findViewById<EditText>(R.id.edit_student_name).setText(students[position].studentName)
        dialogView.findViewById<EditText>(R.id.edit_student_id).setText(students[position].studentId)
        dialog.show()
    }

    private fun showDeleteStudentDialog(holder: StudentViewHolder, position: Int) {
        AlertDialog.Builder(holder.itemView.context)
            .setTitle("Delete Student")
            .setMessage("Are you sure you want to delete ${students[position].studentName}?")
            .setPositiveButton("Delete") { _, _ ->
                val deletedStudent = students[position]
                val deletedPosition = position
                students.removeAt(position)
                notifyItemRemoved(position)

                Snackbar.make(holder.itemView, "${deletedStudent.studentName} deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        students.add(deletedPosition, deletedStudent)
                        notifyItemInserted(deletedPosition)
                    }.show()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
}
