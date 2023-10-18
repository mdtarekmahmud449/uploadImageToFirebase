package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.todo.databinding.ActivityMain2Binding
import com.example.todo.model.UploadDataClass
import com.google.common.collect.Lists
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var lists: MutableList<UploadDataClass>
    private var databaseReference: DatabaseReference? = null
    private var eventListener: ValueEventListener? = null
    private lateinit var adapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        lists = mutableListOf<UploadDataClass>()
        adapter = ImageAdapter(this@MainActivity2, lists)
        binding.apply {
            recyclerView.layoutManager = GridLayoutManager(this@MainActivity2, 1)
            recyclerView.adapter = adapter

        }
        databaseReference = FirebaseDatabase.getInstance().getReference("ToDo Image")
        eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                lists.clear()
                for(item in snapshot.children){
                    val dataClass = item.getValue(UploadDataClass::class.java)
                    if(dataClass != null){
                        lists.add(dataClass)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}