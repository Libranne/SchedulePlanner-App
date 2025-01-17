package com.example.scheduleplanner.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.scheduleplanner.*
import com.example.scheduleplanner.Database.Note
import com.example.scheduleplanner.Database.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private lateinit var addNoteButton: FloatingActionButton
    private lateinit var listView: ListView
    private val notesList = ArrayList<Note>() // Danh sách ghi chú
    private lateinit var adapter: NoteAdapter
    private lateinit var noteProvider: NoteProvider
    private lateinit var noteViewModel: NoteViewModel

    // Đăng ký ActivityResultLauncher tại Fragment
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val note = result.data?.getSerializableExtra("note") as? Note
                val action = result.data?.getStringExtra("action")

                if (note != null) {
                    if (action == "update") {
                        val index = notesList.indexOfFirst { it.id == note.id }
                        if (index != -1) {
                            notesList[index] = note // Cập nhật ghi chú trong danh sách
                            noteViewModel.updateNote(note) // Cập nhật vào ViewModel
                        }
                    } else {
                        // Thêm mới ghi chú
                        notesList.add(note)
                        noteViewModel.addNote(note) // Thêm vào ViewModel
                    }
                    adapter.notifyDataSetChanged() // Cập nhật lại giao diện
                }
            }
        }

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

        // Khởi tạo NoteViewModel
        noteViewModel = ViewModelProvider(requireActivity()).get(NoteViewModel::class.java)

        // Observe LiveData from ViewModel
        noteViewModel.notesLiveData.observe(viewLifecycleOwner, { notes ->
            notesList.clear()
            notesList.addAll(notes)
            adapter.notifyDataSetChanged()
        })

        // Define a delete handler for the adapter
        val deleteNoteCallback: (Note) -> Unit = { note ->
            notesList.remove(note) // Remove note from list
            noteViewModel.deleteNote(note) // Remove from ViewModel
            adapter.notifyDataSetChanged() // Notify adapter about the change
        }

        // Thiết lập Adapter tùy chỉnh và truyền launcher vào Adapter
        adapter = NoteAdapter(requireContext(), notesList, activityResultLauncher, deleteNoteCallback)
        listView.adapter = adapter

        // Sự kiện thêm ghi chú
        addNoteButton.setOnClickListener {
            val intent = Intent(activity, AddNoteActivity::class.java)
            intent.putExtra("action", "insert")
            activityResultLauncher.launch(intent)
        }

        // Nạp dữ liệu ghi chú từ database
        loadNotes()

        return rootView
    }

    private fun loadNotes() {
        // Lấy dữ liệu từ cơ sở dữ liệu
        val notes = noteProvider.getAllNote()
        notesList.addAll(notes) // Thêm các ghi chú
        adapter.notifyDataSetChanged()
    }
}
