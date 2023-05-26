package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMain2Binding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var list = mutableListOf<User>()
    private var adapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("IndoorLight/0969581113")

        getData()

//        binding.btnSave.setOnClickListener { saveData() }


    }

    private fun initRecyclerView() {
        adapter = UserAdapter()
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity2)
            recyclerView.adapter = adapter
        }
    }

//    private fun saveData() {
//        val light1 = binding.edLight.text.toString()
//        val temp = binding.edTemp.text.toString()
//        val user = User(light1 = light1, temp = temp)
//
//        databaseReference?.child("living_room")?.setValue(user)
//    }

    private fun getData() {
        databaseReference?.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.e("ooooo","onDataChange: ${snapshot}")
                list.clear()
                for (ds in snapshot.children) {
//                    Log.e("ooooo","onDataChange: ${ds}")
                    val id = convertToTitleCase(ds.key.toString())
                    val light1 = ds.child("light1").value.toString()
                    val temp = ds.child("temp").value.toString()

                    val user = User(id = id, light1 = light1, temp = temp)
                    list.add(user)
                }
//                Log.e("ooooo","onDataChange: ${list.size}")

                adapter?.setItems(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ooooo", "onCancelled: ${error.toException()}")
            }

        })
    }

    fun convertToTitleCase(input: String): String {
        val words = input.split("_") // Tách chuỗi theo dấu "_"
        val capitalizedWords =
            words.map { it.capitalize() } // Chuyển đổi các từ thành chữ cái đầu tiên viết hoa
        return capitalizedWords.joinToString(" ") // Kết hợp các từ lại với dấu cách
    }


}