
package com.example.scheduleplanner.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.scheduleplanner.R

import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast

import java.util.*





class CalendarFragment : Fragment() {

    private lateinit var datePicker: DatePicker
    private lateinit var editTextDate: EditText
    private lateinit var button: Button
    private lateinit var mView: View

    private var mParam1: String? = null
    private var mParam2: String? = null

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): CalendarFragment {
            val fragment = CalendarFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mParam1 = it.getString(ARG_PARAM1)
            mParam2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_calendar, container, false)

        // Initialize views
        editTextDate = mView.findViewById(R.id.editText_date)
        button = mView.findViewById(R.id.button_date)
        datePicker = mView.findViewById(R.id.datePicker)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        datePicker.init(year, month, day) { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            datePickerChange(selectedYear, selectedMonth, selectedDayOfMonth)
        }

        button.setOnClickListener {
            showDate()
        }

        return mView
    }

    private fun datePickerChange(year: Int, month: Int, dayOfMonth: Int) {
        editTextDate.setText("$dayOfMonth-${month + 1}-$year")
    }

    private fun showDate() {
        val year = datePicker.year
        val month = datePicker.month // 0 - 11
        val day = datePicker.dayOfMonth
        Toast.makeText(activity, "Date: $day-${month + 1}-$year", Toast.LENGTH_LONG).show()
    }

    // Uncomment this if you want to use DatePickerDialog
    // fun onSelectDate(view: View) {
    //    val c = Calendar.getInstance() // Get the current date
    //    val day = c.get(Calendar.DAY_OF_MONTH)
    //    val month = c.get(Calendar.MONTH)
    //    val year = c.get(Calendar.YEAR)

    //    val datePickerDialog = DatePickerDialog(requireActivity(), { _, selectedYear, selectedMonth, selectedDayOfMonth ->
    //        button.text = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
    //    }, year, month, day)

    //    datePickerDialog.show()
    // }
}

