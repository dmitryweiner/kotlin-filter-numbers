package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.util.*


class MainActivity : AppCompatActivity() {
    private val list = mutableListOf<Number>()
    private val filteredList = mutableListOf<Number>()
    private var filterValue: Filter = Filter.All
    private lateinit var adapter: RecyclerAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = RecyclerAdapter(filteredList) { id ->
            list.removeIf { it.id == id }
            val index = filteredList.indexOfFirst { it.id == id }
            filteredList.removeAt(index)
            adapter.notifyItemRemoved(index)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val editText = findViewById<EditText>(R.id.editTextNumber)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val s = editText.text.toString()
            if (s.isNotBlank()) {
                list.add(Number(UUID.randomUUID(), s.toIntOrNull() ?: 0))
            }
            editText.text.clear()
            render()
        }

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when(i) {
                R.id.radioButton -> filterValue = Filter.All
                R.id.radioButton2 -> filterValue = Filter.Positive
                R.id.radioButton3 -> filterValue = Filter.Negative
            }
            render()
        }
    }

    fun render() {
        filteredList.clear()
        when(filterValue) {
            Filter.All -> filteredList.addAll(list)
            Filter.Positive -> filteredList.addAll(list.filter { it.value >= 0 })
            Filter.Negative -> filteredList.addAll(list.filter { it.value < 0 })
        }
        adapter.notifyDataSetChanged()
    }
}