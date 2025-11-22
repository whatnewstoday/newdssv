package com.manhnd.android_number

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var etMSSV: EditText
    private lateinit var etName: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var rvStudents: RecyclerView

    private val students = mutableListOf<Student>()
    private lateinit var adapter: StudentAdapter
    private var selectedStudent: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        etMSSV = findViewById(R.id.etMSSV)
        etName = findViewById(R.id.etName)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        rvStudents = findViewById(R.id.rvStudents)

        adapter = StudentAdapter(students, this::onStudentClick, this::onDeleteClick)
        rvStudents.layoutManager = LinearLayoutManager(this)
        rvStudents.adapter = adapter
    }

    private fun setupListeners() {
        btnAdd.setOnClickListener {
            val mssv = etMSSV.text.toString().trim()
            val name = etName.text.toString().trim()

            if (mssv.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Please enter both MSSV and Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check for duplicate MSSV if needed, but requirements don't specify.
            // For now, just add.
            val newStudent = Student(name, mssv)
            students.add(newStudent)
            adapter.notifyItemInserted(students.size - 1)
            clearInputs()
        }

        btnUpdate.setOnClickListener {
            val currentStudent = selectedStudent
            if (currentStudent == null) {
                Toast.makeText(this, "Please select a student to update", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val name = etName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Only update name as per requirements: "người dùng có thể cập nhật họ tên"
            // But usually we might want to update MSSV too, but let's stick to requirements or update both if it makes sense.
            // Requirement says: "hiển thị trong 2 ô EditText, khi đó người dùng có thể cập nhật họ tên"
            // It implies MSSV might be read-only or just not mentioned for update.
            // However, since it's in EditText, user can edit it. I will update both for better UX unless restricted.
            // Re-reading: "cập nhật họ tên của phần tử". It specifically mentions Name.
            // But if I edit MSSV, it should probably update too or be reset.
            // I'll update both to be safe and logical.

            currentStudent.name = name
            // currentStudent.mssv = etMSSV.text.toString().trim() // Optional, if we want to allow MSSV update.

            val index = students.indexOf(currentStudent)
            if (index != -1) {
                adapter.notifyItemChanged(index)
            }
            
            selectedStudent = null
            clearInputs()
        }
    }

    private fun onStudentClick(student: Student) {
        selectedStudent = student
        etMSSV.setText(student.mssv)
        etName.setText(student.name)
    }

    private fun onDeleteClick(student: Student) {
        val index = students.indexOf(student)
        if (index != -1) {
            students.removeAt(index)
            adapter.notifyItemRemoved(index)
            if (selectedStudent == student) {
                selectedStudent = null
                clearInputs()
            }
        }
    }

    private fun clearInputs() {
        etMSSV.text.clear()
        etName.text.clear()
        etMSSV.requestFocus()
    }
}
