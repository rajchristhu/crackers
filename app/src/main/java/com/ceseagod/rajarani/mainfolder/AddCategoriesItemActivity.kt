package com.ceseagod.rajarani.mainfolder

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.model.Cateitemmodel
import com.ceseagod.rajarani.utilities.SessionMaintainence
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_categories_item.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream

class AddCategoriesItemActivity : AppCompatActivity() {
    var imageLinks: String = ""
    var firestoreDB: FirebaseFirestore? = null
    private val PICK_IMAGE = 100
    private var imageUri: Uri? = null
    var imagePath: String? = ""
    var progress: ProgressDialog? = null
    var bitmap: Bitmap? = null
    private val FINAL_CHOOSE_PHOTO = 2
    private val FINAL_TAKE_PHOTO = 1
    var id=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_categories_item)
        id = intent.getStringExtra("id")

        placeo.setOnClickListener {
            val input1 = inputs1.text.toString()
            val input2 = inputs2.text.toString()
            val input3 = inputs4.text.toString()
            val input4 = inputs5.text.toString()
            val input7 = inputs7.text.toString()
            val sd = checksValidation(input1, imagePath, input2, input3, input4)
            if (sd) {
                photouploadFile(
                    bitmap!!,
                    "CategoryPhoto",
                    SessionMaintainence.instance!!.Uid!!, input1, input2, input3, input4
                )
            } else {
                toast("Fill All details")
                if (inputs1.text.toString().isEmpty()) {
                    inputs1.error = "Fill the details"
                }
                if (inputs2.text.toString().isEmpty()) {
                    inputs2.error = "Fill the details"

                }
                if (inputs4.text.toString().isEmpty()) {
                    inputs4.error = "Fill the details"

                }
                if (inputs5.text.toString().isEmpty()) {
                    inputs5.error = "Fill the details"

                }
                if (inputs7.text.toString().isEmpty()) {
                    inputs7.error = "Fill the details"

                }
            }
        }

        kadaiimages.setOnClickListener {
            val checkSelfPermission =
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
//                openAlbum()
                openGallery()
            }

        }
    }

    private fun checksValidation(
        topti: String,
        imagePath: String?,
        toptitwo: String,
        des: String,
        input4: String
    ): Boolean {
        return topti != "" && imagePath != "" && toptitwo != "" && des != "" && input4 != ""
    }

    private fun addCate(
        imageLinks: String,
        topti: String,
        toptitwo: String,
        des: String,
        input4: String
    ) {
        val instance = SessionMaintainence.instance!!
        val ifd = instance.Uid!! + Timestamp.now()
        val model =
            Cateitemmodel(
                topti,
                ifd,
                id,
                imageLinks,
                toptitwo,
                des,
                input4,
                inputs7.text.toString(),
                Timestamp.now()
            )
        firestoreDB = FirebaseFirestore.getInstance()
        firestoreDB!!.collection("categoriesitem").document(ifd)
            .set(model)
            .addOnSuccessListener {
                progress!!.dismiss()
                startActivity<AdminsActivity>()
            }
            .addOnFailureListener {
                progress!!.dismiss()
            }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    openAlbum()
                    openGallery()
                } else {
                    Toast.makeText(this, "You deied the permission", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun openGallery() {
        val gallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data!!.data
            imagePath = imageUri.toString()
            kadaiimages!!.setImageURI(imageUri)
            bitmap =
                MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

        }
    }

    private fun photouploadFile(
        bitmap: Bitmap,
        folder: String,
        userId: String,
        topti: String,
        toptitwo: String,
        des: String,
        input4: String
    ) {
        progress = ProgressDialog(this)
        progress!!.setMessage("Processing..")
        progress!!.setCancelable(false)
        progress!!.show()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("support_item")
        val mountainImagesRef =
            storageRef.child(folder + "/" + userId + Timestamp.now().toString() + ".png")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, baos)
        val data = baos.toByteArray()
        val uploadTask = mountainImagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            progress!!.dismiss()
        }.addOnSuccessListener { taskSnapshot ->
            val result = taskSnapshot.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {
                imageLinks = it.toString()
                addCate(
                    imageLinks,
                    topti,
                    toptitwo,
                    des, input4
                )
            }
        }
    }

}