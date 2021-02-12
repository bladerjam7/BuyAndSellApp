package com.bladerco.buyandsellapp.view.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bladerco.buyandsellapp.R
import com.bladerco.buyandsellapp.model.data.Post
import com.bladerco.buyandsellapp.view.MainActivity
import com.bladerco.buyandsellapp.viewmodel.ItemViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.DecimalFormat
import java.util.*

class AddNewFragment : Fragment() {

    private lateinit var ivPhoto: ImageView
    private lateinit var btnUpload: CardView
    private lateinit var etDescription: EditText
    private lateinit var etPrice: EditText
    private lateinit var btnDone: CardView
    private lateinit var etTitle: EditText

    private var uploadBitmap: Bitmap? = null
    private var fileDir: String? = null

    private val itemViewModel: ItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_add_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        view.apply {
            ivPhoto = findViewById(R.id.iv_photo)
            btnUpload = findViewById(R.id.btn_upload)
            etDescription = findViewById(R.id.et_description)
            etPrice = findViewById(R.id.et_price)
            btnDone = findViewById(R.id.btn_done_upload)
            etTitle = findViewById(R.id.et_title)

        }

        etDescription.text?.clear()
        etPrice.text?.clear()
        setUpOnClick()
    }

    private fun createTemporaryFile(): File? {
        val fileName = "${FirebaseAuth.getInstance().currentUser?.uid}_${Date().time}"

        val fileDirectory = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val imageFile = File.createTempFile(
            fileName,
            ".png",
            fileDirectory
        )

        fileDir = imageFile.absolutePath
        return imageFile

    }

    private fun setUpOnClick() {
        btnUpload.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            Log.d("TAG_X", "Capture Picture Pressed")
            // Checks if the camera exist
            if (context?.packageManager?.let { it1 -> cameraIntent.resolveActivity(it1) } != null) {
                try {
                    val tmpFile = createTemporaryFile()

                    context?.let { context ->
                        tmpFile?.let {
                            val imageUri = FileProvider.getUriForFile(
                                context,
                                "com.bladerco.buyandsellapp.fileprovider",
                                it
                            )

                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                            startActivityForResult(cameraIntent, 777)
                        }
                    }

                } catch (e: Exception) {
                    Log.d("TAG_X", "Error: $e")
                }
            } else {
                Toast.makeText(context, "Camera Not Working", Toast.LENGTH_SHORT).show()
            }
        }

        btnDone.setOnClickListener {
            uploadBitmap?.let {
                val bOS = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 100, bOS)
                val imageByte = bOS.toByteArray()

                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "default"

                val date = Date().time
                val storageReference =
                    FirebaseStorage.getInstance().reference.child("$userId/$date.jpeg")
                val uploadTask = storageReference.putBytes(imageByte)



                uploadTask.addOnCompleteListener {
                    if (it.isSuccessful) {
                        storageReference
                            .downloadUrl
                            .addOnCompleteListener { dlTask ->
                                if (dlTask.isSuccessful) {
                                    upload(dlTask.result)

                                }
                            }
                    }
                }


            }
        }

    }

    private fun upload(result: Uri?) {


        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "unknown"
        val title = etTitle.text.toString().trim()
        val description = etDescription.text.toString().trim()
        //val price = etPrice.text.toString().trim()

        val formatPrice = DecimalFormat("##.##")
        var price = ""
        if (etPrice.text.toString() == "" || etPrice.text.isEmpty()) {
        } else {
            price = formatPrice.format(etPrice?.text.toString().toDouble())
        }


        if (checkInput(title, description, price)) {
            val post = Post().also {
                it.imageUrl = result.toString()
                it.title = title
                it.price = price
                it.description = description
                it.postedBy = userEmail
                it.bid = false
                it.bidCount = 0
            }

            itemViewModel.uploadPost(post)
            ivPhoto.setImageResource(R.drawable.ic_baseline_add_a_photo_24)
            etTitle.text.clear()
            etDescription.text.clear()
            etPrice.text.clear()

            val activity = getActivity() as MainActivity
            activity.loadFragment(0)
        }


    }

    private fun checkInput(title: String, description: String, price: String): Boolean {
        when {
            title == "" || title.isEmpty() -> {
                showError("Please enter a title")
                return false
            }
            description == "" || description.isEmpty() -> {
                showError("Please enter a description")
                return false
            }
            price == "" || price.isEmpty() -> {
                showError("Please enter a price")
                return false
            }

        }
        return true

    }

    private fun showError(s: String) {
        Toast.makeText(context, "$s", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        uploadBitmap = BitmapFactory.decodeFile(fileDir)
        uploadBitmap?.let {
            ivPhoto.setImageBitmap(it)
        }
    }
}