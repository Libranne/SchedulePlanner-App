package com.example.scheduleplanner.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.scheduleplanner.AddNoteActivity
import com.example.scheduleplanner.Note
import com.example.scheduleplanner.NoteAdapter
import com.example.scheduleplanner.NoteProvider
import com.example.scheduleplanner.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private lateinit var addNoteButton: FloatingActionButton
    private lateinit var listView: ListView
    private val notesList = ArrayList<Note>() // Danh sách ghi chú
    private lateinit var adapter: NoteAdapter
    private lateinit var noteProvider: NoteProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // Ánh xạ View
        addNoteButton = rootView.findViewById(R.id.AddNote)
        listView = rootView.findViewById(R.id.lvNote)

        // Khởi tạo NoteProvider
        noteProvider = NoteProvider(requireContext())

        // Thiết lập Adapter tùy chỉnh
        adapter = NoteAdapter(requireContext(), notesList)
        listView.adapter = adapter

        // Sự kiện thêm ghi chú
        addNoteButton.setOnClickListener {
            val intent = Intent(activity, AddNoteActivity::class.java)
            intent.putExtra("action", "insert")
            startActivityForResult(intent, 1)
        }

        // Nạp dữ liệu ghi chú từ database
        loadNotes()

        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val note = data?.getSerializableExtra("note") as? Note
            val action = data?.getStringExtra("action")

            if (note != null) {
                if (action == "update") {
                    val index = notesList.indexOfFirst { it.id == note.id }
                    if (index != -1) {
                        notesList[index] = note // Cập nhật ghi chú trong danh sách
                        adapter.notifyDataSetChanged() // Cập nhật lại giao diện
                    }
                } else {
                    // Thêm mới ghi chú
                    notesList.add(note)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun loadNotes() {
        // Lấy dữ liệu từ cơ sở dữ liệu
        notesList.clear() // Xóa danh sách cũ
        notesList.addAll(noteProvider.getAllNote()) // Thêm các ghi chú mới từ CSDL
        adapter.notifyDataSetChanged()
    }
}
