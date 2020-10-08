package com.ceseagod.rajarani.mainfolder

import android.R.attr
import android.annotation.TargetApi
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.model.CateModel
import com.ceseagod.showcase.utilities.SessionMaintainence
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_categories.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream


class AddCategoriesActivity : AppCompatActivity() {
    var imageLinks: String = ""
    var firestoreDB: FirebaseFirestore? = null
    private val PICK_IMAGE = 100
    private var imageUri: Uri? = null
    var imagePath: String? = ""
    var progress: ProgressDialog? = null
    var bitmap: Bitmap? = null
    private val FINAL_CHOOSE_PHOTO = 2
    private val FINAL_TAKE_PHOTO = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_categories)
        placeos.setOnClickListener {
            val input1 = inputs1.text.toString()
            val input2 = inputs2.text.toString()
            val input3 = inputs4.text.toString()
            val sd = checksValidation(input1, imagePath, input2, input3)
            if (sd) {
                photouploadFile(
                    bitmap!!,
                    "CategoryPhoto",
                    SessionMaintainence.instance!!.Uid!!, input1, input2, input3
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

    private fun addCate(
        imageLinks: String,
        topti: String,
        toptitwo: String,
        des: String
    ) {
        val instance = SessionMaintainence.instance!!
        val ifd = instance.Uid!! + Timestamp.now()
        val model = CateModel(topti, ifd, imageLinks, toptitwo, des, Timestamp.now())
        firestoreDB = FirebaseFirestore.getInstance()
        firestoreDB!!.collection("categories").document(ifd)
            .set(model)
            .addOnSuccessListener {
                progress!!.dismiss()
                startActivity<AdminsActivity>()
            }
            .addOnFailureListener {
                progress!!.dismiss()
            }

    }


    private fun checksValidation(
        topti: String,
        imagePath: String?,
        toptitwo: String,
        des: String
    ): Boolean {
        return topti != "" && imagePath != "" && toptitwo != "" && des != ""
    }

    private fun openAlbum() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, FINAL_CHOOSE_PHOTO)
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

//    @SuppressLint("ObsoleteSdkInt")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            FINAL_TAKE_PHOTO ->
//                if (resultCode == Activity.RESULT_OK) {
//                    bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!))
//                    kadaiimages!!.setImageBitmap(bitmap)
//                }
//            FINAL_CHOOSE_PHOTO ->
//                if (resultCode == Activity.RESULT_OK) {
//                    if (Build.VERSION.SDK_INT >= 19) {
//                        handleImageOnKitkat(data)
//                    } else {
//                        handleImageBeforeKitkat(data)
//                    }
//                }
//        }
//    }
    private fun openGallery() {
        val gallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data!!.data
            imagePath=imageUri.toString()
            kadaiimages!!.setImageURI(imageUri)
             bitmap =
                MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

        }
    }

    @TargetApi(19)
    private fun handleImageOnKitkat(data: Intent?) {
        val uri = data!!.data
        if (DocumentsContract.isDocumentUri(this, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri!!.authority) {
                val id = docId.split(":")[1]
                val selsetion = MediaStore.Images.Media._ID + "=" + id
                imagePath = imagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selsetion)
            } else if ("com.android.providers.downloads.documents" == uri.authority) {
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(docId)
                )
                imagePath = imagePath(contentUri, null)
            }
        } else if ("content".equals(uri!!.scheme, ignoreCase = true)) {
            imagePath = imagePath(uri, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            imagePath = uri.path
        }

        bitmap = BitmapFactory.decodeFile(imagePath)
        kadaiimages!!.setImageBitmap(bitmap)


//        displayImage(imagePath)
    }

    private fun imagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
        val cursor = contentResolver.query(uri!!, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }

    private fun handleImageBeforeKitkat(data: Intent?) {}

    private fun photouploadFile(
        bitmap: Bitmap,
        folder: String,
        userId: String,
        topti: String,
        toptitwo: String,
        des: String
    ) {
        progress = ProgressDialog(this)
        progress!!.setMessage("Processing..")
        progress!!.setCancelable(false)
        progress!!.show()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("support")
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
                    des
                )
            }
        }
    }

}