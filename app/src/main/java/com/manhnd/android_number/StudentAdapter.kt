package com.manhnd.android_number

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * StudentAdapter - Adapter cho RecyclerView hiển thị danh sách sinh viên
 * 
 * @param students Danh sách sinh viên cần hiển thị
 * @param onStudentClick Callback được gọi khi click vào một sinh viên (để chọn và cập nhật)
 * @param onDeleteClick Callback được gọi khi click vào nút xóa của một sinh viên
 */
class StudentAdapter(
    private val students: MutableList<Student>,
    private val onStudentClick: (Student) -> Unit,
    private val onDeleteClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    /**
     * ViewHolder chứa các views của một item sinh viên
     * Sử dụng inner class để có thể truy cập các callback functions
     */
    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Các views trong layout item
        val tvName: TextView = itemView.findViewById(R.id.tvName)          // TextView hiển thị tên
        val tvMSSV: TextView = itemView.findViewById(R.id.tvMSSV)          // TextView hiển thị MSSV
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete) // Button xóa sinh viên

        /**
         * Bind dữ liệu sinh viên vào views và thiết lập event listeners
         * 
         * @param student Đối tượng sinh viên cần hiển thị
         */
        fun bind(student: Student) {
            // Hiển thị thông tin sinh viên
            tvName.text = student.name
            tvMSSV.text = student.mssv

            // Xử lý sự kiện click vào item để chọn sinh viên để cập nhật
            itemView.setOnClickListener {
                onStudentClick(student)
            }

            // Xử lý sự kiện click vào nút delete để xóa sinh viên
            btnDelete.setOnClickListener {
                onDeleteClick(student)
            }
        }
    }

    /**
     * Tạo ViewHolder mới khi RecyclerView cần hiển thị item
     * Method này được gọi khi RecyclerView cần tạo ViewHolder mới
     * 
     * @param parent ViewGroup cha chứa item view
     * @param viewType Loại view (không sử dụng vì chỉ có 1 loại item)
     * @return StudentViewHolder mới được tạo
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        // Inflate layout item_student thành View object
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    /**
     * Bind dữ liệu vào ViewHolder tại vị trí position
     * Method này được gọi khi RecyclerView cần hiển thị data cho một item
     * 
     * @param holder ViewHolder cần bind dữ liệu
     * @param position Vị trí của item trong danh sách
     */
    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(students[position])
    }

    /**
     * Trả về tổng số lượng items trong danh sách
     * 
     * @return Số lượng sinh viên trong danh sách
     */
    override fun getItemCount(): Int = students.size
}
