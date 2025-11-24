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

/**
 * MainActivity - Activity chính của ứng dụng quản lý danh sách sinh viên
 * Chức năng chính:
 * - Thêm sinh viên mới vào danh sách
 * - Cập nhật thông tin sinh viên đã có
 * - Xóa sinh viên khỏi danh sách
 * - Hiển thị danh sách sinh viên dưới dạng RecyclerView
 */
class MainActivity : AppCompatActivity() {

    // Các View components
    private lateinit var etMSSV: EditText      // EditText nhập mã số sinh viên
    private lateinit var etName: EditText      // EditText nhập họ tên sinh viên
    private lateinit var btnAdd: Button        // Button thêm sinh viên mới
    private lateinit var btnUpdate: Button     // Button cập nhật thông tin sinh viên
    private lateinit var rvStudents: RecyclerView  // RecyclerView hiển thị danh sách sinh viên

    // Dữ liệu và adapter
    private val students = mutableListOf<Student>()  // Danh sách sinh viên
    private lateinit var adapter: StudentAdapter     // Adapter cho RecyclerView
    private var selectedStudent: Student? = null     // Sinh viên đang được chọn để cập nhật

    /**
     * Hàm khởi tạo Activity
     * - Thiết lập layout
     * - Khởi tạo views
     * - Thiết lập event listeners
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        // Xử lý padding cho edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupViews()      // Khởi tạo các views
        setupListeners()  // Thiết lập các event listeners
    }

    /**
     * Khởi tạo và cấu hình tất cả các views
     * - Liên kết views với các biến
     * - Thiết lập RecyclerView với adapter
     */
    private fun setupViews() {
        // Liên kết các views từ layout
        etMSSV = findViewById(R.id.etMSSV)
        etName = findViewById(R.id.etName)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        rvStudents = findViewById(R.id.rvStudents)

        // Khởi tạo adapter với danh sách sinh viên và các callback
        adapter = StudentAdapter(students, this::onStudentClick, this::onDeleteClick)
        
        // Cấu hình RecyclerView
        rvStudents.layoutManager = LinearLayoutManager(this)  // Sử dụng LinearLayoutManager
        rvStudents.adapter = adapter                          // Gán adapter
    }

    /**
     * Thiết lập event listeners cho các buttons
     */
    private fun setupListeners() {
        // Xử lý sự kiện click button Add
        btnAdd.setOnClickListener {
            // Lấy dữ liệu từ EditText và loại bỏ khoảng trắng thừa
            val mssv = etMSSV.text.toString().trim()
            val name = etName.text.toString().trim()

            // Validation: Kiểm tra xem cả MSSV và Name đã được nhập chưa
            if (mssv.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Please enter both MSSV and Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tạo sinh viên mới và thêm vào danh sách
            val newStudent = Student(name, mssv)
            students.add(newStudent)
            
            // Thông báo cho adapter biết có item mới được thêm vào
            adapter.notifyItemInserted(students.size - 1)
            
            // Xóa dữ liệu đã nhập
            clearInputs()
        }

        // Xử lý sự kiện click button Update
        btnUpdate.setOnClickListener {
            val currentStudent = selectedStudent
            
            // Kiểm tra xem có sinh viên nào được chọn không
            if (currentStudent == null) {
                Toast.makeText(this, "Please select a student to update", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Lấy tên mới từ EditText
            val name = etName.text.toString().trim()
            
            // Validation: Kiểm tra tên có được nhập không
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cập nhật tên sinh viên
            // Lưu ý: Chỉ cập nhật tên, không cập nhật MSSV
            currentStudent.name = name

            // Tìm vị trí của sinh viên trong danh sách và thông báo cho adapter
            val index = students.indexOf(currentStudent)
            if (index != -1) {
                adapter.notifyItemChanged(index)
            }
            
            // Reset trạng thái selection và xóa dữ liệu đã nhập
            selectedStudent = null
            clearInputs()
        }
    }

    /**
     * Callback khi một sinh viên trong danh sách được click
     * Hiển thị thông tin sinh viên vào các EditText để cập nhật
     */
    private fun onStudentClick(student: Student) {
        selectedStudent = student           // Đánh dấu sinh viên được chọn
        etMSSV.setText(student.mssv)       // Hiển thị MSSV
        etName.setText(student.name)       // Hiển thị tên
    }

    /**
     * Callback khi button delete của một sinh viên được click
     * Xóa sinh viên khỏi danh sách
     */
    private fun onDeleteClick(student: Student) {
        val index = students.indexOf(student)
        if (index != -1) {
            // Xóa sinh viên khỏi danh sách
            students.removeAt(index)
            
            // Thông báo cho adapter biết item đã bị xóa
            adapter.notifyItemRemoved(index)
            
            // Nếu sinh viên đang được chọn để cập nhật bị xóa, reset trạng thái
            if (selectedStudent == student) {
                selectedStudent = null
                clearInputs()
            }
        }
    }

    /**
     * Xóa dữ liệu trong các EditText và đưa focus về MSSV
     */
    private fun clearInputs() {
        etMSSV.text.clear()      // Xóa nội dung MSSV
        etName.text.clear()      // Xóa nội dung tên
        etMSSV.requestFocus()    // Đưa focus về EditText MSSV
    }
}
