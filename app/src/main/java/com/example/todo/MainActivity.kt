package com.example.todo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.model.UploadDataClass
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.DateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var uri: Uri? = null
    private var imageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickHandler()
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun clickHandler() {
        binding.imageView.setOnClickListener{
            imagePicker.launch("image/*")
        }

        binding.uploadBtn.setOnClickListener {

            val name = binding.nameTextField.text.toString()
            val email = binding.emailTextField.text.toString()
            val phone = binding.phoneTextField.text.toString()
            if(uri == null){
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
            else if (name.isBlank()){
                binding.nameTextField.error = "Please enter your name"
                binding.nameTextField.requestFocus()
            }
            else if (email.isBlank()){
                binding.emailTextField.error = "Please enter your email"
                binding.emailTextField.requestFocus()
            }
            else if (phone.isBlank()){
                binding.phoneTextField.error = "Please enter your phone"
                binding.phoneTextField.requestFocus()
            }else{
                uploadFirebase()
            }
        }

        binding.showBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity2::class.java))
        }
    }


    private fun uploadFirebase(){
        binding.progressBar.visibility = View.VISIBLE
        val storageReference = FirebaseStorage.getInstance().reference.child("Images").child(uri!!.lastPathSegment!!)
        val storeRef = storageReference.child(System.currentTimeMillis().toString() + "." + getFileExtenstion(uri!!))
        storeRef.putFile(uri!!).addOnSuccessListener { uploadTask ->
//            val uriTask = uploadTask.storage.downloadUrl
//            while (!uriTask.isComplete);
//            imageUrl = uriTask.toString()

            val userName = binding.nameTextField.text.toString()
            val userEmail = binding.emailTextField.text.toString()
            val userPhone = binding.phoneTextField.text.toString()

            storeRef.downloadUrl.addOnSuccessListener {
                val uriTask = it
                imageUrl = uriTask.toString()


                val uploadDataClass = UploadDataClass(userName, userEmail, userPhone, imageUrl)
                val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)


                val firebaseDatabase = FirebaseDatabase.getInstance().getReference("ToDo Image").child(currentDate)
                firebaseDatabase.setValue(uploadDataClass).addOnCompleteListener {task ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()
                        binding.imageView.setImageResource(R.drawable.upload)
                        binding.progressBar.visibility = View.GONE
                    }
                }
                    .addOnFailureListener{
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        binding.imageView.setImageResource(R.drawable.upload)
                        binding.progressBar.visibility = View.GONE
                    }

            }

            uri = null
            binding.nameTextField.text = null
            binding.emailTextField.text = null
            binding.phoneTextField.text = null
        }
            .addOnFailureListener{e ->
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
    }

    private fun getFileExtenstion(filUri: Uri) : String?{
        val contentResolver = contentResolver
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getExtensionFromMimeType(contentResolver.getType(filUri))
    }




    private val imagePicker = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ){
        uri = it
        binding.imageView.setImageURI(it)
    }
}